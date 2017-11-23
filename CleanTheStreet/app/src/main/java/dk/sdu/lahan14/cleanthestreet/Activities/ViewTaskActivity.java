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

import dk.sdu.lahan14.cleanthestreet.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        mTaskCreatorTextView = findViewById(R.id.tv_task_creator);
        mIamgeView = findViewById(R.id.iv_view_task_image);
        mDescriptionTextView = findViewById(R.id.tv_view_task_description);
        mUpvoteImageButton = findViewById(R.id.ib_view_task_upvote);
        mDistanceTextView = findViewById(R.id.tv_view_task_distance);
        mScoreTextView = findViewById(R.id.tv_view_task_score_value);
        mAcceptTaskButton = findViewById(R.id.btn_accept_task);

        // TODO: get task
        /* For testing */
        mTask = new Task(null, "Something to clean up", 8, -33.852, 151.211, "Johny");

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.f_view_task_map);
        mapFragment.getMapAsync(this);

        updateDisplayData();

    }

    private void updateDisplayData() {
        mTaskCreatorTextView.setText(mTask.getCreator());
        mIamgeView.setImageBitmap(mTask.getImage());
        mDescriptionTextView.setText(mTask.getDescription());
        mScoreTextView.setText(mTask.getScore());
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
        completeActivityIntent.putExtra(Task.class.toString(), mTask);
        startActivity(completeActivityIntent);
    }

}