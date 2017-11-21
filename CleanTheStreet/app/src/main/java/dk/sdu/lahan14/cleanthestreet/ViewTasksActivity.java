package dk.sdu.lahan14.cleanthestreet;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.*;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.gson.Gson;
import com.loopj.android.http.*;
import com.loopj.android.http.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.nio.file.Files;
import java.io.File;

import java.io.Console;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.util.EntityUtils;


public class ViewTasksActivity extends AppCompatActivity {

    private AsyncHttpClient client;
    private Gson gson;
    private ImageButton upvoteButton;
    private UrlBuilder urlBuilder;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);
        upvoteButton = (ImageButton) findViewById(R.id.imageButton);
        client = new AsyncHttpClient();
        gson = new Gson();
        urlBuilder = new UrlBuilder();
        try {
            upvoteTask(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void upvoteClick(View view) throws IOException {
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

                String json = null;
                try {
                    json = new String(response, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                JSONObject jsonArray = null;
                try {
                    jsonArray = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayList<Task> tasks = new ArrayList();

                        TaskDto task = gson.fromJson(jsonArray.toString(), TaskDto.class);


                        TaskDto taskObject = task;
                        tasks.add(taskObject.toTask());

                        ListView customListView = (ListView) findViewById(R.id.tasksListView);
                        // get data from the table by the ListAdapter
                        //ArrayAdapter customAdapter = new ArrayAdapter(ViewTasksActivity.this, android.R.layout.simple_list_item_1, tasks);
                        TaskAdapter customAdapter = new TaskAdapter(ViewTasksActivity.this, R.layout.task_view, tasks);

                        customListView.setAdapter(customAdapter);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void upvoteTask(int taskId) throws IOException {
        TaskDto task = new TaskDto(taskId);

        String jsonTask = gson.toJson(task);
        StringEntity entity = new StringEntity(jsonTask);
        byte[] image = android.util.Base64.decode("E04FD020ea3a6910a2d808002b30309d", android.util.Base64.URL_SAFE);


        File fi = new File("IMG_20171117_171116_01.jpg");
        byte[] fileContent = Files.readAllBytes(fi.toPath());

        TaskDto taskDto = new TaskDto(1, new User(1), new User(1), fileContent, "test", 1, 2, 3);
        final RequestHandle handle2 = client.post(urlBuilder.createTaskUrl(taskDto), null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
