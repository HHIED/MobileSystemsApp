package dk.sdu.lahan14.cleanthestreet.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import dk.sdu.lahan14.cleanthestreet.Database.Database;
import dk.sdu.lahan14.cleanthestreet.Database.DatabaseHelper;
import dk.sdu.lahan14.cleanthestreet.Network.BitMapConverter;
import dk.sdu.lahan14.cleanthestreet.R;
import dk.sdu.lahan14.cleanthestreet.Network.TaskDto;
import dk.sdu.lahan14.cleanthestreet.Util.Task;
import dk.sdu.lahan14.cleanthestreet.Util.User;

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
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = retrieveBitmap(requestCode, resultCode, data);

        if (bitmap != null) {
            mImageView.setImageBitmap(bitmap);
        }
    }

    public void onSave(View view) throws UnsupportedEncodingException {
        createTask();
        Toast.makeText((Context) this, "Task created", Toast.LENGTH_LONG).show();
        finish();
    }

    private void createTask() throws UnsupportedEncodingException {
        Bitmap image = ((BitmapDrawable)mImageView.getDrawable()).getBitmap();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);

        TaskDto task = new TaskDto(1, imageString, mDescriptionET.getText().toString(), 1, (float) mLastKnownLocation.getLatitude(), (float) mLastKnownLocation.getLongitude(), "", "");
        String jsonTask = gson.toJson(task);
        StringEntity entity = new StringEntity(jsonTask);

        String url = "https://getstarteddotnet-pansophical-bedding.eu-gb.mybluemix.net/api/tasks/create/" + User.userId;

        final RequestHandle handle = client.post(CreateTaskActivity.this, url, entity, "application/json",  new AsyncHttpResponseHandler() {
            DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
