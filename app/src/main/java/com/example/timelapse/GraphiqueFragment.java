package com.example.timelapse;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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


import java.util.ArrayList;
import java.util.Calendar;


public class GraphiqueFragment extends Fragment implements View.OnClickListener{

    View root;
    //SeekBar seekBar;
    GraphView graph;
    LineGraphSeries<DataPoint> mSeries;
    //private double graphLastXValue = -1d;
    Button b;

    EditText dateDeb;
    EditText dateFin;
    DatePickerDialog pickerDeb;
    DatePickerDialog pickerFin;

    ArrayList<Float> temperatureList = new ArrayList<>();
    ArrayList<Float> humiditeList = new ArrayList<>();
    ArrayList<String> dateList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.fragment_graphique, container, false);

        dateDeb = root.findViewById(R.id.dateDeb);
        dateFin = root.findViewById(R.id.dateFin);
        //seekBar = root.findViewById(R.id.seekBar);
        b = root.findViewById(R.id.actualiser);
        b.setOnClickListener(this);
        dateDeb.setOnClickListener(this);
        dateFin.setOnClickListener(this);
        dateDeb.setInputType(InputType.TYPE_NULL);
        dateFin.setInputType(InputType.TYPE_NULL);

        /*seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        });*/

        graph = root.findViewById(R.id.graph);


        // Exemple 3 :

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
                                        temperatureList.add(Float.valueOf(parts[0])); //changement string en float
                                        humiditeList.add(Float.valueOf(parts[1]));   //changement string en float
                                        dateList.add(parts[2]);                      //parsing de la date
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

            case R.id.dateDeb:
                final Calendar cldrDeb = Calendar.getInstance();
                int dayDeb = cldrDeb.get(Calendar.DAY_OF_MONTH);
                int monthDeb = cldrDeb.get(Calendar.MONTH);
                int yearDeb = cldrDeb.get(Calendar.YEAR);
                pickerDeb = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateDeb.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, yearDeb, monthDeb, dayDeb);
                pickerDeb.show();
                break;

            case R.id.dateFin:
                final Calendar cldrFin = Calendar.getInstance();
                int day = cldrFin.get(Calendar.DAY_OF_MONTH);
                int month = cldrFin.get(Calendar.MONTH);
                int year = cldrFin.get(Calendar.YEAR);
                pickerFin = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateFin.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                pickerFin.show();
                break;
        }
    }
}
