package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

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

    private static final Button[][] gridButton = new Button[9][9];//buttons b11-b99

    private static SudokuGenerator initialGame = new SudokuGenerator();
    String[][] Sudoku = new String[9][9];
    private static SudokuChecker resultCheck = new SudokuChecker();
    private boolean InitializedGame = false;
    ArrayList<String> lists = new ArrayList<>();
    private int mistakeCount = 0;
    private String msg;
    String[] eng_wordsList = new String[9];
    String[] span_wordsList = new String[9];
    String[] list = new String[9];

    DBHelper mDBHelper = new DBHelper(this);

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
        //store words from String Resources
        //in case, order of String from String Resources may change
        //store in local variables


        //gameInitial

        //finish Button
        finButton();
    }


    //initial game with data from selectec words by users
    public void setInitialGame(String msg, String[] list){
        Button mButtons;
        int i;
        wordsSplit(list);
        switch (msg){
            case "SPAN":
                Toast result1 = Toast.makeText(MainActivity.this,
                        "User chooses to fill in Spanish",Toast.LENGTH_LONG);
                result1.setGravity(Gravity.TOP, 0, 400);
                result1.show();
                getGameGrid(eng_wordsList); //After choosing "fill in Spanish", start a new game with Spanish
                for (i = 0; i < 9; i++) {
                    mButtons = findViewById(Button_ids[i]);
                    mButtons.setText(span_wordsList[i]);
                }
                break;
            case "ENG":
                Toast result2 = Toast.makeText(MainActivity.this,
                        "User chooses to fill in English",Toast.LENGTH_LONG);
                result2.setGravity(Gravity.TOP, 0, 400);
                result2.show();
                getGameGrid(span_wordsList); //After choosing "fill in Spanish", start a new game with English
                for (i = 0; i < 9; i++) {
                    mButtons = findViewById(Button_ids[i]);
                    mButtons.setText(eng_wordsList[i]);
                }
                break;
        }
    }

    //To guarantee each English word's position is correctly correspondent to a a Spanish word
    public void wordsSplit(String[] list){
        for (int i = 0; i < 9; i++) {
            // `the words in the file are separated by -`, so to get each words
            String[] words = list[i].split("-");
            eng_wordsList[i] = words[0];
            span_wordsList[i] = words[1];
        }
    }

    //gridcell initial
    public void getGameGrid(String[] words){
        InitializedGame = true;
        Sudoku = initialGame.generateGrid(words);
        double remainingGrids = 81;
        double remainingHoles = 50; //set up a num to determine how many words to hide
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                gridButton[x][y].setText(Sudoku[x][y]);
                gridButton[x][y].setClickable(false);
                double makingHole = remainingHoles/remainingGrids;  //randomly hide some words
                if(Math.random() <= makingHole) {
                    gridButton[x][y].setText(null);
                    gridButton[x][y].setClickable(true);
                    remainingHoles--;
                }
                remainingGrids--;
            }
        }
    }


    public void finButton(){
        Button mfinButton;
        //finish button listening action
        //By clicking finish button, will check the correctness of Sudoku
        //display whether right or wrong
        mfinButton = (Button)findViewById(R.id.fin_button);
        mfinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordsSplit(list);
                String[][] checkSudoku = new String[9][9];
                String[][] originalSudoku = new String[9][9];
                for (int y = 0; y < 9; y++) {
                    for (int x = 0; x < 9; x++) {
                        //pass words in grid to a Sting[][] Sudoku in order to check correctness
                        CharSequence temp = gridButton[x][y].getText();
                        checkSudoku[x][y] = temp + "";
                        originalSudoku[x][y] = Sudoku[x][y];
                    }
                }
                checkAnswer(list, checkSudoku, originalSudoku);
            }
        });
    }

    //check sudoku correctness
    public void checkAnswer(String[] list, String[][] Sudoku, String[][] originalSudoku){
        String msg;
        wordsSplit(list);
        if (resultCheck.sudokuCheck(Sudoku, eng_wordsList,span_wordsList)){
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
                words.add(originalSudoku[x][y]);
            }
        }
        intent.putStringArrayListExtra(EXTRA_MESSAGE,words);
        intent.putExtra(EXTRA_MESSAGE,words);
        startActivity(intent);
    }


    private Button SelectedButton; //button that user selects to insert

    public void gridButtonOnClick(View v){
        //user hits one of the grid blocks to insert a word
        if(!InitializedGame){
            Toast.makeText(MainActivity.this ,
                    R.string.not_initialized,Toast.LENGTH_LONG).show();
        }
        else if (mistakeCount >= 3){
            Toast.makeText(MainActivity.this,"You have lost your game", Toast.LENGTH_LONG).show();
        }
        else {
            if (SelectedButton != null) {
                //if a button has already been selected change that button back to normal
                SelectedButton.setBackgroundResource(R.drawable.unclicked_button);
            }
            SelectedButton = (Button) v;
            SelectedButton.setBackgroundResource(R.drawable.clicked_button);
            SelectedButton.setTextColor(Color.parseColor("#FFFFFF"));
            //SelectedButton.setText("clicked");
        }
    }

    public void insertButtonOnClick(View w){
        //user hits button to change text of selectedbutton
        if(SelectedButton != null && mistakeCount < 3) {
            Button button = (Button) w;
            // text of input button is extracted
            CharSequence buttonText = button.getText();
            /* when inserting a new word into puzzle, check if right or wrong
            *  if it's right, make it green
            * *if wrong, put word to be red
            */
            //track the button that user selects
            String tmp = null;
            wordsSplit(list);
            for (int y = 0; y < 9; y++) {
                for (int x = 0; x < 9; x++) {
                    if (gridButton[x][y] == SelectedButton){
                        //translate english to spanish, and vise versa
                        for (int i = 0; i < 9; i++){
                            if (Sudoku[x][y].equals(eng_wordsList[i])){
                                tmp = span_wordsList[i];
                            }
                            if (Sudoku[x][y].equals(span_wordsList[i])){
                                tmp = eng_wordsList[i];
                            }
                        }
                    }
                }
            }
            //if is wrong, puts word to be red
            if (!buttonText.equals(tmp) && !buttonText.equals("")){
                SelectedButton.setBackgroundResource(R.drawable.unclicked_button);
                SelectedButton.setText(buttonText);
                SelectedButton.setTextColor(Color.parseColor("#FFFFC0CB"));
                mistakeCount++;
            }else{
                //if it's right, makes it green
                SelectedButton.setBackgroundResource(R.drawable.unclicked_button);
                SelectedButton.setText(buttonText);
                SelectedButton.setTextColor(Color.parseColor("#FF008577"));
            }
            Log.d(TAG,"tmp is "+tmp);
            //set the Selected Buttons Text as text from input button
        }else{
            Toast.makeText(MainActivity.this,"You have lost your game", Toast.LENGTH_LONG).show();
        }
    }

    public void clearButtonOnClick(View z){
        if(SelectedButton != null) {
            SelectedButton.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    //reinit_dialog Reinit_warn = new reinit_dialog(MainActivity.this);

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Button mButton1 = (Button) findViewById(R.id.button1);

        Button mButtons;
        int i;
        Intent intent;
        String select;
        switch (item.getItemId()){
            case R.id.fill_Span:
                if (!InitializedGame || mistakeCount < 3)  {
                    Log.d(TAG, "User chooses to fill in Spanish");

                    intent = new Intent(MainActivity.this, Words_Selection.class);
                    select = "SPAN";
                    intent.putExtra(EXTRA_MESSAGE, select);
                    startActivityForResult(intent, 1);
                }
                else {//make dialog here
                        //Temporary Toast
                    Toast.makeText(MainActivity.this,R.string.cant_init,Toast.LENGTH_LONG).show();
                }

                //The 9 buttons will display Spanish. works.
                //mButton1.setText(R.string.span_1);
                return true;

            case R.id.fill_Eng:

                //The 9 buttons will display English
                //mButton1.setText(R.string.eng_1);

                if (!InitializedGame || mistakeCount < 3) {
                    Log.d(TAG, "User chooses to fill in English");

                    intent = new Intent(MainActivity.this, Words_Selection.class);
                    select = "ENG";
                    intent.putExtra(EXTRA_MESSAGE, select);
                    startActivityForResult(intent, 1);
                }
                else {
                    //make dialog here
                    //Temporary Toast
                    Toast.makeText(MainActivity.this,R.string.cant_init,Toast.LENGTH_LONG).show();
                }

                return true;

            case R.id.display_words:
                Log.d(TAG,"User chooses to see word pairs");
                //The warning dialog:
                reinit_dialog Reinit_warn = new reinit_dialog(this);
                Reinit_warn.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Reinit_warn.show();

//                Intent display_w = new Intent(this, Display_Words.class);
//                this.startActivity(display_w);
                return true;

            case R.id.load_wordpairs:
                intent = new Intent(MainActivity.this, Words_Selection.class);
                select = "LOAD";
                intent.putExtra(EXTRA_MESSAGE,select);
                startActivity(intent);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != MainActivity.RESULT_OK) {
            return;
        }

        if (requestCode == 1) {
            if (data == null) {
                Log.d(TAG, "data is null");
                return;
            }
            msg = data.getStringExtra("LANGUAGE");
            list = data.getStringArrayExtra("EXTRA_WORDS_LIST");

            for (int i =0; i< 9; i++) {
                Log.d(TAG, "Words from selection ENG are " + list[i]);
              //  Log.d(TAG, "Words from selection SPAN are " + span_wordsList[i]);

            }
            setInitialGame(msg,list);
        }
    }


    @Override
    protected void onDestroy() {
        mDBHelper.close();
        super.onDestroy();
    }
}


