package dk.sdu.lahan14.cleanthestreet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.MapFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class CreateTaskActivity extends BasicTaskActivity {

    private static final String TAG = "CREATE_TASK";

    private ImageView mImageView;
    private EditText mDescriptionET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        super.setContext(this);

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
        // TODO: send data to server
        AsyncHttpClient client = new AsyncHttpClient();
        UrlBuilder urlBuilder = new UrlBuilder();



        Task taskDto = new Task(1, new User(1), new User(1),1,"test", mImageView.getDrawingCache(), 2, 3);
        final RequestHandle handle2 = client.post(urlBuilder.createTaskUrl(taskDto.toDto()), null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

}
