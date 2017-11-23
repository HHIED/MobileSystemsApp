package dk.sdu.lahan14.cleanthestreet.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.MapFragment;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import dk.sdu.lahan14.cleanthestreet.Network.BitMapConverter;
import dk.sdu.lahan14.cleanthestreet.R;
import dk.sdu.lahan14.cleanthestreet.Network.TaskDto;

public class CreateTaskActivity extends BasicTaskActivity {

    private static final String TAG = "CREATE_TASK";

    private ImageView mImageView;
    private EditText mDescriptionET;
    private Gson gson;
    private AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        super.setContext(this);
        gson = new Gson();
        client = new AsyncHttpClient();
        mImageView = findViewById(R.id.iv_task_image);
        mDescriptionET = findViewById(R.id.et_task_description);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.f_map);
        mapFragment.getMapAsync(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = retrieveBitmap(requestCode, resultCode, data);

        if (bitmap != null) {
            mImageView.setImageBitmap(bitmap);
        }
    }

    public void onSave(View view) throws UnsupportedEncodingException {
        createTask();



    }

    private void createTask() throws UnsupportedEncodingException {
        Bitmap image = ((BitmapDrawable)mImageView.getDrawable()).getBitmap();
        byte[] imagebytes = BitMapConverter.getBytes(image);
        String imageString = Base64.encodeToString(imagebytes, Base64.DEFAULT);
        TaskDto task = new TaskDto(1, imageString, "des", 1, (float) mLastKnownLocation.getLatitude(), (float) mLastKnownLocation.getLongitude());
        String jsonTask = gson.toJson(task);
        StringEntity entity = new StringEntity(jsonTask);
        // TODO: Change "1" in url to local userId
        String url = "https://getstarteddotnet-pansophical-bedding.eu-gb.mybluemix.net/api/tasks/create/1";

        final RequestHandle handle = client.post(CreateTaskActivity.this, url, entity, "application/json",  new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //    getTasks();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

}
