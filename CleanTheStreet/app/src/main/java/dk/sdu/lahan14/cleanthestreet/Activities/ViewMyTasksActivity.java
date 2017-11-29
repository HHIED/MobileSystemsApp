package dk.sdu.lahan14.cleanthestreet.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import dk.sdu.lahan14.cleanthestreet.Database.Database;
import dk.sdu.lahan14.cleanthestreet.Database.DatabaseHelper;
import dk.sdu.lahan14.cleanthestreet.Network.MyTaskAdapter;
import dk.sdu.lahan14.cleanthestreet.Network.TaskAdapter;
import dk.sdu.lahan14.cleanthestreet.Network.TaskDto;
import dk.sdu.lahan14.cleanthestreet.R;
import dk.sdu.lahan14.cleanthestreet.Util.ActiveTask;
import dk.sdu.lahan14.cleanthestreet.Util.Constants;
import dk.sdu.lahan14.cleanthestreet.Util.Task;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class ViewMyTasksActivity extends AppCompatActivity {

    private AsyncHttpClient client;
    private Gson gson;
    private ImageButton upvoteButton;
    ListView customListView;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_tasks);
        client = new AsyncHttpClient();
        gson = new Gson();
        getTasks();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        customListView = (ListView) findViewById(R.id.myTasksListView);
        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Task task = (Task) adapterView.getItemAtPosition(i);
                if(task.isCompleted()) {
                    Intent intent = new Intent(ViewMyTasksActivity.this, ApproveCompletedTaskActivity.class);
                    intent.putExtra("id", task.getId());
                    ActiveTask.activeMyTask = task;
                    startActivity(intent);
                }
            }
        });
    }


    public void getTasks() {
        //TODO: Change 1 to userId
        final RequestHandle requestHandle = client.get("https://getstarteddotnet-pansophical-bedding.eu-gb.mybluemix.net/api/tasks/mytasks/1", new AsyncHttpResponseHandler() {

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
                            boolean completed = false;
                            String completedImage = "";
                            boolean isApprove = (boolean) task.getBoolean("IsApproved");
                            if(!task.isNull("CompletedImage")) {
                                completed=true;
                                completedImage = task.getString("CompletedImage");
                            }
                            TaskDto dto = new TaskDto(id, getString(R.string.image_test), description, score, latitude, longitude, "", "", 0);
                            dto.completedimage = completedImage;
                            dto.isCompleted = completed;
                            dto.isApproved = isApprove;


                            Task taskObject = dto.toTask();
                            tasks.add(taskObject);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    // get data from the table by the ListAdapter
                    //ArrayAdapter customAdapter = new ArrayAdapter(ViewTasksActivity.this, android.R.layout.simple_list_item_1, tasks);
                    MyTaskAdapter customAdapter = new MyTaskAdapter(ViewMyTasksActivity.this, R.layout.my_task_view, tasks);

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

}
