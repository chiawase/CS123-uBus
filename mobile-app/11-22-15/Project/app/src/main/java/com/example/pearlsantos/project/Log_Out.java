package com.example.pearlsantos.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.parse.ParseUser;


public class Log_Out extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser();

        ActionBarAttempt back = new ActionBarAttempt();
        back.finish();
        return rootView;
    }


}
