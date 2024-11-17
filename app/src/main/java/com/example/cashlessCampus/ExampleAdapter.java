package com.example.cashlessCampus;

import static com.example.cashlessCampus.login.session;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private List<ExampleItem> exampleList;
    private List<ExampleItem> exampleListFull;
    private Context mContext;

    class ExampleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView1,textView2,textView3,edit,delete;
        LinearLayout parentLayout;

        ExampleViewHolder(View itemView) {
            super(itemView);

            this.imageView = (ImageView) itemView.findViewById(R.id.image_app);
            this.textView1 = (TextView) itemView.findViewById(R.id.textview1);
            this.textView2 = (TextView) itemView.findViewById(R.id.textview2);
            this.textView3 = (TextView) itemView.findViewById(R.id.textview3);
            this.edit = (TextView) itemView.findViewById(R.id.edit);
            this.delete = (TextView) itemView.findViewById(R.id.delete);
            this.parentLayout = (LinearLayout) itemView.findViewById(R.id.container);

        }
    }

    public ExampleAdapter(List<ExampleItem> exampleList2, Context context) {
       // this.mContext = context;
        this.exampleList = exampleList2;
        this.exampleListFull = new ArrayList(exampleList2);
    }

    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       // mContext = parent.getContext();
        return new ExampleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_apps, parent, false));
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mContext = recyclerView.getContext();
    }
    
    public void onBindViewHolder(ExampleViewHolder holder, int position) {

        final ExampleItem currentItem = (ExampleItem) this.exampleList.get(position);

        String stUrl = UrlLinks.urlserverpython+currentItem.getText0();
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new URL(stUrl).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.imageView.setImageBitmap(bitmap);
        holder.textView1.setText("Name : "+currentItem.getText1());
        holder.textView2.setText("Category : "+currentItem.getText3());
        if(currentItem.getText3().equals("Library")){
            holder.textView3.setVisibility(View.GONE);
        }
        else if(currentItem.getText3().equals("Event")){
            holder.textView2.setVisibility(View.GONE);
            holder.textView3.setVisibility(View.VISIBLE);
            holder.textView3.setText("Price : "+currentItem.getText2());
        }
        else{
            holder.textView3.setVisibility(View.VISIBLE);
            holder.textView3.setText("Price : "+currentItem.getText2());
        }

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(currentItem.getText3().equals("Event")){
                    Intent intent = new Intent(mContext, updateEvents.class);
                    intent.putExtra("id", currentItem.getText4());
                    intent.putExtra("image", currentItem.getText0());
                    intent.putExtra("name", currentItem.getText1());
                    intent.putExtra("price", currentItem.getText2());
                    intent.putExtra("category", currentItem.getText3());
                    mContext.startActivity(intent);
                }
                else{
                    Intent intent = new Intent(mContext, updateItems.class);
                    intent.putExtra("id", currentItem.getText4());
                    intent.putExtra("image", currentItem.getText0());
                    intent.putExtra("name", currentItem.getText1());
                    intent.putExtra("price", currentItem.getText2());
                    intent.putExtra("category", currentItem.getText3());
                    mContext.startActivity(intent);
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                String url = UrlLinks.deleteItem;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("id", currentItem.getText4()));
                nameValuePairs.add(new BasicNameValuePair("name", currentItem.getText1()));

                String result = null;
                try {
                    result = jSOnClassforData.forCallingStringAndreturnSTring(url,nameValuePairs);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (result.equals("success")) {
                    Snackbar.make(view, "Item deleted successfully.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    Snackbar.make(view, "Something went wrong.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
    }

    public int getItemCount() {
        return this.exampleList.size();
    }

    public void setFilter(List<ExampleItem> filterdNames) {
        this.exampleList = filterdNames;
        notifyDataSetChanged();
    }
}