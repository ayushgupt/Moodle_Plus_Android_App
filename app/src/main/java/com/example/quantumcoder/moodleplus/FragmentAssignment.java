package com.example.quantumcoder.moodleplus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static com.android.volley.VolleyLog.TAG;


//http://www.mysamplecode.com/2012/08/android-fragment-example.html
//http://www.vogella.com/tutorials/AndroidListView/article.html

public class FragmentAssignment extends  Fragment  {
    private static int SPLASH_TIME_OUT =500 ;
    OnURLSelectedListener mListener;

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v("ListFragment", "onActivityCreated().");
        Log.v("ListsavedInstanceState", savedInstanceState == null ? "true" : "false");


        final String coursecode = MainActivity.selectedcoursecode;
        String assignment_url = ("http://"+LoginActivity.ip + "/courses/course.json/" +coursecode+"/assignments").trim();
        Log.d(TAG, assignment_url);

        //this is entered when the user has just logged in...
        RequestQueue assignmentsRequestQueue = Volley.newRequestQueue(getContext(), SessionManager.httpStack);
        //requestqueue is made using http-stack as we need to check the sessions of the logged in user
        JsonObjectRequest assignmentsRequest = new JsonObjectRequest
                (Request.Method.GET, assignment_url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        final JSONObject course_specific_data = response;
                        SessionManager.assignments.put(coursecode,course_specific_data);
                        displayListView();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        //pDialog.setMessage(error.getMessage());
                        Toast.makeText(getContext(), "Failed to fetch course "+coursecode+" assignments data", LENGTH_LONG).show();
                        //pDialog.setMessage(error.getCause().toString());
                        //pDialog.hide();
                    }
                });
        assignmentsRequestQueue.add(assignmentsRequest);

        //Generate list View from ArrayList
        //displayListView();

    }


    public interface OnURLSelectedListener {
        public void onURLSelected(String URL);
    }
   /*
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnURLSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnURLSelectedListener");
        }
    }
    */
    private void displayListView() {

        String  coursecode = MainActivity.selectedcoursecode;
        JSONObject courseobject = SessionManager.assignments.get(coursecode);
        JSONArray assignmentsarray = null;
        try {
            assignmentsarray = (JSONArray) courseobject.get("assignments");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String t[] = new String[assignmentsarray.length()] ;
        String m[] = new String[assignmentsarray.length()] ;
        int id[] = new int[assignmentsarray.length()];

        for(int i=0;i<assignmentsarray.length();i++){
            try {
                t[i] = assignmentsarray.getJSONObject(i).getString("name");
                m[i] = assignmentsarray.getJSONObject(i).getString("deadline");
                id[i] = assignmentsarray.getJSONObject(i).getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //create an ArrayAdaptar from the String Array
        assignListArrayAdapter dataAdapter = new assignListArrayAdapter(getContext(), t, m, id);
        ListView listView = (ListView) getView().findViewById(R.id.listofAss);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
        //enables filtering for the contents of the given ListView
        listView.setTextFilterEnabled(true);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(), "Assignment selected", LENGTH_SHORT).show();
                TextView textview = (TextView) view.findViewById(R.id.ass_id);
                MainActivity.selectedassignment = Integer.parseInt((String) textview.getText());
                Log.d(TAG, String.valueOf(MainActivity.selectedassignment));

                /*
                FragmentTransaction xfragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                xfragmentTransaction.replace(R.id.containerView, new FragmentAssignmentDisplay()).commit();
                */
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // Start your app main activity
                        Intent i = new Intent(getActivity(), IndividualAssignmentActivity.class);
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



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.assignment_layout,null);
    }
}
