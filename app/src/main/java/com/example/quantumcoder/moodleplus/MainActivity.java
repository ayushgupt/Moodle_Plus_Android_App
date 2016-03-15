package com.example.quantumcoder.moodleplus;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static android.widget.Toast.*;
import static com.android.volley.VolleyLog.TAG;

public class MainActivity extends AppCompatActivity {

    public DrawerLayout mDrawerLayout;
    public NavigationView mNavigationView;
    public FragmentManager mFragmentManager;
    public FragmentTransaction mFragmentTransaction;

    public static String selectedcoursecode;
    public static int selectedassignment;
    public static int selectedthread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        selectedcoursecode="cop290";

        // Setup UI of MainActivity
        setup();

        callCourse();
        //callGrades(); - called in response of courses request
    }

    void setup(){
        /**
         *Setup the DrawerLayout and NavigationView
         */

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff) ;

        //mNavigationView.addView();
        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the FragmentTabs as the first Fragment
         */

        mFragmentManager = getSupportFragmentManager();
        //mFragmentTransaction = mFragmentManager.beginTransaction();
        //mFragmentTransaction.replace(R.id.containerView, new FragmentThreads()).commit();

        /**
         * Setup click events on the Navigation View Items.
         */

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();


                if (menuItem.getItemId() == R.id.nav_item_notif) {
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, new FragmentNotifications()).commit();

                }

                if (menuItem.getItemId() == R.id.nav_item_home) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, new FragmentHome()).commit();
                }
                if (menuItem.getItemId() == R.id.nav_item_gardes) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, new FragmentLeftGrade()).commit();
                }
                if (menuItem.getItemId() == R.id.nav_item_courses) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, new FragmentHome()).commit();
                }
                if (menuItem.getItemId() == R.id.nav_item_logout) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    FragmentLogout fragment = new FragmentLogout();
                    xfragmentTransaction.attach(fragment); //.commit();

                    xfragmentTransaction.replace(R.id.containerView, fragment).commit();
                }
                if (menuItem.getItemId() == R.id.nav_item_profile) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, new FragmentProfile()).commit();
                }
                return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

    }

    public void callCourse(){
        //checks if the Session Manager is logged in
        if (SessionManager.isLoggedIn()) {
            //making the url by concatinating ip and adding course list API url given in pdf
            String course_url = ("http://"+LoginActivity.ip+"/courses/list.json").trim();

            //makes a dialog box which shows that courses are being loaded
            //final ProgressDialog pDialog = new ProgressDialog(this);
            //pDialog.setMessage("Loading...");
            //pDialog.show();

            //course object JSON Array is created
            final JSONObject[] courseobject = {null};

            //for the first time get course data will return null as till now set course data has not been called
            if (SessionManager.getCourseData() != null)
            {
                //This is entered if the user is returning to this course page from somewhere else...
                courseobject[0] = SessionManager.getCourseData();
                //The Dialog box is hidden
                //pDialog.hide();
                if (courseobject[0] != null)
                {
                    // Call home fragment - course buttons created there
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    //xfragmentTransaction.attach(new ) ;
                    xfragmentTransaction.replace(R.id.containerView,new FragmentHome()).commit();

                } else
                {
                    //The Response is null and the user is not registered in any course till now...
                    Toast.makeText(getApplicationContext(), "Not registered for any course", LENGTH_LONG).show();
                }
            } else {
                //this is entered when the user has just logged in...
                RequestQueue courseRequestQueue = Volley.newRequestQueue(this, SessionManager.httpStack);
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
                                //pDialog.hide();
                                if (courseobject[0] != null)
                                {
                                    //Also the Course data is put in a hash map of the Session Manager Preferences so that we dont need to call this API again...
                                    SessionManager.setCourseData(courseobject[0]);

                                    // Call home fragment - course buttons created there
                                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                                    xfragmentTransaction.replace(R.id.containerView,new FragmentHome()).commit();


                                    callGrades();
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
                                //pDialog.hide();
                            }
                        });

                //Course Request is now added to the Request Queue...
                courseRequestQueue.add(courseRequest);
            }

        }
    }

    public void callGrades(){
        //checks if the Session Manager is logged in
        if (SessionManager.isLoggedIn()) {
            //making the url by concatinating ip and adding grades list API url given in pdf
            String grades_url = ("http://"+LoginActivity.ip+"/default/grades.json").trim();

            //makes a dialog box which shows that grades are being loaded
            //final ProgressDialog pDialog = new ProgressDialog(this);
            //pDialog.setMessage("Loading...");
            //pDialog.show();

            //grades object JSON Array is created
            final JSONObject[] gradesobject = {null};

            //for the first time get grades data will return null as till now set grades data has not been called
            if (SessionManager.getGrades() == null) {
                //this is entered when the user has just logged in...
                RequestQueue gradesRequestQueue = Volley.newRequestQueue(this, SessionManager.httpStack);
                //requestqueue is made using http-stack as we need to check the sessions of the logged in user
                JsonObjectRequest gradesRequest = new JsonObjectRequest
                        (Request.Method.GET, grades_url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());
                                //pDialog.setMessage("Response: "+ response.toString());
                                //0th element of the grades object now has the response
                                gradesobject[0] = response;
                                //So the dialog box is hid now...
                                //pDialog.hide();
                                //Also the Grades data is put in a hash map of the Session Manager Preferences so that we dont need to call this API again...
                                SessionManager.setGrades(gradesobject[0]);
                                /*
                                try {
                                    callAssignments();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                */
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                VolleyLog.d(TAG, "Error: " + error.getMessage());
                                //pDialog.setMessage(error.getMessage());
                                Toast.makeText(getApplicationContext(), "Failed to fetch grades data", LENGTH_LONG).show();
                                //pDialog.setMessage(error.getCause().toString());
                                //pDialog.hide();
                            }
                        });

                //Grades Request is now added to the Request Queue...
                gradesRequestQueue.add(gradesRequest);
            }
            /*
            else {
                if(SessionManager.assignments==null){
                    try {
                        callAssignments();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            */
        }
    }

    public void callAssignments() throws JSONException {
        //checks if the Session Manager is logged in
        if (SessionManager.isLoggedIn()) {
            Log.d(TAG,"Fetching assignment data");

            JSONObject courseobject = SessionManager.getCourseData();
            JSONArray courses = null;
            String assignment_url =null;
            try {
                courses = (JSONArray) courseobject.get("courses");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for(int i=0;i<courses.length();i++) {
                JSONObject course = courses.getJSONObject(i);
                final String coursecode = (String) course.get("code");
                assignment_url = ("http://"+LoginActivity.ip + "/courses/course.json/" +coursecode+"/assignments").trim();
                Log.d(TAG, assignment_url);

                //this is entered when the user has just logged in...
                RequestQueue assignmentsRequestQueue = Volley.newRequestQueue(this, SessionManager.httpStack);
                //requestqueue is made using http-stack as we need to check the sessions of the logged in user
                JsonObjectRequest assignmentsRequest = new JsonObjectRequest
                        (Request.Method.GET, assignment_url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());
                                final JSONObject course_specific_data = response;
                                try {
                                    JSONArray assignmentarray = (JSONArray) course_specific_data.get("assignments");
                                    int numassignments = assignmentarray.length();
                                    for(int j=0;j<numassignments;j++){
                                        final int assignment_id = assignmentarray.getJSONObject(j).getInt("id");
                                        String individual_assignment_url = (LoginActivity.ip+"/courses/assignment.json/"+Integer.toString(assignment_id)).trim();
                                        final JSONArray detailed_assignment_array = new JSONArray();
                                        RequestQueue individualRequestQueue = Volley.newRequestQueue(getApplicationContext(), SessionManager.httpStack);
                                        //requestqueue is made using http-stack as we need to check the sessions of the logged in user
                                        JsonObjectRequest individualRequest = new JsonObjectRequest
                                                (Request.Method.GET, individual_assignment_url, null, new Response.Listener<JSONObject>() {

                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        Log.d(TAG, response.toString());
                                                        detailed_assignment_array.put(response);
                                                        try {
                                                            course_specific_data.put("assignments",detailed_assignment_array);
                                                            SessionManager.assignments.put(coursecode,course_specific_data);
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }, new Response.ErrorListener() {

                                                    @Override
                                                    public void onErrorResponse(VolleyError error)
                                                    {
                                                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                                                        //pDialog.setMessage(error.getMessage());
                                                        Toast.makeText(getApplicationContext(), "Failed to fetch assignment id "+assignment_id+" data", LENGTH_LONG).show();
                                                        //pDialog.setMessage(error.getCause().toString());
                                                        //pDialog.hide();
                                                    }
                                                });

                                        //Grades Request is now added to the Request Queue...
                                        individualRequestQueue.add(individualRequest);

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                VolleyLog.d(TAG, "Error: " + error.getMessage());
                                //pDialog.setMessage(error.getMessage());
                                Toast.makeText(getApplicationContext(), "Failed to fetch course "+coursecode+" assignments data", LENGTH_LONG).show();
                                //pDialog.setMessage(error.getCause().toString());
                                //pDialog.hide();
                            }
                        });

                //Grades Request is now added to the Request Queue...
                assignmentsRequestQueue.add(assignmentsRequest);
            }

        }
    }


    public void addThread(View view){
        EditText titletext = (EditText) findViewById(R.id.threadtitle);
        String title = titletext.getText().toString();

        EditText descriptiontext = (EditText) findViewById(R.id.content);
        String description = descriptiontext.getText().toString();

        String titlequery = ""; String descriptionquery = "";
        try {
            titlequery = URLEncoder.encode(title, "utf-8");
            descriptionquery = URLEncoder.encode(description,"utf-8");
            Log.d(TAG,title);
            Log.d(TAG,description);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String coursecode = MainActivity.selectedcoursecode;

        String thread_add_url = ("http://"+LoginActivity.ip +"/threads/new.json?title="+titlequery+"&description="+descriptionquery+"&course_code="+coursecode).trim();
        Log.d(TAG, thread_add_url);

        //this is entered when the user has just logged in...
        RequestQueue addThreadsRequestQueue = Volley.newRequestQueue(getApplicationContext(), SessionManager.httpStack);
        //requestqueue is made using http-stack as we need to check the sessions of the logged in user
        JsonObjectRequest addThreadsRequest = new JsonObjectRequest
                (Request.Method.GET, thread_add_url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        Toast.makeText(getApplicationContext(), "Added thread", LENGTH_LONG).show();
                        // go to tabs fragment
                        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.containerView, new FragmentThreads()).commit();


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        //pDialog.setMessage(error.getMessage());
                        Toast.makeText(getApplicationContext(), "Failed to add thread", LENGTH_LONG).show();
                        //pDialog.setMessage(error.getCause().toString());
                        //pDialog.hide();
                    }
                });
        addThreadsRequestQueue.add(addThreadsRequest);


    }
}
