package dk.sdu.lahan14.cleanthestreet.Activities;

import android.content.Intent;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

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
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        client = new AsyncHttpClient();
        id = getIntent().getIntExtra("id", 0);
        mTaskCreatorTextView = findViewById(R.id.tv_task_creator);
        mIamgeView = findViewById(R.id.iv_view_task_image);
        mDescriptionTextView = findViewById(R.id.tv_view_task_description);
        mUpvoteImageButton = findViewById(R.id.ib_view_task_upvote);
        mDistanceTextView = findViewById(R.id.tv_view_task_distance);
        mScoreTextView = findViewById(R.id.tv_view_task_score_value);
        mAcceptTaskButton = findViewById(R.id.btn_accept_task);

        getTask();



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
        // TODO: identify self
        // TODO: disable upvote button for self
        // TODO: keep track of upvoted tasks??
        if (null != mTask.getAccepter()) {
            mAcceptTaskButton.setClickable(false);
        } else {
            mAcceptTaskButton.setClickable(true);
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
        mTask.incrementScore();
        mUpvoteImageButton.setClickable(false);
        mScoreTextView.setText(mTask.getScore());
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

    public void getTask() {
        final RequestHandle requestHandle = client.get("https://getstarteddotnet-pansophical-bedding.eu-gb.mybluemix.net/api/tasks/"+id, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                try {
                    String json = new String(response, "UTF-8");
                    JSONObject task = new JSONObject(json);

                    try {

                            int score = task.getInt("score");
                            String description = task.getString("description");
                            int id = task.getInt("id");
                            float latitude = (float) task.getDouble("lattitude");
                            float longitude = (float) task.getDouble("longtitude");
                            String imageString = (String) task.getString("image");
                            JSONObject creator = task.getJSONObject("creator");
                            String creatorString = Integer.toString(creator.getInt("id"));
                            String accepterString = null;
                            if(!task.isNull("accepter")) {
                                JSONObject accepter = task.getJSONObject("accepter");
                                accepterString = Integer.toString(accepter.getInt("id"));
                            }
                            Location location = new Location("");
;
                            TaskDto dto = new TaskDto(id, imageString, description, score, latitude, longitude, accepterString, creatorString);

                            Task taskObject = dto.toTask();
                            mTask = taskObject;

                            updateDisplayData();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // get data from the table by the ListAdapter
                    //ArrayAdapter customAdapter = new ArrayAdapter(ViewTasksActivity.this, android.R.layout.simple_list_item_1, tasks);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)

            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

}