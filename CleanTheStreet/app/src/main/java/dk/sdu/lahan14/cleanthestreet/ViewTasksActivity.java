package dk.sdu.lahan14.cleanthestreet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.gson.Gson;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;


import java.io.Console;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.util.EntityUtils;


public class ViewTasksActivity extends AppCompatActivity {

    private  AsyncHttpClient client;
    private  Gson gson;
    private ImageButton upvoteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);
        upvoteButton = (ImageButton) findViewById(R.id.imageButton);
        client = new AsyncHttpClient();
        gson = new Gson();
        getTasks();

    }

    public void upvoteClick(View view) throws UnsupportedEncodingException {
        int i = (int) view.getTag(R.id.upvoteId);
        upvoteTask(i);
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
                    JSONArray jsonArray = new JSONArray(json);
                    ArrayList< Task> tasks = new ArrayList();
                    for(int i=0; i<jsonArray.length(); i++) {
                        try {
                            JSONObject task = jsonArray.getJSONObject(i);
                            int score = task.getInt("score");
                            String description = task.getString("description");
                            int id = task.getInt("id");
                            float latitude = (float) task.getDouble("lattitude");
                            float longitude = (float) task.getDouble("longtitude");

                            Task taskObject = new Task(id, score, description, latitude, longitude);
                            tasks.add(taskObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    ListView customListView = (ListView) findViewById(R.id.tasksListView);
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
