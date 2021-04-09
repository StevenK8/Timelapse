/*
package com.example.timelapse;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Bundle;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;



public class GalerieFragmentZip extends Fragment implements Response.Listener<byte[]>, ErrorListener {
    View root;
    EditText eText;
    Button download, unzip;
    boolean flag = false;
    private static Random random = new Random(Calendar.getInstance().getTimeInMillis());
    String zipFile;
    InputStreamVolleyRequest request;
    int count;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.fragment_galerie, container, false);

        download.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shouldAskPermissions()) {
                    askPermissions();
                }
                String mUrl="https://fastapi.stevenkerautret.eu/video?id_album=56&access_token=F%3E%3Caw%3Bv)9H4JRY%3D4%23g%40%7DYN68b%24%256!j9F8g%3DV2%5EKr%5E8s%3A(%5BN7(%5D";
                request = new InputStreamVolleyRequest(Request.Method.GET, mUrl, GalerieFragmentZip.this, GalerieFragmentZip.this, null);
                RequestQueue mRequestQueue = Volley.newRequestQueue(getContext(),
                        new HurlStack());
                mRequestQueue.add(request);
                /*mRequestQueue.start();
                mProgressDialog = new ProgressDialog(getContext());
                mProgressDialog.setMessage("Downloading Zip File..");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            }
        });

        unzip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                try {
                    unzip();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        return root;
    }
    @Override
    public void onResponse(byte[] response) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            if (response!=null) {

                //Read file name from headers
                /*String content =request.responseHeaders.get("Content-Disposition");
                StringTokenizer st = new StringTokenizer(content, "=");
                /*String arrTag = st.toString();

                String filename = arrTag;
                String filename = "video.mp4";
                filename = filename.replace(":", ".");
                Log.d("DEBUG::RESUME FILE NAME", filename);

                try{
                    long lenghtOfFile = response.length;

                    //covert reponse to input stream
                    InputStream input = new ByteArrayInputStream(response);
                    ContextWrapper cw = new ContextWrapper(getContext());
                    File path = cw.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
                    //File path = Environment.getExternalStorageDirectory();
                    File file = new File(path, filename);
                    zipFile = file.toString();
                    map.put("resume_path", file.toString());
                    BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file));
                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();
                    //mProgressDialog.dismiss();

                }catch(IOException e){
                    e.printStackTrace();

                }
            }
        } catch (Exception e) {
            //mProgressDialog.dismiss();
            // TODO Auto-generated catch block
            Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        //mProgressDialog.dismiss();
        Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE. ERROR:: "+error.getMessage());
    }

    public void unzip() throws IOException {
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("Please Wait...Extracting zip file ... ");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        new UnZipTask().execute(zipFile, unzipLocation);
    }


    private class UnZipTask extends AsyncTask<String, Void, Boolean> {
        @SuppressWarnings("rawtypes")
        @Override
        protected Boolean doInBackground(String... params) {
            String filePath = params[0];
            String destinationPath = params[1];

            File archive = new File(filePath);
            try {


                ZipFile zipfile = new ZipFile(archive);
                for (Enumeration e = zipfile.entries();
                     e.hasMoreElements(); ) {
                    ZipEntry entry = (ZipEntry) e.nextElement();
                    unzipEntry(zipfile, entry, destinationPath);
                }


                UnzipUtil d = new UnzipUtil(zipFile, unzipLocation);
                d.unzip();

            } catch (Exception e) {

                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mProgressDialog.dismiss();

        }


        private void unzipEntry(ZipFile zipfile, ZipEntry entry,
                                String outputDir) throws IOException {

            if (entry.isDirectory()) {
                createDir(new File(outputDir, entry.getName()));
                return;
            }

            File outputFile = new File(outputDir, entry.getName());
            if (!outputFile.getParentFile().exists()) {
                createDir(outputFile.getParentFile());
            }

            // Log.v("", "Extracting: " + entry);
            BufferedInputStream inputStream = new BufferedInputStream(zipfile.getInputStream(entry));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

            try {

            } finally {
                outputStream.flush();
                outputStream.close();
                inputStream.close();


            }
        }

        /*private void createDir(File dir) {
            if (dir.exists()) {
                return;
            }
            if (!dir.mkdirs()) {
                throw new RuntimeException("Can not create dir " + dir);
            }
        }
    }

    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

}*/







