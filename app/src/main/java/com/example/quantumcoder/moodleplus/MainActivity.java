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

        //button present by default
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //checks if the Session Manager is logged in
        if (SessionManager.isLoggedIn()) {
            /*
            HashMap<String,String> userdata = SessionManager.getUserDetails();
            String username = userdata.get(SessionManager.KEY_NAME);
            String password = userdata.get(SessionManager.KEY_PASSWORD);
            */
            Toast.makeText(getApplicationContext(), "Session created", LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), username + "\n" + password , LENGTH_LONG).show();


            //making the url by concatinating ip and adding course list API url given in pdf
            String course_url = "http://"+LoginActivity.ip+"/courses/list.json";

            //makes a dialog box which shows that courses are being loaded
            final ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            //course object JSON Array is created
            final JSONObject[] courseobject = {null};

            //for the first time get course data will return null as till now set course data has not been called
            if (SessionManager.getCourseData() != null)
            {
                //This is entered if the user is returning to this course page from somewhere else...
                courseobject[0] = SessionManager.getCourseData();
                //The Dialog box is hidden
                pDialog.hide();
                if (courseobject[0] != null)
                {
                    try
                    {
                        //create course button is called again now...
                        createCourseButtons(courseobject[0]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else
                {
                    //The Response is null and the user is not registered in any course till now...
                    Toast.makeText(getApplicationContext(), "Not registered for any course", LENGTH_LONG).show();
                }
            } else {
                //this is entered when the user has just logged in...
                RequestQueue requestQueue = Volley.newRequestQueue(this, SessionManager.httpStack);
                //requestqueue is made using http-stack as we need to check the sessions of the logged in user
                JsonObjectRequest courseRequest = new JsonObjectRequest
                        (Request.Method.GET, course_url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());
                                //pDialog.setMessage("Response: "+ response.toString());
                                //0th element of the course object now has the response
                                courseobject[0] = response;
                                //So the dialog box is hid now...
                                pDialog.hide();
                                if (courseobject[0] != null)
                                {
                                    try
                                    {
                                        //now the json object of the course list is passed to the create button function
                                        createCourseButtons(courseobject[0]);
                                    } catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                    //Also the Course data is put in a hash map of the Session Manager Preferences so that we dont need to call this API again...
                                    SessionManager.setCourseData(courseobject[0]);
                                } else
                                {
                                    //The Response is null and the user is not registered in any course till now...
                                    Toast.makeText(getApplicationContext(), "Not registered for any course", LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                VolleyLog.d(TAG, "Error: " + error.getMessage());
                                //pDialog.setMessage(error.getMessage());
                                Toast.makeText(getApplicationContext(), "Failed to fetch course data", LENGTH_LONG).show();
                                //pDialog.setMessage(error.getCause().toString());
                                pDialog.hide();
                            }
                        });

                //Course Request is now added to the Request Queue...
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
                Log.d(TAG, course.toString());
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
            Log.d(TAG, coursecode);
            courseButton.setGravity(Gravity.CENTER);
            courseButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            courseButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle coursebundle = new Bundle();
                    coursebundle.putInt("courseId", finalCourseId);
                    Intent courseintent = new Intent(getApplicationContext(), CoursePage.class);
                    courseintent.putExtras(coursebundle);
                    startActivity(courseintent);
                    finish();
                }
            });

            LinearLayout ll = (LinearLayout) findViewById(R.id.courses);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(20,50,0,0);
            ll.addView(courseButton, lp);
        }

    }
}
