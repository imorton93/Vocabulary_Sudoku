package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

import static android.provider.AlarmClock.EXTRA_MESSAGE;



public class MainActivity extends AppCompatActivity {
    private static final String KEY_InitializedGame = "initializedgame";
    private static final String KEY_Sudoku = "saved_Sudoku" ;
    private static final String KEY_prefilled_words = "prefilled_Words";
    private static final String KEY_userfilled_words = "userfilled_Words";
    private static final String KEY_fill_Eng = "fill_Eng";
    private static final String KEY_fill_Span = "fill_Span";
    private static final String KEY_WORDS_LIST = "words_list";
    private static final String KEY_Eng_wordlist = "Eng_wordlist";
    private static final String KEY_Span_wordlist = "Span_wordlist";
    private static final String KEY_Listen = "Listen_Mode";

    /*    private static final String KEY_filled_words_0 = "col_0"; //The words that the user has filled
        //The leftmost column
        private static final String KEY_filled_words_1 = "col_1"; //The words that the user has filled
        private static final String KEY_filled_words_2 = "col_2"; //The words that the user has filled
        private static final String KEY_filled_words_3 = "col_3"; //The words that the user has filled
        private static final String KEY_filled_words_4 = "col_4"; //The words that the user has filled
        private static final String KEY_filled_words_5 = "col_5"; //The words that the user has filled
        private static final String KEY_filled_words_6 = "col_6"; //The words that the user has filled
        private static final String KEY_filled_words_7 = "col_7"; //The words that the user has filled
        private static final String KEY_filled_words_8 = "col_8"; //The words that the user has filled */
/*    private String[] KEY_filled_words =
            {KEY_filled_words_0, KEY_filled_words_1, KEY_filled_words_2, KEY_filled_words_3,
            KEY_filled_words_4, KEY_filled_words_5, KEY_filled_words_6, KEY_filled_words_7,
            KEY_filled_words_8 }; */
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
    private static SudokuGenerator initialGame = new SudokuGenerator(); //Generate an instance of class SudokuGenerator.
    String[][] Sudoku = new String[9][9];
    private static SudokuChecker resultCheck = new SudokuChecker();
    private boolean fill_Eng = false;
    private boolean fill_Span = false;
    private boolean InitializedGame = false;
    private boolean restored_s = false; //boolean for checking if sudoku is restored.
    private boolean listen_mode = false; //Checks if the app is in listen comprehension mode
    private int mistakeCount = 0;
    private String msg;
    String[] eng_wordsList = new String[9];
    String[] span_wordsList = new String[9];
    String[][] Sudoku_temp = new String[9][9];
    String[][] Sudoku_user = new String[9][9];
    String[] list = new String[9];

    DBHelper mDBHelper = new DBHelper(this);
    Menu menu;
    private final String[] l_numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9"}; //The string array for listen comprehension mode
    private int remain_nums = 0;
    private int l_number = 0; //a variable for listen mode functions
    String[] assigned = new String[9]; //For listen mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate(Bundle) called");

        //initial gameGrid
        //show words in button
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                String buttonID = "b" + (x+1) + (y+1);
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                gridButton[x][y] = findViewById(resID);
            }
        }
        //store words from String Resources
        //in case, order of String from String Resources may change
        //store in local variables
        //gameInitial

        if ((savedInstanceState != null)) {
            //If there is an incomplete sudoku, the game loads the words on Sudoku that the user filled in before,
            // so user does not need to restart game.
            Button mButtons;
            InitializedGame = savedInstanceState.getBoolean(KEY_InitializedGame);
            fill_Eng = savedInstanceState.getBoolean(KEY_fill_Eng);
            fill_Span = savedInstanceState.getBoolean(KEY_fill_Span);
            listen_mode = savedInstanceState.getBoolean(KEY_Listen);
            list = savedInstanceState.getStringArray(KEY_WORDS_LIST);
            int j = 0;  //For breakpoint purpose
            Log.i(TAG, "loads the words user filled in before");
            /* for (int i = 0; i < 9; i++) {
                Sudoku_temp[i] = savedInstanceState.getStringArray(KEY_filled_words[i]); //break point here
                j = 1; //for break point purpose
                //BUG: at least after the screen is turned and turned back, the words (wrong) are preserved
                //Problem: sudoku [i] is the same for all i
            } */
            //
            wordsSplit(list);
            if (fill_Eng){
                for (int i = 0; i < 9; i++) {
                    mButtons = findViewById(Button_ids[i]);
                    mButtons.setText(eng_wordsList[i]);
                }
            }
            else if (fill_Span){
                for (int i = 0; i < 9; i++) {
                    mButtons = findViewById(Button_ids[i]);
                    mButtons.setText(span_wordsList[i]);
                }
            }
            Sudoku_temp = (String [][]) savedInstanceState.getSerializable(KEY_prefilled_words);
            Sudoku_user = (String [][]) savedInstanceState.getSerializable(KEY_userfilled_words);
            Sudoku = (String [][]) savedInstanceState.getSerializable(KEY_Sudoku);
            // The arrays in Sudoku_temp has the columns of the original sudoku from right to left (should be from left to right)
            for (int x = 0; x < 9; x++) { //break point here
                for (int y = 0; y < 9; y++) {
                    gridButton[x][y].setText(Sudoku_temp[x][y]); //No problem here.
                    j += 5; //break point here
                    if (Sudoku_temp[x][y] != null) {
                        gridButton[x][y].setClickable(false);
                        //Sudoku 'memorizes' the pre-set words
                    } else {
                        gridButton[x][y].setClickable(true);
                        if (Sudoku_user[x][y] != null) {
                            gridButton[x][y].setText(Sudoku_user[x][y]);
                            String tmp = null;
                            //if is wrong, puts word to be red
                            for (int i = 0 ; i < 9; i++){
                                if (gridButton[x][y].getText().equals(eng_wordsList[i])){
                                    tmp = span_wordsList[i];
                                }
                                if (gridButton[x][y].getText().equals(span_wordsList[i])){
                                    tmp = eng_wordsList[i];
                                }
                            }
                            if (tmp.equals(Sudoku[x][y])){
                                Log.d(TAG, " SUDOKU[X][Y] is "+ Sudoku[x][y] );
                                Log.d(TAG, " GRIDBUTTON[X][Y] is "+ tmp );
                                gridButton[x][y].setTextColor(Color.parseColor("#FF008577"));
                            }else{
                                //if it's right, makes it green
                                Log.d(TAG, " SUDOKU[X][Y] is "+ Sudoku[x][y] );
                                Log.d(TAG, " GRIDBUTTON[X][Y] is "+ tmp );
                                gridButton[x][y].setTextColor(Color.parseColor("#FFFFC0CB"));
                            }
                            //Set the button to clikable and the text to user's text color
                        }
                        else{
                            gridButton[x][y].setText(null);
                        }
                    }
                }
            }

            restored_s = true;
            //InitializedGame = true;
        }
        else {
            Log.i(TAG, "onCreate - savedInstanceState is null");
        }
        //finish Button
        finButton();
    } //End of OnCreate()


    //initial game with data from selectec words by users
    public void setInitialGame(String msg, String[] list){
        Button mButtons;
        int i;
        wordsSplit(list);
        switch (msg){
            case "SPAN":
                getGameGrid(eng_wordsList); //After choosing "fill in Spanish", start a new game with Spanish
                for (i = 0; i < 9; i++) {
                    mButtons = findViewById(Button_ids[i]);
                    mButtons.setText(span_wordsList[i]);
                }
                break;
            case "ENG":
                getGameGrid(span_wordsList); //After choosing "fill in Spanish", start a new game with English
                for (i = 0; i < 9; i++) {
                    mButtons = findViewById(Button_ids[i]);
                    mButtons.setText(eng_wordsList[i]);
                }
                break;
        }
    }

    //initial game with data from selectec words by users
    public void setListenInitialGame(String msg, String[] list){
        Button mButtons;
        int i;
        wordsSplit(list);
        switch (msg){
            case "SPAN":
                getListenGameGrid(eng_wordsList); //After choosing "fill in Spanish", start a new game with Spanish
                for (i = 0; i < 9; i++) {
                    mButtons = findViewById(Button_ids[i]);
                    mButtons.setText(span_wordsList[i]);
                }
                break;
            case "ENG":
                getListenGameGrid(span_wordsList); //After choosing "fill in Spanish", start a new game with English
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
            //the words in the file are separated by -`, so to get each words
            String[] words = list[i].split("-");
            eng_wordsList[i] = words[0];
            span_wordsList[i] = words[1];
            i += 0;
        }
        int b = 0;
    }

    //grid cell initialization
    public void getGameGrid(String[] words) {
        Log.d(TAG, "Game in normal mode is initialized.");
        InitializedGame = true;
        if (!(restored_s)) {
            Sudoku = initialGame.generateGrid(words);
            double remainingGrids = 81;
            double remainingHoles = 50; //set up a number to determine how many words to hide
            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 9; y++) {
                    gridButton[x][y].setText(Sudoku[x][y]);
                    gridButton[x][y].setTextColor(Color.parseColor("#000000"));
                    gridButton[x][y].setClickable(false);
                    double makingHole = remainingHoles / remainingGrids;  //randomly hide some words
                    if (Math.random() <= makingHole) {
                        gridButton[x][y].setText(null);
                        gridButton[x][y].setClickable(true);
                        remainingHoles--;
                    }
                    remainingGrids--;
                }
            }
        }
    }

    //Initialize the grid cells for listening comprehension mode
    /*
        Pseudo-code:
        l_number = 1
        for all grid cells:
            if the grid cell is a pre-filled cell:
                if sudoku[x][y] not in list_of_words:
                //    assign l_number to sudoku[x][y]
                    list_of_words.append(sudoku[x][y])
                    gridButton[x][y].setText(string(l_number));
                    l_number ++;
                if sudoku[x][y] in list_of_words:
                    gridButton[x][y].setText(string(l_number));

        remain_nums = 9 - number //number = number of words that are assigned a number

    */
    Locale locSpanish = new Locale("spa", "ES");
    Locale locEnglish = new Locale("eng", "US");
    TextToSpeech span;
    TextToSpeech eng;
    public void getListenGameGrid(String[] words) {
        Log.d(TAG, "Game in listen mode is initialized.");
        InitializedGame = true;
        l_number = 0;
        int indx_temp = -1;
        if (!(restored_s)) {
            Sudoku = initialGame.generateGrid(words);
            double remainingGrids = 81;
            double remainingHoles = 50; //set up a number to determine how many words to hide
            span = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if(status != TextToSpeech.ERROR) {
                        span.setLanguage(locSpanish);
                    }
                }
            });
            eng = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if(status != TextToSpeech.ERROR) {
                        eng.setLanguage(locEnglish);
                    }
                }
            });
            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 9; y++) {
                    double makingHole = remainingHoles / remainingGrids;  //randomly hide some words
                    if (Math.random() <= makingHole) {
                        gridButton[x][y].setText(null);
                        gridButton[x][y].setClickable(true);
                        remainingHoles--;
                    }
                    else{
                        //If the cells aren't to be hidden, display them on the sudoku Board
                        indx_temp = Arrays.asList(assigned).indexOf(Sudoku[x][y]);
                        //indx_temp = the first occurance of Sudoku[x][y] in assigned
                        if (indx_temp != -1){
                            //if sudoku[x][y] in assigned:
                            gridButton[x][y].setText(l_numbers[indx_temp]);
                            gridButton[x][y].setTextColor(Color.parseColor("#000000"));
                            gridButton[x][y].setClickable(true); //bug: user is able to update the pre-filled words because now the cell is clickable

                        }
                        else{
                            //if sudoku[x][y] not in assigned:
                            assigned[l_number] = Sudoku[x][y];
                            gridButton[x][y].setText(l_numbers[l_number]);
                            l_number++;
                        }
                        if (fill_Span) {
                            final int temp_x = x;
                            final int temp_y = y;
                            gridButton[x][y].setOnClickListener(new View.OnClickListener() {
                                //Whenever user clicks on the numbered cells, a sound occurs.
                                //The sound is the pronunciation of English words
                                public void onClick(View view) {
                                    eng.speak(Sudoku[temp_x][temp_y],TextToSpeech.QUEUE_FLUSH,null);
                                }
                            });
                        }
                        else if (fill_Eng){
                            final int temp_x = x;
                            final int temp_y = y;
                            gridButton[x][y].setOnClickListener(new View.OnClickListener() {
                                //Whenever user clicks on the numbered cells, a sound occurs
                                //The sound is the pronunciation of Spanish words
                                public void onClick(View view) {
                                    span.speak(Sudoku[temp_x][temp_y],TextToSpeech.QUEUE_FLUSH,null);
                                }
                            });
                        }
                    }
                    remainingGrids--;
                }
            }
        }
    }

    public void finButton(){
        //finish button listening action
        //By clicking finish button, will check the correctness of Sudoku
        //display whether right or wrong
        Button mfinButton = (Button) findViewById(R.id.fin_button);
        mfinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordsSplit(list);
                String[][] checkSudoku = new String[9][9];
                String[][] originalSudoku = new String[9][9];
                if (listen_mode){
                    //convert the numbered cells to actual words when passing the array
                    for (int x = 0; x < 9; x++) {
                        for (int y = 0; y < 9; y++) {
                            originalSudoku[x][y] = Sudoku[x][y];
                            //pass words in grid to a Sting[][] Sudoku in order to check correctness
                            CharSequence temp = gridButton[x][y].getText();
                            int indx_temp = Arrays.asList(l_numbers).indexOf(temp);
                            if (indx_temp != -1){
                                //If the grid is a pre-filled number, put the corresponding string in checkSudoku
                                checkSudoku[x][y] = assigned[indx_temp] + "";
                                gridButton[x][y].setOnClickListener(null);
                                gridButton[x][y].setOnClickListener(new View.OnClickListener() {
                                    //user hits one of the grid blocks to insert a word
                                    public void onClick(View v) {
                                         if(!InitializedGame){
                                            Toast.makeText(MainActivity.this ,
                                                    R.string.not_initialized,Toast.LENGTH_LONG).show();
                                        }
                                   /*     else if (mistakeCount >= 3){
                                            Toast.makeText(MainActivity.this,"You have lost your game", Toast.LENGTH_LONG).show();
                                        }*/
                                        else {
                                            if (SelectedButton != null) {
                                                //if a button has already been selected change that button back to normal
                                                SelectedButton.setBackgroundResource(R.drawable.unclicked_button);
                                            }
                                            SelectedButton = (Button) v;
                                            SelectedButton.setBackgroundResource(R.drawable.clicked_button);
                                            //SelectedButton.setText("clicked");
                                        }
                                    }
                                });
                               // gridButton[x][y].setOnClickListener(gridButtonOnClick());
                                int hhh = 0; //For breakpoint purpose
                            }
                            else {
                                checkSudoku[x][y] = temp + "";
                            }
                            gridButton[x][y].setText(null);

                        }
                    }

                }
                else {
                    for (int x = 0; x < 9; x++) {
                        for (int y = 0; y < 9; y++) {
                            //pass words in grid to a Sting[][] Sudoku in order to check correctness
                            CharSequence temp = gridButton[x][y].getText();
                            checkSudoku[x][y] = temp + "";
                            originalSudoku[x][y] = Sudoku[x][y];
                            gridButton[x][y].setText(null);
                        }
                    }
                }
                int ddd = 0; //For breakpoint purpose
                checkAnswer(list, checkSudoku, originalSudoku);
                InitializedGame = false;
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
        for (int x = 0; x < 9; x++) {
            words.addAll(Arrays.asList(originalSudoku[x]).subList(0, 9));
        }
        intent.putStringArrayListExtra(EXTRA_MESSAGE,words);
        intent.putExtra(EXTRA_MESSAGE,words);
        startActivity(intent);
    }


    private Button SelectedButton; //button that user selects to insert

    public void gridButtonOnClick(View v){
        //user hits one of the grid blocks to insert a word
        if(!InitializedGame){
            Toast need_init = Toast.makeText(MainActivity.this ,
                    R.string.not_initialized,Toast.LENGTH_LONG);
            need_init.setGravity(Gravity.TOP, 0, 400);
            need_init.show();
        }
   /*     else if (mistakeCount >= 3){
            Toast.makeText(MainActivity.this,"You have lost your game", Toast.LENGTH_LONG).show();
        }*/
        else {
            if (SelectedButton != null) {
                //if a button has already been selected change that button back to normal
                SelectedButton.setBackgroundResource(R.drawable.unclicked_button);
            }
            SelectedButton = (Button) v;
            SelectedButton.setBackgroundResource(R.drawable.clicked_button);
            //SelectedButton.setText("clicked");
        }
    }



    public void insertButtonOnClick(View w) {
        //user hits button to change text of selectedbutton
        if (listen_mode){
            /*
            For listen mode, all buttons are clickable,
            so I need to make it so that the user cannot change the word on the sudoku if the cell is a number
             */
            if (SelectedButton != null) {
                CharSequence cellText = SelectedButton.getText();
                if (!(Arrays.asList(l_numbers).contains(cellText))) {
                    //If selected grid isn't a number, update the cell.
                    Button button = (Button) w;
                    // text of input button is extracted
                    CharSequence buttonText = button.getText();
                    SelectedButton.setText(buttonText);
                    SelectedButton.setTextColor(Color.parseColor("#FF008577"));
                }
            }
        }
        else {
            //Normal mode
            if (SelectedButton != null) {
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
                for (int x = 0; x < 9; x++) {
                    for (int y = 0; y < 9; y++) {
                        if (gridButton[x][y] == SelectedButton) {
                            //translate english to spanish, and vise versa
                            for (int i = 0; i < 9; i++) {
                                if (Sudoku[x][y].equals(eng_wordsList[i])) {
                                    tmp = span_wordsList[i];
                                }
                                if (Sudoku[x][y].equals(span_wordsList[i])) {
                                    tmp = eng_wordsList[i];
                                }
                            }
                        }
                    }
                }
                //if is wrong, puts word to be red
                if (!buttonText.equals(tmp) && !buttonText.equals("")) {
                    SelectedButton.setBackgroundResource(R.drawable.unclicked_button);
                    SelectedButton.setText(buttonText);
                    SelectedButton.setTextColor(Color.parseColor("#FFFFC0CB"));
                    mistakeCount++;
                } else {
                    //if it's right, makes it green
                    SelectedButton.setBackgroundResource(R.drawable.unclicked_button);
                    SelectedButton.setText(buttonText);
                    SelectedButton.setTextColor(Color.parseColor("#FF008577"));
                }
                Log.d(TAG, "tmp is " + tmp);
                //set the Selected Buttons Text as text from input button
            }
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
       // menu = this.menu;
        Button mButtons;
        int i;
        Intent intent;
        String select;
        switch (item.getItemId()){
            case R.id.fill_Span:
                fill_Span = true;
                Toast result1 = Toast.makeText(MainActivity.this,
                        "User chooses to fill in Spanish",Toast.LENGTH_LONG);
                result1.setGravity(Gravity.TOP, 0, 400);
                result1.show();
                if (!InitializedGame || mistakeCount >= 3)  {
                    /* If the game has not been initialized, and there had been more than 3 mistakes,
                    A new game is generated and the sudoku cells will be filled. [The functions that generate the sudoku will be called.
                    */
                    Log.d(TAG, "User chooses to fill in Spanish");
                    intent = new Intent(MainActivity.this, Words_Selection.class);
                    select = "SPAN";
                    intent.putExtra(EXTRA_MESSAGE, select);
                    startActivityForResult(intent, 1);

                }
                else {
                    //Temporary Toast
                    Toast fin = Toast.makeText(MainActivity.this,R.string.cant_init,Toast.LENGTH_LONG);
                    fin.setGravity(Gravity.TOP, 0, 400);
                    fin.show();
                }
                return true;

            case R.id.fill_Eng:
                //The 9 buttons will display English
                //mButton1.setText(R.string.eng_1);
                fill_Eng = true;
                Toast result2 = Toast.makeText(MainActivity.this,
                        "User chooses to fill in English",Toast.LENGTH_LONG);
                result2.setGravity(Gravity.TOP, 0, 400);
                result2.show();
                if (!InitializedGame || mistakeCount >= 3) {
                    /* If the game has not been initialized, and there had been more than 3 mistakes,
                    A new game is generated and the sudoku cells will be filled. [The functions that generate the sudoku will be called.
                    */
                    Log.d(TAG, "User chooses to fill in English");
                        intent = new Intent(MainActivity.this, Words_Selection.class);
                        select = "ENG";
                        intent.putExtra(EXTRA_MESSAGE, select);
                        startActivityForResult(intent, 1);
                }
                else {
                    //Temporary Toast
                    Toast.makeText(MainActivity.this,R.string.cant_init,Toast.LENGTH_LONG).show();
                }

                return true;

            case R.id.display_words:
                Log.d(TAG,"User chooses to see word pairs");

                /*
                //Displays the warning dialog before displaying the word pairs translation
                reinit_dialog Reinit_warn = new reinit_dialog(this);
                Reinit_warn.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Reinit_warn.show();  */

                if (InitializedGame) {
                    Intent display_w = new Intent(this, Display_Words.class);
                    wordsSplit(list); //Ensures eng_wordsList and span_wordsList match
                    display_w.putExtra(KEY_Eng_wordlist, eng_wordsList);
                    display_w.putExtra(KEY_Span_wordlist, span_wordsList);
                    display_w.putExtra(KEY_fill_Eng, fill_Eng);
                    display_w.putExtra(KEY_fill_Span, fill_Span);
                    display_w.putExtra(KEY_InitializedGame, InitializedGame);
                    this.startActivity(display_w);
                    Log.d(TAG, "Word pairs page loaded");
                }
                else {
                    //Do a new layout here. Display all word pairs
                }
                return true;

            case R.id.load_wordpairs:
                intent = new Intent(MainActivity.this, Words_Selection.class);
                select = "LOAD";
                intent.putExtra(EXTRA_MESSAGE, select);
                startActivityForResult(intent, 1);

                return true;

            case R.id.listen:
                //listening comprehension mode
                if (listen_mode){ //App already in listen mode. User clicks this option to exit listen mode.
                    listen_mode = false;
                    item.setTitle("Listen Comprehension Mode");
                    Toast listen = Toast.makeText(MainActivity.this,
                            "Exiting Listening Comprehension Mode" ,Toast.LENGTH_LONG);
                    listen.setGravity(Gravity.TOP, 0, 400);
                    listen.show();
                    Log.d(TAG, "Exiting Listening Comprehension Mode");
                }
                else {
                    listen_mode = true;
                    // MenuItem listen = menu.findItem(R.id.listen);
                    item.setTitle("Exit Listen Comprehension Mode");
                    Toast listen = Toast.makeText(MainActivity.this,
                            "Entering Listen Comprehension Mode, " +
                                    "please now choose a language to fill the sudoku in",Toast.LENGTH_LONG);
                    //I could make a dialog here
                    listen.setGravity(Gravity.TOP, 0, 400);
                    listen.show();
                    Log.d(TAG, "Entering Listen Comprehension Mode");
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //After the grids are created, save the words
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        //Saves: InitializedGame , Sudoku[][], words on gridButton
        savedInstanceState.putBoolean(KEY_InitializedGame, InitializedGame);
        savedInstanceState.putBoolean(KEY_fill_Eng, fill_Eng);
        savedInstanceState.putBoolean(KEY_fill_Span, fill_Span);
        savedInstanceState.putBoolean(KEY_Listen, listen_mode);
        savedInstanceState.putStringArray(KEY_WORDS_LIST,list);
        //savedInstanceState.put(KEY_filled_words, gridButton);
        int x = 0;
        String[] stringA_p_temp = new String[9];
        String[][] stringA_preset = new String[9][9]; //Array of preset words
        String[] stringA_u_temp = new String[9];
        String[][] stringA_user_filled = new String[9][9]; //Array of user-filled words
        CharSequence temp = "";
        Boolean temp_not_null = false;
        savedInstanceState.putSerializable(KEY_Sudoku,Sudoku);
        for (int i = 0; i < 9; i++){
            x += 0; //for break point purpose
            for (int j = 0; j < 9; j++ ){
                //savedInstanceState.putString(KEY_filled_words[i][j], gridButton[i][j].getText());
                temp = gridButton[i][j].getText();
                //temp_Color = gridButton[i][j].getCurrentTextColor();
                temp_not_null = (temp == null || temp == "");
                if (!temp_not_null) {
                    if (!(gridButton[i][j].isClickable())) { //the word on gridButton is pre-set
                        stringA_p_temp[j] = temp + "";
                        stringA_u_temp[j] = null;
                    } else { //The word on gridButton is user-filled
                        stringA_u_temp[j] = temp + "";
                        stringA_p_temp[j] = null;
                    }
                }
                else{ //temp == null
                    stringA_p_temp[j] = null;
                    stringA_u_temp[j] = null;
                }
            }
            stringA_preset[i] = Arrays.copyOf(stringA_p_temp, stringA_p_temp.length);
            stringA_user_filled[i] = Arrays.copyOf(stringA_u_temp, stringA_u_temp.length);
            // savedInstanceState.putStringArray(KEY_filled_words[i],stringA[i]);
            // savedInstanceState.putStringArray(KEY_filled_words[i],stringA_temp);
            x += 5; //for break point purpose
        }

        for (int i = 0; i < 9; i++){
            Log.d(TAG, "STRING PRESET are  "+ Arrays.toString(stringA_preset[i]));
            Log.d(TAG, "STRING USER FILLED are  "+ Arrays.toString(stringA_user_filled[i]));
        }

        savedInstanceState.putSerializable(KEY_prefilled_words, stringA_preset);
        savedInstanceState.putSerializable(KEY_userfilled_words, stringA_user_filled);
        x = 8;
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
            //Deals with the results sent from words selection
            msg = data.getStringExtra("LANGUAGE");
            list = data.getStringArrayExtra("EXTRA_WORDS_LIST");

            for (int i =0; i< 9; i++) {
                Log.d(TAG, "Words from selection ENG are " + list[i]);
                //  Log.d(TAG, "Words from selection SPAN are " + span_wordsList[i]);

            }
            if (listen_mode){
                //Initialize the game in listen mode
                setListenInitialGame(msg,list);
            }
            else {
                //Initialize the game with normal node
                setInitialGame(msg, list);
            }
        }
    }

    //To make debugger display tags
    @Override public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
    @Override public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override public void onDestroy() {
        mDBHelper.close();
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }


}
