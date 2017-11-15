package dk.sdu.lahan14.cleanthestreet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lasse on 15-11-2017.
 */

public class TaskAdapter extends ArrayAdapter<dk.sdu.lahan14.cleanthestreet.Task>{

    public TaskAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }


    public TaskAdapter(Context context, int resource, List<Task> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.task_view, null);
        }

        Task p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.score);
            TextView tt2 = (TextView) v.findViewById(R.id.description);



            if (tt1 != null) {
                tt1.setText((Float.toString(p.getScore())));
            }

            if (tt2 != null) {
                tt2.setText(p.getDescription());
            }
        }

        return v;
    }

}
