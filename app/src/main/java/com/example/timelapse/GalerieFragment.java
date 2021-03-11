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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class GalerieFragment extends Fragment{

    View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.fragment_galerie, container, false);
        ImageButton Reglage = (ImageButton) root.findViewById(R.id.reglage_button);


        /*Reglage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new ReglagesPage());
                fr.commit();
            }
        });*/
        return root;
    }

    public static ArrayList<Integer> getTemperature() {

        ArrayList<Integer> temperature = new ArrayList<Integer>();

        try {
            String myurl= "http://www.exemple.com/getPersonnes";

            URL url = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            /*
             * InputStreamOperations est une classe complémentaire:
             * Elle contient une méthode InputStreamToString.
             */
            String result = InputStreamOperations.InputStreamToString(inputStream);

            // On récupère le JSON complet
            JSONObject jsonObject = new JSONObject(result);
            // On récupère le tableau d'objets qui nous concernent
            JSONArray array = new JSONArray(jsonObject.getString("personnes"));
            // Pour tous les objets on récupère les infos
            for (int i = 0; i < array.length(); i++) {
                // On récupère un objet JSON du tableau
                JSONObject obj = new JSONObject(array.getString(i));
                // On fait le lien Personne - Objet JSON
               /* Personne personne = new Personne();
                personne.setNom(obj.getString("nom"));
                personne.setPrenom(obj.getString("prenom"));
                // On ajoute la personne à la liste
                personnes.add(personne);*/

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return temperature;
    }


    public static class InputStreamOperations {

        /**
         * @param in      : buffer with the php result
         * @param bufSize : size of the buffer
         * @return : the string corresponding to the buffer
         */
        public static String InputStreamToString(InputStream in, int bufSize) {
            final StringBuilder out = new StringBuilder();
            final byte[] buffer = new byte[bufSize];
            try {
                for (int ctr; (ctr = in.read(buffer)) != -1; ) {
                    out.append(new String(buffer, 0, ctr));
                }
            } catch (IOException e) {
                throw new RuntimeException("Cannot convert stream to string", e);
            }
            // On retourne la chaine contenant les donnees de l'InputStream
            return out.toString();
        }

        /**
         * @param in : buffer with the php result
         * @return : the string corresponding to the buffer
         */
        public static String InputStreamToString(InputStream in) {
            // On appelle la methode precedente avec une taille de buffer par defaut
            return InputStreamToString(in, 1024);
        }
    }
}


