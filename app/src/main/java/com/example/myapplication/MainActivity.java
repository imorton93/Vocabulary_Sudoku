package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
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
import java.util.Random;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


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

    private static final Button[][] gridButton = new Button[9][9];//button b11-b99
    String[] span_words;
    String[] eng_words;
    private static SudokuGenerator initialGame = new SudokuGenerator();
    String[][] Sudoku = new String[9][9];
    private static SudokuChecker resultCheck = new SudokuChecker();
    private Button mfinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initial gameGrid
        //show words in button
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                String buttonID = "b" + (y+1) + (x+1);

                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                gridButton[x][y] = (Button) findViewById(resID);
            }
        }
        //gameInitial
        //finish Button
        finButton();
    }

    public void getGameGrid(String[] words){
        Sudoku = initialGame.generateGrid(words);
        Random rand = new Random();
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                gridButton[x][y].setText("");
                gridButton[x][y].setClickable(true);
                int i = rand.nextInt(x+1);
                int j = rand.nextInt(y+1);
                gridButton[i][j].setText(Sudoku[i][j]);
                gridButton[i][j].setClickable(false);
            }
        }
    }

    public void finButton(){
        //finish button listening action
        //By clicking finish button, will check the correctness of Sudoku
        //display whether right or wrong
        mfinButton = (Button)findViewById(R.id.fin_button);
        mfinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[][] checkSudoku = new String[9][9];
                String[][] originalSudoku = new String[9][9];
                for (int y = 0; y < 9; y++) {
                    for (int x = 0; x < 9; x++) {
                        CharSequence temp = gridButton[x][y].getText();
                        checkSudoku[x][y] = "" + temp ;
                        originalSudoku[x][y] = Sudoku[x][y];
                    }
                }
                checkAnswer(checkSudoku, originalSudoku);
            }
        });
    }

    //check sudoku correctness
    public void checkAnswer(String[][] Sudoku, String[][] originalSudoku){
        String msg;
        if (resultCheck.sudokuCheck(Sudoku)){
            msg = "Congratulation! Sudoku is correct!";
        }else{
            msg = "Sudoku is incorrect, try again!";
        }
        Toast result = Toast.makeText(MainActivity.this,msg,Toast.LENGTH_LONG);
        result.setGravity(Gravity.TOP, 0, 400);
        result.show();
        //only for test
        //intent the 9*9 Grid Sudoku to a new page
        Intent intent = new Intent(MainActivity.this, SudokuDisplay.class);
        ArrayList<String> words = new ArrayList<String>();
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                words.add(Sudoku[x][y]);
            }
        }
        intent.putStringArrayListExtra(EXTRA_MESSAGE,words);
        startActivity(intent);
    }


    private Button SelectedButton; //button that user selects to insert

    public void gridButtonOnClick(View v){
        //user hits one of the grid blocks to insert a word
        if(SelectedButton != null){
            SelectedButton.setBackgroundResource(R.drawable.unclicked_button);
            SelectedButton.setTextColor(Color.parseColor("#000000"));
        }
        SelectedButton = (Button) v;
        SelectedButton.setBackgroundResource(R.drawable.clicked_button);
        SelectedButton.setTextColor(Color.parseColor("#FFFFFF"));
        //SelectedButton.setText("clicked");
    }

    public void insertButtonOnClick(View w){
        //user hits button to change text of selectedbutton
        if (SelectedButton != null){
            Button button = (Button) w;
            // text of input button is extracted
            CharSequence buttonText = button.getText();
            //set the Selected Buttons Text as text from input button
            SelectedButton.setText(buttonText);
        }
    }

    public void clearButtonOnClick(View z){
        if(SelectedButton != null){
            SelectedButton.setText("");
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
                Toast result1 = Toast.makeText(MainActivity.this,"User chooses to fill in Spanish",Toast.LENGTH_LONG);
                result1.setGravity(Gravity.TOP, 0, 400);
                result1.show();
                getGameGrid(span_words); //After choosing "fill in Spanish", start a new game with Spanish
                for (i = 0; i < 9; i++){
                    mButtons = findViewById(Button_ids[i]);
                    mButtons.setText(span_words[i]);
                }
                return true;

            case R.id.fill_Eng:
                Log.d(TAG,"User chooses to fill in English");
                //The 9 buttons will display English
                //mButton1.setText(R.string.eng_1);
                Toast result2 = Toast.makeText(MainActivity.this,"User chooses to fill in English",Toast.LENGTH_LONG);
                result2.setGravity(Gravity.TOP, 0, 400);
                result2.show();
                getGameGrid(eng_words); //After choosing "fill in Spanish", start a new game with English
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
