package controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;

import com.example.myapplication.R;

import java.util.Random;
import java.util.Set;

import controller.Load_Pairs;

public class StartPage extends AppCompatActivity {
    Boolean from_start = true;
    private static final String KEY_from_start = "from_start";
    private static final String KEY_fill_Eng = "fill_Eng";
    private static final String KEY_fill_Span = "fill_Span";
    private static final String KEY_Listen = "Listen_Mode";
    private static final String KEY_GRID_SIZE = "grid_size";

    Boolean listen_mode = false;
    Boolean fill_English = false;
    Boolean fill_Spanish = false;
    int gridSize = 9;
    int[] g_Sizes = {4,4,6,6,6,9,9,9,9,9,12,12,12}; //13-element array

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startpage);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int density = metrics.densityDpi;

        Button q_game = findViewById(R.id.quick_game);
        q_game.setOnClickListener( new View.OnClickListener()
        {
            public void onClick(View view) {
                //Do stuff for Quick Start game
               //Generate a random game for user
                final int fill = new Random().nextInt(2); //return 0 or 1
                //Determine whether game starts mode in fill english or spanish
                if (fill == 0){
                    fill_English = true;
                    fill_Spanish = false;
                }
                else{
                    fill_Spanish = true;
                    fill_English = false;
                }
                final int listen = new Random().nextInt(4); //return 0,... 3
                //Determine whether game starts in listen mode
                if (listen == 0){
                    listen_mode = true;
                }
                else{
                    listen_mode = false;
                }

                //Determine which grid-size the game has
                final int g_Sizes_indx;
                if (density == DisplayMetrics.DENSITY_XHIGH) {
                    // 12 by 12 grid is displayable on device
                    g_Sizes_indx = new Random().nextInt(13);
                }
                else{
                    g_Sizes_indx = new Random().nextInt(10);
                }

                Intent quick_game = new Intent(StartPage.this, QuickNbyBActivity.class);
                quick_game.putExtra(KEY_fill_Eng, fill_English);
                quick_game.putExtra(KEY_fill_Span, fill_Spanish);
                quick_game.putExtra(KEY_Listen, listen_mode);
                quick_game.putExtra(KEY_GRID_SIZE, g_Sizes[g_Sizes_indx]);
                startActivity(quick_game);
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
                //Do Guide, create a new layout for guide page.
                Intent guide = new Intent (StartPage.this, Guide.class);
                startActivity(guide);

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
