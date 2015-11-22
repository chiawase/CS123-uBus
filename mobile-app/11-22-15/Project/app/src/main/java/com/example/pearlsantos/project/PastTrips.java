package com.example.pearlsantos.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class PastTrips extends Fragment {
    ArrayList<Receipt> pastTrips;
    SharedPreferences busSchedule;
    String bN, pN, f, t, dT, nOS, a, pur;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_pasttrips, container, false);

            busSchedule = getContext().getSharedPreferences("bus", Context.MODE_PRIVATE);
            if(busSchedule!=null){
              //  bN = busSchedule.getString("busNum", "").trim();
               // pN = busSchedule.getString("plateNum", "").trim();
                f = busSchedule.getString("from", "").trim();
                t = busSchedule.getString("to", "").trim();
                dT = busSchedule.getString("depTime", "").trim();
                nOS = busSchedule.getString("noOfSeats", "").trim();
                pur = busSchedule.getString("purchase", "").trim();
                a = busSchedule.getString("arrival", "").trim();
            }

            ParseUser user = ParseUser.getCurrentUser();
            if (user!=null){
                pastTrips = (ArrayList<Receipt>) user.get("trips");
            }

            ListView lv = (ListView) rootView.findViewById(R.id.listView2);
            SampleCustomAdapter adapter2 = new SampleCustomAdapter(pastTrips);
            lv.setTextFilterEnabled(true);
            lv.setAdapter(adapter2);


            return rootView;
    }
    private class SampleCustomAdapter extends BaseAdapter {

        private ArrayList<Receipt> internalList;

        public SampleCustomAdapter(ArrayList<Receipt> contacts) {
            internalList = contacts;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return internalList.size();
        }

        @Override
        public Object getItem(int index) {
            // TODO Auto-generated method stub
            return internalList.get(index);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // NOTE: you can only do this if you have access to the Activity object
            //		 which is why this is an inner class
            //LayoutInflater inflater = getLayoutInflater();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view;

            System.out.println(parent.getClass().getName());
            System.out.println(position);

            if (convertView == null) {
                view = inflater.inflate(R.layout.past_trips_list, null);
            } else {
                view = convertView;
            }


            // extract the views to be populated
           // TextView busNum = (TextView) view.findViewById(R.id.busNum);
           // TextView plateNum = (TextView) view.findViewById(R.id.plateNum);
            TextView from = (TextView) view.findViewById(R.id.from);
            TextView to = (TextView) view.findViewById(R.id.to);
            TextView depTime = (TextView) view.findViewById(R.id.depTime);
            TextView seatsAvailable = (TextView) view.findViewById(R.id.seatsAvailable);
            TextView noOfSeats =(TextView) view.findViewById(R.id.noOfSeats);
            TextView purchase = (TextView) view.findViewById(R.id.purchase);
            TextView seatsPurchased =(TextView) view.findViewById(R.id.seatsPurchased);
            TextView arrival = (TextView) view.findViewById(R.id.arrTime);

            // extract the object that will fill these
            Receipt contact = internalList.get(position);


         //   busNum.setText(bN);
          //  plateNum.setText(pN);
            from.setText(f);
            to.setText(t);
            depTime.setText(dT);
            seatsAvailable.setText(nOS);
            noOfSeats.setText(nOS);
            seatsPurchased.setText(nOS);
            purchase.setText(pur);
            arrival.setText(a);


            // return the view
            return view;
        }


    }

}
