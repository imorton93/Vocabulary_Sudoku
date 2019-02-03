package com.example.myapplication;

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


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "CMPT276-1191E1-Delta";

    private Button mfinButton;
    private SudokuGenerator initialGame = new SudokuGenerator();
    private SudokuChecker resultCheck = new SudokuChecker();
    String[] sudokuWords = new String[9]; //get words from string resources


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

        switch (item.getItemId()){
            case R.id.fill_Span:
                Log.d(TAG,"User choses to fill in Spanish");
                return true;

            case R.id.fill_Eng:
                Log.d(TAG,"User choses to fill in English");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
