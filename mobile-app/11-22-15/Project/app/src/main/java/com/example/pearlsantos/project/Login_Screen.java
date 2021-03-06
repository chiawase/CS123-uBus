package com.example.pearlsantos.project;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

public class Login_Screen extends AppCompatActivity {
    //public final static String EXTRA_MESSAGE = ;
    String name, password;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__screen);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(getApplicationContext(), "waBL5APV9kwdeqnm1kQ34BivGHQjjHQr1I58ubmJ", "psMvyx7KRmMhy5dVaZ8Mn68hE9aRGjoXVTvlcFSb");

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            final ProgressDialog ringProgressDialog = ProgressDialog.show(Login_Screen.this,
                    "Maaring maghintay lamang ...", "Logging in ...", true);
            ringProgressDialog.setCancelable(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Intent intent = new Intent(Login_Screen.this, ActionBarAttempt.class);
                        //EditText editText = (EditText) findViewById(R.id.name);
                        //String message = editText.getText().toString();
                        //intent.putExtra(EXTRA_MESSAGE, message);
                        startActivity(intent);
                        finish();
                        ringProgressDialog.dismiss();
                        Thread.sleep(10000);
                    } catch (Exception e) {
                    }
                    ringProgressDialog.dismiss();

                }

            }).start();

        } else {

            Button submit = (Button) findViewById(R.id.submit);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ProgressDialog ringProgressDialog = ProgressDialog.show(Login_Screen.this,
                            "Maaring maghintay lamang ...", "Logging in ...", true);
                    ringProgressDialog.setCancelable(true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EditText n = (EditText) findViewById(R.id.name);
                                name = n.getText().toString();

                                EditText p = (EditText) findViewById(R.id.password);
                                password = p.getText().toString();

                                ParseUser.logInInBackground(name, password, new LogInCallback() {
                                    public void done(ParseUser user, ParseException e) {
                                        if (user != null) {
                                            Intent intent = new Intent(Login_Screen.this, ActionBarAttempt.class);
                                            //EditText editText = (EditText) findViewById(R.id.name);
                                            //String message = editText.getText().toString();
                                            //intent.putExtra(EXTRA_MESSAGE, message);
                                            startActivity(intent);
                                            ringProgressDialog.dismiss();
                                            finish();

                                        } else {
                                            //Log.d(e, "What happened?");
                                            Toast.makeText(Login_Screen.this, "Name and phone number combination " +
                                                    "does not exist", Toast.LENGTH_LONG).show();
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
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login__screen, menu);
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


    public void newUser(View view){
        Intent intent = new Intent(this, SignUp.class);
        //EditText editText = (EditText) findViewById(R.id.name);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        finish();

    }

    public void onBackPressed() {
        super.onBackPressed();
        new AlertDialog.Builder(this)
                .setTitle("You are now exiting the application")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Login_Screen.super.onBackPressed();
                        finish();
                    }
                }).create().show();
    }

}
