package com.example.cashlessCampus;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ExampleAdapter2 extends RecyclerView.Adapter<ExampleAdapter2.ExampleViewHolder> {
    private List<ExampleItem2> exampleList;
    private List<ExampleItem2> exampleListFull;
    private Context mContext;

    class ExampleViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3,textView4;
        TextView initial;
        TextView price;
        RelativeLayout parentLayout;

        ExampleViewHolder(View itemView) {
            super(itemView);

            this.textView1 = (TextView) itemView.findViewById(R.id.textview1);
            this.textView2 = (TextView) itemView.findViewById(R.id.textview2);
            this.textView3 = (TextView) itemView.findViewById(R.id.textview3);
            this.textView4 = (TextView) itemView.findViewById(R.id.textview4);
            this.initial = (TextView) itemView.findViewById(R.id.initial);
            this.price = (TextView) itemView.findViewById(R.id.price);
            this.parentLayout = (RelativeLayout) itemView.findViewById(R.id.container);

        }
    }

    public ExampleAdapter2(List<ExampleItem2> exampleList2, Context context) {
       // this.mContext = context;
        this.exampleList = exampleList2;
        this.exampleListFull = new ArrayList(exampleList2);
    }

    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       // mContext = parent.getContext();
        return new ExampleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_apps2, parent, false));
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mContext = recyclerView.getContext();
    }
    
    public void onBindViewHolder(ExampleViewHolder holder, int position) {

        final ExampleItem2 currentItem = (ExampleItem2) this.exampleList.get(position);
        holder.textView1.setText(currentItem.getText1());
        holder.textView2.setText(currentItem.getText2());
        holder.textView3.setText(currentItem.getText3());
        String word = currentItem.getText2();
        String capitalizedWord = word.substring(0, 1).toUpperCase();
        holder.initial.setText(capitalizedWord);
        if(currentItem.getText2().equals("Library")){
            holder.textView4.setVisibility(View.VISIBLE);
            int daysToAdd = 7;
            String resultDate = addDaysToDate(currentItem.getText3(), daysToAdd);
            holder.textView4.setText("Return Date:"+resultDate.split(" ")[0]);
            holder.price.setText(currentItem.getText4());
        }
        else {
            holder.price.setText("Rs."+currentItem.getText4());
        }
    }
    public static String addDaysToDate(String inputDate, int daysToAdd) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        try {
            Date date = sdf.parse(inputDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);
            String resultDate = sdf.format(calendar.getTime());
            return resultDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Handle the parse exception as needed
        }
    }

    public int getItemCount() {
        return this.exampleList.size();
    }

    /* access modifiers changed from: 0000 */
    public void setFilter(List<ExampleItem2> filterdNames) {
        this.exampleList = filterdNames;
        notifyDataSetChanged();
    }
}