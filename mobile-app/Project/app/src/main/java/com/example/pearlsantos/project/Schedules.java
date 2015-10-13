package com.example.pearlsantos.project;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Schedules.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Schedules#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Schedules extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedules, container, false);
        return rootView;
    }
}



