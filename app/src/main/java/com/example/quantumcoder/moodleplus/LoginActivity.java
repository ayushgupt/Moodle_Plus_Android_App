package com.example.quantumcoder.moodleplus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.*;
import static android.widget.Toast.LENGTH_LONG;
import static com.android.volley.VolleyLog.TAG;


/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity {
    static final String ip = "192.168.133.1";

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Session Manager
        new SessionManager(getApplicationContext());

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);

        mPasswordView = (EditText) findViewById(R.id.password);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        Button LoginButton = (Button) findViewById(R.id.login_button);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform validation here again to prevent submission of invalid fields
                if (checkValidation()) {
                    //Toast.makeText(LoginActivity.this, "Attempting to login", LENGTH_LONG).show();
                    attemptLogin();
                }

            }
        });

        if(!isNetworkConnected()){ makeText(LoginActivity.this, "Please connect to network", LENGTH_LONG).show(); }


    }


    private void attemptLogin() {
        /* Form is validated, can be submitted now */
        checkValidation();

        if(!isNetworkConnected()){ makeText(LoginActivity.this, "Please connect to network", LENGTH_LONG).show(); }


        // Display message while submission
        // Toast.makeText(this, "Submitting form...", Toast.LENGTH_LONG).show();

        final String username = mUsernameView.getText().toString().trim();
        final String password = mPasswordView.getText().toString().trim();
        String url = String.format("http://"+LoginActivity.ip+"/default/login.json?userid=%s&password=%s",username,password);


        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();


        /*
        Map<String, String> params = new HashMap<String, String>();                        //Map for input data
        params.put("userid", mUsernameView.getText().toString().trim());
        params.put("password", mPasswordView.getText().toString().trim());
        pDialog.setMessage("Loading");
        */

        /*
        CookieManager manager = new CookieManager();
        CookieHandler.setDefault(manager);
        */


        RequestQueue requestQueue = Volley.newRequestQueue( getApplicationContext(), SessionManager.httpStack  );

        JsonObjectRequest loginRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        //pDialog.setMessage(response.toString());
                        try {
                            pDialog.hide();
                            String str = response.getString("success") ;
                            if(str.equals("true")){

                                // Create cookies - implicitly passed to the httpUrlConnection
                                SessionManager.createLoginSession(username, password);
                                SessionManager.setUserData((JSONObject) response.get("user"));

                                Toast.makeText(getApplicationContext(), "Login Successful.", LENGTH_SHORT).show();
                                // Starting MainActivity
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Invalid username or password", LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        //pDialog.setMessage(error.getMessage());
                        Toast.makeText(getApplicationContext(),"Login failed.", LENGTH_LONG).show();
                        //pDialog.setMessage(error.getCause().toString());
                        pDialog.hide();

                    }
                });

             requestQueue.add(loginRequest);
    }


    //function for validating form data
    private boolean checkValidation() {
        // if(!isNetworkConnected()){ Toast.makeText(MainActivity.this,"Please connect to a network",LENGTH_LONG);   }

        //if(!Validation.isUsername(mUsernameView)){ makeText(LoginActivity.this, "Invalid username", LENGTH_LONG).show(); return false; }

        return true;
    }


    // Check if connected to network (internet assumed)
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


}

