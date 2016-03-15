package com.example.quantumcoder.moodleplus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.widget.Toast.LENGTH_SHORT;

public class FragmentLeftGrade extends Fragment {


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v("ListFragment", "onActivityCreated().");
        Log.v("ListsavedInstanceState", savedInstanceState == null ? "true" : "false");

        //Generate list View from ArrayList
        displayListView();

    }

    private void displayListView() {

        JSONObject gradesobject = SessionManager.getGrades();
        JSONArray grades_array = null;
        JSONArray course_array = null;
        try {
            grades_array = (JSONArray) gradesobject.get("grades");
            course_array = (JSONArray) gradesobject.get("courses");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String c[] = new String[grades_array.length()] ;
        String t[] = new String[grades_array.length()] ;
        String m[] = new String[grades_array.length()] ;
        String weight[] = new String[grades_array.length()] ;
        String absmarks[] = new String[grades_array.length()] ;

        for(int i=0;i<grades_array.length();i++){
            try {
                c[i] = course_array.getJSONObject(i).getString("code");
                t[i] = grades_array.getJSONObject(i).getString("name");
                m[i] = String.valueOf(grades_array.getJSONObject(i).getInt("score"));
                weight[i] = String.valueOf(grades_array.getJSONObject(i).getInt("weightage"));
                int out_of = grades_array.getJSONObject(i).getInt("out_of");
                float marks = Float.parseFloat(m[i]);
                float wt = Float.parseFloat(weight[i]);
                float abs = (float)(marks*wt)/(float)(out_of);
                absmarks[i] = String.format("%.2f", abs);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        //create an ArrayAdaptar from the String Array
        LeftGradeArrayAdapter dataAdapter = new LeftGradeArrayAdapter(getContext(),c ,t, m ,weight, absmarks);
        ListView listView = (ListView) getView().findViewById(R.id.left_grades);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
        //enables filtering for the contents of the given ListView
        listView.setTextFilterEnabled(true);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(), "URL selected", LENGTH_SHORT).show();

                // Send the URL to the host activity
                //    mListener.onURLSelected(((TextView) view).getText().toString());

            }
        });
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.left_grade_layout,null);
    }
}
