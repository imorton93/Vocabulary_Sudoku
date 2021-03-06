package controller;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.example.myapplication.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

import Model.DBHelper;
import Model.SudokuChecker;
import Model.SudokuGenerator;
import Model.WordsPairs;
import Model.pop;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


public class QuickNbyNActivity extends AppCompatActivity {
    private static final String KEY_InitializedGame = "initializedgame";
    private static final String KEY_Sudoku = "saved_Sudoku";
    private static final String KEY_prefilled_words = "prefilled_Words";
    private static final String KEY_userfilled_words = "userfilled_Words";
    private static final String KEY_fill_Eng = "fill_Eng";
    private static final String KEY_fill_Span = "fill_Span";
    private static final String KEY_WORDS_LIST = "words_list";
  //  private static final String KEY_Eng_wordlist = "Eng_wordlist";
  //  private static final String KEY_Span_wordlist = "Span_wordlist";
    private static final String KEY_Listen = "Listen_Mode";
    private static final String KEY_assigned = "Assigned";
    private static final String KEY_wordlist = "list";
    private static final String KEY_preset = "Preset";
    private static final String KEY_listen_game = "Game initialzied in listen mode";
    private static final String KEY_GRID_SIZE = "grid_size";
    private static final String KEY_Chrono_Time = "ChronoTime";
    private static final String KEY_QUICK_LAN = "quick_language";
    private static final String MESSAGE_LANGUAGE = "Message_Language";
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
    private int gridSize;
    private static Button[][] gridButton = null;//buttons b11-b99
    private static Button[] Button_ids = null;
    private static SudokuGenerator initialGame = new SudokuGenerator(); //Generate an instance of class SudokuGenerator.
    String[][] Sudoku;
    private static SudokuChecker resultCheck = new SudokuChecker();
    private boolean fill_Eng = false;
    private boolean fill_Span = false;
    private boolean InitializedGame = false;
    private boolean restored_s = false; //boolean for checking if sudoku is restored.
    private boolean listen_mode = false; //Checks if the app is in listen comprehension mode
    private boolean listen_mode_game_init = false; //Checks if the app has a game started in listen comprehension mode
    private int mistakeCount = 0;
    String[][] Sudoku_temp;
    String[][] Sudoku_user;
    //WordsPairs object
    private ArrayList<WordsPairs> list = new ArrayList<>();
    private String msg;

    int[] preset;
    //initial database
    DBHelper mDBHelper = new DBHelper(this);
    Menu menu;
    //Begin of variables for listen mode
    private String[] l_numbers = null; //The string array for listen comprehension mode
    private int remain_nums = 0;
    private int l_number = 0; //a variable for listen mode functions
    String[] assigned; //For listen mode
    Locale locSpanish = new Locale("spa", "ES");
    Locale locEnglish = new Locale("eng", "US");
    TextToSpeech span;
    TextToSpeech eng;
    private Chronometer timer;
    //End of variables for listen mode
    /*
    Button[] mainButtons = { //This Button array is actually for TestCases
            findViewById(Button_ids[0]),
            findViewById(Button_ids[1]),
            findViewById(Button_ids[2]),
            findViewById(Button_ids[3]),
            findViewById(Button_ids[4]),
            findViewById(Button_ids[5]),
            findViewById(Button_ids[6]),
            findViewById(Button_ids[7]),
            findViewById(Button_ids[8]),

    }; */

    String[] span_words = new String[9];
    String[] eng_words = new String[9];



    View.OnLongClickListener longClickListener = new View.OnLongClickListener(){
        @Override
        public boolean onLongClick(View v){
            if(!InitializedGame){
                Toast need_init = Toast.makeText(QuickNbyNActivity.this ,
                        R.string.not_initialized,Toast.LENGTH_LONG);
                need_init.setGravity(Gravity.TOP, 0, 400);
                need_init.show();
                return true;
            }
            Object tag;
            if(gridSize == 4){
                return true;
            }
            else if(gridSize == 6){
                tag = findViewById(R.id.Main6).getTag();
            }
            else if(gridSize == 9){
                tag = findViewById(R.id.Main9).getTag();
            }
            else{
                tag = findViewById(R.id.Main12).getTag();
            }


            Button button = (Button) v;
            pop window = new pop();
            // inflate the layout of the popup window
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(LAYOUT_INFLATER_SERVICE);
            CharSequence buttonText = button.getText();
            int orientation = getResources().getConfiguration().orientation;
            window.createWindow(v,inflater,fill_Span, fill_Eng,list,orientation,gridSize, tag);


            return true;
        }
    };


    View.OnClickListener listener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            if(!InitializedGame){
                Toast need_init = Toast.makeText(QuickNbyNActivity.this ,
                        R.string.not_initialized,Toast.LENGTH_LONG);
                need_init.setGravity(Gravity.TOP, 0, 400);
                need_init.show();
            }

   /*     else if (mistakeCount >= 3){
                    Toast.makeText(MainActivity.this,"You have lost your game", Toast.LENGTH_LONG).show();
                }*/
            else {
                Button test = (Button) v;
                if(test.getCurrentTextColor() == Color.parseColor("#000000")){
                    return;
                }

                else if (SelectedButton != null) {
                    //if a button has already been selected change that button back to normal
                    SelectedButton.setBackgroundResource(R.drawable.unclicked_button);
                }
                SelectedButton = (Button) v;
                SelectedButton.setBackgroundResource(R.drawable.clicked_button);
                Log.d(TAG, "buttonText length is" );
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timer = findViewById(R.id.finalTime);

        Log.d(TAG, "onCreate(Bundle) called");
        gridSize = getIntent().getIntExtra(KEY_GRID_SIZE, 9);

        //based on grid size, to get differnt layout
        if (gridSize != 9){
            int layoutID = getResources().getIdentifier("activity_main"+ gridSize, "layout", getPackageName());
            setContentView(layoutID);
        }
        //set up size of grids
        gridButton = new Button[gridSize][gridSize];//buttons b11-b99
        Button_ids = new Button[gridSize];
        Sudoku = new String[gridSize][gridSize];
        Sudoku_temp = new String[gridSize][gridSize];
        Sudoku_user = new String[gridSize][gridSize];
        preset = new int[(int)Math.pow(gridSize,2)];
        l_numbers = new String[gridSize];
        for (int i = 0; i < gridSize; i++ ){
            l_numbers[i] = String.valueOf(i+1);
        }
        assigned = new String[gridSize]; //For listen mode
        //initial gameGrid
        //show words in button
        if (gridSize == 12){
            for (int x = 0; x < gridSize; x++) {
                for (int y = 0; y < gridSize; y++) {
                    String buttonID = "b" + (x+1) + "_"+ (y+1);
                    int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                    gridButton[x][y] = findViewById(resID);
                }
                String wordButton = "button" + (x+1);
                int ID = getResources().getIdentifier(wordButton, "id", getPackageName());
                Button_ids[x] = findViewById(ID);
            }
        }else{
            for (int x = 0; x < gridSize; x++) {
                for (int y = 0; y < gridSize; y++) {
                    String buttonID = "b" + (x+1) + (y+1);
                    int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                    gridButton[x][y] = findViewById(resID);
                }
                String wordButton = "button" + (x+1);
                int ID = getResources().getIdentifier(wordButton, "id", getPackageName());
                Button_ids[x] = findViewById(ID);
            }
        }

    for (int x = 0; x < gridSize; x++){
        for (int y = 0; y < gridSize; y++){
            gridButton[x][y].setOnClickListener(listener);
            gridButton[x][y].setOnLongClickListener(longClickListener);
        }
    }

    Intent prev_intent = getIntent();
    fill_Eng = prev_intent.getBooleanExtra(KEY_fill_Eng, false);
    fill_Span = prev_intent.getBooleanExtra(KEY_fill_Span, false);
    listen_mode =  prev_intent.getBooleanExtra(KEY_Listen, false);
    msg = getIntent().getStringExtra(KEY_QUICK_LAN);
    int jj = 0;

    span_words = getResources().getStringArray(R.array.Span_words);
    eng_words = getResources().getStringArray(R.array.Eng_words);

    /*
    for (int i = 0; i < gridSize; i++) { //Set up list
        list.add(new WordsPairs(eng_words[i], span_words[i]));
    } */

    list =  generateRandomList(gridSize);

        if ((savedInstanceState != null)) {
            //If there is an incomplete sudoku, the game loads the words on Sudoku that the user filled in before,
            // so user does not need to restart game.
            timer = findViewById(R.id.finalTime);
            timer.setBase(savedInstanceState.getLong(KEY_Chrono_Time));
            timer.start();
            Button mButtons;
            InitializedGame = savedInstanceState.getBoolean(KEY_InitializedGame);
            fill_Eng = savedInstanceState.getBoolean(KEY_fill_Eng);
            fill_Span = savedInstanceState.getBoolean(KEY_fill_Span);
            listen_mode = savedInstanceState.getBoolean(KEY_Listen);
            list = savedInstanceState.getParcelableArrayList(KEY_WORDS_LIST);
            preset = savedInstanceState.getIntArray(KEY_preset);
            listen_mode_game_init =  savedInstanceState.getBoolean(KEY_listen_game);
            gridSize = savedInstanceState.getInt(KEY_GRID_SIZE);
            msg = savedInstanceState.getString(MESSAGE_LANGUAGE);
      /*      if (listen_mode){
                MenuItem listen_t = menu.findItem(R.id.listen);
                listen_t.setTitle("Exit Listen Comprehension Mode");
                int xxxxx = 0;
            } */

            //set timer on rotation
            timer = findViewById(R.id.finalTime);
            timer.setBase(savedInstanceState.getLong(KEY_Chrono_Time));

            if (InitializedGame) {
                timer.start();
                //If a game has been initialized
                int j = 0;  //For breakpoint purpose
                Log.i(TAG, "loads the words user filled in before");
            /* for (int i = 0; i < 9; i++) {
                Sudoku_temp[i] = savedInstanceState.getStringArray(KEY_filled_words[i]); //break point here
                j = 1; //for break point purpose
                //BUG: at least after the screen is turned and turned back, the words (wrong) are preserved
                //Problem: sudoku [i] is the same for all i
            } */
                //
                if (fill_Eng) {
                    for (int i = 0; i < gridSize; i++) {
                        // mButtons = findViewById(Button_ids[i]);
                        // mButtons.setText(list.get(i).getENG());
                        Button_ids[i].setText(list.get(i).getENG());
                    }
                } else if (fill_Span) {
                    for (int i = 0; i < gridSize; i++) {
                        //  mButtons = findViewById(Button_ids[i]);
                        // mButtons.setText(list.get(i).getSPAN());
                        Button_ids[i].setText(list.get(i).getSPAN());
                    }
                }
                Sudoku_temp = (String[][]) savedInstanceState.getSerializable(KEY_prefilled_words);
                Sudoku_user = (String[][]) savedInstanceState.getSerializable(KEY_userfilled_words);
                Sudoku = (String[][]) savedInstanceState.getSerializable(KEY_Sudoku);
                // The arrays in Sudoku_temp has the columns of the original sudoku from right to left (should be from left to right)
                if (listen_mode_game_init) { //If there is a game initialized in listen mode
                    assigned = savedInstanceState.getStringArray(KEY_assigned);
                    span = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if (status != TextToSpeech.ERROR) {
                                span.setLanguage(locSpanish);
                            }
                        }
                    });
                    eng = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if (status != TextToSpeech.ERROR) {
                                eng.setLanguage(locEnglish);
                            }
                        }
                    });
                    int temp_indx = -1;
                    if (fill_Eng){
                        for (int x = 0; x < gridSize; x++) { //break point here
                            for (int y = 0; y < gridSize; y++) {
                                gridButton[x][y].setText(Sudoku_temp[x][y]); //No problem here.
                                j += 5; //break point here
                                if (Sudoku_temp[x][y] != null) {
                                    //This cell (gridbutton) is pre-filled.
                                    gridButton[x][y].setTextColor(Color.parseColor("#000000"));
                                    temp_indx =  Arrays.asList(l_numbers).indexOf(Sudoku_temp[x][y]);
                                    // final String tospeak = assigned[temp_indx];
                                    final String tospeak = list.get(temp_indx).getSPAN();
                                    gridButton[x][y].setOnClickListener(new View.OnClickListener() {
                                        //Whenever user clicks on the numbered cells, a sound occurs
                                        //The sound is the pronunciation of Spanish words
                                        public void onClick(View view) {
                                            span.speak(tospeak,TextToSpeech.QUEUE_FLUSH,null);
                                        }
                                    });
                                }
                                else {
                                    //This cell is user-fillable.
                                    gridButton[x][y].setClickable(true);
                                    if (Sudoku_user[x][y] != null) {
                                        if (Sudoku_user[x][y].contains("-red")){
                                            String str = Sudoku_user[x][y];
                                            str = str.replace(str.substring(str.length()-4), "");
                                            gridButton[x][y].setText(str);
                                            gridButton[x][y].setTextColor(Color.parseColor("#FFB00000"));
                                            gridButton[x][y].setTypeface(null, Typeface.BOLD);
                                        }else{
                                            gridButton[x][y].setText(Sudoku_user[x][y]);
                                            //if it's right, makes it green
                                            gridButton[x][y].setTextColor(Color.parseColor("#FF008577"));
                                            gridButton[x][y].setTypeface(null, Typeface.NORMAL);
                                        }
                                        //Set the button to clikable and the text to user's text color
                                    } else {
                                        gridButton[x][y].setText(null);
                                    }
                                }
                            }
                        }
                    }
                    else if (fill_Span){
                        for (int x = 0; x < gridSize; x++) { //break point here
                            for (int y = 0; y < gridSize; y++) {
                                gridButton[x][y].setText(Sudoku_temp[x][y]); //No problem here.
                                j += 5; //break point here
                                if (Sudoku_temp[x][y] != null) {
                                    //This cell (gridbutton) is pre-filled.
                                    gridButton[x][y].setTextColor(Color.parseColor("#000000"));
                                    temp_indx =  Arrays.asList(l_numbers).indexOf(Sudoku_temp[x][y]);
                                    // final String tospeak = assigned[temp_indx];
                                    final String tospeak = list.get(temp_indx).getENG();
                                    gridButton[x][y].setOnClickListener(new View.OnClickListener() {
                                        //Whenever user clicks on the numbered cells, a sound occurs.
                                        //The sound is the pronunciation of English words
                                        public void onClick(View view) {
                                            eng.speak(tospeak,TextToSpeech.QUEUE_FLUSH,null);
                                        }
                                    });
                                } else {
                                    //This cell is user-fillable
                                    gridButton[x][y].setClickable(true);
                                    if (Sudoku_user[x][y] != null) {
                                        if (Sudoku_user[x][y].contains("-red")){
                                            String str = Sudoku_user[x][y];
                                            str = str.replace(str.substring(str.length()-4), "");
                                            gridButton[x][y].setText(str);
                                            gridButton[x][y].setTextColor(Color.parseColor("#FFB00000"));
                                            gridButton[x][y].setTypeface(null, Typeface.BOLD);
                                        }else{
                                            gridButton[x][y].setText(Sudoku_user[x][y]);
                                            //if it's right, makes it green
                                            gridButton[x][y].setTextColor(Color.parseColor("#FF008577"));
                                            gridButton[x][y].setTypeface(null, Typeface.NORMAL);
                                        }
                                        //Set the button to clikable and the text to user's text color
                                    } else {
                                        gridButton[x][y].setText(null);
                                    }
                                }
                            }
                        }
                    }
                    restored_s = true;
                } else {
                    //Restore state for normal mode
                    for (int x = 0; x < gridSize; x++) { //break point here
                        for (int y = 0; y < gridSize; y++) {
                            gridButton[x][y].setText(Sudoku_temp[x][y]); //No problem here.
                            j += 5; //break point here
                            if (Sudoku_temp[x][y] != null) {
                                gridButton[x][y].setClickable(false);
                                gridButton[x][y].setTextColor(Color.parseColor("#000000"));
                                //Sudoku 'memorizes' the pre-set words
                            }
                            else {
                                gridButton[x][y].setClickable(true);
                                if (Sudoku_user[x][y] != null) {
                                    if (Sudoku_user[x][y].contains("-red")){
                                        String str = Sudoku_user[x][y];
                                        str = str.replace(str.substring(str.length()-4), "");
                                        gridButton[x][y].setText(str);
                                        gridButton[x][y].setTextColor(Color.parseColor("#FFB00000"));
                                        gridButton[x][y].setTypeface(null, Typeface.BOLD);
                                    }else{
                                        gridButton[x][y].setText(Sudoku_user[x][y]);
                                        //if it's right, makes it green
                                        gridButton[x][y].setTextColor(Color.parseColor("#FF008577"));
                                        gridButton[x][y].setTypeface(null, Typeface.NORMAL);
                                    }
                                    //Set the button to clikable and the text to user's text color
                                } else {
                                    gridButton[x][y].setText(null);
                                }
                            }
                        }
                    }

                    restored_s = true;
                    //InitializedGame = true;
                }
            }
            int count = 0;
            if(preset != null) {
                for (int i = 0; i < gridSize; i++) {
                    for (int j = 0; j < gridSize; j++) {
                        if (preset[i*gridSize + j] == 1) {
                            count++;
                            gridButton[i][j].setTextColor(Color.parseColor("#000000"));
                            //System.out.println("blackened text" + count);
                        }
                    }
                }
            }
        }
    else {
        int i;
        Toast tip_popup = Toast.makeText(this, "Word will be magnified if you press the cell it is in for a long time.",Toast.LENGTH_LONG);
        tip_popup.setGravity(Gravity.TOP, 0, 500);
        tip_popup.show();
        Log.i(TAG, "onCreate - savedInstanceState is null");
        if (fill_Eng){
            fill_Span = false;
            Toast result2 = Toast.makeText(QuickNbyNActivity.this,
                    "Fill in English", Toast.LENGTH_LONG);
            result2.setGravity(Gravity.TOP, 0, 400);
            result2.show();
                /* If the game has not been initialized, and there had been more than 3 mistakes,
                A new game is generated and the sudoku cells will be filled. [The functions that generate the sudoku will be called.
                */
                Log.d(TAG, "Fill in English");
            String msg = "ENG";
            for (i = 0; i < list.size(); i++) {
                Button_ids[i].setText(list.get(i).getENG());
            }
            if (listen_mode){
                getListenGameGrid(msg);
            }
            else{
                getGameGrid(msg);
            }

        }
        else if (fill_Span){
            fill_Eng = false;
            Toast result1 = Toast.makeText(QuickNbyNActivity.this,
                    "Fill in Spanish", Toast.LENGTH_LONG);
            result1.setGravity(Gravity.TOP, 0, 400);
            result1.show();
                /* If the game has not been initialized, and there had been more than 3 mistakes,
                A new game is generated and the sudoku cells will be filled. [The functions that generate the sudoku will be called.
                */
             Log.d(TAG, "Fill in Spanish");
            String msg = "SPAN";
            for (i = 0; i < list.size(); i++) {
                Button_ids[i].setText(list.get(i).getSPAN());
            }
            if (listen_mode){
                getListenGameGrid(msg);
            }
            else{
                getGameGrid(msg);
            }
        }
    }
    //finish Button
    finButton();
    //setting text color of prefilled cells



    } //End of OnCreate()

    //grid cell initialization
    public void getGameGrid(String msg) {
        Log.d(TAG, "Game in normal mode is initialized.");
        InitializedGame = true;
        timer = findViewById(R.id.finalTime);
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        Sudoku = initialGame.generateGrid(msg,list);
        Log.d(TAG, "restored_s is " + restored_s);
        for(int i = 0; i < gridSize; i++){
            for(int j = 0; j < gridSize; j++){
                preset[i*gridSize + j] = 1;
            }
        }

        double remainingGrids = Math.pow(gridSize,2);
        double remainingHoles = remainingGrids*3/5; //set up a number to determine how many words to hide
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                //Adjust the text based on the length of the word
                if (Sudoku[x][y].length() > 6 && gridSize != 4) {
                    CharSequence text = Sudoku[x][y].subSequence(0, 6) + "..";
                    gridButton[x][y].setText(text);

                } else {
                    gridButton[x][y].setText(Sudoku[x][y]);
                }
                gridButton[x][y].setTextColor(Color.parseColor("#000000"));
                gridButton[x][y].setOnClickListener(null);
                gridButton[x][y].setClickable(false);
                double makingHole = remainingHoles / remainingGrids;  //randomly hide some words
                if (Math.random() <= makingHole) {
                    gridButton[x][y].setText(null);
                    gridButton[x][y].setClickable(true);
                    gridButton[x][y].setOnClickListener(listener);
                    gridButton[x][y].setTextColor(Color.parseColor("#FF008577"));
                    remainingHoles--;
                    preset[x * gridSize + y] = 0;
                }
                remainingGrids--;
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

    public void getListenGameGrid(String msg) {
        Log.d(TAG, "Game in listen mode is initialized.");
        InitializedGame = true;
        timer = findViewById(R.id.finalTime);
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        listen_mode_game_init = true; //User has initialized a game in listen mode
        l_number = 0;
        int indx_temp = -1;
        for(int i = 0; i < gridSize; i++){
            for(int j = 0; j < gridSize; j++){
                preset[i*gridSize + j] = 1;
                gridButton[i][j].setText("");
            }
        }
        Sudoku = initialGame.generateGrid(msg,list);
        double remainingGrids = Math.pow(gridSize,2);
        double remainingHoles = remainingGrids*3/5; //set up a number to determine how many words to hide
        span = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    span.setLanguage(locSpanish);
                }
            }
        });
        eng = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    eng.setLanguage(locEnglish);
                }
            }
        });
        //Randomize the numbers in l_numbers[i]
        final int min = 0;
        final int max = gridSize-1;
        String temp = null;
        for (int j = 0; j < gridSize/4; j++){
            final int random1 = new Random().nextInt((max - min) + 1) + min;
            final int random2 = new Random().nextInt((max - min) + 1) + min;
            temp = l_numbers[random1];
            l_numbers[random1] = l_numbers[random2];
            l_numbers[random2] = temp;
        }
        //End of randomize numbers

        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                double makingHole = remainingHoles / remainingGrids;  //randomly hide some words
                //gridButton[x][y].setOnClickListener(listener);
                gridButton[x][y].setClickable(true);
                if (Math.random() <= makingHole) {
                    gridButton[x][y].setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            if(!InitializedGame){
                                Toast need_init = Toast.makeText(QuickNbyNActivity.this ,
                                        R.string.not_initialized,Toast.LENGTH_LONG);
                                need_init.setGravity(Gravity.TOP, 0, 400);
                                need_init.show();
                            }
                            else {
                                Button test = (Button) v;
                                if (SelectedButton != null) {
                                    //if a button has already been selected change that button back to normal
                                    SelectedButton.setBackgroundResource(R.drawable.unclicked_button);
                                }
                                SelectedButton = (Button) v;
                                SelectedButton.setBackgroundResource(R.drawable.clicked_button);
                                Log.d(TAG, "buttonText length is" );
                            }
                        }
                    });
                    preset[x * gridSize + y] = 0;
                    remainingHoles--;
                } else {
                    //  switch (msg) {
                    //     case "SPAN":
                    if (fill_Span) {
                        //If the cells aren't to be hidden, display them on the sudoku Board
                        // indx_temp = list.indexOf(Sudoku[x][y]);
                        for (int i = 0; i < gridSize; i++){
                            if (( list.get(i).getENG()).equals(Sudoku[x][y])){
                                indx_temp = i;
                                break;
                            }
                        }
                        gridButton[x][y].setText(l_numbers[indx_temp]);
                        gridButton[x][y].setTextColor(Color.parseColor("#000000"));
                        final int temp_x = x;
                        final int temp_y = y;
                        gridButton[x][y].setOnClickListener(new View.OnClickListener() {
                            //Whenever user clicks on the numbered cells, a sound occurs
                            //The sound is the pronunciation of Spanish words
                            public void onClick(View view) {
                                eng.speak(Sudoku[temp_x][temp_y], TextToSpeech.QUEUE_FLUSH, null);
                            }
                        });
                    } else if (fill_Eng) {
                        //If the cells aren't to be hidden, display them on the sudoku Board
                        for (int i = 0; i < gridSize; i++) {
                            if ((list.get(i).getSPAN()).equals(Sudoku[x][y])) {
                                indx_temp = i;
                                break;
                            }
                        }
                        gridButton[x][y].setText(l_numbers[indx_temp]);
                        gridButton[x][y].setTextColor(Color.parseColor("#000000"));
                        final int temp_x = x;
                        final int temp_y = y;
                        gridButton[x][y].setOnClickListener(new View.OnClickListener() {
                            //Whenever user clicks on the numbered cells, a sound occurs
                            //The sound is the pronunciation of Spanish words
                            public void onClick(View view) {
                                span.speak(Sudoku[temp_x][temp_y], TextToSpeech.QUEUE_FLUSH, null);
                            }
                        });
                    }
                }
                gridButton[x][y].setOnLongClickListener(longClickListener);
                remainingGrids--;
            }
        }
    }

    public void finButton() {
        //finish button listening action
        //By clicking finish button, will check the correctness of Sudoku
        //display whether right or wrong
        Button mfinButton = (Button) findViewById(R.id.fin_button);
        mfinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InitializedGame) {
                    timer.stop();

                    String[][] checkSudoku = new String[gridSize][gridSize];
                    String[][] originalSudoku = new String[gridSize][gridSize];
                    if (listen_mode_game_init) {
                        //convert the numbered cells to actual words when passing the array
                        for (int x = 0; x < gridSize; x++) {
                            for (int y = 0; y < gridSize; y++) {
                                originalSudoku[x][y] = Sudoku[x][y];
                                //pass words in grid to a Sting[][] Sudoku in order to check correctness
                                CharSequence temp = gridButton[x][y].getText();
                                int indx_temp = Arrays.asList(l_numbers).indexOf(temp);
                                if (indx_temp != -1) {
                                    //If the grid is a pre-filled number, put the corresponding string in checkSudoku
                                    if (fill_Span){
                                        checkSudoku[x][y] = list.get(indx_temp).getENG();
                                    }
                                    else if (fill_Eng){
                                        checkSudoku[x][y] = list.get(indx_temp).getSPAN();
                                    }
                                    // gridButton[x][y].setClickable(false);
                                    //gridButton[x][y].setOnClickListener(listener); //INCorrect way to set listener
                                    gridButton[x][y].setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v){
                                            if(!InitializedGame){
                                                Toast need_init = Toast.makeText(QuickNbyNActivity.this ,
                                                        R.string.not_initialized,Toast.LENGTH_LONG);
                                                need_init.setGravity(Gravity.TOP, 0, 400);
                                                need_init.show();
                                            }
                                            else {
                                                Button test = (Button) v;
                                                if (SelectedButton != null) {
                                                    //if a button has already been selected change that button back to normal
                                                    SelectedButton.setBackgroundResource(R.drawable.unclicked_button);
                                                }
                                                SelectedButton = (Button) v;
                                                SelectedButton.setBackgroundResource(R.drawable.clicked_button);
                                                Log.d(TAG, "buttonText length is" );
                                            }
                                        }
                                    });
                                    // gridButton[x][y].setOnClickListener(listener); //This line casuses bug??? Nope, it does make no sound appear, but button also becomes unclickable
                                    //  gridButton[x][y].setClickable(true);
                                    //gridButton[x][y].setOnClickListener(null);
                                    /*
                                    gridButton[x][y].setOnClickListener(new View.OnClickListener() {
                                        //user hits one of the grid blocks to insert a word
                                        public void onClick(View v) {
                                            if (!InitializedGame) {
                                                Toast.makeText(MainActivity.this,
                                                        R.string.not_initialized, Toast.LENGTH_LONG).show();
                                            }
                                   /*     else if (mistakeCount >= 3){
                                            Toast.makeText(MainActivity.this,"You have lost your game", Toast.LENGTH_LONG).show();
                                        }*/ /*
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
                                    }); */
                                    // gridButton[x][y].setOnClickListener(gridButtonOnClick());
                                    int hhh = 0; //For breakpoint purpose
                                } else {
                                    checkSudoku[x][y] = temp + "";
                                }
                                gridButton[x][y].setText(null);

                            }
                        }
                        //Case for listen mode ends.
                    } else {
                        for (int x = 0; x < gridSize; x++) {
                            for (int y = 0; y < gridSize; y++) {
                                //pass words in grid to a Sting[][] Sudoku in order to check correctness
                                CharSequence temp = gridButton[x][y].getText();
                                checkSudoku[x][y] = temp + "";
                                originalSudoku[x][y] = Sudoku[x][y];
                                gridButton[x][y].setText(null);
                            }
                        }
                    }
                    int ddd = 0; //For breakpoint purpose
                    checkAnswer(checkSudoku, originalSudoku);
                    InitializedGame = false;
                    listen_mode_game_init = false;
                }
                //Generate a new game immediately ,with the same parameters
/*                String msg;
                if (fill_Eng){
                    msg = "ENG";
                }
                else{
                    msg = "SPAN";
                }
                if (listen_mode){
                    getListenGameGrid(msg);
                }
                else{
                    getGameGrid(msg);
                }*/
            }
        });
    }


    //check sudoku correctness
    public void checkAnswer(String[][] Sudoku, String[][] originalSudoku) {
        //String msg;
        Boolean resultmsg;
        if (resultCheck.sudokuCheck(Sudoku, list)){
            resultmsg = true;
            //msg = "Congratulation! Sudoku is correct!";
        }else {
            resultmsg = false;
            //msg = "Sudoku is incorrect, try again!";
        }
        /*Toast result = Toast.makeText(QuickNbyNActivity.this, msg, Toast.LENGTH_LONG);
        result.setGravity(Gravity.TOP, 0, 400);
        result.show();*/
        //only for test
        //intent the 9*9 Grid Sudoku to a new page

        Intent intent = new Intent(QuickNbyNActivity.this, finishPage.class);
        ArrayList<String> words = new ArrayList<String>();
        for (int x = 0; x < gridSize; x++) {
            words.addAll(Arrays.asList(originalSudoku[x]).subList(0, gridSize));
        }
        intent.putStringArrayListExtra(EXTRA_MESSAGE,words);
        intent.putExtra(KEY_GRID_SIZE, gridSize);
        intent.putParcelableArrayListExtra("PLAYING_WORDS_LIST", list);
        intent.putExtra("PLAYING_LANGUAGE", msg);
        intent.putExtra("result",resultmsg);
        intent.putExtra("LISTEN_MODE", listen_mode);
        intent.putExtra("finalTime", timer.getBase());
        timer.setBase(SystemClock.elapsedRealtime());
        startActivityForResult(intent, 1);
    }


    private Button SelectedButton; //button that user selects to insert


    public boolean checkFilledWord(String buttonText){
        //Future updates: the word becomes red only when conflict arise
        String tmp = null;
        String str = null;
        //check which word should be right
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                if (gridButton[x][y] == SelectedButton) {
                    str = Sudoku[x][y];
                    //translate english to spanish, and vise versa
                    for (int i = 0; i < gridSize; i++) {
                        if (Sudoku[x][y].equals(list.get(i).getENG())) {
                            tmp = list.get(i).getSPAN();
                        }
                        if (Sudoku[x][y].equals(list.get(i).getSPAN())) {
                            tmp = list.get(i).getENG();
                        }
                    }
                }
            }
        }
        Log.d(TAG, "SUDOKU[X][Y] is " + str);
        Log.d(TAG, "tmp is " + tmp);
        return buttonText.equals(tmp) && !buttonText.equals("");
    }


    public void insertButtonOnClick(View w) {
        //user hits button to change text of selectedbutton
        if (listen_mode) {
            /*
            For listen mode, all buttons are clickable,
            so I need to make it so that the user cannot change the word on the sudoku if the cell is a number
             */
            // text of input button is extracted
            /* when inserting a new word into puzzle, check if right or wrong
             *  if it's right, make it green
             * *if wrong, put word to be red
             */
            //track the button that user selects
            if (SelectedButton != null) {
                CharSequence cellText = SelectedButton.getText();
                if (!(Arrays.asList(l_numbers).contains(cellText))) {
                    //If selected grid isn't a number, update the cell.
                    Button button = (Button) w;
                    // text of input button is extracted
                    CharSequence buttonText = button.getText();
                    String eng = null;
                    String span = null;
                    if (fill_Span){
                        span = buttonText.toString();
                        for (int i = 0; i < list.size(); i++){
                            if (span.equals(list.get(i).getSPAN())){
                                eng = list.get(i).getENG();
                            }
                        }
                    }else{
                        eng = buttonText.toString();
                        for (int i = 0; i < list.size(); i++){
                            if (eng.equals(list.get(i).getENG())){
                                span = list.get(i).getSPAN();
                            }
                        }
                    }
                    Log.d(TAG, "buttonText length is" + buttonText.length());
                    //if is wrong, puts word to be red
                    if (!checkFilledWord(buttonText.toString())) {
                        if(buttonText.length() > 6 && gridSize != 4){
                            buttonText = buttonText.subSequence(0,6)+"..";
                            System.out.println("Constrained to six letters");
                        }
                        SelectedButton.setText(buttonText);
                        SelectedButton.setTextColor(Color.parseColor("#FFB00000"));
                        SelectedButton.setTypeface(null, Typeface.BOLD);
                        mistakeCount++;
                        addMyWords(eng,span);
                    } else {
                        //if it's right, makes it green
                        if(buttonText.length() > 6 && gridSize != 4){
                            buttonText = buttonText.subSequence(0,6)+"..";
                            System.out.println("Constrained to six letters");
                        }
                        SelectedButton.setText(buttonText);
                        SelectedButton.setTextColor(Color.parseColor("#FF008577"));
                        SelectedButton.setTypeface(null, Typeface.NORMAL);
                        //mistakeCount--;
                    }
                    //set the Selected Buttons Text as text from input button
                }
                SelectedButton.setBackgroundResource(R.drawable.clicked_button);
            }
        } else {
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
                //if is wrong, puts word to be red
                String eng = null;
                String span = null;
                if (fill_Span){
                    span = buttonText.toString();
                    for (int i = 0; i < list.size(); i++){
                        if (span.equals(list.get(i).getSPAN())){
                            eng = list.get(i).getENG();
                        }
                    }
                }else{
                    eng = buttonText.toString();
                    for (int i = 0; i < list.size(); i++){
                        if (eng.equals(list.get(i).getENG())){
                            span = list.get(i).getSPAN();
                        }
                    }
                }
                if (!checkFilledWord(buttonText.toString())) {
                    //SelectedButton.setBackgroundResource(R.drawable.unclicked_button);
                    if(buttonText.length() > 6 && gridSize != 4){
                        buttonText = buttonText.subSequence(0,6) + "..";
                    }
                    SelectedButton.setText(buttonText);
                    SelectedButton.setTextColor(Color.parseColor("#FFB00000"));
                    SelectedButton.setTypeface(null, Typeface.BOLD);
                    mistakeCount++;
                    addMyWords(eng,span);
                    //allow user to keep track of what they get wrong
                    //ask user whether they want to save to My words
                } else {
                    //if it's right, makes it green
                    //SelectedButton.setBackgroundResource(R.drawable.unclicked_button);
                    if(buttonText.length() > 6 && gridSize != 4){
                        buttonText = buttonText.subSequence(0,6) + "..";
                    }
                    SelectedButton.setText(buttonText);
                    SelectedButton.setTextColor(Color.parseColor("#FF008577"));
                    SelectedButton.setTypeface(null, Typeface.NORMAL);
                    takeMyWords(eng,span);
                    //allow user to keep track of what they get correct
                }
                //set the Selected Buttons Text as text from input button
                SelectedButton.setBackgroundResource(R.drawable.clicked_button);
            }
        }
    }


    public void clearButtonOnClick(View z){
        if(SelectedButton != null) {
            SelectedButton.setText(null);
        }
    }

    //After the grids are created, save the words
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        timer = findViewById(R.id.finalTime);
        Log.i(TAG, "onSaveInstanceState");
        //Saves: InitializedGame , Sudoku[][], words on gridButton
        savedInstanceState.putBoolean(KEY_InitializedGame, InitializedGame);
        savedInstanceState.putBoolean(KEY_fill_Eng, fill_Eng);
        savedInstanceState.putBoolean(KEY_fill_Span, fill_Span);
        savedInstanceState.putBoolean(KEY_Listen, listen_mode);
        savedInstanceState.putBoolean(KEY_listen_game, listen_mode_game_init);
        savedInstanceState.putParcelableArrayList(KEY_WORDS_LIST, list);
        savedInstanceState.putSerializable(KEY_Sudoku, Sudoku);
        savedInstanceState.putIntArray(KEY_preset, preset);
        //savedInstanceState.put(KEY_filled_words, gridButton);
        savedInstanceState.putInt(KEY_GRID_SIZE, gridSize);
        savedInstanceState.putLong(KEY_Chrono_Time,timer.getBase());
        savedInstanceState.putString(MESSAGE_LANGUAGE,msg);
        int x = 0;
        String[] stringA_p_temp = new String[gridSize];
        String[][] stringA_preset = new String[gridSize][gridSize]; //Array of preset words
        String[] stringA_u_temp = new String[gridSize];
        String[][] stringA_user_filled = new String[gridSize][gridSize]; //Array of user-filled words
        CharSequence temp = "";
        Boolean temp_not_null = false;
        if (listen_mode_game_init) {
            //A game is already initialized in listen mode
            savedInstanceState.putStringArray(KEY_assigned, assigned);
            for (int i = 0; i < gridSize; i++) {
                x += 0; //for break point purpose
                for (int j = 0; j < gridSize; j++) {
                    //savedInstanceState.putString(KEY_filled_words[i][j], gridButton[i][j].getText());
                    temp = gridButton[i][j].getText();
                    //temp_Color = gridButton[i][j].getCurrentTextColor();
                    temp_not_null = (temp == null || temp == "");
                    if (!temp_not_null) {
                        //int indx_temp = Arrays.asList(l_numbers).indexOf(temp);
                        //   if (indx_temp != -1){
                        if (Arrays.asList(l_numbers).contains(temp)) {
                            //the word on gridButton is pre-set
                            stringA_p_temp[j] = temp + "";
                            stringA_u_temp[j] = null;
                        } else { //The word on gridButton is user-filled
                            if (gridButton[i][j].getCurrentTextColor() == Color.parseColor("#FFB00000")){
                                stringA_u_temp[j] = temp + "-red";
                            }else{
                                stringA_u_temp[j] = temp + "";
                            }
                            stringA_p_temp[j] = null;
                        }
                    } else { //temp == null
                        stringA_p_temp[j] = null;
                        stringA_u_temp[j] = null;
                    }
                }
                stringA_preset[i] = Arrays.copyOf(stringA_p_temp, stringA_p_temp.length);
                stringA_user_filled[i] = Arrays.copyOf(stringA_u_temp, stringA_u_temp.length);
                x += 5; //for break point purpose
            }

            for (int i = 0; i < gridSize; i++) {
                Log.d(TAG, "l-STRING PRESET are  " + Arrays.toString(stringA_preset[i]));
                Log.d(TAG, "l-STRING USER FILLED are  " + Arrays.toString(stringA_user_filled[i]));
            }


            savedInstanceState.putSerializable(KEY_prefilled_words, stringA_preset);
            savedInstanceState.putSerializable(KEY_userfilled_words, stringA_user_filled);
            x = 8;

        } else {
            //Normal mode saveOnInstanceState
            for (int i = 0; i < gridSize; i++) {
                x += 0; //for break point purpose
                for (int j = 0; j < gridSize; j++) {
                    //savedInstanceState.putString(KEY_filled_words[i][j], gridButton[i][j].getText());
                    temp = gridButton[i][j].getText();
                    //temp_Color = gridButton[i][j].getCurrentTextColor();
                    temp_not_null = (temp == null || temp == "");
                    if (!temp_not_null) {
                        if (!(gridButton[i][j].isClickable())) { //the word on gridButton is pre-set
                            stringA_p_temp[j] = temp + "";
                            stringA_u_temp[j] = null;
                        } else { //The word on gridButton is user-filled
                            if (gridButton[i][j].getCurrentTextColor() == Color.parseColor("#FFB00000")){
                                stringA_u_temp[j] = temp + "-red";
                            }else{
                                stringA_u_temp[j] = temp + "";
                            }
                            stringA_p_temp[j] = null;
                        }
                    } else { //temp == null
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

            for (int i = 0; i < gridSize; i++) {
                Log.d(TAG, "STRING PRESET are  " + Arrays.toString(stringA_preset[i]));
                Log.d(TAG, "STRING USER FILLED are  " + Arrays.toString(stringA_user_filled[i]));
            }

            savedInstanceState.putSerializable(KEY_prefilled_words, stringA_preset);
            savedInstanceState.putSerializable(KEY_userfilled_words, stringA_user_filled);
            x = 8;
        }
    }

    public void addMyWords(String eng, String span) {
        //initial Database
        //store wrong word that made by user
        //check if there is same word inside Database
        if (mDBHelper.hasWord(new WordsPairs(eng, span))){
            //update Total number of wrong words
            int num = mDBHelper.numWrong(new WordsPairs(eng, span));
            num++;
            //update database
            mDBHelper.updateWrongNum(new WordsPairs(eng, span,num));
        }else{
            //insert word to database
            mDBHelper.updateWrongWord(new WordsPairs(eng, span,1));
        }
        ArrayList<WordsPairs> arrayList = mDBHelper.getData();
        for (int i = 0; i < arrayList.size(); i++){
            Log.d(TAG, "mDBHELPER database has  " + arrayList.get(i).getENG()+"   "+
                    arrayList.get(i).getSPAN()+"  "+arrayList.get(i).getTotal());
        }
    }

    public void takeMyWords(String eng, String span) {
        //initial Database
        //store correct word that made by user
        //check if there is same word inside Database
        if (mDBHelper.hasWord(new WordsPairs(eng, span))){
            //update Total number of wrong words
            int num = mDBHelper.numWrong(new WordsPairs(eng, span));
            num--;
            //update database
            mDBHelper.updateWrongNum(new WordsPairs(eng, span,num));
        }else{
            //insert word to database
            mDBHelper.updateWrongWord(new WordsPairs(eng, span,-1));
        }
        ArrayList<WordsPairs> arrayList = mDBHelper.getData();
        for (int i = 0; i < arrayList.size(); i++){
            Log.d(TAG, "mDBHELPER database has  " + arrayList.get(i).getENG()+"   "+
                    arrayList.get(i).getSPAN()+"  "+arrayList.get(i).getTotal());
        }
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
            list = data.getParcelableArrayListExtra("EXTRA_WORDS_LIST");
            listen_mode = data.getBooleanExtra("LISTEN_MODE", false);

            for (int i = 0; i < gridSize; i++) {
                Log.d(TAG, "Words from selection ENG and SPAN are " + list.get(i).getENG() + "  " +list.get(i).getSPAN());
                Log.d(TAG, "Words from selection LANGUAGE msg is " + msg);
            }
            /*Button mButtons;
            int i;
            switch (msg) {
                case "SPAN":
                    for (i = 0; i < list.size(); i++) {
                        Button_ids[i].setText(list.get(i).getSPAN());
                    }
                    break;
                case "ENG":
                    for (i = 0; i < list.size(); i++) {
                        Button_ids[i].setText(list.get(i).getENG());
                    }
                    break;
            }*/
            if (listen_mode) {
                //Initialize the game in listen mode
                getListenGameGrid(msg);
            } else {
                //Initialize the game with normal node
                getGameGrid(msg);
            }
        }
    }


    ArrayList<String> readTextFromUri(Uri uri) throws IOException {
        ArrayList<String> strings = new ArrayList<>();
        InputStream inputStream = getContentResolver().openInputStream(uri);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String eachline = null;
        try{
            eachline = bufferedReader.readLine();
        } catch (IOException e){
            e.printStackTrace();
        }
        while (eachline != null){
            strings.add(eachline);
            try {
                eachline = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return strings;
    }

    int getRandomInteger(int size){
        Random r = new Random();
        return r.nextInt(size);
    }

    void swap(ArrayList<String> strings, int a, int b){
        String tmp = strings.get(a);
        strings.set(a, strings.get(b-1));
        strings.set(b-1, tmp);
    }

    ArrayList<WordsPairs> generateRandomList(int gridsize){
        ArrayList<WordsPairs> wordpairs = new ArrayList<>();
        ArrayList<String> strings = new ArrayList<>();
        Uri uri = null;
        uri = uri.parse("android.resource://" + getPackageName() + "/" + R.raw.wordpairs);
        try{
            strings = readTextFromUri(uri);
        }	catch (IOException e) {
            e.printStackTrace();
        }

        int rand_int;
        int pos;
        int curr = 0;
        while(curr < gridsize){
            rand_int = getRandomInteger(strings.size() - curr);
            pos = strings.get(rand_int).indexOf(",");
            WordsPairs tmp = new WordsPairs();
            tmp.setENG(strings.get(rand_int).substring(0, pos));
          //  tmp.setSPAN(strings.get(rand_int).substring(pos+1, strings.get(rand_int).length()));
            tmp.setSPAN(strings.get(rand_int).substring(pos+1));
            wordpairs.add(tmp);
            swap(strings, rand_int, strings.size() - curr);
            curr++;
        }
        return wordpairs;
    }

    //To make debugger display tags
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        mDBHelper.close();
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }



}
