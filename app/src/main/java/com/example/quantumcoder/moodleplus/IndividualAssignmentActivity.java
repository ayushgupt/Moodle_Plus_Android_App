package com.example.quantumcoder.moodleplus;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
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

import static android.widget.Toast.LENGTH_LONG;
import static com.android.volley.VolleyLog.TAG;

public class IndividualAssignmentActivity extends AppCompatActivity {

    JSONObject assignment_specific_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indi_ass);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final int assignment_no = MainActivity.selectedassignment;
        String indi_assignment_url = ("http://" + LoginActivity.ip + "/courses/assignment.json/" + assignment_no).trim();
        //Log.d(TAG, indi_assignment_url);

        //this is entered when the user has just logged in...
        RequestQueue assignmentRequestQueue = Volley.newRequestQueue(getApplicationContext(), SessionManager.httpStack);
        //requestqueue is made using http-stack as we need to check the sessions of the logged in user
        JsonObjectRequest assignmentRequest = new JsonObjectRequest
                (Request.Method.GET, indi_assignment_url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        assignment_specific_data = response;
                        SessionManager.assignment_data.put(assignment_no, assignment_specific_data);
                        displayData();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        //pDialog.setMessage(error.getMessage());
                        Toast.makeText(getApplicationContext(), "Failed to fetch " + assignment_no + " data", LENGTH_LONG).show();
                        //pDialog.setMessage(error.getCause().toString());
                        //pDialog.hide();
                    }
                });
        assignmentRequestQueue.add(assignmentRequest);

    }

    private void displayData() {


        try {
            TextView nameview = (TextView) findViewById(R.id.indi_ass_name);
            nameview.setText(assignment_specific_data.getJSONObject("assignment").getString("name"));

            TextView detailsview = (TextView) findViewById(R.id.ass_info);
            detailsview.setText(Html.fromHtml((String) assignment_specific_data.getJSONObject("assignment").get("description")));

            TextView createdview = (TextView) findViewById(R.id.created_at);
            createdview.setText(assignment_specific_data.getJSONObject("assignment").getString("created_at"));

            TextView deadlineview = (TextView) findViewById(R.id.deadline);
            deadlineview.setText(assignment_specific_data.getJSONObject("assignment").getString("deadline"));

            TextView latedaysview = (TextView) findViewById(R.id.late_days);
            latedaysview.setText(assignment_specific_data.getJSONObject("assignment").getString("late_days_allowed"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}