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

import static android.widget.Toast.*;
import static com.android.volley.VolleyLog.TAG;

public class MainActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



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

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the FragmentTabs as the first Fragment
         */

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new FragmentThreads()).commit();

        /**
         * Setup click events on the Navigation View Items.
         */

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();



                if (menuItem.getItemId() == R.id.nav_item_notif) {
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,new FragmentNotifications()).commit();

                }

                if (menuItem.getItemId() == R.id.nav_item_home) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView,new FragmentHome()).commit();
                }
                if (menuItem.getItemId() == R.id.nav_item_gardes) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView,new FragmentLeftGrade()).commit();
                }
                if (menuItem.getItemId() == R.id.nav_item_logout) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    FragmentLogout fragment = new FragmentLogout() ;
                    xfragmentTransaction.attach(fragment); //.commit();

                    xfragmentTransaction.replace(R.id.containerView, fragment).commit();
                }
                if (menuItem.getItemId() == R.id.nav_item_profile) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView,new FragmentProfile()).commit();
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
            String course_url = "http://"+LoginActivity.ip+"/courses/list.json";

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
                requestQueue.add(courseRequest);
            }

        }
    }

    public void callGrades(){
        //checks if the Session Manager is logged in
        if (SessionManager.isLoggedIn()) {
            //making the url by concatinating ip and adding grades list API url given in pdf
            String grades_url = "http://"+LoginActivity.ip+"/default/grades.json";

            //makes a dialog box which shows that grades are being loaded
            //final ProgressDialog pDialog = new ProgressDialog(this);
            //pDialog.setMessage("Loading...");
            //pDialog.show();

            //grades object JSON Array is created
            final JSONObject[] gradesobject = {null};

            //for the first time get grades data will return null as till now set grades data has not been called
            if (SessionManager.getGrades() == null) {
                //this is entered when the user has just logged in...
                RequestQueue requestQueue = Volley.newRequestQueue(this, SessionManager.httpStack);
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
                requestQueue.add(gradesRequest);
            }

        }
    }


}
