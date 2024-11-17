package com.example.cashlessCampus.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cashlessCampus.R;
import com.example.cashlessCampus.UrlLinks;
import com.example.cashlessCampus.jSOnClassforData;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class updateFunds extends Fragment {
    TextInputLayout username,fund;
    EditText ed1,ed2;
    TextView update;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_update_funds, container, false);

        username = root.findViewById(R.id.username);
        ed1 = username.getEditText();
        fund = root.findViewById(R.id.fund);
        ed2 = fund.getEditText();
        update = root.findViewById(R.id.upload);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idno = ed1.getText().toString();
                String fund = ed2.getText().toString();

                if(idno.equals("") || fund.equals("")){
                    Snackbar.make(v, "Please fill details.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else {

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String url = UrlLinks.updateFunds;

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

                    nameValuePairs.add(new BasicNameValuePair("idno", idno));
                    nameValuePairs.add(new BasicNameValuePair("fund", fund));

                    String result = null;
                    try {
                        result = jSOnClassforData.forCallingStringAndreturnSTring(url, nameValuePairs);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (result.equals("success")) {

                        ed1.setText("");
                        ed2.setText("");
                        Snackbar.make(v, "Funds updated.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else {
                        Snackbar.make(v, "User not found.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }
            }
        });
        return root;
    }

}