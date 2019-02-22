package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class Display_Words extends AppCompatActivity {
    private static final String TAG = "CMPT276-1191E1-Delta";
    private static final String KEY_x = "key_x"; //X is for testing if savedInstanceState in this activity relates to the other

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "Displaying Word Pairs -- onSaveInstanceState -- saving x");
        savedInstanceState.putInt(KEY_x, 250);
    }
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_words);

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

