package com.example.quantumcoder.moodleplus;

/**
 * Created by Manish Singh on 21-02-2016.
 */
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NotifArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private final String[] deadlines ;

    public NotifArrayAdapter(Context context, String[] values, String[] dead) {
        super(context, -1, values);
        this.deadlines=dead ;
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.notification_list, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.notif_description);
        TextView textView2 = (TextView) rowView.findViewById(R.id.notif_time);
        TextView number_View = (TextView) rowView.findViewById(R.id.notif_sno);
        textView.setText(Html.fromHtml(values[position]));
        textView2.setText(deadlines[position]);
        // change the icon for Windows and iPhone
        number_View.setText(Integer.toString(position+1)+")");
        return rowView;
    }
}

