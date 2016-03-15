package com.example.quantumcoder.moodleplus;

/**
 * Created by Manish Singh on 21-02-2016.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LeftGradeArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private final String[] deadlines ;
    private final String[] weight ;
    private final String[] marks ;
    private final String[] course ;
    public LeftGradeArrayAdapter(Context context,  String[] cou ,String[] values, String[] dead ,String[] wei, String[] mar) {
        super(context, -1, values);
        this.course = cou ;
        this.deadlines=dead ;

        this.context = context;
        this.values = values;
        this.weight=wei ;
        this.marks=mar;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.left_gradelist, parent, false);
        TextView B1 = (TextView) rowView.findViewById(R.id.left_grade_coursename);
        B1.setText(course[position]);
        TextView B2 = (TextView) rowView.findViewById(R.id.left_grade_name);
        B2.setText(values[position]);
        TextView B5 = (TextView) rowView.findViewById(R.id.left_grade_score);
        B5.setText("Score:"+deadlines[position]);

        TextView B3 = (TextView) rowView.findViewById(R.id.left_grade_weight);
        B3.setText("Weight "+weight[position]);
        TextView B4 = (TextView) rowView.findViewById(R.id.left_grade_marks);
        B4.setText("Absolute Marks "+marks[position]);


        return rowView;
    }
}

