package com.example.pearlsantos.project;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class Schedules extends Fragment {
    SharedPreferences busSchedule;
    String load;
    View rootView;
    SampleCustomAdapter adapter;
    ListView lv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_schedules, container, false);

//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("foo", "bar");
//        testObject.saveInBackground();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Schedule");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> schedule, ParseException e) {
                if (e == null) {
                    Log.d("Brand", "Retrieved " + schedule.size() + " Brands");


                    lv = (ListView) rootView.findViewById(R.id.list);
                    SampleCustomAdapter adapter2 = new SampleCustomAdapter(schedule);
                    lv.setTextFilterEnabled(true);
                    lv.setAdapter(adapter2);

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            busSchedule = rootView.getContext().getSharedPreferences("bus", Context.MODE_PRIVATE);

                            SharedPreferences.Editor edit2 = busSchedule.edit();
                            //System.out.println(adapter.getObjectIDOfItem());
                           // edit2.putString("ID", adapter.getObjectIDOfItem());

                            TextView busNum = (TextView) rootView.findViewById(R.id.busNum);
                            TextView plateNum = (TextView) rootView.findViewById(R.id.plateNum);
                            TextView from = (TextView) rootView.findViewById(R.id.from);
                            TextView to = (TextView) rootView.findViewById(R.id.to);
                            TextView depTime = (TextView) rootView.findViewById(R.id.depTime);
                            TextView seatsAvailable = (TextView) rootView.findViewById(R.id.seatsAvailable);
                            TextView cost = (TextView) rootView.findViewById(R.id.cost);

                            SharedPreferences.Editor edit = busSchedule.edit();
                            edit.putString("busNum", busNum.getText().toString().trim());
                            edit.putString("plateNum", plateNum.getText().toString().trim());
                            edit.putString("from", (String) from.getText().toString().trim());
                            edit.putString("to", (String) to.getText().toString().trim());
                            edit.putString("depTime", (String) depTime.getText().toString().trim());
                            edit.putString("seatsAvailable", (String) seatsAvailable.getText().toString().trim());
                            edit.putString("cost", (String) cost.getText().toString().trim());
                            edit.commit();

                            ReservingSeats dialog = new ReservingSeats(rootView.getContext());
                            dialog.show();

                        }
                    });

                } else {

                }
            }
        });

//        ArrayList<BusSchedule> sampleList = new ArrayList<BusSchedule>();
//        for(int i=0; i<10; i++){
//        	BusSchedule sample = new BusSchedule("Bus1", "UXC 123", "Manila",
//        			"Malolos", "4:00PM", 30, 7.50);
//        	sampleList.add(sample);
//        }


        busSchedule = rootView.getContext().getSharedPreferences("bus", Context.MODE_PRIVATE);

        ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
            load = user.getString("load");
            TextView setLoad = (TextView) rootView.findViewById(R.id.load);
            if (load != null)
                setLoad.setText(load);
            else {
                setLoad.setText(load);
            }
        }

        return rootView;
    }

    private class SampleCustomAdapter extends BaseAdapter {

        private List<ParseObject> internalList;
        private String objectID;

        public SampleCustomAdapter(List<ParseObject> contacts) {
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
                view = inflater.inflate(R.layout.list_view2, null);
            } else {
                view = convertView;
            }


            // extract the views to be populated
            TextView busNum = (TextView) view.findViewById(R.id.busNum);
            TextView plateNum = (TextView) view.findViewById(R.id.plateNum);
            TextView from = (TextView) view.findViewById(R.id.from);
            TextView to = (TextView) view.findViewById(R.id.to);
            TextView depTime = (TextView) view.findViewById(R.id.depTime);
            TextView seatsAvailable = (TextView) view.findViewById(R.id.seatsAvailable);
            TextView cost = (TextView) view.findViewById(R.id.cost);
            // extract the object that will fill these
            ParseObject contact = internalList.get(position);

            objectID = internalList.get(position).getObjectId();

            busNum.setText(contact.getString("busCompany"));
            plateNum.setText(contact.getString("busPlate"));
            from.setText(contact.getString("startingTerminal"));
            to.setText(contact.getString("destination"));
            depTime.setText(contact.getString("departure"));
            seatsAvailable.setText(contact.getString("seatsAmount"));
            cost.setText(contact.getString("ticketPrice"));


            // return the view
            return view;
        }

        public String getObjectIDOfItem() {
            return objectID;
        }
    }
}




