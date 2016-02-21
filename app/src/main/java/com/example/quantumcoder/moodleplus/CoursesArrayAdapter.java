package com.example.quantumcoder.moodleplus;

/**
 * Created by Manish Singh on 21-02-2016.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CoursesArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public CoursesArrayAdapter(Context context, String[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.courselist_layout, parent, false);
        //TextView textView = (TextView) rowView.findViewById(R.id.no);
        TextView textView2 = (TextView) rowView.findViewById(R.id.courseName);

        //textView.setText(Integer.toString(position));
        textView2.setText(values[position]);
        // change the icon for Windows and iPhone
        String s = values[position];

        return rowView;
    }
}

