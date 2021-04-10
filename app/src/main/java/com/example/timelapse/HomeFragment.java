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
    private String urlStatus = "http://ryzen.ddns.net:8000/timelapse/status/?access_token=!%24j%3B%2C%3DQzViep%5E%5CZP~9_pWg%5B%5B%7B8p*3d9ZP9NxxB5XFDNpB5Btv~";


    EditText desc;
    EditText longueur;
    EditText interval;
    EditText rotation;
    EditText iso;
    EditText shutterSpeed;
    Switch autoWhiteBalance;
    EditText Status;
    Button b;

    String status = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);
        b = root.findViewById(R.id.boutonGo);
        b.setOnClickListener(this);
        Button t = root.findViewById(R.id.test);
        t.setOnClickListener(this);

        desc = root.findViewById(R.id.descriptionAlbum);
        longueur = root.findViewById(R.id.length);
        interval = root.findViewById(R.id.interval);
        rotation = root.findViewById(R.id.rotation);
        iso = root.findViewById(R.id.iso);
        shutterSpeed = root.findViewById(R.id.shutterSpeed);
        autoWhiteBalance = root.findViewById(R.id.switch1);


        EditText description = root.findViewById(R.id.descriptionAlbum);

        Status = root.findViewById(R.id.status);
        getStatus();
        SetStatus(status);

        return root;
    }

    public void getStatus(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlStatus,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        status = response;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "erreur", Toast.LENGTH_SHORT).show();
                status = "erreur";
            }
        });
        queue.add(stringRequest);
        queue.start();
    }

    public void SetStatus(String status){
        if(status.contains("photos")){
            char[] charArray = status.toCharArray();
            for(int i = 0; i < charArray.length; i++){
                if(charArray[i] == '{'){
                    charArray[i] = '\0';
                }
                if(charArray[i] == '\"'){
                    charArray[i] = '\0';
                }
                if(charArray[i] == 'Â'){
                    charArray[i] = '\0';
                }
            }
            String sortie = String.valueOf(charArray);
            Status.setText(sortie);
            b.setClickable(false);
        }
        else{
            Status.setText("pas de timelapse");
            b.setClickable(true);
        }
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
                getStatus();
                SetStatus(status);
                break;

            case R.id.test:
                getStatus();
                SetStatus(status);
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
