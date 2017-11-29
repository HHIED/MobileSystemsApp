package dk.sdu.lahan14.cleanthestreet.Activities;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import dk.sdu.lahan14.cleanthestreet.Database.Database;
import dk.sdu.lahan14.cleanthestreet.Database.DatabaseHelper;
import dk.sdu.lahan14.cleanthestreet.R;
import dk.sdu.lahan14.cleanthestreet.Util.ActiveTask;
import dk.sdu.lahan14.cleanthestreet.Util.Task;

public class ApproveCompletedTaskActivity extends BasicTaskActivity {

    private static final String TAG = "APPROVE_TASK";

    private Task mTask;

    private TextView mTaskCreatorTextView;
    private ImageView mOriginalImageView;
    private ImageView mCompletedImageView;
    private TextView mDescriptionTextView;
    private TextView mScoreTextView;
    private AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_completed_task);
        client = new AsyncHttpClient();
        mTaskCreatorTextView = findViewById(R.id.tv_approve_task_creator);
        mDescriptionTextView = findViewById(R.id.tv_approve_task_description);
        mScoreTextView = findViewById(R.id.tv_approve_task_score_value);
        mOriginalImageView = findViewById(R.id.iv_approve_task_image);
        mCompletedImageView = findViewById(R.id.iv_approve_completed_task_image);

        mTask = ActiveTask.activeMyTask;

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.f_approve_task_map);
        mapFragment.getMapAsync(this);

        updateDisplayData();
    }

    private void updateDisplayData() {
        mTaskCreatorTextView.setText(mTask.getCreator());
        mOriginalImageView.setImageBitmap(mTask.getImage());
        mCompletedImageView.setImageBitmap(mTask.getCompletedImage());
        mDescriptionTextView.setText(mTask.getDescription());
        mScoreTextView.setText(String.valueOf(mTask.getScore()));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            LatLng latLng = new LatLng(mTask.getLatitude(), mTask.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(latLng));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        } catch (SecurityException se) {
            Log.e(TAG, se.getMessage());
        }
    }

    public void onApproveTask(View view) {
        approveTask();
        finish();
    }

    public void onDisapproveTask(View view) {
        declineTask();
        finish();
    }

    private void approveTask() {
        String url = "https://getstarteddotnet-pansophical-bedding.eu-gb.mybluemix.net/api/tasks/approveTask/"+mTask.getId();
        final RequestHandle handle = client.post(ApproveCompletedTaskActivity.this, url, null,  new AsyncHttpResponseHandler() {
            DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void declineTask() {
        String url = "https://getstarteddotnet-pansophical-bedding.eu-gb.mybluemix.net/api/tasks/declineTask/"+mTask.getId();
        final RequestHandle handle = client.post(ApproveCompletedTaskActivity.this, url, null,  new AsyncHttpResponseHandler() {
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
