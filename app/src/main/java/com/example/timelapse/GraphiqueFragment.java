package com.example.timelapse;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;


public class GraphiqueFragment extends Fragment implements View.OnClickListener{

    View root;
    GraphView graph;
    LineGraphSeries<DataPoint> tempserie = new LineGraphSeries<>();
    LineGraphSeries<DataPoint> humidserie = new LineGraphSeries<>();
    Button b;

    EditText dateDeb;
    EditText dateFin;

    ArrayList<Float> temperatureList = new ArrayList<>();
    ArrayList<Float> humiditeList = new ArrayList<>();
    ArrayList<String> dateList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.fragment_graphique, container, false);


        b = root.findViewById(R.id.actualiser);
        b.setOnClickListener(this);

        dateDeb = root.findViewById(R.id.dateDeb);
        dateFin = root.findViewById(R.id.dateFin);
        dateDeb.setOnClickListener(this);
        dateFin.setOnClickListener(this);
        dateDeb.setInputType(InputType.TYPE_NULL);
        dateFin.setInputType(InputType.TYPE_NULL);

        graph = root.findViewById(R.id.graph);

        //parametrage du graphique
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(50);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(100);
        graph.getSecondScale();
        graph.getSecondScale().setMinY(0);
        graph.getSecondScale().setMaxY(100);


        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setFixedPosition(0,0);

        // Zooming and scrolling
        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().setScrollableY(true); // enables vertical scrolling



        return root;
    }

    public String parseDate(String time) { //changement du format de date pour l'envoie de la requete
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String parseDate2(String time) { // changement du format de la date pour l'affichage
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "dd/MM/yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public DataPoint[] generateData(ArrayList<Float> list) { //genere les points pour mettre sur le graphe
        DataPoint[] values = new DataPoint[list.size()];
        for(int i = 0; i < list.size(); i++){
            int x = i;
            float y = list.get(i);
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }
    public void traceGraph(ArrayList<Float> temperatureList, ArrayList<Float> humiditeList){ //permet de tracer les lines du graphs
        graph.removeSeries(tempserie);
        if(temperatureList.size()> 0){
            tempserie.resetData(generateData(temperatureList));
            /*for(int i = 0; i < temperatureList.size(); i++){
                tempserie.appendData(new DataPoint(i,temperatureList.get(i)),true,temperatureList.size());
            }*/
            graph.addSeries(tempserie);
            tempserie.setTitle("température");
            graph.getViewport().setMinY(Collections.min(temperatureList));
            graph.getViewport().setMaxY(Collections.max(temperatureList));
        }

        graph.getSecondScale().removeSeries(humidserie);
        if(humiditeList.size() > 0){
            humidserie.resetData(generateData(humiditeList));
            /*for(int i = 0; i < humiditeList.size(); i++){
                humidserie.appendData(new DataPoint(i,humiditeList.get(i)),true,humiditeList.size());
            }*/
            graph.getSecondScale().addSeries(humidserie);
            humidserie.setTitle("humidité");
            humidserie.setColor(Color.RED);
            graph.getSecondScale().setMinY(Collections.min(humiditeList));
            graph.getSecondScale().setMaxY(Collections.max(humiditeList));
            graph.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(Color.RED);
            graph.getViewport().setMaxX(humiditeList.size());
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Requete lors du clique sur le bouton actualiser
            case R.id.actualiser:
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String myurl = "https://fastapi.stevenkerautret.eu/th?first_date=" + parseDate(dateDeb.getText().toString()) + "&last_date=" + parseDate(dateFin.getText().toString()) + "&access_token=F%3E%3Caw%3Bv)9H4JRY%3D4%23g%40%7DYN68b%24%256!j9F8g%3DV2%5EKr%5E8s%3A(%5BN7(%5D";
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
                traceGraph(temperatureList, humiditeList);
                break;

            //generation des calendriers
            case R.id.dateDeb:
                final Calendar cldrDeb = Calendar.getInstance();
                int dayDeb = cldrDeb.get(Calendar.DAY_OF_MONTH);
                int monthDeb = cldrDeb.get(Calendar.MONTH);
                int yearDeb = cldrDeb.get(Calendar.YEAR);
                // Date Select Listener.
                DatePickerDialog.OnDateSetListener dateSetListenerDeb = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String time = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        dateDeb.setText(parseDate2(time));
                    }
                };
                // Create DatePickerDialog (Spinner Mode):
                DatePickerDialog datePickerDialogDeb = new DatePickerDialog(getContext(), dateSetListenerDeb, yearDeb, monthDeb, dayDeb);
                // Show
                datePickerDialogDeb.show();
                break;

            case R.id.dateFin:
                final Calendar cldrFin = Calendar.getInstance();
                int dayfin = cldrFin.get(Calendar.DAY_OF_MONTH);
                int monthfin = cldrFin.get(Calendar.MONTH);
                int yearfin = cldrFin.get(Calendar.YEAR);
                // Date Select Listener.
                DatePickerDialog.OnDateSetListener dateSetListenerFin = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String time = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        dateFin.setText(parseDate2(time));
                    }
                };
                // Create DatePickerDialog (Spinner Mode):
                DatePickerDialog datePickerDialogFin = new DatePickerDialog(getContext(), dateSetListenerFin, yearfin, monthfin, dayfin);
                // Show
                datePickerDialogFin.show();
                break;

        }
    }
}
