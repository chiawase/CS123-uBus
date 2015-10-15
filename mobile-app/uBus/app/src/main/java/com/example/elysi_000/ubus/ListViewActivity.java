package com.example.elysi_000.ubus;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class ListViewActivity extends ListActivity {
    SharedPreferences busSchedule;
    double load;
    ArrayList<BusSchedule> BusSchedule, samplelist;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

//        Parse.enableLocalDatastore(this);
//        Parse.initialize(this, "waBL5APV9kwdeqnm1kQ34BivGHQjjHQr1I58ubmJ", "psMvyx7KRmMhy5dVaZ8Mn68hE9aRGjoXVTvlcFSb");
//
//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("foo", "bar");
//        testObject.saveInBackground();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("BusSchedule");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> schedule, ParseException e) {
                if (e == null) {
                    Log.d("Brand", "Retrieved " + schedule.size() + " Brands");
                     SampleCustomAdapter adapter = new SampleCustomAdapter(samplelist);
        setListAdapter(adapter);

        ListView lv = (ListView) findViewById(android.R.id.list);
        lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                busSchedule = getSharedPreferences("bus", Context.MODE_PRIVATE);

                TextView busNum = (TextView) findViewById(R.id.busNum);
                TextView plateNum = (TextView) findViewById(R.id.plateNum);
                TextView from = (TextView) findViewById(R.id.from);
                TextView to = (TextView) findViewById(R.id.to);
                TextView depTime = (TextView) findViewById(R.id.depTime);
                TextView seatsAvailable = (TextView) findViewById(R.id.seatsAvailable);
                TextView cost = (TextView) findViewById(R.id.cost);

                SharedPreferences.Editor edit = busSchedule.edit();
                edit.putString("busNum", busNum.getText().toString().trim());
                edit.putString("plateNum", plateNum.getText().toString().trim());
                edit.putString("from", (String) from.getText().toString().trim());
                edit.putString("to", (String) to.getText().toString().trim());
                edit.putString("depTime", (String) depTime.getText().toString().trim());
                edit.putString("seatsAvailable", (String) seatsAvailable.getText().toString().trim());
                edit.putString("cost", (String) cost.getText().toString().trim());
                edit.commit();

                ReservingSeats dialog = new ReservingSeats(ListViewActivity.this);
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


        busSchedule = getSharedPreferences("bus", Context.MODE_PRIVATE);

       
        load = 100.00;

        TextView setLoad = (TextView) findViewById(R.id.load);
        setLoad.setText(Double.toString(load));


    }
    private class SampleCustomAdapter extends BaseAdapter {

        private List<ParseObject> internalList;

        public SampleCustomAdapter(List<ParseObject> contacts)
        {
            internalList = contacts;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return internalList.size();
        }

        @Override
        public Object getItem(int index)
        {
            // TODO Auto-generated method stub
            return internalList.get(index);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            // NOTE: you can only do this if you have access to the Activity object
            //		 which is why this is an inner class
            LayoutInflater inflater = getLayoutInflater();
            View view;

            System.out.println(parent.getClass().getName());
            System.out.println(position);


            if (convertView==null)
            {
                view = inflater.inflate(R.layout.list_view2, null);
            }
            else
            {
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

            busNum.setText(contact.getString("busCompany"));
            plateNum.setText(contact.getString("busPlate"));
            from.setText(contact.getString("startingTerminal"));
            to.setText(contact.getString("destination"));
            depTime.setText(contact.getString("departure"));
            seatsAvailable.setText(contact.getString("seatsAmount"));
            cost.setText(contact.getCost("ticketPrice"));


            // return the view
            return view;
        }
    }


}


