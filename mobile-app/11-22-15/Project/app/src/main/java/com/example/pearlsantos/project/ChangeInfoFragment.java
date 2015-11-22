package com.example.pearlsantos.project;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseUser;

public class ChangeInfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_changeinfo, container, false);

        ParseUser user = ParseUser.getCurrentUser();
        if (user!=null){
            TextView load = (TextView) rootView.findViewById(R.id.load);
            load.setText(user.getString("load"));
        }
        else System.out.println ("NO USER FOUND");

        return rootView;
    }
}
