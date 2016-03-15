package com.example.quantumcoder.moodleplus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.widget.Toast.LENGTH_SHORT;
import static com.android.volley.VolleyLog.TAG;

public class FragmentHome extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_layout,null);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v("HomeFragment", "onActivityCreated().");
        Log.v("ListsavedInstanceState", savedInstanceState == null ? "true" : "false");

        //Generate list View from ArrayList
        displayListView();

    }
    private void displayListView() {

        JSONObject courseobject = SessionManager.getCourseData();
        JSONArray courses = null;
        try {
            courses = (JSONArray) courseobject.get("courses");
        } catch (JSONException e) {
            Toast.makeText(getContext(),"Not registered for any courses",Toast.LENGTH_LONG).show();
        }
        //Array of courses
        String t[] = new String[courses.length()];
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

                t[i] = coursecode +":"+ coursename;
            } catch (JSONException e)
            {
                e.printStackTrace();
            }

        }


        //create an ArrayAdaptar from the String Array
        CoursesArrayAdapter dataAdapter = new CoursesArrayAdapter(getContext(), t);
        ListView listView = (ListView) getView().findViewById(R.id.Courses);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
        //enables filtering for the contents of the given ListView
        listView.setTextFilterEnabled(true);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(), "Course selected", LENGTH_SHORT).show();
                TextView textview = (TextView) view.findViewById(R.id.courseName);
                MainActivity.selectedcoursecode = textview.getText().toString().split(":")[0];
                Log.d(TAG,MainActivity.selectedcoursecode);

                FragmentTransaction xfragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                xfragmentTransaction.replace(R.id.containerView, new FragmentTabs()).commit();


            }
        });
    }
    /*
    @Override
    public void onStart() {
        super.onStart();
        try {
            createCourseButtons(SessionManager.getCourseData());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
*/
    // creates course buttons given json object
    //TODO: Change layout and design of course buttons
    /*
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
    */
}
