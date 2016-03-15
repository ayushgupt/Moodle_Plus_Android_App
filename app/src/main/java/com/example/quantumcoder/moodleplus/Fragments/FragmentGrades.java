package com.example.quantumcoder.moodleplus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static com.android.volley.VolleyLog.TAG;

public class FragmentGrades extends Fragment {

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v("ListFragment", "onActivityCreated().");
        Log.v("ListsavedInstanceState", savedInstanceState == null ? "true" : "false");

        final String coursecode = MainActivity.selectedcoursecode;
        String grades_url = ("http://"+LoginActivity.ip + "/courses/course.json/" +coursecode+"/grades").trim();
        Log.d(TAG, grades_url);

        //this is entered when the user has just logged in...
        RequestQueue gradesRequestQueue = Volley.newRequestQueue(getContext(), SessionManager.httpStack);
        //requestqueue is made using http-stack as we need to check the sessions of the logged in user
        JsonObjectRequest gradesRequest = new JsonObjectRequest
                (Request.Method.GET, grades_url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        final JSONObject grades_data = response;
                        SessionManager.grades.put(coursecode,grades_data);
                        displayListView();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        //pDialog.setMessage(error.getMessage());
                        Toast.makeText(getContext(), "Failed to fetch course "+coursecode+" grades data", LENGTH_LONG).show();
                        //pDialog.setMessage(error.getCause().toString());
                        //pDialog.hide();
                    }
                });
        gradesRequestQueue.add(gradesRequest);



        //Generate list View from ArrayList
        //displayListView();


    }

    private void displayListView() {

        String  coursecode = MainActivity.selectedcoursecode;
        JSONObject gradesobject = SessionManager.grades.get(coursecode);
        JSONArray grades_array = null;
        try {
            grades_array = (JSONArray) gradesobject.get("grades");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String t[] = new String[grades_array.length()] ;
        String m[] = new String[grades_array.length()] ;
        String weight[] = new String[grades_array.length()] ;
        String absmarks[] = new String[grades_array.length()] ;

        for(int i=0;i<grades_array.length();i++){
            try {
                t[i] = grades_array.getJSONObject(i).getString("name");
                m[i] = String.valueOf(grades_array.getJSONObject(i).getInt("score"));
                weight[i] = String.valueOf(grades_array.getJSONObject(i).getInt("weightage"));
                int out_of = grades_array.getJSONObject(i).getInt("out_of");
                float marks = Float.parseFloat(m[i]);
                float wt = Float.parseFloat(weight[i]);
                float abs = (float)(marks*wt)/(float)(out_of);
                absmarks[i] = String.format("%.2f",abs);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        //create an ArrayAdaptar from the String Array
        GradeArrayAdapter dataAdapter = new GradeArrayAdapter(getContext(), t, m ,weight, absmarks);
        ListView listView = (ListView) getView().findViewById(R.id.grades);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
        //enables filtering for the contents of the given ListView
        listView.setTextFilterEnabled(true);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(), "URL selected", LENGTH_SHORT).show();

                // Send the URL to the host activity
                //    mListener.onURLSelected(((TextView) view).getText().toString());

            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.grades_layout,null);

    }
}
