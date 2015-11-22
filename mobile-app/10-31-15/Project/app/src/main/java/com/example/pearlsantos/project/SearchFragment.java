package com.example.pearlsantos.project;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DigitalClock;
import android.widget.TextView;

import java.util.Calendar;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchFragment extends Fragment {
    private TextView clk;
    private TimePicker timePicker;

    private int selectedHour;
    private int selectedMinute;

    static final int TIME_DIALOG_ID = 999;
    public SearchFragment() {
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstance) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        clk = (TextView) rootView.findViewById(R.id.digitalClock);

        //String time = hour + ":" + minute;

        clk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TimePicker newDialog = new TimePicker();
                newDialog.show(getActivity().getSupportFragmentManager(), "timePicker");
            }
        });

//        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(R.mipmap.ic_search);
        return rootView;


    }


}


