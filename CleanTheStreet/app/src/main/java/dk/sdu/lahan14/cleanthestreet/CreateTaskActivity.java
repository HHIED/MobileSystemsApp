package dk.sdu.lahan14.cleanthestreet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.MapFragment;

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

    public void onSave(View view) {
        // TODO: send data to server
    }

}
