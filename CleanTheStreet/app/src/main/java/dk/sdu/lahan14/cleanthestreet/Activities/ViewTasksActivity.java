package dk.sdu.lahan14.cleanthestreet.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import dk.sdu.lahan14.cleanthestreet.Database.DatabaseHelper;
import dk.sdu.lahan14.cleanthestreet.R;
import dk.sdu.lahan14.cleanthestreet.Util.ActiveTask;
import dk.sdu.lahan14.cleanthestreet.Util.Constants;
import dk.sdu.lahan14.cleanthestreet.Util.Task;
import dk.sdu.lahan14.cleanthestreet.Network.TaskAdapter;
import dk.sdu.lahan14.cleanthestreet.Network.TaskDto;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class ViewTasksActivity extends AppCompatActivity {

    private AsyncHttpClient client;
    private Gson gson;
    private ImageButton upvoteButton;
    ListView customListView;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);
        upvoteButton = (ImageButton) findViewById(R.id.imageButton);
        client = new AsyncHttpClient();
        gson = new Gson();
        getTasks();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        customListView = (ListView) findViewById(R.id.tasksListView);
        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Task task = (Task) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(ViewTasksActivity.this, ViewTaskActivity.class);
                intent.putExtra("id", task.getId());
                ActiveTask.activeTask = task;
                startActivity(intent);
            }
        });
        //   try {
        //       createTask();
        //  } catch (UnsupportedEncodingException e) {
        //     e.printStackTrace();
        // }
        //   getTasks();

    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[] {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, Constants.PERMISSIONS_REQUEST_LOCATION);
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            lastLocation = location;
                        }
                    }
                });
    }

    public void upvoteClick(View view) throws UnsupportedEncodingException {
        int i = (int) view.getTag(R.id.upvoteId);
        upvoteTask(i);
    }


    private void viewTask() {

    }

    public void getTasks() {
        final RequestHandle requestHandle = client.get("https://getstarteddotnet-pansophical-bedding.eu-gb.mybluemix.net/api/tasks", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                try {
                    String json = new String(response, "UTF-8");
                    JSONObject jsonObject = new JSONObject(json);
                    ArrayList<Task> tasks = new ArrayList();

                        try {
                            JSONArray array = jsonObject.getJSONArray("$values");
                            for(int i =0; i<array.length();i++) {
                                JSONObject task = array.getJSONObject(i);
                                int score = task.getInt("Score");
                                String description = task.getString("Description");
                                int id = task.getInt("Id");
                                float latitude = (float) task.getDouble("Lattitude");
                                float longitude = (float) task.getDouble("Longtitude");
                                String imageString = (String) task.getString("Image");
                                boolean isApprove = (boolean) task.getBoolean("IsApproved");
                                Location location = new Location("");
                                location.setLongitude(longitude);
                                location.setLatitude(latitude);
                                float distanceToLocation = lastLocation.distanceTo(location);
                                TaskDto dto = new TaskDto(id, imageString, description, score, latitude, longitude, "", "", distanceToLocation);
                                dto.isApproved = isApprove;


                                Task taskObject = dto.toTask();
                                tasks.add(taskObject);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    // get data from the table by the ListAdapter
                    //ArrayAdapter customAdapter = new ArrayAdapter(ViewTasksActivity.this, android.R.layout.simple_list_item_1, tasks);
                    TaskAdapter customAdapter = new TaskAdapter(ViewTasksActivity.this, R.layout.task_view, tasks);

                    customListView.setAdapter(customAdapter);

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

    private void upvoteTask(final int taskId) throws UnsupportedEncodingException {

        final TaskDto task = new TaskDto(taskId);

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());

        Cursor query = databaseHelper.queryTask(taskId);
        query.moveToNext();

        if(query.getCount() > 0 && query.getInt(1)==taskId){
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

            String jsonTask = gson.toJson(task);
            final StringEntity entity = new StringEntity(jsonTask);
            final RequestHandle handle = client.post(ViewTasksActivity.this, "https://getstarteddotnet-pansophical-bedding.eu-gb.mybluemix.net/api/tasks/IncreaseScore", entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String json = new String(responseBody, "UTF-8");
                        JSONObject task = new JSONObject(json);
                        int id = task.getInt("id");
                        persistUpvotedTask(id);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    getTasks();

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }
    }

    protected void persistUpvotedTask(int taskId){
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.persistUpvotedTask(taskId);
    }
}
