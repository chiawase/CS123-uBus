package com.example.pearlsantos.project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pearl Santos on 10/15/2015.
 */
public class SignUp extends AppCompatActivity {
    String firstName, lastName, password, confirmPassword, phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Sign Up");

        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText fn = (EditText) findViewById(R.id.firstName);
                firstName = fn.getText().toString();

                EditText ln = (EditText) findViewById(R.id.lastName);
                lastName = ln.getText().toString().trim();

                EditText pa = (EditText) findViewById(R.id.password);
                password = pa.getText().toString().trim();

                EditText cPa = (EditText) findViewById(R.id.confirmPassword);
                confirmPassword = cPa.getText().toString().trim();

                EditText ph = (EditText) findViewById(R.id.phone);
                phone = ph.getText().toString().trim();

                final ProgressDialog ringProgressDialog = ProgressDialog.show(SignUp.this,
                        "Maaring maghintay lamang ...", "Logging in ...", true);
                ringProgressDialog.setCancelable(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            if (password.equals(confirmPassword)) {
                                ParseUser user = new ParseUser();
                                user.setUsername(firstName + " " + lastName);
                                user.setPassword(password);
                                //user.setEmail(email);
                                user.put("cellPhone", phone);
                                user.put("load", "0");

                                user.signUpInBackground(new SignUpCallback() {
                                    public void done(ParseException e) {
                                        if (e == null) {



                                            Intent intent = new Intent(SignUp.this, ActionBarAttempt.class);
                                            //EditText editText = (EditText) findViewById(R.id.name);
                                            //String message = editText.getText().toString();
                                            //intent.putExtra(EXTRA_MESSAGE, message);
                                            startActivity(intent);


                                        } else {
                                            System.out.println("DIDN'T WORK");
                                            // Sign up didn't succeed. Look at the ParseException
                                            Log.d("WHAT HAPPENED", e.getMessage());
                                            Toast.makeText(SignUp.this, "User already exists", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else
                                Toast.makeText(SignUp.this, "Please confirm password", Toast.LENGTH_SHORT).show();

                            Thread.sleep(10000);

                        } catch (Exception e) {
                        }
                        ringProgressDialog.dismiss();

                    }

                }).start();

            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_bar_attempt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    public void okay(){
//        Intent intent = new Intent(this, Login_Screen.class);
//        //EditText editText = (EditText) findViewById(R.id.name);
//        //String message = editText.getText().toString();
//        //intent.putExtra(EXTRA_MESSAGE, message);
//
//        startActivity(intent);
//
//    }
}
