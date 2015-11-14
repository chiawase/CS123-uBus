package com.example.pearlsantos.project;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchFragment extends Fragment {
    SharedPreferences busSchedule;
    View rootView;
    Spinner s;
    public SearchFragment() {

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstance) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);

        DigitalClock clk = (DigitalClock) rootView.findViewById(R.id.digitalClock);
        clk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TimePicker newDialog = new TimePicker();
                newDialog.show(getActivity().getSupportFragmentManager(), "timePicker");
            }
        });

        List<String> busCompanies = new ArrayList<>();
        busCompanies.add("None");
        s = (Spinner) rootView.findViewById(R.id.busLiners);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, busCompanies);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(dataAdapter);

        Button searchFor = (Button) rootView.findViewById(R.id.searchFor);
        searchFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog ringProgressDialog = ProgressDialog.show(getContext(),
                        "Maaring maghintay lamang ...", "Kinukuha ang talaan ng bus ...", true);
                ringProgressDialog.setCancelable(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            busSchedule = getContext().getSharedPreferences("bus", getContext().MODE_PRIVATE);
                            String hour = busSchedule.getString("hourOfDay", "");
                            String minute = busSchedule.getString("minute", "");

                            EditText from = (EditText) rootView.findViewById(R.id.fromSearch);
                            String fromSearch = from.getText().toString().trim();

                            EditText to = (EditText) rootView.findViewById(R.id.toSearch);
                            String toSearch = to.getText().toString().trim();


                            String bC = s.getSelectedItem().toString().trim();

                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Schedule");
                            query.orderByAscending("departure");
                            query.whereContains("startingTerminal", fromSearch);
                            query.whereContains("destination", toSearch);
                            if(!(bC.equals("None")))
                                query.whereContains("busCompany", bC);
//                query.whereContains("departure", hour);
//                query.whereContains("arrival", minute);

                            query.findInBackground(new FindCallback<ParseObject>() {
                                public void done(List<ParseObject> bC, ParseException e) {
                                    if (e == null) {
                                        ListView lv = (ListView) rootView.findViewById(R.id.schedule);
                                        SampleCustomAdapter adapter2 = new SampleCustomAdapter(bC);
                                        lv.setTextFilterEnabled(true);
                                        lv.setAdapter(adapter2);

                                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                                SharedPreferences.Editor edit2 = busSchedule.edit();
                                                //System.out.println(adapter.getObjectIDOfItem());
                                                // edit2.putString("ID", adapter.getObjectIDOfItem());

                                                // TextView busNum = (TextView) rootView.findViewById(R.id.busNum);
                                                // TextView plateNum = (TextView) rootView.findViewById(R.id.plateNum);
                                                TextView from = (TextView) rootView.findViewById(R.id.from);
                                                TextView to = (TextView) rootView.findViewById(R.id.to);
                                                TextView depTime = (TextView) rootView.findViewById(R.id.depTime);
                                                TextView seatsAvailable = (TextView) rootView.findViewById(R.id.seatsAvailable);
                                                TextView cost = (TextView) rootView.findViewById(R.id.cost);
                                                TextView arrival = (TextView) rootView.findViewById(R.id.arrTime);


                                                SharedPreferences.Editor edit = busSchedule.edit();
                                                //   edit.putString("busNum", busNum.getText().toString().trim());
                                                // edit.putString("plateNum", plateNum.getText().toString().trim());
                                                edit.putString("from", from.getText().toString().trim());
                                                edit.putString("to", to.getText().toString().trim());
                                                edit.putString("depTime", depTime.getText().toString().trim());
                                                edit.putString("seatsAvailable", seatsAvailable.getText().toString().trim());
                                                edit.putString("cost", cost.getText().toString().trim());
                                                edit.putString("arrival", arrival.getText().toString().trim());
                                                edit.commit();

                                                ReservingSeats dialog = new ReservingSeats(rootView.getContext());
                                                dialog.show();


                                            }
                                        });

                                    } else {

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

        });

//
//        String date = "2014-11-25 14:30";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:MM");
//        Date testDate = null;
//        try {
//            testDate = sdf.parse(date);
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
//        SimpleDateFormat formatter = new SimpleDateFormat("MM dd,yyyy hh:mm a");
//        String newFormat = formatter.format(testDate);
//        System.out.println(".....Date..."+newFormat);
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
            TextView seatsAvailable = (TextView) view.findViewById(R.id.seatsAvailable);
            TextView cost = (TextView) view.findViewById(R.id.cost);
            TextView arrival = (TextView) view.findViewById(R.id.arrTime);
            // extract the object that will fill these
            ParseObject contact = internalList.get(position);


            //  busNum.setText(contact.getString("busCompany"));
            // plateNum.setText(contact.getString("busPlate"));
            // companyLogo.setImageBitmap();
            from.setText(contact.getString("startingTerminal"));
            to.setText(contact.getString("destination"));
            depTime.setText(contact.getString("departure"));
            seatsAvailable.setText(contact.getString("seatsAmount"));
            cost.setText(contact.getString("ticketPrice"));
            arrival.setText(contact.getString("arrival"));

            // return the view
            return view;
        }

    }


}
