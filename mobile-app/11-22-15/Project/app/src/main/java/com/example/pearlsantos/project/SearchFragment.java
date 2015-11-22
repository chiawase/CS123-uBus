package com.example.pearlsantos.project;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
/**
 * A placeholder fragment containing a simple view.
 */
public class SearchFragment extends Fragment {
    SharedPreferences busSchedule;
    View rootView;
    Spinner s;
    private TextView mTimeDisplay;
    private int mHour;
    private int mMinute;
    static final int TIME_DIALOG_ID = 0;

    public SearchFragment() {

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstance) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);

        final CheckBox checkTime = (CheckBox) rootView.findViewById(R.id.checkTime);
        mTimeDisplay = (TextView) rootView.findViewById(R.id.timePicker);
        mTimeDisplay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCreateDialog(TIME_DIALOG_ID).show();
            }
        });


        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        updateDisplay();

//        clk = (TextView) rootView.findViewById(R.id.digitalClock);
//        clk.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                TimePicker newDialog = new TimePicker();
//                newDialog.show(getActivity().getSupportFragmentManager(), "timePicker");
//            }
//        });


        final List<String> busCompanies = new ArrayList<>();
        busCompanies.add("None");
        final ParseQuery busComp = ParseQuery.getQuery("Bus_Company");
        busComp.orderByAscending("companyName");
        busComp.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> bC, ParseException e) {
                if (e == null) {
                    int j = 0;
                    boolean withSame = false;
                    for (int i = 0; i < bC.size(); i++) {
                        String company = bC.get(i).getString("companyName");
                        while(j<busCompanies.size()) {
                            if (busCompanies.get(j).equals(company)){
                                withSame = true;
                                break;
                            }
                            else j++;
                        }
                        if(withSame==false) busCompanies.add(company);
                    }
                } else System.out.println("NO BUS COMPANy FOUND");
            }
        });

        s=(Spinner)rootView.findViewById(R.id.busLiners);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, busCompanies);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(dataAdapter);

        Button searchFor = (Button) rootView.findViewById(R.id.searchFor);
        searchFor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v){
                final ProgressDialog ringProgressDialog = ProgressDialog.show(getContext(),
                        "Maaring maghintay lamang ...", "Kinukuha ang talaan ng bus ...", true);
                ringProgressDialog.setCancelable(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            busSchedule = getContext().getSharedPreferences("bus", getContext().MODE_PRIVATE);

                            EditText from = (EditText) rootView.findViewById(R.id.fromSearch);
                            String fromSearch = from.getText().toString().trim();

                            EditText to = (EditText) rootView.findViewById(R.id.toSearch);
                            String toSearch = to.getText().toString().trim();


                            String bC = s.getSelectedItem().toString().trim();

                            ParseQuery<ParseObject> schedule = ParseQuery.getQuery("Schedule");
                            if(checkTime.isChecked()) {
                                schedule.whereContains("departureTime", Integer.toString(mHour) +
                                        Integer.toString(mMinute));
                            }
                            ParseQuery<ParseObject> bus = ParseQuery.getQuery("Bus");
                            ParseQuery<ParseObject> terminalf = ParseQuery.getQuery("Terminal");
                            terminalf.whereContains("location", fromSearch);
                            ParseQuery<ParseObject> terminalt = ParseQuery.getQuery("Terminal");
                            terminalt.whereContains("location", toSearch);
                            ParseQuery<ParseObject> busCompany = ParseQuery.getQuery("Bus_Company");
                            if(!bC.equals("None"))
                                busCompany.whereContains("companyName", bC);

                            ParseQuery<ParseObject> trips = ParseQuery.getQuery("Trip");
                            trips.include("assignedBus");
                            trips.include("assignedSchedule");
                            trips.include("busCompany");
                            trips.include("destination");
                            trips.include("origin");
                            trips.whereMatchesQuery("assignedSchedule", schedule);
                            trips.whereMatchesQuery("destination", terminalt);
                            trips.whereMatchesQuery("origin", terminalf);
                            trips.whereMatchesQuery("busCompany", busCompany);


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
                                        Toast.makeText(SearchFragment.this.getContext(), "No trips" +
                                                "fit your criteria", Toast.LENGTH_LONG).show();
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

            }

            }

            );

//
//
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
            seatsAvailable.setText(contact.getString("seatNumber"));
            cost.setText(contact.getParseObject("assignedBus").getNumber("seatCost").toString());
            day.setText(contact.getParseObject("assignedSchedule").getString("day").toString());
            String dp = contact.getParseObject("assignedSchedule").getString("departureTime");
            if(dp.length()>3) dp = dp.substring(0, 2) + ":" + dp.substring(2);
            else dp = dp.substring(0, 1) + ":" + dp.substring(1);

            depTime.setText(dp);


            // return the view
            return view;
        }

    }

    private void updateDisplay() {
        mTimeDisplay.setText(
                new StringBuilder()
                        .append(pad(mHour)).append(":")
                        .append(pad(mMinute)));
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(getContext(), mTimeSetListener, mHour, mMinute, true);
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
                    mHour = hourOfDay;
                    mMinute = minute;
                    updateDisplay();
                }

            };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }



}
