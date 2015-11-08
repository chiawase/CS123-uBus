package com.example.pearlsantos.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;

/**
 * Created by Pearl Santos on 10/15/2015.
 */
public class SignUp extends AppCompatActivity {

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
                String firstName = fn.getText().toString();

                EditText ln = (EditText) findViewById(R.id.lastName);
                String lastName = ln.getText().toString().trim();


                EditText pa = (EditText) findViewById(R.id.password);
                String password = pa.getText().toString().trim();

                EditText cPa = (EditText) findViewById(R.id.confirmPassword);
                String confirmPassword = pa.getText().toString().trim();


                EditText ph = (EditText) findViewById(R.id.phone);
                String phone = ph.getText().toString().trim();

//                EditText eM = (EditText) findViewById(R.id.email);
//                String email = ph.getText().toString();

                if(password.equals(confirmPassword)) {
                    ParseUser user = new ParseUser();
                    user.setUsername(firstName + " " + lastName);
                    user.setPassword(password);
                    //user.setEmail(email);
                    user.put("cellPhone", phone);
                    user.put("load", "0");
                    user.put("trips", new ArrayList<Receipt>());

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
                                Toast.makeText(SignUp.this, "No internet connection", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else Toast.makeText(SignUp.this, "Please confirm password", Toast.LENGTH_SHORT).show();

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
