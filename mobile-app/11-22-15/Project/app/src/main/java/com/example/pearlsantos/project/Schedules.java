package com.example.pearlsantos.project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class Schedules extends Fragment {
    SharedPreferences busSchedule;
    String load;
    TextView newLoad;
    View rootView;
    SampleCustomAdapter adapter;
    TextView setLoad;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

//    public void onResume() {
//        super.onResume();
//
//    }
//
//    public void onPause(){
//        super.onPause();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_schedules, container, false);

//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("foo", "bar");
//        testObject.saveInBackground();
        final ProgressDialog ringProgressDialog = ProgressDialog.show(getContext(),
                "Maaring maghintay lamang ...", "Kinukuha ang talaan ng bus ...", true);
        ringProgressDialog.setCancelable(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    busSchedule = getContext().getSharedPreferences("bus", getContext().MODE_PRIVATE);

                    ParseQuery<ParseObject> trips = ParseQuery.getQuery("Trip");
                    trips.include("assignedBus");
                    trips.include("assignedSchedule");
                    trips.include("busCompany");
                    trips.include("destination");
                    trips.include("origin");

                    trips.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> bC, ParseException e) {
                            if (e == null) {
                                ListView lv = (ListView) rootView.findViewById(R.id.schedule);
                                SampleCustomAdapter adapter = new SampleCustomAdapter(bC);
                                lv.setTextFilterEnabled(true);
                                lv.setAdapter(adapter);

                                ringProgressDialog.dismiss();

                                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                        //System.out.println(adapter.getObjectIDOfItem());
                                        // edit2.putString("ID", adapter.getObjectIDOfItem());

                                        // TextView busNum = (TextView) rootView.findViewById(R.id.busNum);
                                        // TextView plateNum = (TextView) rootView.findViewById(R.id.plateNum);
                                        TextView from = (TextView) rootView.findViewById(R.id.from);
                                        TextView to = (TextView) rootView.findViewById(R.id.to);
                                        TextView depTime = (TextView) rootView.findViewById(R.id.depTime);
                                        TextView day = (TextView) rootView.findViewById(R.id.day);
                                        TextView seatsAvailable = (TextView) rootView.findViewById(R.id.seatsAvailable);
                                        TextView cost = (TextView) rootView.findViewById(R.id.cost);


                                        SharedPreferences.Editor edit = busSchedule.edit();
                                        //   edit.putString("busNum", busNum.getText().toString().trim());
                                        // edit.putString("plateNum", plateNum.getText().toString().trim());
                                        edit.putString("from", from.getText().toString().trim());
                                        edit.putString("to", to.getText().toString().trim());
                                        String[] time = depTime.getText().toString().trim().split(":");
                                        edit.putString("depTime", time[0] + time[1]);
                                        edit.putString("day", day.getText().toString().trim());
                                        edit.putString("seatsAvailable", seatsAvailable.getText().toString().trim());
                                        edit.putString("cost", cost.getText().toString().trim());
                                        edit.commit();

                                        ReservingSeats dialog = new ReservingSeats(rootView.getContext());
                                        dialog.show();


                                    }
                                });

                            } else {
                                System.out.println("No TRIPS FOUND");
                                Toast.makeText(Schedules.this.getContext(), "No trips" +
                                        "available", Toast.LENGTH_LONG).show();
                                ringProgressDialog.dismiss();
                            }
                        }
                    });
                    Thread.sleep(10000);

                } catch (Exception e) {
                }
                ringProgressDialog.dismiss();

            }

        }).start();



//        ArrayList<BusSchedule> sampleList = new ArrayList<BusSchedule>();
//        for(int i=0; i<10; i++){
//        	BusSchedule sample = new BusSchedule("Bus1", "UXC 123", "Manila",
//        			"Malolos", "4:00PM", 30, 7.50);
//        	sampleList.add(sample);
//        }

        TextView userLoad = (TextView) rootView.findViewById(R.id.load);
        ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
            load = user.getString("load");
            userLoad.setText(load);
        }
            return rootView;
    }

    private class SampleCustomAdapter extends BaseAdapter {

        private List<ParseObject> internalList;

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
                view = inflater.inflate(R.layout.schedule_list, null);
            } else {
                view = convertView;
            }


            // extract the views to be populated
          //  TextView busNum = (TextView) view.findViewById(R.id.busNum);
         //   TextView plateNum = (TextView) view.findViewById(R.id.plateNum);
            ImageView companyLogo = (ImageView) view.findViewById(R.id.busCompanyLogo);

            TextView from = (TextView) view.findViewById(R.id.from);
            TextView to = (TextView) view.findViewById(R.id.to);
            TextView depTime = (TextView) view.findViewById(R.id.depTime);
            TextView day = (TextView) view.findViewById(R.id.day);
            TextView seatsAvailable = (TextView) view.findViewById(R.id.seatsAvailable);
            TextView cost = (TextView) view.findViewById(R.id.cost);
            // extract the object that will fill these
            ParseObject contact = internalList.get(position);


            //  busNum.setText(contact.getString("busCompany"));
            // plateNum.setText(contact.getString("busPlate"));
            // companyLogo.setImageBitmap();


            to.setText(contact.getParseObject("destination").getString("location"));
            from.setText(contact.getParseObject("origin").getString("location"));
            seatsAvailable.setText(contact.getParseObject("assignedBus").getNumber("seatNumber").toString());
            cost.setText(contact.getParseObject("assignedBus").getNumber("seatCost").toString());
            day.setText(contact.getParseObject("assignedSchedule").getString("day").toString());
            String dp = contact.getParseObject("assignedSchedule").getString("departureTime");
            if(dp.length()>3) dp = dp.substring(0, 2) + ":" + dp.substring(2);
            else dp = dp.substring(0, 1) + ":" + dp.substring(1);

            depTime.setText(dp);

            System.out.println(day.getText().toString().trim());
            System.out.println(depTime.getText().toString().trim());


            // return the view
            return view;
        }

    }

}




