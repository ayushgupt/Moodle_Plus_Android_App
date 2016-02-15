package com.example.quantumcoder.moodleplus;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;

import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if(SessionManager.isLoggedIn()){
            HashMap<String,String> userdata = SessionManager.getUserDetails();
            String username = userdata.get(SessionManager.KEY_NAME);
            String password = userdata.get(SessionManager.KEY_PASSWORD);

            Toast.makeText(getApplicationContext(), "Session created", LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), username + "\n" + password , LENGTH_LONG).show();
        }
    }

}
