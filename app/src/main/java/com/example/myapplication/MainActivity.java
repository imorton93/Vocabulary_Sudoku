package com.example.myapplication;

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
