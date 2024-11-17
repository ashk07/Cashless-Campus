package com.example.cashlessCampus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cashlessCampus.ui.home1;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class updateItems extends AppCompatActivity {

    Spinner spin1;
    TextInputLayout title,price;
    EditText ed1,ed2;
    TextView upload;
    private static final int REQUEST_GALLERY = 200;

    ImageView imageView;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    String cameraPermission[];
    String storagePermission[];
    static String fileNameOfImage = "";
    String file_path = null;

    String[] listnumber;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_items);

        // Enable the back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            // Set the title with white color
            SpannableString spannableString = new SpannableString("Update Item");
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

        listnumber = getResources().getStringArray(R.array.items);
        spin1 = findViewById(R.id.auto);
        title = findViewById(R.id.title);
        price = findViewById(R.id.price);
        ed1 = title.getEditText();
        ed2 = price.getEditText();
        imageView = findViewById(R.id.imageView2);
        upload = findViewById(R.id.upload);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(updateItems.this, R.layout.dropdown_item, listnumber);
        spin1.setAdapter(adapter);

        String stUrl = UrlLinks.urlserverpython+getIntent().getStringExtra("image");
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new URL(stUrl).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView.setImageBitmap(bitmap);
        ed1.setText(getIntent().getStringExtra("name"));
        ed2.setText(getIntent().getStringExtra("price"));
        String searchText = getIntent().getStringExtra("category");

        if(searchText.equals("Library")){
            price.setVisibility(View.GONE);
            ed2.setText("-");
        }else {
            price.setVisibility(View.VISIBLE);
        }
        int index = -1;

        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(searchText)) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            spin1.setSelection(index);
        } else {
            spin1.setSelection(0);
        }

        cameraPermission = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePicDialog();
            }
        });

        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("Library")){
                    price.setVisibility(View.GONE);
                    ed2.setText("-");
                }else {
                    price.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String spin = spin1.getSelectedItem().toString();
                String title1 = ed1.getText().toString();
                String price1 = ed2.getText().toString();

                if(spin.equals("Select item category") || title1.equals("") || price1.equals("")){
                    Snackbar.make(v, "Please fill details.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else {

                    ImageView img = findViewById(R.id.imageView2);
                    Bitmap bitmap;
                    bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
                    SaveImage(bitmap);

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String url = UrlLinks.updateItems;

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);

                    nameValuePairs.add(new BasicNameValuePair("id", getIntent().getStringExtra("id")));
                    nameValuePairs.add(new BasicNameValuePair("filename", fileNameOfImage));
                    nameValuePairs.add(new BasicNameValuePair("title", title1));
                    nameValuePairs.add(new BasicNameValuePair("price", price1));
                    nameValuePairs.add(new BasicNameValuePair("category", spin));

                    String result = null;
                    try {
                        result = jSOnClassforData.forCallingStringAndreturnSTring(url, nameValuePairs);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (result.equals("success")) {
                        Snackbar.make(v, "Item updated.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else {
                        Snackbar.make(v, "Something went wrong.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }
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


    private void showImagePicDialog() {
        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(updateItems.this);
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                } else if (which == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        filePicker();
                    }
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            String filePath = getRealPathFromUri(data.getData(), updateItems.this);
            Log.d("File Path : ", " " + filePath);
            this.file_path = filePath;
            File file = new File(filePath);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),bmOptions);
            // bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);
            imageView.setImageBitmap(bitmap);
        }
        else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            file_path = "Not null";
            Bitmap theImage = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(theImage);
        }
    }

    public String getRealPathFromUri(Uri uri, Activity activity){
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor=activity.getContentResolver().query(uri,proj,null,null,null);
        if(cursor==null){
            return uri.getPath();
        }
        else{
            cursor.moveToFirst();
            int id=cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(id);
        }
    }

    private void SaveImage(Bitmap finalBitmap) {
        String username="image";
        String spin = spin1.getSelectedItem().toString();
        String timeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        String fname = spin+"-"+username+"-"+timeStamp+".jpg";
        ContextWrapper cw = new ContextWrapper(updateItems.this);
        File directory = cw.getDir("OTAPP", Context.MODE_PRIVATE);
        if (!directory.exists ()){
            directory.mkdirs();

        }
        File file = new File(directory, fname);

        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        fileNameOfImage=fname;
        new UploadFileAsync().execute("");
        Snackbar.make(updateItems.this.findViewById(android.R.id.content),  "File uploaded.", Snackbar.LENGTH_LONG).setAction("Action", null).show();

    }

    private void filePicker(){
        Snackbar.make(findViewById(android.R.id.content),  "File Picker Call", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        //Let's Pick File
        Intent opengallery=new Intent(Intent.ACTION_PICK);
        opengallery.setType("image/*");
        startActivityForResult(opengallery,REQUEST_GALLERY);
    }

    public class UploadFileAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                //  File file = new File (Environment.getExternalStorageDirectory(), "OTAPP//"+fileNameOfImage);
                String sourceFileUri = fileNameOfImage;// "/mnt/sdcard/abc.png";

                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                //  File sourceFile = new File(sourceFileUri);
                File sourceFile = new File(Environment.getExternalStorageDirectory(), "OTAPP//" + fileNameOfImage);


                ContextWrapper cw = new ContextWrapper(updateItems.this);
                File directory = cw.getDir("OTAPP", Context.MODE_PRIVATE);
                File file = new File(directory, fileNameOfImage);
                sourceFile = file;
                sourceFileUri = file.getAbsolutePath();
                if (sourceFile.isFile()) {

                    try {
                        String upLoadServerUri = UrlLinks.uploadimg;

                        FileInputStream fileInputStream = new FileInputStream(
                                sourceFile);
                        URL url = new URL(upLoadServerUri);

                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true); // Allow Inputs
                        conn.setDoOutput(true); // Allow Outputs
                        conn.setUseCaches(false); // Don't use a Cached Copy
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("ENCTYPE",
                                "multipart/form-data");
                        conn.setRequestProperty("Content-Type",
                                "multipart/form-data;boundary=" + boundary);
                        conn.setRequestProperty("bill", sourceFileUri);
                        dos = new DataOutputStream(conn.getOutputStream());

                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"bill\";filename=\""
                                + sourceFileUri + "\"" + lineEnd);

                        dos.writeBytes(lineEnd);

                        bytesAvailable = fileInputStream.available();

                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        buffer = new byte[bufferSize];

                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        while (bytesRead > 0) {

                            dos.write(buffer, 0, bufferSize);
                            bytesAvailable = fileInputStream.available();
                            bufferSize = Math
                                    .min(bytesAvailable, maxBufferSize);
                            bytesRead = fileInputStream.read(buffer, 0,
                                    bufferSize);

                        }

                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens
                                + lineEnd);
                        int serverResponseCode = conn.getResponseCode();
                        String serverResponseMessage = conn.getResponseMessage();
                        fileInputStream.close();
                        dos.flush();
                        dos.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } // End else block

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    // checking storage permissions
    private Boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(updateItems.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(storagePermission, STORAGE_REQUEST);
        }
    }

    // checking camera permissions
    private Boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(updateItems.this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(updateItems.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(cameraPermission, CAMERA_REQUEST);
        }
    }

}