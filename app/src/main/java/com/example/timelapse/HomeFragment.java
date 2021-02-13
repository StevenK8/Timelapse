package com.example.timelapse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
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


public class HomeFragment extends Fragment implements View.OnClickListener{

    View root;

    private String url = "http://ryzen.ddns.net:8000/";
    private String urlStartTimelapse = "timelapse/start/";
    private String urlLongueur = "length_in_seconds=";
    private String urlInterval = "interval_in_seconds=";
    private String urlRotation = "rotation=";
    private String urlIso = "iso=";
    private String urlShutterSpeed = "shutter_speed=";
    private String urlAutoWhiteBalance = "autoWhiteBalance=";
    private String urlDescription = "description_album=";
    private String urlAccessToken = "access_token=!%24j%3B%2C%3DQzViep%5E%5CZP~9_pWg%5B%5B%7B8p*3d9ZP9NxxB5XFDNpB5Btv~";


    EditText desc;
    EditText longueur;
    EditText interval;
    EditText rotation;
    EditText iso;
    EditText shutterSpeed;
    Switch autoWhiteBalance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);
        Button b = root.findViewById(R.id.boutonGo);
        b.setOnClickListener(this);

        desc = root.findViewById(R.id.descriptionAlbum);
        longueur = root.findViewById(R.id.length);
        interval = root.findViewById(R.id.interval);
        rotation = root.findViewById(R.id.rotation);
        iso = root.findViewById(R.id.iso);
        shutterSpeed = root.findViewById(R.id.shutterSpeed);
        autoWhiteBalance = root.findViewById(R.id.switch1);


        EditText description = root.findViewById(R.id.descriptionAlbum);
        return root;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.boutonGo:
                checkFieldsValues();

                RequestQueue queue = Volley.newRequestQueue(getContext());
                String urlLancement = url + urlStartTimelapse + "?" + urlLongueur + longueur.getText()
                        + "&"  + urlInterval + interval.getText()
                        + "&"  + urlRotation + rotation.getText()
                        + "&"  + urlIso + iso.getText()
                        + "&"  + urlShutterSpeed + shutterSpeed.getText()
                        + "&"  + urlAutoWhiteBalance + autoWhiteBalance.isChecked()
                        + "&"  + urlDescription + desc.getText()
                        + "&"  + "width=2592&height=1944"
                        + "&"  + urlAccessToken;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLancement,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Reessayer", Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(stringRequest);
                queue.start();
                break;
        }
    }

    private void checkFieldsValues(){
        if(desc.getText().equals("")){
            desc.setText("Timelapse");
        }
        if(longueur.getText().equals("")){
            longueur.setText("1");
        }
        if(interval.getText().equals("")){
            interval.setText("1");
        }
        if(interval.getText().equals("")){
            interval.setText("1");
        }
        if(rotation.getText().equals("")){
            rotation.setText("0");
        }
        if(iso.getText().equals("")){
            iso.setText("0");
        }
        if(shutterSpeed.getText().equals("")){
            shutterSpeed.setText("0");
        }
    }
}
