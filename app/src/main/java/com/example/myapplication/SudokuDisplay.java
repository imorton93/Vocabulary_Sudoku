package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class SudokuDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sudoku_display);

        final Button tv_view1;
        final ArrayList<String> list=getIntent().getStringArrayListExtra(EXTRA_MESSAGE);
        String text = "The Correct Sudoku Vocabulary:" + System.getProperty("line.separator") + System.getProperty("line.separator");
       // textView.setText(Text);

        final GridView gridView = (GridView)findViewById(R.id.sudoku_view);
        gridView.setAdapter(new GridAdapter(list,this));

    }

}


