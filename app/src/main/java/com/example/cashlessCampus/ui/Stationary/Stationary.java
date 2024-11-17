package com.example.cashlessCampus.ui.Stationary;

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
import android.widget.TextView;

import com.example.cashlessCampus.ExampleAdapter1;
import com.example.cashlessCampus.ExampleItem1;
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

public class Stationary extends Fragment {
    EditText Searchtext;
    private ExampleAdapter1 adapter;
    private List<ExampleItem1> exampleList;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_stationary, container, false);

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
        this.adapter = new ExampleAdapter1(exampleList,getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(this.adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Loading...", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                fillExampleList();

                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                adapter = new ExampleAdapter1(exampleList,getActivity());
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

        String url = UrlLinks.getAllItems1;
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

        nameValuePairs.add(new BasicNameValuePair("category", "Stationary"));

        String result = null;
        try {
            result = jSOnClassforData.forCallingStringAndreturnSTring(url, nameValuePairs);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                String image = String.valueOf(jsonArray.getJSONArray(i).getString(1));
                String name = String.valueOf(jsonArray.getJSONArray(i).getString(2));
                String price = String.valueOf(jsonArray.getJSONArray(i).getString(3));
                String cat = String.valueOf(jsonArray.getJSONArray(i).getString(4));
                String id = String.valueOf(jsonArray.getJSONArray(i).getString(0));

                exampleList.add(new ExampleItem1(image,name,price,cat,id));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /* access modifiers changed from: private */
    public void filterQuery(String text) {
        ArrayList<ExampleItem1> filterdNames = new ArrayList<>();
        for (ExampleItem1 s : exampleList) {
            if (s.getText1().toLowerCase().contains(text) || s.getText3().toLowerCase().contains(text)) {
                filterdNames.add(s);
            }
        }
        this.adapter.setFilter(filterdNames);
    }
}