/*package com.example.timelapse;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.ZipFile;


public class GalerieFragment extends Fragment implements View.OnClickListener{

    View root;
    TextView texte;
    Button b;
    ArrayList<Integer> temperature = new ArrayList<Integer>();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.fragment_galerie, container, false);
        ImageButton Reglage = (ImageButton) root.findViewById(R.id.reglage_button);

        texte = root.findViewById(R.id.textViewSQL);
       // b = root.findViewById(R.id.buttonSQL);
        b.setOnClickListener(this);


        return root;
    }

   /* @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonSQL:
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String myurl= "https://fastapi.stevenkerautret.eu/album";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, myurl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                                texte.setText(response.toString());
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "nope", Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(stringRequest);
                queue.start();
                break;
        }
    }
}*/


