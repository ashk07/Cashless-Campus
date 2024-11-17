package com.example.cashlessCampus.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cashlessCampus.ExampleAdapterC;
import com.example.cashlessCampus.ExampleAdapterE;
import com.example.cashlessCampus.ExampleAdapterL;
import com.example.cashlessCampus.ExampleAdapterS;
import com.example.cashlessCampus.ExampleItem5;
import com.example.cashlessCampus.R;
import com.example.cashlessCampus.UrlLinks;
import com.example.cashlessCampus.jSOnClassforData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class PaymentCount extends Fragment {
    private ExampleAdapterC adapter;
    private ExampleAdapterL adapter1;
    private ExampleAdapterS adapter2;
    private ExampleAdapterE adapter3;
    private List<ExampleItem5> exampleList,exampleList1,exampleList2,exampleList3;
    RecyclerView recyclerView,recyclerView1,recyclerView2,recyclerView3;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_payment_count, container, false);

        fillExampleList();

        recyclerView = (RecyclerView) root.findViewById( R.id.RecyclerView1);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        this.adapter = new ExampleAdapterC(exampleList,getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(this.adapter);

        recyclerView1 = (RecyclerView) root.findViewById( R.id.RecyclerView2);
        recyclerView1.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        this.adapter1 = new ExampleAdapterL(exampleList1,getActivity());
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setAdapter(this.adapter1);

        recyclerView2 = (RecyclerView) root.findViewById( R.id.RecyclerView3);
        recyclerView2.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        this.adapter2 = new ExampleAdapterS(exampleList2,getActivity());
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setAdapter(this.adapter2);

        recyclerView3 = (RecyclerView) root.findViewById( R.id.RecyclerView4);
        recyclerView3.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(getActivity());
        this.adapter3 = new ExampleAdapterE(exampleList3,getActivity());
        recyclerView3.setLayoutManager(layoutManager3);
        recyclerView3.setAdapter(this.adapter3);

        return root;
    }



    @SuppressLint("NewApi")
    private void fillExampleList() {
        exampleList = new ArrayList();
        exampleList1 = new ArrayList();
        exampleList2 = new ArrayList();
        exampleList3 = new ArrayList();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String url = UrlLinks.getSumCount;
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

        nameValuePairs.add(new BasicNameValuePair("user", "admin"));

        String result = null;
        try {
            result = jSOnClassforData.forCallingStringAndreturnSTring(url, nameValuePairs);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = new JSONArray(result);

            JSONArray librarysumcount = new JSONArray(jsonArray.getJSONArray(0).toString());
            JSONArray canteensumcount = new JSONArray(jsonArray.getJSONArray(1).toString());
            JSONArray stationarysumcount = new JSONArray(jsonArray.getJSONArray(2).toString());
            JSONArray eventsumcount = new JSONArray(jsonArray.getJSONArray(3).toString());

            for (int i = 0; i < canteensumcount.length(); i++) {
                String image = String.valueOf(canteensumcount.getJSONArray(i).getString(0));
                String name = String.valueOf(canteensumcount.getJSONArray(i).getString(1));
                String price = String.valueOf(canteensumcount.getJSONArray(i).getString(2));
                exampleList.add(new ExampleItem5(image,name,price));
            }

            for (int i = 0; i < librarysumcount.length(); i++) {
                String image = String.valueOf(librarysumcount.getJSONArray(i).getString(0));
                String name = String.valueOf(librarysumcount.getJSONArray(i).getString(1));
                String price = String.valueOf(librarysumcount.getJSONArray(i).getString(2));
                exampleList1.add(new ExampleItem5(image,name,price));
            }

            for (int i = 0; i < stationarysumcount.length(); i++) {
                String image = String.valueOf(stationarysumcount.getJSONArray(i).getString(0));
                String name = String.valueOf(stationarysumcount.getJSONArray(i).getString(1));
                String price = String.valueOf(stationarysumcount.getJSONArray(i).getString(2));
                exampleList2.add(new ExampleItem5(image,name,price));
            }

            for (int i = 0; i < eventsumcount.length(); i++) {
                String image = String.valueOf(eventsumcount.getJSONArray(i).getString(0));
                String name = String.valueOf(eventsumcount.getJSONArray(i).getString(1));
                String price = String.valueOf(eventsumcount.getJSONArray(i).getString(2));
                exampleList3.add(new ExampleItem5(image,name,price));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}