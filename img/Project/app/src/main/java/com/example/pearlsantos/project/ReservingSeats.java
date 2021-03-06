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

public class ReservingSeats extends Dialog {
    NumberPicker tens;
    NumberPicker ones;
    SharedPreferences busSchedule;
    EditText noOfSeats;

    public ReservingSeats(Context context) {
        super(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        busSchedule = getContext().getSharedPreferences("bus", Context.MODE_PRIVATE);
        this.setTitle("Reserve Seat(s) for Bus");

        setContentView(R.layout.number_of_tickets);

        //String bN = busSchedule.getString("busNum", "").trim();
        //String pN = busSchedule.getString("plateNum", "").trim();
//        String f = busSchedule.getString("from", "").trim();
//        String t = busSchedule.getString("to", "").trim();
//        String dT = busSchedule.getString("depTime", "").trim();
//        String sA = busSchedule.getString("seatsAvailable", "").trim();
//        String c = busSchedule.getString("cost", "").trim();
//
//
//        //TextView busNum = (TextView)findViewById(R.id.busNum);
//        //TextView plateNum = (TextView) findViewById(R.id.plateNum);
//        TextView from = (TextView) findViewById(R.id.from);
//        TextView to = (TextView) findViewById(R.id.to);
//        TextView depTime = (TextView) findViewById(R.id.depTime);
//        TextView seatsAvailable = (TextView) findViewById(R.id.seatsAvailable);
//        TextView cost = (TextView) findViewById(R.id.cost);

       // busNum.setText(bN);
        //plateNum.setText(pN);
//        from.setText(f);
//        to.setText(t);
//        depTime.setText(dT);
//        seatsAvailable.setText(sA);
//        cost.setText(c);


        tens = (NumberPicker) findViewById(R.id.numberPicker);
        ones = (NumberPicker) findViewById(R.id.numberPicker2);

        tens.setMinValue(0);
        ones.setMinValue(0);

        tens.setMaxValue(9);
        ones.setMaxValue(9);

        tens.setWrapSelectorWheel(true);
        ones.setWrapSelectorWheel(true);


        Button ok = (Button) findViewById(R.id.okButtonForBuying);
        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                cancel();
            }
        });
//        ok.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                SharedPreferences.Editor edit = busSchedule.edit();
//                edit.putString("noOfSeats", noOfSeats.getText().toString().trim());
//                edit.commit();
//                Confirmation confirm = new Confirmation(getContext());
//                dismiss();
//                confirm.show();
//
//            }
//        });

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