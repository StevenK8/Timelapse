package com.example.timelapse;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class GalerieFragment extends Fragment{

    View root;
    TextView text;
    Button show;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.fragment_galerie, container, false);
        ImageButton Reglage = (ImageButton) root.findViewById(R.id.reglage_button);
        text = root.findViewById(R.id.textViewSQL);
        show = root.findViewById(R.id.buttonSQL);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Task().execute();
            }
        });


        Reglage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new ReglagesPage());
                fr.commit();
            }
        });
        return root;
    }
    class Task extends AsyncTask<Void, Void, Void>{
        String records = "";
        @Override
        protected Void doInBackground(Void... voids){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://ryzen.ddns.net:3306", "timelapse", "9_7b:r%HR-G%y@*U;>*3KDrU!-v,65U]Wq6H.xT5G}uiPAE}8k");

                Statement statement = connection.createStatement();

                ResultSet resultSet = statement.executeQuery("SELECT * FROM mesures");

                while(resultSet.next()) {

                    records += resultSet.getString(1) /*+ " " + resultSet.getString(2)*/ + "\n";

                }
            }
            catch(Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid){
            //do something
            //text.setText(records);
            super.onPostExecute(aVoid);
        }
    }
}


