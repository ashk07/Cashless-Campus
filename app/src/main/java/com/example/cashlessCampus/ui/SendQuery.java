package com.example.cashlessCampus.ui;

import static com.example.cashlessCampus.login.session;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cashlessCampus.R;
import com.example.cashlessCampus.UrlLinks;
import com.example.cashlessCampus.jSOnClassforData;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class SendQuery extends Fragment {
    TextView login;
    TextInputLayout ti1;
    EditText ed1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_send_query, container, false);

        login = root.findViewById(R.id.log);
        ti1 = root.findViewById(R.id.username1);
        ed1 = ti1.getEditText();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String query = ed1.getText().toString();

                if(query.equals("")){
                    Snackbar.make(v, "Please fill details.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else {

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String url = UrlLinks.sendQuery;

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

                    nameValuePairs.add(new BasicNameValuePair("cid", session));
                    nameValuePairs.add(new BasicNameValuePair("query", query));

                    String result = null;
                    try {
                        result = jSOnClassforData.forCallingStringAndreturnSTring(url,nameValuePairs);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (result.equals("success")) {
                        ed1.setText("");
                        Snackbar.make(v, "Query successfully send", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else {
                        Snackbar.make(v, "Query not send!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }
            }
        });

        return root;
    }
}