package dk.sdu.lahan14.cleanthestreet.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

import cz.msebera.android.httpclient.entity.StringEntity;
import dk.sdu.lahan14.cleanthestreet.Database.DatabaseHelper;
import dk.sdu.lahan14.cleanthestreet.Network.TaskDto;
import dk.sdu.lahan14.cleanthestreet.R;
import dk.sdu.lahan14.cleanthestreet.Util.ActiveTask;
import dk.sdu.lahan14.cleanthestreet.Util.Task;

public class ViewTaskActivity extends BasicTaskActivity {

    private static final String TAG = "VIEW_TASK";

    private Task mTask;

    private TextView mTaskCreatorTextView;
    private ImageView mIamgeView;
    private TextView mDescriptionTextView;
    private ImageButton mUpvoteImageButton;
    private TextView mDistanceTextView;
    private TextView mScoreTextView;
    private Button mAcceptTaskButton;
    private int id = 0;
    private AsyncHttpClient client;
    private Gson gson;
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        client = new AsyncHttpClient();
        gson = new Gson();
        id = getIntent().getIntExtra("id", 0);
        mTaskCreatorTextView = findViewById(R.id.tv_task_creator);
        mIamgeView = findViewById(R.id.iv_view_task_image);
        mDescriptionTextView = findViewById(R.id.tv_view_task_description);
        mUpvoteImageButton = findViewById(R.id.ib_view_task_upvote);
        mDistanceTextView = findViewById(R.id.tv_view_task_distance);
        mScoreTextView = findViewById(R.id.tv_view_task_score_value);
        mAcceptTaskButton = findViewById(R.id.btn_accept_task);
        mTask = ActiveTask.activeTask;
        mDatabaseHelper = new DatabaseHelper(getApplicationContext());
        updateDisplayData();
    }

    private void updateDisplayData() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.f_view_task_map);
        mapFragment.getMapAsync(this);
        mTaskCreatorTextView.setText(mTask.getCreator());
        mIamgeView.setImageBitmap(mTask.getImage());
        mDescriptionTextView.setText(mTask.getDescription());
        int score = mTask.getScore();
        mScoreTextView.setText(Integer.toString(score));
        calculateAndSetDistance();
        mAcceptTaskButton.setClickable(true);

        updateUpvoteButton();
    }

    private void updateUpvoteButton() {
        // TODO: disable upvote button for self
        Cursor query = mDatabaseHelper.queryTask(id);
        query.moveToNext();
        if(query.getCount() > 0 && query.getInt(1)==id){
            mUpvoteImageButton.setColorFilter(Color.argb(255, 130, 130, 130));
        } else {
            mUpvoteImageButton.setColorFilter(Color.argb(255, 0, 255, 0));
        }
    }

    private void calculateAndSetDistance() {
        getLastLocation().addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task task) {
                if (task.isSuccessful()) {
                    Location deviceLocation = (Location) task.getResult();
                    if (deviceLocation != null) {
                        Location taskLocation = new Location("");
                        taskLocation.setLatitude(mTask.getLatitude());
                        taskLocation.setLongitude(mTask.getLongitude());
                        float distanceInMeters = deviceLocation.distanceTo(taskLocation);
                        if (distanceInMeters > 1000) {
                            mDistanceTextView.setText(String.format("%.2f km", distanceInMeters / 1000.0 ));
                        } else {
                            mDistanceTextView.setText(String.format("%d m", (int) distanceInMeters));
                        }
                    }
                }
            }
        });
    }

    public void onUpvote(View view) {

        Cursor query = mDatabaseHelper.queryTask(id);
        query.moveToNext();

        if(query.getCount() > 0 && query.getInt(1)==id){
            AlertDialog.Builder message = new AlertDialog.Builder(this);
            message.setMessage("You have already upvoted this task once");
            message.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                        }
                    });
            message.show();
        } else {
            try {
            final TaskDto task = new TaskDto(id);
            String jsonTask = gson.toJson(task);
            final StringEntity entity = new StringEntity(jsonTask);
            final RequestHandle handle = client.post(ViewTaskActivity.this, "https://getstarteddotnet-pansophical-bedding.eu-gb.mybluemix.net/api/tasks/IncreaseScore", entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String json = new String(responseBody, "UTF-8");
                        JSONObject task = new JSONObject(json);
                        mDatabaseHelper.persistUpvotedTask(id);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mTask.incrementScore();
                    updateUpvoteButton();
                    mScoreTextView.setText(String.valueOf(mTask.getScore()));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
            } catch (UnsupportedEncodingException uee) {

            }
        }
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

    public void onAcceptTask(View view) {
        // TODO: identify self
        mTask.setAccepter("Me");
        Intent completeActivityIntent = new Intent(this, CompleteTaskActivity.class);
        //completeActivityIntent.putExtra(Task.class.toString(), mTask);
        ActiveTask.activeTask = mTask;
        startActivity(completeActivityIntent);
    }

}