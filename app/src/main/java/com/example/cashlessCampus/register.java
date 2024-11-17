package com.example.cashlessCampus;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class register extends AppCompatActivity {

    TextView reg;
    Spinner spin1;
    TextInputLayout ti1,ti2,ti3,ti4,ti5,ti6,ti7,ti8;
    EditText ed1,ed2,ed3,ed4,ed5,ed6,ed7,ed8;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        reg = findViewById(R.id.txreg);
        ti1 = findViewById(R.id.fname);
        ed1 = ti1.getEditText();
        ti2 = findViewById(R.id.lname);
        ed2 = ti2.getEditText();
        ti3 = findViewById(R.id.cid);
        ed3 = ti3.getEditText();
        ti4 = findViewById(R.id.year);
        ed4 = ti4.getEditText();
        ti5 = findViewById(R.id.email);
        ed5 = ti5.getEditText();
        ti6 = findViewById(R.id.mobile);
        ed6 = ti6.getEditText();
        ti7 = findViewById(R.id.password);
        ed7 = ti7.getEditText();
        ti8 = findViewById(R.id.cpassword);
        ed8 = ti8.getEditText();
        spin1 = findViewById(R.id.branch);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(register.this, R.layout.dropdown_item, getResources().getStringArray(R.array.branch));
        spin1.setAdapter(adapter2);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fname = ed1.getText().toString();
                String lname = ed2.getText().toString();
                String cid = ed3.getText().toString();
                String branch = spin1.getSelectedItem().toString();
                String year = ed4.getText().toString();
                String email = ed5.getText().toString();
                String mobile = ed6.getText().toString();
                String password = ed7.getText().toString();
                String cpassword = ed8.getText().toString();

                Pattern pattern = Patterns.EMAIL_ADDRESS;

                if(fname.equals("") || lname.equals("")|| cid.equals("")|| branch.equals("Select branch") || year.equals("") ||
                        email.equals("") || mobile.equals("")|| password.equals("")|| cpassword.equals("")){
                    Snackbar.make(v, "Please fill details", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else if(!pattern.matcher(email).matches()){
                    Snackbar.make(v, "Please enter valid email", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else if(cid.length() != 11){
                    Snackbar.make(v, "Please enter valid college id", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else if(mobile.length() != 10){
                    Snackbar.make(v, "Please enter valid mobile number", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else if(!password.equals(cpassword)){
                    Snackbar.make(v, "Password and confirm password should match", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else {

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String url = UrlLinks.pyregister;

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);

                    nameValuePairs.add(new BasicNameValuePair("fname", fname));
                    nameValuePairs.add(new BasicNameValuePair("lname", lname));
                    nameValuePairs.add(new BasicNameValuePair("cid", cid));
                    nameValuePairs.add(new BasicNameValuePair("branch", branch));
                    nameValuePairs.add(new BasicNameValuePair("year", year));
                    nameValuePairs.add(new BasicNameValuePair("email", email));
                    nameValuePairs.add(new BasicNameValuePair("mobile", mobile));
                    nameValuePairs.add(new BasicNameValuePair("password", password));

                    String result = null;
                    try {
                        result = jSOnClassforData.forCallingStringAndreturnSTring(url,nameValuePairs);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (result.equals("success")) {
                        Snackbar.make(v, "User Added successfully", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        Intent io = new Intent(register.this, login.class);

                        startActivity(io);
                        finish();
                    }
                    else if(result.equals("invalidmail")){
                        Snackbar.make(v, "Invalid email id !", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                    }
                    else {
                        Snackbar.make(v, "Something went wrong !", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }

                }

            }
        });

    }
}