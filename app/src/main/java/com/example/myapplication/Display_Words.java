package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Display_Words extends AppCompatActivity {
    private static final String TAG = "CMPT276-1191E1-Delta";
    private static final String KEY_x = "key_x"; //X is for testing if savedInstanceState in this activity relates to the other
    private static final String KEY_Eng_wordlist = "Eng_wordlist";
    private static final String KEY_Span_wordlist = "Span_wordlist";
    private static final String KEY_fill_Eng = "fill_Eng";
    private static final String KEY_fill_Span = "fill_Span";
    private static final String KEY_InitializedGame = "initializedgame";
    private static final int[] W_Button_ids = { //ID's for the 9 big buttons in the word pairs page
            R.id.w_b1,
            R.id.w_b2,
            R.id.w_b3,
            R.id.w_b4,
            R.id.w_b5,
            R.id.w_b6,
            R.id.w_b7,
            R.id.w_b8,
            R.id.w_b9,
    };
    boolean InitializedGame = false;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "Displaying Word Pairs -- onSaveInstanceState -- saving x");
        savedInstanceState.putInt(KEY_x, 250);
    }
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final String[] eng_wordlist = intent.getStringArrayExtra(KEY_Eng_wordlist);
        final String[] span_wordlist = intent.getStringArrayExtra(KEY_Span_wordlist);
        Boolean fill_Eng = intent.getBooleanExtra(KEY_fill_Eng, false); //Return false if value not initialized
        Boolean fill_Span = intent.getBooleanExtra(KEY_fill_Span, false); //Return false if value not initialized
        InitializedGame = intent.getBooleanExtra(KEY_InitializedGame, false);
        setContentView(R.layout.display_words);

        final TextView wordstextview = findViewById(R.id.words_view);

      //  wordstextview.setText(@string/warn_see_words);
     //   wordstextview.setText("WORD HERE!!!"); //DOES NOT WORK
        if (InitializedGame) {
            Button button;
            if (fill_Span) {
                for (int i = 0; i < span_wordlist.length; i++) {
                    button = findViewById(W_Button_ids[i]);
                    button.setText(span_wordlist[i]);
                    final int temp_i = i;
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            wordstextview.setText(eng_wordlist[temp_i]);
                        }
                    });
                }

            } else if (fill_Eng) {
                for (int i = 0; i < eng_wordlist.length; i++) {
                    button = findViewById(W_Button_ids[i]);
                    button.setText(eng_wordlist[i]);
                    final int temp_i = i;
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            wordstextview.setText(span_wordlist[temp_i]);
                        }
                    });
                }
            }
        }
  /*      else{
            //Do a new layout here.
            //Display the whole lists?
            //If the games aren't initialized, maybe load the old lists?
            //Or, I could create a new option? For the menu: if not(initialized game), go to a new layout, where all words are displayed.
        } */


        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //The user can get back to the puzzle by clicking this
    }

    //To make debugger display tags
    @Override public void onStart() {
        super.onStart();
        Log.d(TAG, "Display Words - onStart() called");
    }
    @Override public void onPause() {
        super.onPause();
        Log.d(TAG, "Display Words - onPause() called");
    }
    @Override public void onResume() {
        super.onResume();
        Log.d(TAG, "Display Words - onResume() called");
    }
    @Override public void onStop() {
        super.onStop();
        Log.d(TAG, "Display Words - onStop() called");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Display Words - onDestroy() called");
    }


}

