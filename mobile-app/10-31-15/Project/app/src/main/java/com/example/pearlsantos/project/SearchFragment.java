package com.example.pearlsantos.project;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DigitalClock;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchFragment extends Fragment {
    SharedPreferences time;
    public SearchFragment() {

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstance) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        DigitalClock clk = (DigitalClock) rootView.findViewById(R.id.digitalClock);
        clk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TimePicker newDialog = new TimePicker();
                newDialog.show(getActivity().getSupportFragmentManager(), "timePicker");
            }
        });

        time = getContext().getSharedPreferences("bus", getContext().MODE_PRIVATE);
        String hour = time.getString("hourOfDay", "");
        String minute = time.getString("minute", "");

        TextView from = (TextView) rootView.findViewById(R.id.fromSearch);
        String fromSearch = from.toString().trim();

        TextView to = (TextView) rootView.findViewById(R.id.toSearch);
        String toSearch = to.toString().trim();

        List<String> busCompanies = new ArrayList<>();
        busCompanies.add("None");
        Spinner s = (Spinner) rootView.findViewById(R.id.busLiners);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, busCompanies);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(dataAdapter);

        String bC = s.getSelectedItem().toString().trim();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Schedule");
        query.orderByAscending("departure");
        query.whereContains("startingTerminal", fromSearch);
        query.whereContains("destination", toSearch);
        query.whereContains("busCompany", bC);
        query.whereContains("departure", hour);
        query.whereContains("arrival", minute);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> bC, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + bC.size() + " scores");

                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

//
//        String date = "2014-11-25 14:30";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:MM");
//        Date testDate = null;
//        try {
//            testDate = sdf.parse(date);
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
//        SimpleDateFormat formatter = new SimpleDateFormat("MM dd,yyyy hh:mm a");
//        String newFormat = formatter.format(testDate);
//        System.out.println(".....Date..."+newFormat);

        return rootView;


    }




}
