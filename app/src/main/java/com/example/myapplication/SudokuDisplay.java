package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class SudokuDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sudoku_display);


        TextView textView = (TextView)findViewById(R.id.sudoku_view);
        ArrayList<String> list=getIntent().getStringArrayListExtra(EXTRA_MESSAGE);
        String text = "The Correct Sudoku Vocabulary:" + System.getProperty("line.separator") + System.getProperty("line.separator");
        for(int i = 1; i <= list.size(); i++) {
            if (i % 9 == 0){
                text = text +  " " + list.get(i-1) + System.getProperty("line.separator") + System.getProperty("line.separator");
            }else{
                text = text + " " + list.get(i-1);
            }
        }
       // Boolean text = getIntent().getBooleanExtra(EXTRA_MESSAGE,false);
        //String sText = "check = "+text;
        textView.setText(text);
    }
}