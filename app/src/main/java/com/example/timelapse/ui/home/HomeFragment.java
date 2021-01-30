package com.example.timelapse.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.*;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.timelapse.MainActivity;
import com.example.timelapse.R;

import java.io.IOException;

import okhttp3.*;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private HomeViewModel homeViewModel;
    View root;

    private String url = "http://ryzen.ddns.net:8000/";
    private String postBodyString;
    private MediaType mediaType;
    private RequestBody requestBody;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        Button b = root.findViewById(R.id.boutonGo);
        b.setOnClickListener(this);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.boutonGo:
                //Toast.makeText(getContext(),"LANCEMENT TIMELAPSE", Toast.LENGTH_SHORT).show();
                //http://ryzen.ddns.net:8000/timelapse/start/?length_in_seconds=1&interval_in_seconds=1&rotation=0&iso=0&shutter_speed=0&autoWhiteBalance=true&description_album=Aucune&access_token=!%24j%3B%2C%3DQzViep%5E%5CZP~9_pWg%5B%5B%7B8p*3d9ZP9NxxB5XFDNpB5Btv~


                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url ="http://ryzen.ddns.net:8000/timelapse/start/?length_in_seconds=1&interval_in_seconds=1&rotation=0&iso=0&shutter_speed=0&autoWhiteBalance=true&description_album=Aucune&access_token=!%24j%3B%2C%3DQzViep%5E%5CZP~9_pWg%5B%5B%7B8p*3d9ZP9NxxB5XFDNpB5Btv~\n";

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "nope", Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(stringRequest);
                queue.start();

                break;
        }
    }



    /*private RequestBody buildRequestBody(String msg) {
        postBodyString = msg;
        mediaType = MediaType.parse("text/plain");
        requestBody = RequestBody.create(postBodyString, mediaType);
        return requestBody;
    }

    private void postRequest(String message, String URL) {
        RequestBody requestBody = buildRequestBody(message);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request
                .Builder()
                .post(requestBody)
                .url(URL)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(getContext(), "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        call.cancel();


                    }
                });

            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Toast.makeText(getContext(), response.body().string(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });
    }*/
}