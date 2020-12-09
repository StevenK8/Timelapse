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
    public void clickBtn1(View v){
        setContentView(R.layout.activity_page2);
    }
    public void clickBtn3(View v){
        setContentView(R.layout.activity_main);
    }
}
