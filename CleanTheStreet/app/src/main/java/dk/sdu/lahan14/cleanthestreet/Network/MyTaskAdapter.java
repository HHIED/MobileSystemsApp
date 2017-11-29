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

public class MyTaskAdapter extends ArrayAdapter<Task>{

    public MyTaskAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }


    public MyTaskAdapter(Context context, int resource, List<Task> items) {
        super(context, resource, items);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

      //  ImageButton upvote = (ImageButton) v.findViewById(R.id.imageButton);


        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.my_task_view, null);
        }

        Task p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.score);
            TextView tt2 = (TextView) v.findViewById(R.id.description);
            TextView tt3 = (TextView) v.findViewById(R.id.isCompleted);

            if (tt1 != null) {
                tt1.setText((Float.toString(p.getScore())));


            }

            if (tt2 != null) {
                tt2.setText(p.getDescription());
            }

            if(tt3!= null) {
                if(p.isCompleted()) {
                    tt3.setText("Is completed - click to approve");
                }
            }
            //v.setTag(R.id.upvoteId, p.getId());
        }

        return v;
    }

}
