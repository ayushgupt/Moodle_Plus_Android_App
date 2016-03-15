package com.example.quantumcoder.moodleplus;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import static android.widget.Toast.LENGTH_LONG;
import static com.android.volley.VolleyLog.TAG;

/**
 * Created by quantumcoder on 2/23/2016.
 */
public class FragmentAssignmentDisplay extends Fragment {

    JSONObject assignment_specific_data;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v("AssignmentDisplay", "onActivityCreated().");
        Log.v("AssignmentDisplay", savedInstanceState == null ? "true" : "false");

        final int assignment_no = MainActivity.selectedassignment;
        String indi_assignment_url = ("http://"+LoginActivity.ip + "/courses/assignment.json/" +assignment_no).trim();
        Log.d(TAG, indi_assignment_url);

        //this is entered when the user has just logged in...
        RequestQueue assignmentRequestQueue = Volley.newRequestQueue(getContext(), SessionManager.httpStack);
        //requestqueue is made using http-stack as we need to check the sessions of the logged in user
        JsonObjectRequest assignmentRequest = new JsonObjectRequest
                (Request.Method.GET, indi_assignment_url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        assignment_specific_data = response;
                        SessionManager.assignment_data.put(assignment_no,assignment_specific_data);
                        displayData();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        //pDialog.setMessage(error.getMessage());
                        Toast.makeText(getContext(), "Failed to fetch " + assignment_no + " data", LENGTH_LONG).show();
                        //pDialog.setMessage(error.getCause().toString());
                        //pDialog.hide();
                    }
                });
        assignmentRequestQueue.add(assignmentRequest);


    }

    private void displayData() {

        try {
            TextView nameview = (TextView)getView().findViewById(R.id.indi_ass_name);
            nameview.setText(assignment_specific_data.getJSONObject("assignment").getString("name"));

            TextView detailsview = (TextView)getView().findViewById(R.id.ass_info);
            nameview.setText(Html.fromHtml(assignment_specific_data.getJSONObject("assignment").getString("description")));

            TextView createdview = (TextView)getView().findViewById(R.id.created_at);
            createdview.setText(assignment_specific_data.getJSONObject("assignment").getString("created_at"));

            TextView deadlineview = (TextView)getView().findViewById(R.id.deadline);
            deadlineview.setText(assignment_specific_data.getJSONObject("assignment").getString("deadline"));

            TextView latedaysview = (TextView)getView().findViewById(R.id.late_days);
            latedaysview.setText(assignment_specific_data.getJSONObject("assignment").getString("late_days_allowed"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //  Toast.makeText(getActivity().getApplicationContext(), "Login failed.", LENGTH_LONG).show();
        return inflater.inflate(R.layout.indi_assignment_layout,null);
    }
}
