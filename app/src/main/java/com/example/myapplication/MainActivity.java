package com.example.myapplication;

import android.content.Intent;
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

        final String[][] SudokuEng = initialGame.generateGrid('E');
        final String[][] SudokuSpa = initialGame.generateGrid('S');
        printSudoku(SudokuEng);
        printSudoku(SudokuSpa);

        // test SudokuChecker
       // SudokuEng[8][4] = null;
       // SudokuEng[8][1] = "dog";
        /*SudokuEng[8][2] = "girl";
        SudokuEng[8][3] = "boy";
        SudokuEng[8][4] = "parrot";
        SudokuEng[8][5] = "sad";
        SudokuEng[8][6] = "happy";
        SudokuEng[8][7] = "father";
        SudokuEng[8][8] = "mother";
        printSudoku(SudokuEng);
        SudokuEng[0][8] = "cat";
        SudokuEng[1][8] = "dog";
        SudokuEng[2][8] = "girl";
        SudokuEng[3][8] = "boy";
        SudokuEng[4][8] = "parrot";
        SudokuEng[5][8] = "sad";
        SudokuEng[6][8] = "happy";
        SudokuEng[7][8] = "father";
        SudokuEng[8][8] = "mother";*/
        //printSudoku(SudokuEng);


        //get string(eng,spa) from resources
        Resources res = MainActivity.this.getResources();
        sudokuWords[0] = res.getString(R.string.eng_1);

        //finish button
        //By clicking finish button, will check the correctness of Sudoku
        //display whether right or wrong
        mfinButton = (Button)findViewById(R.id.fin_button);
        mfinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg;
                if (resultCheck.sudokuCheck(SudokuEng)){
                    msg = "Congratulation! Sudoku is correct!";
                }else{
                    msg = "Sudoku is incorrect, try again!";
                }
                Toast result = Toast.makeText(MainActivity.this,msg,Toast.LENGTH_LONG);
                result.setGravity(Gravity.TOP, 0, 400);
                result.show();
            }
        });

    }

    private void printSudoku(String[][] Sudoku) {
        for (int y = 0; y < 9; y++) {
            System.out.print("       ");
            for (int x = 0; x < 9; x++) {
                System.out.print("|" + Sudoku[x][y] + "|");
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
