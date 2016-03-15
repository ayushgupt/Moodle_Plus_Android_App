package com.example.quantumcoder.moodleplus;

/**
 * Created by Manish Singh on 21-02-2016.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DownloadArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private final String[] deadlines ;
    private final String[] privacy ;

    public DownloadArrayAdapter(Context context, String[] values, String[] dead , String[] pri) {
        super(context, -1, values);
        this.deadlines=dead ;
        this.context = context;
        this.values = values;
        this.privacy=pri ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.download_list, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.download_title);
        TextView textView2 = (TextView) rowView.findViewById(R.id.download_time);
        TextView textView3 = (TextView) rowView.findViewById(R.id.download_privacy);
        TextView number_View = (TextView) rowView.findViewById(R.id.download_sno);
        textView.setText(values[position]);
        textView2.setText(deadlines[position]);
        textView3.setText(privacy[position]);
        // change the icon for Windows and iPhone
        number_View.setText(Integer.toString(position+1)+")");
        return rowView;
    }
}

