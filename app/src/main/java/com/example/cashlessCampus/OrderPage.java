package com.example.cashlessCampus;

import static com.example.cashlessCampus.login.session;

import android.annotation.SuppressLint;
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
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

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

public class OrderPage extends AppCompatActivity {
    ImageView imageView;
    TextView textView1,textView2,scan;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);

        // Enable the back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            // Set the title with white color
            SpannableString spannableString = new SpannableString("Pay bill");
            int whiteColor = ContextCompat.getColor(this, android.R.color.white);
            spannableString.setSpan(new ForegroundColorSpan(whiteColor), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            getSupportActionBar().setTitle(spannableString);

            // Set the color of the back button icon to white
            Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.back); // Replace with your own back icon
            if (upArrow != null) {
                upArrow.setColorFilter(whiteColor, PorterDuff.Mode.SRC_ATOP);
                getSupportActionBar().setHomeAsUpIndicator(upArrow);
            }
        }

        imageView = findViewById(R.id.imageView2);
        textView1 = findViewById(R.id.textview1);
        textView2 = findViewById(R.id.textview2);
        scan = findViewById(R.id.scan);

        String stUrl = UrlLinks.urlserverpython+getIntent().getStringExtra("image");
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new URL(stUrl).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView.setImageBitmap(bitmap);
        textView1.setText("Name : "+getIntent().getStringExtra("name"));
        textView2.setText("Price : "+getIntent().getStringExtra("price"));

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(OrderPage.this);

                intentIntegrator.setPrompt("For Flash use volume button");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setCaptureActivity(Capture.class);
                intentIntegrator.initiateScan();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(OrderPage.this);
            builder.setTitle("Message");
            builder.setMessage("Do you want to continue payment?");

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String url = UrlLinks.paybill;

                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);

                    nameValuePairs.add(new BasicNameValuePair("pname", getIntent().getStringExtra("name")));
                    nameValuePairs.add(new BasicNameValuePair("pcat", getIntent().getStringExtra("category")));
                    nameValuePairs.add(new BasicNameValuePair("pprize", getIntent().getStringExtra("price")));
                    nameValuePairs.add(new BasicNameValuePair("scandata", intentResult.getContents()));
                    nameValuePairs.add(new BasicNameValuePair("userid", session));

                    String result = null;
                    try {
                        result = jSOnClassforData.forCallingStringAndreturnSTring(url, nameValuePairs);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (result.equals("wrongscan")) {
                        Snackbar.make(findViewById(android.R.id.content), "Wrong QR code scanned !", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                    else if (result.equals("fail")) {
                        Snackbar.make(findViewById(android.R.id.content), "You have insufficient funds !", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                    else {
                        showOrderAlertDialog(OrderPage.this,result.split("&&")[0]);
                        if(getIntent().getStringExtra("category").equals("Event")){
                            makeNotification1(result.split("&&")[1],"Your registration is done for "+getIntent().getStringExtra("name"));
                        }
                        else{
                            makeNotification(result.split("&&")[1]);
                        }
                    }
                }
            });

            builder.show();
        }
    }
    @SuppressLint("NotificationPermission")
    public void makeNotification1(String fine,String message){
        String chid = "channalid";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),chid);
        builder.setSmallIcon(R.drawable.eventlogo)
                .setContentTitle("Transaction alert")
                .setContentText(fine+" Rs. deducted from you account! \n"+message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,i,PendingIntent.FLAG_MUTABLE);

        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

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

    @SuppressLint("NotificationPermission")
    public void makeNotification(String fine){
        String chid = "channalid";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),chid);
        builder.setSmallIcon(R.drawable.eventlogo)
                .setContentTitle("Transaction alert")
                .setContentText(fine+" Rs. deducted from you account!")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,i,PendingIntent.FLAG_MUTABLE);

        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

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
        messageTextView.setText("Your order has been successfully placed. Remaining balance: Rs "+Val);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent io = new Intent(OrderPage.this, MainActivity.class);
                startActivity(io);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}