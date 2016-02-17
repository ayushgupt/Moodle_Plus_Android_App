package com.example.quantumcoder.moodleplus;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CoursePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Bundle coursebundle = getIntent().getExtras();
        int courseId = coursebundle.getInt("courseId");

        JSONArray courses = null;
        try {
            courses = (JSONArray) SessionManager.getCourseData().get("courses");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject coursedata = null;
        if (courses != null) {
            try {
                coursedata = courses.getJSONObject(courseId-1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage(coursedata.toString());
        pDialog.show();
    }

}
