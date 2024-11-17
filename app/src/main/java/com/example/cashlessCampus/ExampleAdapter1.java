package com.example.cashlessCampus;

import static com.example.cashlessCampus.login.session;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ExampleAdapter1 extends RecyclerView.Adapter<ExampleAdapter1.ExampleViewHolder> {
    private List<ExampleItem1> exampleList;
    private List<ExampleItem1> exampleListFull;
    private Context mContext;

    class ExampleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView1,textView2,textView3,pay;
        LinearLayout parentLayout;

        ExampleViewHolder(View itemView) {
            super(itemView);

            this.imageView = (ImageView) itemView.findViewById(R.id.image_app);
            this.textView1 = (TextView) itemView.findViewById(R.id.textview1);
            this.textView2 = (TextView) itemView.findViewById(R.id.textview2);
            this.textView3 = (TextView) itemView.findViewById(R.id.textview3);
            this.pay = (TextView) itemView.findViewById(R.id.pay);
            this.parentLayout = (LinearLayout) itemView.findViewById(R.id.container);

        }
    }

    public ExampleAdapter1(List<ExampleItem1> exampleList2, Context context) {
       // this.mContext = context;
        this.exampleList = exampleList2;
        this.exampleListFull = new ArrayList(exampleList2);
    }

    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExampleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_apps1, parent, false));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mContext = recyclerView.getContext();
    }
    
    public void onBindViewHolder(ExampleViewHolder holder, int position) {

        final ExampleItem1 currentItem = (ExampleItem1) this.exampleList.get(position);

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
        holder.textView3.setText("Price : "+currentItem.getText2());
        if(currentItem.getText3().equals("Library")){
            holder.textView3.setVisibility(View.GONE);
        }
        else{
            holder.textView3.setVisibility(View.VISIBLE);
            holder.textView3.setText("Price : "+currentItem.getText2());
        }

        if(currentItem.getText3().equals("Library")){
            holder.pay.setText("Issue Book");
        }
        else {
            holder.pay.setText("Pay Using QR");
        }

        holder.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentItem.getText3().equals("Library")){
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String url = UrlLinks.issuebook;

                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

                    nameValuePairs.add(new BasicNameValuePair("pname", currentItem.getText1()));
                    nameValuePairs.add(new BasicNameValuePair("pcat", currentItem.getText3()));
                    nameValuePairs.add(new BasicNameValuePair("userid", session));

                    String result = null;
                    try {
                        result = jSOnClassforData.forCallingStringAndreturnSTring(url, nameValuePairs);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (result.equals("fail")) {
                        Snackbar.make(view, "Already issued !", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else {
                        showOrderAlertDialog(mContext,"Book issued Successfully!");
                        makeNotification(currentItem.getText1()+" book is issued for 7 days only.");
                    }
                }
                else {

                    Intent intent = new Intent(mContext, OrderPage.class);
                    intent.putExtra("id", currentItem.getText4());
                    intent.putExtra("image", currentItem.getText0());
                    intent.putExtra("name", currentItem.getText1());
                    intent.putExtra("price", currentItem.getText2());
                    intent.putExtra("category", currentItem.getText3());
                    mContext.startActivity(intent);
                }
            }
        });

    }

    public int getItemCount() {
        return this.exampleList.size();
    }

    public void setFilter(List<ExampleItem1> filterdNames) {
        this.exampleList = filterdNames;
        notifyDataSetChanged();
    }

    @SuppressLint("NotificationPermission")
    public void makeNotification(String fine){
        String chid = "channalid";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext,chid);
        builder.setSmallIcon(R.drawable.eventlogo)
                .setContentTitle("Book Issue")
                .setContentText(fine)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent i = new Intent(mContext,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,0,i,PendingIntent.FLAG_MUTABLE);

        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(chid);
            if(notificationChannel == null){
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(chid,"Some",importance);
                notificationChannel.setLightColor(Color.BLUE);

                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);

            }
        }

        notificationManager.notify(0,builder.build());

    }

    private void showOrderAlertDialog(Context context, String Val) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        builder.setView(dialogView);

        TextView messageTextView = dialogView.findViewById(R.id.dialog_message);
        messageTextView.setText(Val);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}