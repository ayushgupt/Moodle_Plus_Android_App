package com.example.quantumcoder.moodleplus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.VolleyLog.TAG;

public class FragmentHome extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_layout,null);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            createCourseButtons(SessionManager.getCourseData());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // creates course buttons given json object
    //TODO: Change layout and design of course buttons
    public void createCourseButtons(JSONObject courseobject) throws JSONException
    {
        //The CourseObject contained Json of user and courses..So courses now has only course specific json..
        JSONArray courses = (JSONArray) courseobject.get("courses");
        String coursename = "";
        String coursecode = "";
        int courseId = 0;
        //For Loop for Making Buttons.. Button for each course is made in one loop
        for (int i = 0; i < courses.length(); i++)
        {
            //made a temporary jsonobject which has json of only one course at a time
            JSONObject course = null;
            try
            {
                course = courses.getJSONObject(i);
                Log.d(TAG, course.toString());
                coursename = (String) course.get("name");
                coursecode = (String) course.get("code");
                courseId = (int) course.get("id");
                //Course Name, Code and Id is assigned in different Temporary variables...
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            final Button courseButton = new Button(getActivity());
            final int finalCourseId = courseId;
            //Buttons Text is Set By concatenating Code and Name
            courseButton.setText(coursecode + ": " + coursename);
            //Buttons Id is its course Id
            courseButton.setId(finalCourseId);
            Log.d(TAG, coursecode);
            //Buttons Alignment
            courseButton.setGravity(Gravity.CENTER);
            courseButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            //What happens on Clicking The Button..???
            courseButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                }
            });

            View rootView = getView();

            //ll object of type Linear Layout is made which is basically instance of Linear Layout whose id is courses...
            //lp object of linear layout parameters are made
            RelativeLayout rl = (RelativeLayout) rootView.findViewById(R.id.layout_home);
            RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            rp.setMargins(20, 80, 0, 0);
            //Button and Parameters are added to the reference of Linear Layout we made...
            rl.addView(courseButton, rp);
        }

    }
}
