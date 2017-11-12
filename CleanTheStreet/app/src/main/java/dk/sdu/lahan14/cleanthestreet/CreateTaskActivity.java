package dk.sdu.lahan14.cleanthestreet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;

public class CreateTaskActivity extends AppCompatActivity {

    private static final String TAG = "CREATE_TASK";

    private ImageView mImageView;
    private EditText mDescriptionET;
    private Location mLastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        mImageView = findViewById(R.id.iv_task_image);
        mDescriptionET = findViewById(R.id.et_task_description);
        LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null && location.equals(mLastKnownLocation)) {
                    mLastKnownLocation.set(location);
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    public void onAddPicture(View view) {
        CharSequence[] items = {getString(R.string.dlg_option_gallery), getString(R.string.dlg_option_camera)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dlg_choose_image_source).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    getImageFromGallery();
                } else if (i == 1) {
                    getImageFromCamera();
                }
            }
        });
        builder.create().show();
    }

    private void getImageFromGallery() {
        Intent getPicureFromGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(getPicureFromGalleryIntent, Constants.GALLERY_IMAGE_REQUEST_CODE);
    }

    private void getImageFromCamera() {
        Intent takePictueIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictueIntent, Constants.CAMERA_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.GALLERY_IMAGE_REQUEST_CODE || requestCode == Constants.CAMERA_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri imageUri = data.getData();
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    if (imageBitmap.getHeight() > 1024) {
                        int nh = (int) (imageBitmap.getHeight() * (1024.0 / imageBitmap.getWidth()));
                        imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 1024, nh, true);
                    }
                    mImageView.setImageBitmap(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onSave(View view) {
        // TODO: send data to server
    }
}
