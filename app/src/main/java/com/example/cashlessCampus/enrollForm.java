package com.example.cashlessCampus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class enrollForm extends AppCompatActivity {
    TextView enroll;
    TextInputLayout ti1,ti2,ti3,ti4;
    EditText ed1,ed2,ed3,ed4;
    Spinner spin1;
    String[] yearlst;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll_form);

        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Enrollment form");

        enroll = findViewById(R.id.enroll);
        yearlst = getResources().getStringArray(R.array.year);
        ti1 = findViewById(R.id.fname);
        ed1 = ti1.getEditText();
        ti2 = findViewById(R.id.email);
        ed2 = ti2.getEditText();
        ti3 = findViewById(R.id.mobile);
        ed3 = ti3.getEditText();
        ti4 = findViewById(R.id.branch);
        ed4 = ti4.getEditText();
        spin1 = findViewById(R.id.year);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(enrollForm.this, R.layout.dropdown_item, yearlst);
        spin1.setAdapter(adapter);

        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = getIntent().getStringExtra("username");
                String fname = ed1.getText().toString();
                String email = ed2.getText().toString();
                String mobile = ed3.getText().toString();
                String branch = ed4.getText().toString();
                String year = spin1.getSelectedItem().toString();
                String title = getIntent().getStringExtra("title");
                String category = getIntent().getStringExtra("category");

                Pattern pattern = Patterns.EMAIL_ADDRESS;

                if(fname.equals("") || email.equals("") || mobile.equals("") || branch.equals("") || year.equals("Select year")){
                    Snackbar.make(v, "Please fill details.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else if(!pattern.matcher(email).matches()){
//                    Toast.makeText(register.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                    Snackbar.make(v, "Please enter valid email", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else if(mobile.length() != 10){
//                    Toast.makeText(register.this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
                    Snackbar.make(v, "Please enter valid mobile number", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else {

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String url = UrlLinks.Enroll;

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);

                    nameValuePairs.add(new BasicNameValuePair("username", username));
                    nameValuePairs.add(new BasicNameValuePair("fname", fname));
                    nameValuePairs.add(new BasicNameValuePair("email", email));
                    nameValuePairs.add(new BasicNameValuePair("mobile", mobile));
                    nameValuePairs.add(new BasicNameValuePair("branch", branch));
                    nameValuePairs.add(new BasicNameValuePair("year", year));
                    nameValuePairs.add(new BasicNameValuePair("title", title));
                    nameValuePairs.add(new BasicNameValuePair("category", category));

                    String result = null;
                    try {
                        result = jSOnClassforData.forCallingStringAndreturnSTring(url,nameValuePairs);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (result.equals("success")) {
                        Snackbar.make(v, "You response submited sucessfully !", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        Intent io = new Intent(enrollForm.this, MainActivity.class);
                        startActivity(io);
                        finish();

                    } else {
                        Snackbar.make(v, "Your response is already submitted.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}