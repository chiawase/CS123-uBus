package com.example.elysi_000.ubus;

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

public class Confirmation extends Dialog{
    EditText noOfSeats;
    SharedPreferences busSchedule;
    String bN, pN, f, t, dT, sA, c, nOS;

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
        setContentView(R.layout.activity_confirmation);
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


        TextView busNum = (TextView)findViewById(R.id.busNum);
        TextView plateNum = (TextView) findViewById(R.id.plateNum);
        TextView from = (TextView) findViewById(R.id.from);
        TextView to = (TextView) findViewById(R.id.to);
        TextView depTime = (TextView) findViewById(R.id.depTime);
        TextView seatsAvailable = (TextView) findViewById(R.id.seatsAvailable);
        TextView cost = (TextView) findViewById(R.id.cost);
        TextView noOfSeats =(TextView) findViewById(R.id.noOfSeatsReserved);

        busNum.setText(bN);
        plateNum.setText(pN);
        from.setText(f);
        to.setText(t);
        depTime.setText(dT);
        seatsAvailable.setText(sA);
        if(nOS!="" && noOfSeats!=null){
            cost.setText(Double.toString(Double.parseDouble(c)*Double.parseDouble(nOS)));
            noOfSeats.setText(nOS);
        }
        else System.out.print("Lel");


        //get number of seats from reserving seats

        Button ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //put data into database
                //send to receipt

                dismiss();
            }
        });

        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                cancel();
            }
        });
    }

}
