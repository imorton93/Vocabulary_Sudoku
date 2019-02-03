package com.example.myapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;


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

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static SudokuGenerator initialGame = new SudokuGenerator();
    private static SudokuChecker resultCheck = new SudokuChecker();
    private Button mfinButton;


//    String[] span_words = { //Takes a while for app to open
//        "gato", "perro", "niña","niño", "loro", "triste","feliz", "padre","madre"} ;
//
//    String[] eng_words = { "cat", "dog", "girl","boy", "parrot", "sad", "happy", "father","mother"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        span_words = getResources().getStringArray(R.array.Span_words); //Length : 9
        eng_words = getResources().getStringArray(R.array.Eng_words);


        final String[][] Sudoku = initialGame.generateGrid(eng_words,'E');
        //final String[][] SudokuSpan = initialGame.generateGrid(span_words,'S');//initial Spanish Sudoku Grid

        //finish button
        //By clicking finish button, will check the correctness of Sudoku
        //display whether right or wrong
        mfinButton = (Button)findViewById(R.id.fin_button);
        mfinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg;
                if (resultCheck.sudokuCheck(Sudoku)){
                    msg = "Congratulation! You successfully answer the Sudoku!";
                    checkAnswer(msg);
                    Intent intent;
                    intent = new Intent(MainActivity.this, SudokuDisplay.class);
                    ArrayList<String> words = new ArrayList<String>();
                    for (int y = 0; y < 9; y++) {
                        for (int x = 0; x < 9; x++) {
                            words.add(Sudoku[x][y]);
                        }
                    }
                    intent.putStringArrayListExtra(EXTRA_MESSAGE,words);
                   // intent.putExtra(EXTRA_MESSAGE, resultCheck.sudokuCheck(Sudoku));
                    startActivity(intent);
                }else{
                    msg = "It is incorrect, Please try again!";
                    checkAnswer(msg);
                }
            }
        });
    }

    //Toast answer to users
    private void checkAnswer(String msg) {
        Toast result = Toast.makeText(MainActivity.this,msg,Toast.LENGTH_LONG);
        result.setGravity(Gravity.TOP, 0, 400);
        result.show();
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
