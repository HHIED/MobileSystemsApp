package dk.sdu.lahan14.cleanthestreet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;

public class CompleteTaskActivity extends BasicTaskActivity {

    private Task mTask;
    private TextView mCreatorTextView;
    private TextView mDescriptionTextView;
    private RatingBar mRatingBar;
    private ImageView mTaskImageView;
    private ImageView mCompletedImageView;
    private Button mDoneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_task);

        super.setContext(this);

        mCreatorTextView = findViewById(R.id.tv_complete_task_creator);
        mDescriptionTextView = findViewById(R.id.tv_complete_task_description);
        mRatingBar = findViewById(R.id.rb_complete_task_rating);
        mTaskImageView = findViewById(R.id.iv_complete_task_image);
        mCompletedImageView = findViewById(R.id.iv_completed_task_image);
        mDoneButton = findViewById(R.id.btn_done_task);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.f_complete_task_map);
        mapFragment.getMapAsync(this);


        // TODO: get task
        /* For testing */
        Location location = new Location("this");
        location.setLatitude(-33.852);
        location.setLongitude(151.211);
        mTask = new Task(null, "Something to clean up", 8, location, "Johny");

        updateDisplayData();
    }

    private void updateDisplayData() {
        mCreatorTextView.setText(mTask.getCreator());
        mDescriptionTextView.setText(mTask.getDescription());
        mRatingBar.setRating(mTask.getRating());
        mTaskImageView.setImageBitmap(mTask.getImage());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = retrieveBitmap(requestCode, resultCode, data);

        if (bitmap != null) {
            mCompletedImageView.setImageBitmap(bitmap);
            mDoneButton.setClickable(true);
        }
    }

    public void onDoneTask(View view) {
        // TODO: send to server
        finish();
    }

    public void onAbandonTask(View view) {
        mTask.setAccepter(null);
        // TODO: send to server
        finish();
    }
}