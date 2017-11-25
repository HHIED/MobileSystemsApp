package dk.sdu.lahan14.cleanthestreet.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;

import dk.sdu.lahan14.cleanthestreet.Util.Constants;
import dk.sdu.lahan14.cleanthestreet.R;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by Arpad on 11/14/2017.
 */

public class BasicTaskActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "BASIC_TASK";

    protected Context context;
    protected FusedLocationProviderClient mFusedLocationProviderClient;
    protected GoogleMap mMap;
    protected Location mLastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void onAddPicture(View view) {
        CharSequence[] items = {getString(R.string.dlg_option_camera), getString(R.string.dlg_option_gallery)};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.dlg_choose_image_source).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    getImageFromCamera();
                } else if (i == 1) {
                    getImageFromGallery();
                }
            }
        });
        builder.create().show();
    }

    private void getImageFromGallery() {
        Intent getPicureFromGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (getPicureFromGalleryIntent.resolveActivity(getPackageManager()) != null) {
            ((Activity) context).startActivityForResult(getPicureFromGalleryIntent, Constants.GALLERY_IMAGE_REQUEST_CODE);
        }
    }

    private void getImageFromCamera() {
        Intent takePictueIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictueIntent.resolveActivity(getPackageManager()) != null) {

            ((Activity) context).startActivityForResult(takePictueIntent, Constants.CAMERA_IMAGE_REQUEST_CODE);
        }
    }

    protected Bitmap retrieveBitmap(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = null;

        if (requestCode == Constants.GALLERY_IMAGE_REQUEST_CODE || requestCode == Constants.CAMERA_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri imageUri = data.getData();
                try {
                    if(imageUri==null) {
                        bitmap = (Bitmap)data.getExtras().get("data");
                    } else {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    }
                    if (bitmap.getHeight() > 1024) {
                        int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                        bitmap = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bitmap;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        updateLocationUI();

        setDeviceLocationOnMap();
    }

    protected void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, Constants.PERMISSIONS_REQUEST_LOCATION);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    protected void setDeviceLocationOnMap() {
        Task locationResult = getLastLocation();
        if (locationResult != null) {
            locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        mLastKnownLocation = (Location) task.getResult();
                        if (mLastKnownLocation != null) {
                            LatLng latLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(latLng));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        }
                    }
                }
            });
        }
    }

    protected Task getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, Constants.PERMISSIONS_REQUEST_LOCATION);
            return null;
        }
        return mFusedLocationProviderClient.getLastLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateLocationUI();
                    setDeviceLocationOnMap();
                }
        }
    }
}
