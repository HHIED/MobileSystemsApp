package dk.sdu.lahan14.cleanthestreet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.ListView;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class ViewTasksActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://getstarteddotnet-disjunctive-petulance.eu-gb.mybluemix.net/api/tasks", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
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
        client.get("http://localhost:5000/api/tasks", new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray responseBody) {
                ArrayList< Task> tasks = new ArrayList();
                for(int i=0; i<responseBody.length(); i++) {
                    try {
                        JSONObject task = responseBody.getJSONObject(i);
                        int score = task.getInt("Score");
                        String description = task.getString("Description");
                        Task taskObject = new Task(score, description);
                        tasks.add(taskObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                ListView customListView = (ListView) findViewById(R.id.tasksListView);
                // get data from the table by the ListAdapter
                TaskAdapter customAdapter = new TaskAdapter(ViewTasksActivity.this, R.layout.task_view, tasks);

                customListView.setAdapter(customAdapter);

            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
}
