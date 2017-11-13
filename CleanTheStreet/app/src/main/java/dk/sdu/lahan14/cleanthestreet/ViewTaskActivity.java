package dk.sdu.lahan14.cleanthestreet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewTaskActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Task mTask;

    private TextView mTaskCreatorTextView;
    private ImageView mIamgeView;
    private TextView mDescriptionTextView;
    private TextView mDistanceTextView;
    private RatingBar mRatingBar;
    private Button mAcceptTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        mTaskCreatorTextView = findViewById(R.id.tv_task_creator);
        mIamgeView = findViewById(R.id.iv_view_task_image);
        mDescriptionTextView = findViewById(R.id.tv_view_task_description);
        mDistanceTextView = findViewById(R.id.tv_view_task_distance);
        mRatingBar = findViewById(R.id.rb_view_task_rating);
        mAcceptTaskButton = findViewById(R.id.btn_accept_task);

        // TODO: get task
        /* For testing
        Location location = new Location("this");
        location.setLatitude(-33.852);
        location.setLongitude(151.211);
        mTask = new Task(null, "Something to clean up", 8, location, "Johny");
        */

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.f_view_task_map);
        mapFragment.getMapAsync(this);

        updateDisplayData();
    }

    private void updateDisplayData() {
        mTaskCreatorTextView.setText(mTask.getCreator());
        mIamgeView.setImageBitmap(mTask.getImage());
        mDescriptionTextView.setText(mTask.getDescription());
        calculateAndSetDistance();
        mRatingBar.setRating(mTask.getRating());
        // TODO: identify self
        if (null != mTask.getAccepter()) {
            mAcceptTaskButton.setClickable(false);
        } else {
            mAcceptTaskButton.setClickable(true);
        }
    }

    private void calculateAndSetDistance() {
        //TODO: calculate distance
        mDistanceTextView.setText("0.7 km");
    }

    public void onUpvote(View view) {
        mTask.incrementRating();
        mRatingBar.setRating(mTask.getRating());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(mTask.getLocation().getLatitude(), mTask.getLocation().getLongitude());
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    public void onAcceptTask(View view) {
        // TODO: identify self
        mTask.setAccepter("Me");
        // TODO: start AcceptTaskActivity
    }
}
