package com.example.pearlsantos.project;


import android.app.Dialog;
import android.content.Context;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Confirmation extends Dialog{
    SharedPreferences busSchedule;
    String f, t, dT, sA, c, nOS, d;
    double totalCost;


    public Confirmation(Context context) {
        super(context);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        this.setTitle("Confirm Reservation");
        setContentView(R.layout.activity_confirmation);

        busSchedule = getContext().getSharedPreferences("bus", Context.MODE_PRIVATE);
        if(busSchedule!=null){
          //  bN = busSchedule.getString("busNum", "").trim();
            //pN = busSchedule.getString("plateNum", "").trim();
            f = busSchedule.getString("from", "").trim();
            t = busSchedule.getString("to", "").trim();
            c = busSchedule.getString("cost", "").trim();
            sA = busSchedule.getString("seatsAvailable", "").trim();
            dT = busSchedule.getString("depTime", "").trim();
            nOS = busSchedule.getString("noOfSeats", "").trim();
            d = busSchedule.getString("day", "").trim();
        }

        System.out.println(f);
        System.out.println(t);
        System.out.println(c);
        System.out.println(dT);
        System.out.println(nOS);
        System.out.println(d);

        //TextView busNum = (TextView)findViewById(R.id.busNum);
        //TextView plateNum = (TextView) findViewById(R.id.plateNum);
        TextView from = (TextView) findViewById(R.id.from);
        TextView to = (TextView) findViewById(R.id.to);
        TextView purchase = (TextView) findViewById(R.id.purchase);
        TextView noOfSeats =(TextView) findViewById(R.id.noOfSeats);
        TextView seatsPurchased =(TextView) findViewById(R.id.seatsPurchased);
        TextView depTime = (TextView) findViewById(R.id.depTime);
        TextView day = (TextView) findViewById(R.id.day);


      //  busNum.setText(bN);
       // plateNum.setText(pN);
        from.setText(f);
        to.setText(t);
        String dp = dT;
        if(dp.length()>3) dp = dp.substring(0, 2) + ":" + dp.substring(2);
        else dp = dp.substring(0, 1) + ":" + dp.substring(1);
        depTime.setText(dp);
        day.setText(d);
        totalCost = Double.parseDouble(c)*Double.parseDouble(nOS);
        purchase.setText(Double.toString(totalCost));
        noOfSeats.setText(nOS);
        seatsPurchased.setText(nOS);


        //get number of seats from reserving seats

        Button ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final ParseUser user = ParseUser.getCurrentUser();
                if(user!=null) {
                    String load = user.getString("load");
                    if(Double.parseDouble(load)>=totalCost){
                        String newLoad = Double.toString(Double.parseDouble(load) - totalCost);
                        user.put("load", newLoad);
                        user.saveInBackground();

                        ParseQuery<ParseObject> trips = ParseQuery.getQuery("Trip");
                        ParseQuery<ParseObject> schedule = ParseQuery.getQuery("Schedule");
                        ParseQuery<ParseObject> bus = ParseQuery.getQuery("Bus");
                        ParseQuery<ParseObject> terminalf = ParseQuery.getQuery("Terminal");
                        ParseQuery<ParseObject> terminalt = ParseQuery.getQuery("Terminal");
                        ParseQuery<ParseObject> busCompany = ParseQuery.getQuery("Bus_Company");

                        schedule.whereContains("day", d);
                        schedule.whereContains("departureTime", dT);
                        terminalf.whereContains("location", f);
                        terminalt.whereContains("location", t);

                        bus.whereEqualTo("seatCost", Integer.parseInt(c));

                        trips.include("assignedBus");
                        trips.include("assignedSchedule");
                        trips.include("busCompany");
                        trips.include("destination");
                        trips.include("origin");

                        trips.whereMatchesQuery("assignedSchedule", schedule);
                        //trips.whereMatchesQuery("assignedBus", bus);
                        trips.whereMatchesQuery("origin", terminalf);
                        trips.whereMatchesQuery("destination", terminalt);
                        trips.whereMatchesQuery("busCompany", busCompany);

                        trips.getFirstInBackground(new GetCallback<ParseObject>() {
                            public void done(ParseObject object, ParseException e) {
                                if (object != null) {
                                    object.put("seatNumber", Integer.toString(Integer.parseInt(sA) - Integer.parseInt(nOS)));
                                    object.saveInBackground();
                                    ParseObject userTrip = new ParseObject("User_Trip");
                                    userTrip.put("tripPurchased", object);
                                    userTrip.put("tripDate", object.getParseObject("assignedSchedule").getString("day") + " " +
                                            object.getParseObject("assignedSchedule").getString("departureTime"));
                                    userTrip.put("ticketQuantity", nOS);
                                    userTrip.put("tripCost", Double.toString(totalCost));
                                    userTrip.put("boughtBy", user);
                                    userTrip.saveInBackground();

                                    Toast.makeText(Confirmation.this.getContext(), "Seat/s Reserved!", Toast.LENGTH_SHORT).show();

                                } else {
                                    System.out.println("NOT FOUND");
                                }
                            }

                        });

                        dismiss();


                    }
                    else
                        Toast.makeText(Confirmation.this.getContext(), "Your load is not enough to " +
                                "reserve seat/s", Toast.LENGTH_LONG).show();
                }
                else {
                    System.out.println("NO USER FOUND");
                }
            }
        });

//        Button cancel = (Button) findViewById(R.id.cancel);
//        cancel.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                cancel();
//            }
//        });
    }

}
