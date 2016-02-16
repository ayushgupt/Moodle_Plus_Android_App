package com.example.quantumcoder.moodleplus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;

import static android.widget.Toast.*;
import static com.android.volley.VolleyLog.TAG;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (SessionManager.isLoggedIn()) {
            /*
            HashMap<String,String> userdata = SessionManager.getUserDetails();
            String username = userdata.get(SessionManager.KEY_NAME);
            String password = userdata.get(SessionManager.KEY_PASSWORD);
            */
            Toast.makeText(getApplicationContext(), "Session created", LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), username + "\n" + password , LENGTH_LONG).show();


            String course_url = "http://"+LoginActivity.ip+"/courses/list.json";

            final ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            final JSONObject[] courseobject = {null};

            if (SessionManager.getCourseData() != null) {
                courseobject[0] = SessionManager.getCourseData();
            } else {

                RequestQueue requestQueue = Volley.newRequestQueue(this, SessionManager.httpStack);

                JsonObjectRequest courseRequest = new JsonObjectRequest
                        (Request.Method.GET, course_url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());
                                //pDialog.setMessage("Response: "+ response.toString());
                                pDialog.hide();
                                courseobject[0] = response;
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d(TAG, "Error: " + error.getMessage());
                                //pDialog.setMessage(error.getMessage());
                                Toast.makeText(getApplicationContext(), "Failed to fetch course data", LENGTH_LONG).show();
                                //pDialog.setMessage(error.getCause().toString());
                                pDialog.hide();

                            }
                        });

                if (courseobject[0] != null) {
                    try {
                        createCourseButtons(courseobject[0]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SessionManager.setCourseData(courseobject[0]);
                } else {
                    Toast.makeText(getApplicationContext(), "Not registered for any course", LENGTH_LONG).show();
                }

                requestQueue.add(courseRequest);


            }
        }
    }

    // creates course buttons given json object
    public void createCourseButtons(JSONObject courseobject) throws JSONException {
        JSONArray courses = (JSONArray) courseobject.get("courses");
        String coursename = "";
        String coursecode = "";
        int courseId = 0;
        for (int i = 0; i < courses.length(); i++) {
            JSONObject course = null;
            try {
                course = courses.getJSONObject(i);
                coursename = (String) course.get("name");
                coursecode = (String) course.get("code");
                courseId = (int) course.get("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final Button courseButton = new Button(getApplicationContext());
            final int finalCourseId = courseId;
            courseButton.setText(coursecode + ": " + coursename);
            courseButton.setId(finalCourseId);
            courseButton.setGravity(Gravity.CENTER);
            courseButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            courseButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle coursebundle = new Bundle();
                    coursebundle.putInt("courseId", finalCourseId);
                    Intent i = new Intent(getApplicationContext(), CoursePage.class);
                    i.putExtras(coursebundle);
                    startActivity(i);
                    finish();
                }
            });

            LinearLayout ll = (LinearLayout) findViewById(R.id.courses);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(20, 5, 0, 0);
            ll.addView(courseButton, lp);
        }

    }
}
