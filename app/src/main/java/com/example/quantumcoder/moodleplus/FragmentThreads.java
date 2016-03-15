package com.example.quantumcoder.moodleplus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static com.android.volley.VolleyLog.TAG;


public class FragmentThreads extends Fragment {

    private static int SPLASH_TIME_OUT =500 ;

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v("ListFragment", "onActivityCreated().");
        Log.v("ListsavedInstanceState", savedInstanceState == null ? "true" : "false");

        final String coursecode = MainActivity.selectedcoursecode;
        String threads_url = ("http://"+LoginActivity.ip + "/courses/course.json/" +coursecode+"/threads").trim();
        Log.d(TAG, threads_url);

        //this is entered when the user has just logged in...
        RequestQueue threadsRequestQueue = Volley.newRequestQueue(getContext(), SessionManager.httpStack);
        //requestqueue is made using http-stack as we need to check the sessions of the logged in user
        JsonObjectRequest threadsRequest = new JsonObjectRequest
                (Request.Method.GET, threads_url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        final JSONObject course_specific_data = response;
                        SessionManager.threads.put(coursecode,course_specific_data);
                        displayListView();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        //pDialog.setMessage(error.getMessage());
                        Toast.makeText(getContext(), "Failed to fetch course "+coursecode+" threads data", LENGTH_LONG).show();
                        //pDialog.setMessage(error.getCause().toString());
                        //pDialog.hide();
                    }
                });
        threadsRequestQueue.add(threadsRequest);



        //Generate list View from ArrayList
        //displayListView();

    }
    private void displayListView() {

        String  coursecode = MainActivity.selectedcoursecode;
        JSONObject courseobject = SessionManager.threads.get(coursecode);
        JSONArray threads_array = null;
        try {
            threads_array = (JSONArray) courseobject.get("course_threads");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String t[] = new String[threads_array.length()] ;
        String m[] = new String[threads_array.length()] ;
        int id[] = new int[threads_array.length()];

        for(int i=0;i<threads_array.length();i++){
            try {
                t[i] = threads_array.getJSONObject(i).getString("title");
                m[i] = threads_array.getJSONObject(i).getString("description");
                id[i] =threads_array.getJSONObject(i).getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //create an ArrayAdaptar from the String Array
        ThreadArrayAdapter dataAdapter = new ThreadArrayAdapter(getContext(), t, m, id);
        ListView listView = (ListView) getView().findViewById(R.id.threads);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
        //enables filtering for the contents of the given ListView
        listView.setTextFilterEnabled(true);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(), "Thread selected", LENGTH_SHORT).show();

                TextView textview = (TextView) view.findViewById(R.id.thread_id);
                MainActivity.selectedthread = Integer.parseInt((String) textview.getText());
                Log.d(TAG, String.valueOf(MainActivity.selectedthread));

                /*
                FragmentTransaction xfragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                xfragmentTransaction.replace(R.id.containerView, new FragmentAssignmentDisplay()).commit();
                */
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // Start your app main activity
                        Intent i = new Intent(getActivity(), IndividualThreadActivity.class);
                        startActivity(i);
                        // overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out) ;
                        // close this activity
                        //getActivity().finish();
                    }
                }, SPLASH_TIME_OUT);
            }


        });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      //  Toast.makeText(getActivity().getApplicationContext(), "Login failed.", LENGTH_LONG).show();
        return inflater.inflate(R.layout.threads_layout,null);
    }


}
