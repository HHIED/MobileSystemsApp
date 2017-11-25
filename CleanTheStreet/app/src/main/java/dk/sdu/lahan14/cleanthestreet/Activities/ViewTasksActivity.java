package dk.sdu.lahan14.cleanthestreet.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.gson.Gson;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import dk.sdu.lahan14.cleanthestreet.R;
import dk.sdu.lahan14.cleanthestreet.Util.Task;
import dk.sdu.lahan14.cleanthestreet.Network.TaskAdapter;
import dk.sdu.lahan14.cleanthestreet.Network.TaskDto;


public class ViewTasksActivity extends AppCompatActivity {

    private  AsyncHttpClient client;
    private  Gson gson;
    private ImageButton upvoteButton;
    ListView customListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);
        upvoteButton = (ImageButton) findViewById(R.id.imageButton);
        client = new AsyncHttpClient();
        gson = new Gson();
        getTasks();
        customListView = (ListView) findViewById(R.id.tasksListView);
        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Task task  = (Task) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(ViewTasksActivity.this, ViewTaskActivity.class);
                intent.putExtra("id", task.getId());
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
                                TaskDto dto = new TaskDto(id, getString(R.string.image_test), description, score, latitude, longitude);

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

    private void upvoteTask(int taskId) throws UnsupportedEncodingException {
        TaskDto task = new TaskDto(taskId);

        String jsonTask = gson.toJson(task);
        StringEntity entity = new StringEntity(jsonTask);
        final RequestHandle handle = client.post(ViewTasksActivity.this, "https://getstarteddotnet-pansophical-bedding.eu-gb.mybluemix.net/api/tasks/IncreaseScore", entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                getTasks();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
