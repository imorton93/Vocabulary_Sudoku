package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Display_Words extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_words);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //The user can get back to the puzzle by clicking this
    }

}

