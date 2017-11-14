package dk.sdu.lahan14.cleanthestreet;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

/**
 * Created by Arpad on 11/14/2017.
 */

public class BasicTaskActivity extends AppCompatActivity implements OnMapReadyCallback {

    protected Context context;
    protected FusedLocationProviderClient mFusedLocationProviderClient;
    protected GoogleMap mMap;
    protected Location mLastKnownLocation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    public void setContext(Context context) {
        this.context = context;

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void onAddPicture(View view) {
        CharSequence[] items = {context.getString(R.string.dlg_option_gallery), context.getString(R.string.dlg_option_camera)};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
        ((Activity) context).startActivityForResult(getPicureFromGalleryIntent, Constants.GALLERY_IMAGE_REQUEST_CODE);
    }

    private void getImageFromCamera() {
        Intent takePictueIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ((Activity) context).startActivityForResult(takePictueIntent, Constants.CAMERA_IMAGE_REQUEST_CODE);
    }

    protected Bitmap retrieveBitmap(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = null;

        if (requestCode == Constants.GALLERY_IMAGE_REQUEST_CODE || requestCode == Constants.CAMERA_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri imageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
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

        getDeviceLocation();
    }

    protected void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } catch (SecurityException se) {

        }
    }

    protected void getDeviceLocation() {
        try {
            com.google.android.gms.tasks.Task locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        mLastKnownLocation = (Location) task.getResult();
                        LatLng latLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    }
                }
            });
        } catch (SecurityException se) {

        }
    }
}
