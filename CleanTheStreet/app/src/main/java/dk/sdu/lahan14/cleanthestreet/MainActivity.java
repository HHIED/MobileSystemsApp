package dk.sdu.lahan14.cleanthestreet;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import dk.sdu.lahan14.cleanthestreet.Database.UserEntry;

public class MainActivity extends AppCompatActivity {
    private AsyncHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setContentView(R.layout.activity_view_tasks);
        client = new AsyncHttpClient();

        createUser();
    }

    private void createUser(){
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());

        SQLiteDatabase db_out = databaseHelper.getReadableDatabase();

        SQLiteStatement s = db_out.compileStatement("SELECT * FROM sqlite_master WHERE name ='" + UserEntry.TABLE_NAME + "' and type='table'");
        long count = s.simpleQueryForLong();

        if(count <= 0) {
            client.post("http://getstarteddotnet-disjunctive-petulance.eu-gb.mybluemix.net/api/users/create", new AsyncHttpResponseHandler() {
                DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());

                @Override
                public void onStart() {
                    // called before request is started
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    try {
                        String[] projection = {
                                UserEntry._ID,
                                UserEntry.COLUMN_USER_ID,
                        };
                        String json = new String(response, "UTF-8");
                        Gson converter = new Gson();
                        User user = converter.fromJson(json, User.class);
                        SQLiteDatabase db_in = databaseHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(UserEntry.COLUMN_USER_ID, user.getId());

                        db_in.insert(UserEntry.TABLE_NAME, null, values);

                    } catch (UnsupportedEncodingException e) {
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

    public void viewTasksClick(View view ){
        Intent intent = new Intent(this, ViewTasksActivity.class);
        startActivity(intent);
    }

}
