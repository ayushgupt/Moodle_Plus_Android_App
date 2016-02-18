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

        //Id of the specific course which was put into extras is now extracted into coursebundle
        Bundle coursebundle = getIntent().getExtras();
        int courseId = coursebundle.getInt("courseId");
        //course id is hence succesfully passed...!!!
        JSONArray courses = null;
        try
        {
            //courses now has json array of all the courses.!!
            courses = (JSONArray) SessionManager.getCourseData().get("courses");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject coursedata = null;
        if (courses != null) {
            try
            {
                //course data has json only of that course
                coursedata = courses.getJSONObject(courseId-1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        final ProgressDialog pDialog = new ProgressDialog(this);
        //course data is shown to message to test whether things are working till now..
        pDialog.setMessage(coursedata.toString());
        pDialog.show();
    }

}
