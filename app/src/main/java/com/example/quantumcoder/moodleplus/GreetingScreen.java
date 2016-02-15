package com.example.quantumcoder.moodleplus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class GreetingScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(GreetingScreen.this, LoginActivity.class);
                startActivity(i);
                // overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                overridePendingTransition(R.anim.right_in, R.anim.left_out) ;
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
