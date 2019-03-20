package controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;

import java.util.Set;

public class StartPage extends AppCompatActivity {
    Boolean from_start = true;
    private static final String KEY_from_start = "from_start";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startpage);

        Button q_game = findViewById(R.id.quick_game);
        q_game.setOnClickListener( new View.OnClickListener()
        {
            public void onClick(View view) {
                //Do stuff for Quick Start game
                Intent q_start = new Intent(StartPage.this, QuickSetUp.class );
                startActivity(q_start);
            }

        });

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

        Button load = findViewById(R.id.load);
        load.setOnClickListener( new View.OnClickListener()
        {
            public void onClick(View view) {
                //Call load word pairs function
                Intent load_words = new Intent(StartPage.this, Load_Pairs.class);
                load_words.putExtra(KEY_from_start, from_start);
                startActivity(load_words);

            }

        });

    }
}
