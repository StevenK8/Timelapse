package com.example.timelapse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class GraphiqueFragment extends Fragment implements View.OnClickListener{

    View root;
    SeekBar seekBar;
    GraphView graph;
    private LineGraphSeries<DataPoint> mSeries;
    private double graphLastXValue = -1d;
    Button b;
    ArrayList<Float> temperatureList = new ArrayList<>();
    ArrayList<Float> humiditeList = new ArrayList<>();
    ArrayList<String> dateList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.fragment_graphique, container, false);

        seekBar = (SeekBar) root.findViewById(R.id.seekBar);
        b = root.findViewById(R.id.actualiser);
        b.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                graphLastXValue += 1d;
                mSeries.appendData(new DataPoint(graphLastXValue, progress), true, 40);
                System.out.println("<Dessin> progress : " + progress);
            }
        });

        graph = (GraphView) root.findViewById(R.id.graph);
        // Exemple 1
        /*LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);*/


        // Exemple 3 : avec la seekbar
        graph.setTitle("Graphique temps");
        graph.setTitleTextSize(40);
        graph.setTitleColor(Color.BLUE);

        mSeries = new LineGraphSeries<>();
        graph.addSeries(mSeries);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(40);

        // Légende
        mSeries.setTitle("temperature");
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        // Zooming and scrolling
        //graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        //graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
        //graph.getViewport().setScrollable(true); // enables horizontal scrolling
        //graph.getViewport().setScrollableY(true); // enables vertical scrolling


        return root;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actualiser:
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String myurl = "https://fastapi.stevenkerautret.eu/th?first_date=2021-03-03&last_date=2021-03-04&access_token=F%3E%3Caw%3Bv)9H4JRY%3D4%23g%40%7DYN68b%24%256!j9F8g%3DV2%5EKr%5E8s%3A(%5BN7(%5D";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, myurl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                                try {
                                    JSONArray array = new JSONArray(response);
                                    for(int i = 0; i < array.length(); i++){
                                        String parts[] = array.get(i).toString().split(",");
                                        for(int j = 0; j < 3; j++){
                                            parts[j] = parts[j].replace("[", "");
                                            parts[j] = parts[j].replace("]", "");
                                            parts[j] = parts[j].replace("\"", "");
                                        }
                                        temperatureList.add(Float.valueOf(parts[0].toString()));
                                        humiditeList.add(Float.valueOf(parts[1].toString()));
                                        dateList.add(parts[2].toString());
                                    }
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
                break;
        }
    }
}
