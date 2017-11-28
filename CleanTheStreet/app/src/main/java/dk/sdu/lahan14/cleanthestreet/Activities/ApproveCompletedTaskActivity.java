package dk.sdu.lahan14.cleanthestreet.Activities;

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

import dk.sdu.lahan14.cleanthestreet.R;
import dk.sdu.lahan14.cleanthestreet.Util.Task;

public class ApproveCompletedTaskActivity extends BasicTaskActivity {

    private static final String TAG = "APPROVE_TASK";

    private Task mTask;

    private TextView mTaskCreatorTextView;
    private ImageView mOriginalImageView;
    private ImageView mCompletedImageView;
    private TextView mDescriptionTextView;
    private TextView mScoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_completed_task);

        mTaskCreatorTextView = findViewById(R.id.tv_approve_task_creator);
        mDescriptionTextView = findViewById(R.id.tv_approve_task_description);
        mScoreTextView = findViewById(R.id.tv_approve_task_score_value);
        mOriginalImageView = findViewById(R.id.iv_approve_task_image);
        mCompletedImageView = findViewById(R.id.iv_approve_completed_task_image);

        // TODO: get Task;
        /* For testing */
        mTask = new Task(null, "Something to clean up", 8, -33.852, 151.211, "Johny");

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
        //TODO: send to server
    }

    public void onDisapproveTask(View view) {
        //TODO: send to server
    }
}
