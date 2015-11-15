package com.example.pearlsantos.project;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;

public class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    int selectedHour;
    int selectedMinute;
    SharedPreferences time;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));

    }


    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minuteOfHour) {
        selectedHour = hourOfDay;
        selectedMinute = minuteOfHour;

        SharedPreferences.Editor edit = time.edit();
        edit.putString("hourOfDay", Integer.toString(selectedHour));
        edit.putString("minuteOfHour", Integer.toString(selectedMinute));
        edit.commit();

        //hi EJ, I think this is where you put what you want to do with the time the user picks. Just tell me if you need help
        //actually, here's one thing you can do, since I kinda forgot how to SharedPreferences
        //1) get the time set using this time picker
        //2) send it to SearchFragment
        //3) set the digital clock to the said time
        //4) other things you need to do :)


    }
}
