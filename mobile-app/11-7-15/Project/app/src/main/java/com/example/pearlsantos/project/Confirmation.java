package com.example.pearlsantos.project;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class Confirmation extends Dialog{
    EditText noOfSeats;
    SharedPreferences busSchedule;
    String bN, pN, f, t, dT, sA, c, nOS, a;
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
            dT = busSchedule.getString("depTime", "").trim();
            sA = busSchedule.getString("seatsAvailable", "").trim();
            //c = busSchedule.getString("cost", "").trim();
            nOS = busSchedule.getString("noOfSeats", "").trim();
            a = busSchedule.getString("arrival", "").trim();
        }


        //TextView busNum = (TextView)findViewById(R.id.busNum);
        //TextView plateNum = (TextView) findViewById(R.id.plateNum);
        TextView from = (TextView) findViewById(R.id.from);
        TextView to = (TextView) findViewById(R.id.to);
        TextView depTime = (TextView) findViewById(R.id.depTime);
        TextView seatsAvailable = (TextView) findViewById(R.id.seatsAvailable);
        TextView purchase = (TextView) findViewById(R.id.purchase);
        TextView noOfSeats =(TextView) findViewById(R.id.noOfSeats);
        TextView seatsPurchased =(TextView) findViewById(R.id.seatsPurchased);
        TextView arrival =(TextView) findViewById(R.id.arrTime);

      //  busNum.setText(bN);
       // plateNum.setText(pN);
        from.setText(f);
        to.setText(t);
        depTime.setText(dT);
        seatsAvailable.setText(sA);
        arrival.setText(a);
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
                ParseUser user = ParseUser.getCurrentUser();
                if(user!=null) {
                    String load = user.getString("load");
                    if(Double.parseDouble(load)>=totalCost){
                        String newLoad = Double.toString(Double.parseDouble(load) - totalCost);
                        user.put("load", newLoad);
                        user.saveInBackground();

                        SharedPreferences.Editor edit = busSchedule.edit();
                        edit.putString("load", newLoad);
                        edit.putString("totalCost", Double.toString(totalCost));
                        edit.commit();

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Schedule");
                        //query.whereEqualTo("busPlate", pN);
                        query.whereEqualTo("arrival", a);
                        query.whereEqualTo("departure", dT);
                        query.whereEqualTo("startingTerminal", f);
                        query.whereEqualTo("destination", t);

                        query.getFirstInBackground(new GetCallback<ParseObject>() {
                            public void done(ParseObject object, ParseException e) {
                                if (object != null) {
                                    object.put("seatsAmount", Integer.parseInt(sA) - Integer.parseInt(nOS));
                                    object.saveInBackground();
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

        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

}
