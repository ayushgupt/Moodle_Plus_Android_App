package com.example.quantumcoder.moodleplus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class IndividualThreadActivity extends AppCompatActivity {

    JSONObject thread_specific_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indi_thread);
        Log.v("ListFragment", "onActivityCreated().");
        Log.v("ListsavedInstanceState", savedInstanceState == null ? "true" : "false");

        int thread_id = MainActivity.selectedthread;
        String thread_url = ("http://"+LoginActivity.ip + "/threads/thread.json/" + thread_id ).trim();
        Log.d(TAG, thread_url);

        //this is entered when the user has just logged in...
        RequestQueue threadsRequestQueue = Volley.newRequestQueue(getApplicationContext(), SessionManager.httpStack);
        //requestqueue is made using http-stack as we need to check the sessions of the logged in user
        JsonObjectRequest threadsRequest = new JsonObjectRequest
                (Request.Method.GET, thread_url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        thread_specific_data = response;

                        TextView codeview = (TextView) findViewById(R.id.thread_course_no);
                        codeview.setText("#"+MainActivity.selectedcoursecode);

                        String created_at = "";
                        TextView deadlineview = (TextView) findViewById(R.id.deadline);
                        try {
                            created_at = thread_specific_data.getJSONObject("thread").getString("created_at");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        deadlineview.setText(created_at);


                        String assignment_name = "";
                        TextView nameview = (TextView) findViewById(R.id.ass_name);
                        try {
                            assignment_name = thread_specific_data.getJSONObject("thread").getString("title");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        nameview.setText(assignment_name);



                        displayListView();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        //pDialog.setMessage(error.getMessage());
                        Toast.makeText(getApplicationContext(), "Failed to fetch thread data", LENGTH_LONG).show();
                        //pDialog.setMessage(error.getCause().toString());
                        //pDialog.hide();
                    }
                });
        threadsRequestQueue.add(threadsRequest);


    }

    private void displayListView() {

        JSONArray comments_array = null;
        JSONArray comments_users_array = null;
        try {
            comments_array = (JSONArray) thread_specific_data.getJSONArray("comments");
            comments_users_array = (JSONArray) thread_specific_data.getJSONArray("comment_users");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String c[] = new String[comments_array.length()] ;
        String t[] = new String[comments_array.length()] ;

        for(int i=0;i<comments_array.length();i++){
            try {
                String user_firstname = comments_users_array.getJSONObject(i).getString("first_name");
                String user_lastname = comments_users_array.getJSONObject(i).getString("last_name");
                String comment = comments_array.getJSONObject(i).getString("description");
                c[i] = user_firstname+" "+user_lastname+"     "+comment;
                t[i] = comments_array.getJSONObject(i).getString("created_at");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //create an ArrayAdaptar from the String Array
        CommentsArrayAdapter dataAdapter = new CommentsArrayAdapter(this,c ,t);
        ListView listView = (ListView) findViewById(R.id.comments);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
        //enables filtering for the contents of the given ListView
        listView.setTextFilterEnabled(true);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(), "URL selected", LENGTH_SHORT).show();

                // Send the URL to the host activity
                //    mListener.onURLSelected(((TextView) view).getText().toString());

            }
        });
    }

    public void addComment(View view){
        EditText commenttext = (EditText) findViewById(R.id.title);
        String comment = commenttext.getText().toString();
        String commentquery = "";
        try {
            commentquery = URLEncoder.encode(comment,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        int thread_id = MainActivity.selectedthread;
        String comment_url = ("http://"+LoginActivity.ip + "/threads/post_comment.json?thread_id=" + thread_id +"&description=" + commentquery).trim();
        Log.d(TAG, comment_url);

        //this is entered when the user has just logged in...
        RequestQueue commentsRequestQueue = Volley.newRequestQueue(getApplicationContext(), SessionManager.httpStack);
        //requestqueue is made using http-stack as we need to check the sessions of the logged in user
        JsonObjectRequest commentsRequest = new JsonObjectRequest
                (Request.Method.GET, comment_url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        Toast.makeText(getApplicationContext(), "Added comment", LENGTH_LONG).show();
                        // reload activity
                        finish();
                        startActivity(getIntent());

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        //pDialog.setMessage(error.getMessage());
                        Toast.makeText(getApplicationContext(), "Failed to add comment", LENGTH_LONG).show();
                        //pDialog.setMessage(error.getCause().toString());
                        //pDialog.hide();
                    }
                });
        commentsRequestQueue.add(commentsRequest);

    }
}
