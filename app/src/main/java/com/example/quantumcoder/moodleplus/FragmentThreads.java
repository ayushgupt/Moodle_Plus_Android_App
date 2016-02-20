package com.example.quantumcoder.moodleplus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_LONG;


public class FragmentThreads extends Fragment {

    @Nullable

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      //  Toast.makeText(getActivity().getApplicationContext(), "Login failed.", LENGTH_LONG).show();
        return inflater.inflate(R.layout.threads_layout,null);
    }
}
