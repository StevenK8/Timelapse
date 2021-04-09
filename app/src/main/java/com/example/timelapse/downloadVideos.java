package com.example.timelapse;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class downloadVideos extends Fragment {
    View root;
    Button download;

    private static final int PERMISSION_STORAGE_CODE = 1000;
    ArrayList<Integer> timelapseNumber = new ArrayList<>();
    ArrayList<String> timelapseName = new ArrayList<>();


    ListView listView;
    ArrayAdapter arrayAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_galerie, container, false);
        listView = root.findViewById(R.id.list);
        getAlbums();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_STORAGE_CODE);
                    }
                    else{
                        startDownloading(timelapseNumber.get(position), timelapseName.get(position));
                    }
                }
                else{
                    startDownloading(timelapseNumber.get(position),timelapseName.get(position));
                }
            }
        });

        return root;
    }

    private void getAlbums(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String myurl= "https://fastapi.stevenkerautret.eu/albums?access_token=F%3E%3Caw%3Bv)9H4JRY%3D4%23g%40%7DYN68b%24%256!j9F8g%3DV2%5EKr%5E8s%3A(%5BN7(%5D";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, myurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for(int i = 0; i < array.length(); i++){
                                String parts[] = array.get(i).toString().split(",");
                                for(int j = 0; j < 3; j++){
                                    parts[j] = parts[j].replace("[", "");
                                    parts[j] = parts[j].replace("]", "");
                                    parts[j] = parts[j].replace("\"", "");
                                }
                                timelapseNumber.add(Integer.valueOf(parts[0]));
                                timelapseName.add(parts[1]);
                            }

                            arrayAdapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, timelapseName);
                            listView.setAdapter(arrayAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "nope", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
        queue.start();
    }

    private void startDownloading(int albumNumber, String albumName){
        String url = "https://fastapi.stevenkerautret.eu/video?id_album=" + albumNumber +"&access_token=F%3E%3Caw%3Bv)9H4JRY%3D4%23g%40%7DYN68b%24%256!j9F8g%3DV2%5EKr%5E8s%3A(%5BN7(%5D";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Download");
        request.setDescription("Downloading file ...");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, ""+albumName);

        DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }


}
