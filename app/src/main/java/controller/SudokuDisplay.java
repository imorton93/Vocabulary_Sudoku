package controller;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import View.GridAdapter;
import com.example.myapplication.R;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class SudokuDisplay extends AppCompatActivity {

    private static final String KEY_GRID_SIZE = "grid_size";

    public TextView Result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sudoku_display);

        final Button tv_view1;
        final ArrayList<String> list=getIntent().getStringArrayListExtra(EXTRA_MESSAGE);
        String text = "The Correct Sudoku Vocabulary:" + System.getProperty("line.separator") + System.getProperty("line.separator");
       // textView.setText(Text);

        final GridView gridView = (GridView)findViewById(R.id.sudoku_view);
        gridView.setAdapter(new GridAdapter(list,SudokuDisplay.this));
        int gridSize = getIntent().getIntExtra(KEY_GRID_SIZE, 9);
        gridView.setNumColumns((int)Math.sqrt(list.size()));

        Boolean result = getIntent().getExtras().getBoolean("result");

        Result  = (TextView)findViewById(R.id.sResult);
        if(result){
            Result.setText(getString(R.string.s_correct));

        }
        else{
            Result.setText(getString(R.string.s_incorrect));
        }

    }
}


