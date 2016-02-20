package com.example.quantumcoder.moodleplus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;

public class FragmentLogout extends Fragment {
    private static int SPLASH_TIME_OUT = 500;

    Activity temp ;

    @Nullable

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Logging out");
        pDialog.show();
        Toast.makeText( getActivity().getApplicationContext() ,"Succesfully logged out", LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                // overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                // close this activity
                getActivity().finish();
            }
        }, SPLASH_TIME_OUT);

        return inflater.inflate(R.layout.logout_layout,null);
            //move to loginactivity page




    }
}
