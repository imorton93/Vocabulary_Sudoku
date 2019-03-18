package controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;

public class StartPage extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.startpage);

        Button start = findViewById(R.id.start_game1);
        start.setOnClickListener( new View.OnClickListener()
        {
            public void onClick(View view) {
            //Do stuff for Start game
                Intent start = new Intent(StartPage.this, SetUpPage.class );
                startActivity(start);
            }

        });

        Button guide = findViewById(R.id.guide);
        guide.setOnClickListener( new View.OnClickListener()
        {
            public void onClick(View view) {
            //Do Guide

            }

        });

    }
}
