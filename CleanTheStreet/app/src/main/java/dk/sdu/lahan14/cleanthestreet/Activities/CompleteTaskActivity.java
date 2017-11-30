package dk.sdu.lahan14.cleanthestreet.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import dk.sdu.lahan14.cleanthestreet.Database.DatabaseHelper;
import dk.sdu.lahan14.cleanthestreet.Network.BitMapConverter;
import dk.sdu.lahan14.cleanthestreet.Network.TaskDto;
import dk.sdu.lahan14.cleanthestreet.R;
import dk.sdu.lahan14.cleanthestreet.Util.ActiveTask;
import dk.sdu.lahan14.cleanthestreet.Util.Task;
import dk.sdu.lahan14.cleanthestreet.Util.User;

public class CompleteTaskActivity extends BasicTaskActivity {

    private Task mTask;
    private TextView mCreatorTextView;
    private TextView mDescriptionTextView;
    private TextView mScoreTextView;
    private ImageView mTaskImageView;
    private ImageView mCompletedImageView;
    private Button mDoneButton;
    private Gson gson;
    private AsyncHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_task);

        super.setContext(this);
        gson = new Gson();
        client = new AsyncHttpClient();
        mCreatorTextView = findViewById(R.id.tv_complete_task_creator);
        mDescriptionTextView = findViewById(R.id.tv_complete_task_description);
        mScoreTextView = findViewById(R.id.tv_complete_task_score_value);
        mTaskImageView = findViewById(R.id.iv_complete_task_image);
        mCompletedImageView = findViewById(R.id.iv_completed_task_image);
        mDoneButton = findViewById(R.id.btn_done_task);
        mDoneButton.setEnabled(false);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.f_complete_task_map);
        mapFragment.getMapAsync(this);

        mTask = ActiveTask.activeTask;
        takeTask();

        updateDisplayData();
    }

    private void updateDisplayData() {
        mCreatorTextView.setText(mTask.getCreator());
        mDescriptionTextView.setText(mTask.getDescription());
        int score = mTask.getScore();
        mScoreTextView.setText(Integer.toString(score));
        mTaskImageView.setImageBitmap(mTask.getImage());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = retrieveBitmap(requestCode, resultCode, data);

        if (bitmap != null) {
            mCompletedImageView.setImageBitmap(bitmap);
            mDoneButton.setEnabled(true);
        }
    }

    public void onDoneTask(View view) throws UnsupportedEncodingException {
        finishTask();
        finish();
    }

    public void onAbandonTask(View view) {
        mTask.setAccepter(null);
        abandonTask();
        finish();
    }

    private void finishTask() throws UnsupportedEncodingException {
        Bitmap image = ((BitmapDrawable)mCompletedImageView.getDrawable()).getBitmap();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        byte[] dirtyImage = BitMapConverter.getBytes(mTask.getImage());
        String dirtyImageString = Base64.encodeToString(dirtyImage, Base64.DEFAULT);

        TaskDto task = new TaskDto(mTask.getId(), dirtyImageString, mDescriptionTextView.getText().toString(), 1, (float) mLastKnownLocation.getLatitude(), (float) mLastKnownLocation.getLongitude(), "", "", imageString);
        String jsonTask = gson.toJson(task);
        StringEntity entity = new StringEntity(jsonTask);
        String url = "https://getstarteddotnet-pansophical-bedding.eu-gb.mybluemix.net/api/tasks/finishTask/" + User.userId;

        final RequestHandle handle = client.post(CompleteTaskActivity.this, url, entity, "application/json",  new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //    getTasks();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void takeTask() {
        String url = "https://getstarteddotnet-pansophical-bedding.eu-gb.mybluemix.net/api/tasks/takeTask/"+mTask.getId();
        final RequestHandle handle = client.post(CompleteTaskActivity.this, url, null,  new AsyncHttpResponseHandler() {
            DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void abandonTask() {
        String url = "https://getstarteddotnet-pansophical-bedding.eu-gb.mybluemix.net/api/tasks/abandonTask/"+mTask.getId();
        final RequestHandle handle = client.post(CompleteTaskActivity.this, url, null,  new AsyncHttpResponseHandler() {
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
