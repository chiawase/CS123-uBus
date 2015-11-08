package com.example.pearlsantos.project;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class ReservingSeats extends Dialog {
    SharedPreferences busSchedule;
    EditText noOfSeats;
    String bN, pN, f, t, dT, sA, c, nOS, a;
    NumberPicker tens;
    NumberPicker ones;

    public ReservingSeats(Context context) {
        super(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        busSchedule = getContext().getSharedPreferences("bus", Context.MODE_PRIVATE);
//        if(busSchedule!=null){
//            bN = busSchedule.getString("busNum", "").trim();
//            pN = busSchedule.getString("plateNum", "").trim();
//            f = busSchedule.getString("from", "").trim();
//            t = busSchedule.getString("to", "").trim();
//            dT = busSchedule.getString("depTime", "").trim();
            sA = busSchedule.getString("seatsAvailable", "").trim();
//            c = busSchedule.getString("cost", "").trim();
//            a = busSchedule.getString("arrival", "").trim();
//        }

        tens = (NumberPicker) findViewById(R.id.numberPicker);
        ones = (NumberPicker) findViewById(R.id.numberPicker2);

        tens.setMinValue(0);
        ones.setMinValue(0);

        tens.setMaxValue(9);
        ones.setMaxValue(9);

        tens.setWrapSelectorWheel(true);
        ones.setWrapSelectorWheel(true);


        setContentView(R.layout.activity_reserving_seats);

//        noOfSeats = (EditText) findViewById(R.id.noOfSeats);
//        noOfSeats.setText("1");

        Button ok = (Button) findViewById(R.id.okButtonForBuying);
        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                NumberPicker tensSeats = (NumberPicker) findViewById(R.id.numberPicker);
                NumberPicker unitSeats = (NumberPicker) findViewById(R.id.numberPicker2);

                int nOS1 = (tensSeats.getValue()*10) + unitSeats.getValue();

                int remainingSeats = Integer.parseInt(sA) - nOS1;
                if (remainingSeats > 0) {
                    SharedPreferences.Editor edit = busSchedule.edit();
                    edit.putString("noOfSeats", noOfSeats.getText().toString().trim());
                    edit.commit();
                    Confirmation confirm = new Confirmation(getContext());
                    confirm.show();
                    dismiss();

                } else {
                    Toast.makeText(ReservingSeats.this.getContext(), "There are not enough seats available"
                            , Toast.LENGTH_LONG).show();
                }

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