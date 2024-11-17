package com.example.cashlessCampus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class ExampleAdapter3 extends RecyclerView.Adapter<ExampleAdapter3.ExampleViewHolder> {
    private List<ExampleItem3> exampleList;
    private List<ExampleItem3> exampleListFull;
    private Context mContext;

    class ExampleViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3,textView4,textView5;
        TextView initial;
        RelativeLayout parentLayout;

        ExampleViewHolder(View itemView) {
            super(itemView);

            this.textView1 = (TextView) itemView.findViewById(R.id.textview1);
            this.textView2 = (TextView) itemView.findViewById(R.id.textview2);
            this.textView3 = (TextView) itemView.findViewById(R.id.textview3);
            this.textView4 = (TextView) itemView.findViewById(R.id.textview4);
            this.textView5 = (TextView) itemView.findViewById(R.id.textview5);
            this.initial = (TextView) itemView.findViewById(R.id.initial);
            this.parentLayout = (RelativeLayout) itemView.findViewById(R.id.container);

        }
    }

    public ExampleAdapter3(List<ExampleItem3> exampleList2, Context context) {
       // this.mContext = context;
        this.exampleList = exampleList2;
        this.exampleListFull = new ArrayList(exampleList2);
    }

    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       // mContext = parent.getContext();
        return new ExampleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_apps3, parent, false));
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mContext = recyclerView.getContext();
    }
    
    public void onBindViewHolder(ExampleViewHolder holder, int position) {

        final ExampleItem3 currentItem = (ExampleItem3) this.exampleList.get(position);
        holder.textView1.setText(currentItem.getText1());
        holder.textView2.setText("Vid : "+currentItem.getText2());
        holder.textView3.setText("Branch/Year : "+currentItem.getText3());
        holder.textView4.setText("Email : "+currentItem.getText4());
        holder.textView5.setText("Mobile No. : "+currentItem.getText5());
        String word = currentItem.getText1();
        String capitalizedWord = word.substring(0, 1).toUpperCase();
        holder.initial.setText(capitalizedWord);
    }

    public int getItemCount() {
        return this.exampleList.size();
    }

    /* access modifiers changed from: 0000 */
    public void setFilter(List<ExampleItem3> filterdNames) {
        this.exampleList = filterdNames;
        notifyDataSetChanged();
    }
}