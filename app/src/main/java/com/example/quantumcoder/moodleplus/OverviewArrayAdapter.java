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

public class OverviewArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private final String[] dates;
    public OverviewArrayAdapter(Context context, String[] values ,String[] date) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.dates= date;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.overview_list, parent, false);
        //TextView textView = (TextView) rowView.findViewById(R.id.no);
        TextView textView1 = (TextView) rowView.findViewById(R.id.week);
        TextView textView2 = (TextView) rowView.findViewById(R.id.timeline);
        //textView.setText(Integer.toString(position));
        textView1.setText(values[position]);
        textView2.setText(dates[position]);
        // change the icon for Windows and iPhone


        return rowView;
    }
}

