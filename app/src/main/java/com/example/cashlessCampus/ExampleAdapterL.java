package com.example.cashlessCampus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ExampleAdapterL extends RecyclerView.Adapter<ExampleAdapterL.ExampleViewHolder> {
    private List<ExampleItem5> exampleList1;
    private List<ExampleItem5> exampleListFull;
    private Context mContext;
    class ExampleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView1,textView2;
        RelativeLayout parentLayout;

        ExampleViewHolder(View itemView) {
            super(itemView);

            this.imageView = (ImageView) itemView.findViewById(R.id.image_app);
            this.textView1 = (TextView) itemView.findViewById(R.id.textview1);
            this.textView2 = (TextView) itemView.findViewById(R.id.price);
            this.parentLayout = (RelativeLayout) itemView.findViewById(R.id.container);

        }
    }

    public ExampleAdapterL(List<ExampleItem5> exampleList2, Context context) {
       // this.mContext = context;
        this.exampleList1 = exampleList2;
        this.exampleListFull = new ArrayList(exampleList2);
    }

    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       // mContext = parent.getContext();
        return new ExampleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_apps5, parent, false));
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mContext = recyclerView.getContext();
    }
    
    public void onBindViewHolder(ExampleViewHolder holder, int position) {

        final ExampleItem5 currentItem = (ExampleItem5) this.exampleList1.get(position);

        String stUrl = UrlLinks.urlserverpython+currentItem.getText0();
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new URL(stUrl).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.imageView.setImageBitmap(bitmap);
        holder.textView1.setText(currentItem.getText1());
        holder.textView2.setText(currentItem.getText2());

    }

    public int getItemCount() {
        return this.exampleList1.size();
    }
}