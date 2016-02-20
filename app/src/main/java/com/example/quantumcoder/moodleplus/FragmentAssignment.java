package com.example.quantumcoder.moodleplus;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;


//http://www.mysamplecode.com/2012/08/android-fragment-example.html

public class FragmentAssignment extends  Fragment  {

    OnURLSelectedListener mListener;

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v("ListFragment", "onActivityCreated().");
        Log.v("ListsavedInstanceState", savedInstanceState == null ? "true" : "false");

        //Generate list View from ArrayList
        displayListView();

    }


    public interface OnURLSelectedListener {
        public void onURLSelected(String URL);
    }
   /*
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnURLSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnURLSelectedListener");
        }
    }
    */
    private void displayListView() {

        //Array list of countries
        List<String> urlList = new ArrayList<String>();
        urlList.add("http://www.google.com");
        urlList.add("http://mail.google.com");
        urlList.add("http://maps.google.com");
        String t[] = {"hello","yo"} ;
        //create an ArrayAdaptar from the String Array
        MySimpleArrayAdapter dataAdapter = new MySimpleArrayAdapter(getContext(), t);
        ListView listView = (ListView) getView().findViewById(R.id.listofURLs);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
        //enables filtering for the contents of the given ListView
        listView.setTextFilterEnabled(true);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(), "Clicked on url ", LENGTH_SHORT).show();

                // Send the URL to the host activity
          //      mListener.onURLSelected(((TextView) view).getText().toString());

            }
        });
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.assignment_layout,null);
    }
}
