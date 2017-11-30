package dk.sdu.lahan14.cleanthestreet.Network;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dk.sdu.lahan14.cleanthestreet.Database.Database;
import dk.sdu.lahan14.cleanthestreet.Database.DatabaseHelper;
import dk.sdu.lahan14.cleanthestreet.R;
import dk.sdu.lahan14.cleanthestreet.Util.Task;

/**
 * Created by lasse on 15-11-2017.
 */

public class TaskAdapter extends ArrayAdapter<Task>{

    public TaskAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }


    public TaskAdapter(Context context, int resource, List<Task> items) {
        super(context, resource, items);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        String[] projection = {
                Database.UpvotedTasksEntry._ID,
                Database.UpvotedTasksEntry.COLUMN_TASK_ID,
        };

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor query = db.query(Database.UpvotedTasksEntry.TABLE_NAME, projection, null, null, null, null, null);

        query.moveToNext();
        int listSize = query.getCount();

        List<Integer> upvotedTasks = new ArrayList<>();

        for(int i = 0; i < listSize; i++){
            upvotedTasks.add(query.getInt(1));
            query.moveToNext();
        }

        View v = convertView;

      //  ImageButton upvote = (ImageButton) v.findViewById(R.id.imageButton);


        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.task_view, null);
        }

        Task p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.score);
            TextView tt2 = (TextView) v.findViewById(R.id.description);
            TextView tt3 = (TextView) v.findViewById(R.id.distance);
            ImageButton upvoteButton = (ImageButton) v.findViewById(R.id.imageButton);
            upvoteButton.setTag(R.id.upvoteId, p.getId());
            upvoteButton.setFocusable(View.NOT_FOCUSABLE);
            if(upvotedTasks.contains(p.getId())) {
                upvoteButton.setColorFilter(Color.argb(255, 130, 130, 130));
            } else {
                upvoteButton.setColorFilter(Color.argb(255, 0, 255, 0));
            }



            if (tt1 != null) {
                tt1.setText((Float.toString(p.getScore())));


            }

            if (tt2 != null) {
                tt2.setText(p.getDescription());
            }

            if(tt3!= null) {
                float distanceInMeters = p.getDistance();
                if (distanceInMeters > 1000) {
                    tt3.setText(String.format("Distance: %.2f km", distanceInMeters / 1000.0 ));
                } else {
                    tt3.setText(String.format("Distance: %d m", (int) distanceInMeters));
                }
            }
            //v.setTag(R.id.upvoteId, p.getId());
        }

        return v;
    }

}
