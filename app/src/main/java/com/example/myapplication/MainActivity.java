package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "CMPT276-1191E1-Delta";
    private static final int[] Button_ids = { //ID's for the 9 big buttons
            R.id.button1,
            R.id.button2,
            R.id.button3,
            R.id.button4,
            R.id.button5,
            R.id.button6,
            R.id.button7,
            R.id.button8,
            R.id.button9,
    };
    String[] span_words;
    String[] eng_words;

  //  String[] span_words = getResources().getStringArray(R.array.Span_words); //Length : 9
  //  String[] eng_words = getResources().getStringArray(R.array.Eng_words);


//    String[] span_words = { //Takes a while for app to open
//        "gato", "perro", "niña","niño", "loro", "triste","feliz", "padre","madre"} ;
//
//    String[] eng_words = { "cat", "dog", "girl","boy", "parrot", "sad", "happy", "father","mother"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[][] SudokuEng = SudokuGenerator.getInitialGame().generateGrid('E');
        final String[][] SudokuSpa = SudokuGenerator.getInitialGame().generateGrid('S');
        printSudoku(SudokuEng);
        printSudoku(SudokuSpa);

    }

    private void printSudoku(String[][] Sudoku) {
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                System.out.print(Sudoku[x][y] + "|");
            }
            System.out.println();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Button mButton1 = (Button) findViewById(R.id.button1);
        Button mButtons;
        int i;
        span_words = getResources().getStringArray(R.array.Span_words);
        eng_words = getResources().getStringArray(R.array.Eng_words);
        switch (item.getItemId()){
            case R.id.fill_Span:
                Log.d(TAG,"User chooses to fill in Spanish");
                //The 9 buttons will display Spanish. works.
                //mButton1.setText(R.string.span_1);
                for (i = 0; i < 9; i++){
                    mButtons = findViewById(Button_ids[i]);
                    mButtons.setText(span_words[i]);
                }
                return true;

            case R.id.fill_Eng:
                Log.d(TAG,"User chooses to fill in English");
                //The 9 buttons will display English
                //mButton1.setText(R.string.eng_1);
                for (i = 0; i < 9; i++){
                    mButtons = findViewById(Button_ids[i]);
                    mButtons.setText(eng_words[i]);
                }
                return true;

            case R.id.display_words:
                Log.d(TAG,"User chooses to see word pairs");
                Intent display_w = new Intent(this, Display_Words.class);
                this.startActivity(display_w);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
