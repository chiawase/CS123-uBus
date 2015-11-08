package com.example.pearlsantos.project;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

//import com.parse.ParseUser;

public class Confirmation extends Dialog{
    //EditText noOfSeats;
    //NumberPicker tens;
    //NumberPicker ones;
    SharedPreferences busSchedule;
    String bN, pN, f, t, dT, sA, c, nOS;
    double totalCost;

    public Confirmation(Context context) {
        super(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        this.setTitle("Confirm Reservation");

        busSchedule = getContext().getSharedPreferences("bus", Context.MODE_PRIVATE);
        setContentView(R.layout.confirmation);
        if(busSchedule!=null){
            bN = busSchedule.getString("busNum", "").trim();
            pN = busSchedule.getString("plateNum", "").trim();
            f = busSchedule.getString("from", "").trim();
            t = busSchedule.getString("to", "").trim();
            dT = busSchedule.getString("depTime", "").trim();
            sA = busSchedule.getString("seatsAvailable", "").trim();
            c = busSchedule.getString("cost", "").trim();
            nOS = busSchedule.getString("noOfSeats", "").trim();
        }


        //TextView busNum = (TextView)findViewById(R.id.busNum);
        //TextView plateNum = (TextView) findViewById(R.id.plateNum);
        TextView from = (TextView) findViewById(R.id.from);
        TextView to = (TextView) findViewById(R.id.to);
        TextView depTime = (TextView) findViewById(R.id.depTime);
        TextView seatsAvailable = (TextView) findViewById(R.id.seatsAvailable);
        TextView cost = (TextView) findViewById(R.id.cost);
        TextView noOfSeats =(TextView) findViewById(R.id.noOfSeats);


        //busNum.setText(bN);
        //plateNum.setText(pN);
        from.setText(f);
        to.setText(t);
        depTime.setText(dT);
        seatsAvailable.setText(sA);
        totalCost = Double.parseDouble(c)*Double.parseDouble(nOS);
        if(nOS!="" && noOfSeats!=null){
            cost.setText(Double.toString(totalCost));
            noOfSeats.setText(nOS);
        }
        else System.out.print("Lel");


        //get number of seats from reserving seats

        Button ok = (Button) findViewById(R.id.ok);
        //dummy code
        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                cancel();
            }
        });
//        ok.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                ParseUser user = ParseUser.getCurrentUser();
//                if(user!=null) {
//                    String load = user.getString("load");
//                    if(Double.parseDouble(load)>=totalCost){
//                        user.put("load", Double.parseDouble(load)-totalCost);
//                        user.saveInBackground();
//                        //update noOfseats in buses
//                        //send receipt
//                        dismiss();
//                    }
//                    else
//                        Toast.makeText(Confirmation.this.getContext(), "Your load is not enough to " +
//                                "reserve seat/s", Toast.LENGTH_LONG).show();
//                }
//                else {
//                    System.out.println("NO USER FOUND");
//                }
//            }
//        });

        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                cancel();
            }
        });
    }

}
