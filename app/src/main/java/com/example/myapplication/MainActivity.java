package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //voir dans le xml activity_main.xml "button1:onClick"
    public void clickButton1(View v){
        Toast t = Toast.makeText(getApplicationContext(),
                "Bouton 1",
                Toast.LENGTH_SHORT);
        t.show();
    }
}
