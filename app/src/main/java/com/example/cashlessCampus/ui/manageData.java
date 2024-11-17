package com.example.cashlessCampus.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.cashlessCampus.ExampleAdapter1;
import com.example.cashlessCampus.ExampleAdapter3;
import com.example.cashlessCampus.ExampleItem1;
import com.example.cashlessCampus.ExampleItem3;
import com.example.cashlessCampus.R;
import com.example.cashlessCampus.UrlLinks;
import com.example.cashlessCampus.jSOnClassforData;
import com.google.android.material.snackbar.Snackbar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class manageData extends Fragment {
    EditText Searchtext;
    private ExampleAdapter3 adapter;
    private List<ExampleItem3> exampleList;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_manage_data, container, false);

        fillExampleList();
        this.Searchtext = (EditText) root.findViewById(R.id.search_input);
        this.Searchtext.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                filterQuery(editable.toString());
            }
        });

        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh_layout);

        recyclerView = (RecyclerView) root.findViewById( R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        this.adapter = new ExampleAdapter3(exampleList,getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(this.adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Loading...", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                fillExampleList();

                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                adapter = new ExampleAdapter3(exampleList,getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return root;
    }


    @SuppressLint("NewApi")
    private void fillExampleList() {
        exampleList = new ArrayList();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String url = UrlLinks.getAllUsers;
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

        nameValuePairs.add(new BasicNameValuePair("username", "admin"));

        String result = null;
        try {
            result = jSOnClassforData.forCallingStringAndreturnSTring(url, nameValuePairs);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                String fname = String.valueOf(jsonArray.getJSONArray(i).getString(1));
                String lname = String.valueOf(jsonArray.getJSONArray(i).getString(2));
                String cid = String.valueOf(jsonArray.getJSONArray(i).getString(3));
                String branch = String.valueOf(jsonArray.getJSONArray(i).getString(4));
                String year = String.valueOf(jsonArray.getJSONArray(i).getString(5));
                String email = String.valueOf(jsonArray.getJSONArray(i).getString(6));
                String mob = String.valueOf(jsonArray.getJSONArray(i).getString(7));

                exampleList.add(new ExampleItem3(fname+" "+lname,cid,branch+"/"+year,email,mob));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /* access modifiers changed from: private */
    public void filterQuery(String text) {
        ArrayList<ExampleItem3> filterdNames = new ArrayList<>();
        for (ExampleItem3 s : exampleList) {
            if (s.getText2().toLowerCase().contains(text) || s.getText3().toLowerCase().contains(text)) {
                filterdNames.add(s);
            }
        }
        this.adapter.setFilter(filterdNames);
    }

}