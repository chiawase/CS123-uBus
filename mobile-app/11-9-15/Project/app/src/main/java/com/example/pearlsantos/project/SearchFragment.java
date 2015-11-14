package com.example.pearlsantos.project;

import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DigitalClock;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchFragment extends Fragment {

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

        return rootView;


    }




}
