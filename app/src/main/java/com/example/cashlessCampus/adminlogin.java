package com.example.cashlessCampus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class adminlogin extends AppCompatActivity {
    TextView login;
    TextInputLayout ti1,ti2;
    EditText ed1,ed2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);

        login = findViewById(R.id.log);
        ti1 = findViewById(R.id.username1);
        ed1 = ti1.getEditText();
        ti2 = findViewById(R.id.password1);
        ed2 = ti2.getEditText();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = ed1.getText().toString();
                String password = ed2.getText().toString();

                if(!username.equals("admin") || !password.equals("admin")){
                    Snackbar.make(v, "Wrong username or password", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else {
                    Snackbar.make(v, "Successfully", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    Intent io = new Intent(adminlogin.this, MainActivity1.class);
                    startActivity(io);
                    finish();
                }
            }
        });
    }
}