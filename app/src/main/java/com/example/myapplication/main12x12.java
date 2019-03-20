package com.example.myapplication;

import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Locale;

import Model.DBHelper;
import Model.SudokuChecker;
import Model.SudokuGenerator;
import controller.MainActivity;

public class main12x12 extends AppCompatActivity{
        private static final String KEY_InitializedGame = "initializedgame";
        private static final String KEY_Sudoku = "saved_Sudoku";
        private static final String KEY_prefilled_words = "prefilled_Words";
        private static final String KEY_userfilled_words = "userfilled_Words";
        private static final String KEY_fill_Eng = "fill_Eng";
        private static final String KEY_fill_Span = "fill_Span";
        private static final String KEY_WORDS_LIST = "words_list";
        private static final String KEY_Eng_wordlist = "Eng_wordlist";
        private static final String KEY_Span_wordlist = "Span_wordlist";
        private static final String KEY_Listen = "Listen_Mode";
        private static final String KEY_assigned = "Assigned";
        private static final String KEY_preset = "Preset";

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
                R.id.button10,
                R.id.button11,
                R.id.button12
        };

        private static final Button[][] gridButton = new Button[12][12];//buttons b11-b99
        private static SudokuGenerator initialGame = new SudokuGenerator(); //Generate an instance of class SudokuGenerator.
        String[][] Sudoku = new String[12][12];
        private static SudokuChecker resultCheck = new SudokuChecker();
        private boolean fill_Eng = false;
        private boolean fill_Span = false;
        private boolean InitializedGame = false;
        private boolean restored_s = false; //boolean for checking if sudoku is restored.
        private boolean listen_mode = false; //Checks if the app is in listen comprehension mode
        private int mistakeCount = 0;
        private String msg;
        String[] eng_wordsList = new String[12];
        String[] span_wordsList = new String[12];
        String[][] Sudoku_temp = new String[12][12];
        String[][] Sudoku_user = new String[12][12];
        String[] list = new String[12];
        int[] preset = new int[144];


        //------------Part needs to be checked----------------------------
        /*DBHelper mDBHelper = new DBHelper(this);
        Menu menu;
        //Begin of variables for listen mode
        private final String[] l_numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9"}; //The string array for listen comprehension mode
        private int remain_nums = 0;
        private int l_number = 0; //a variable for listen mode functions
        String[] assigned = new String[9]; //For listen mode
        Locale locSpanish = new Locale("spa", "ES");
        Locale locEnglish = new Locale("eng", "US");
        TextToSpeech span;
        TextToSpeech eng;
        //End of variables for listen mode
        Button[] mainButtons = new Button[9];*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main12x12);


        //long click function to bring up popup text
        View.OnLongClickListener longClickListener = new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                if(!InitializedGame){
                    Toast need_init = Toast.makeText(main12x12.this ,
                            R.string.not_initialized,Toast.LENGTH_LONG);
                    need_init.setGravity(Gravity.TOP, 0, 400);
                    need_init.show();
                    return true;
                }
                Button button = (Button) v;
                PopupMenu popup = new PopupMenu(main12x12.this, (Button) v);

                popup.getMenuInflater().inflate(R.menu.popup_text, popup.getMenu());

                MenuItem text = popup.getMenu().getItem(0);

                CharSequence buttonText = button.getText();
                Log.d(TAG, "buttonText length is " + buttonText.length() );
                if(buttonText.length() > 6){
                    CharSequence sixText = buttonText.subSequence(0,6);
                    CharSequence shortlist;
                    if(fill_Eng){
                        if(button.getCurrentTextColor() == Color.parseColor("#000000")){
                            for(int i = 0; i < 9; i++){
                                if(span_wordsList[i].length() > 6){
                                    shortlist = span_wordsList[i].subSequence(0,6);
                                    Log.d(TAG, "comparing " + sixText + " and " + shortlist);
                                    if(sixText.equals(shortlist)){
                                        Log.d(TAG, "passed comparison");
                                        buttonText = span_wordsList[i];
                                    }
                                }

                            }
                        }
                        else{
                            for(int j = 0; j < 9; j++){
                                if(eng_wordsList[j].length() > 6){
                                    shortlist = eng_wordsList[j].subSequence(0,6);
                                    Log.d(TAG, "comparing " + sixText + " and " + shortlist);
                                    if(sixText.equals(shortlist)){
                                        Log.d(TAG, "passed comparison");
                                        buttonText = eng_wordsList[j];
                                    }
                                }
                            }
                        }
                    }
                    if(fill_Span){
                        if(button.getCurrentTextColor() == Color.parseColor("#000000")){
                            for(int i = 0; i < 9; i++){
                                if(eng_wordsList[i].length() > 6){
                                    shortlist = eng_wordsList[i].subSequence(0,6);
                                    Log.d(TAG, "comparing " + sixText + " and " + shortlist);
                                    if(sixText.equals(shortlist)){
                                        Log.d(TAG, "passed comparison");
                                        buttonText = eng_wordsList[i];
                                    }
                                }

                            }
                        }
                        else{
                            for(int j = 0; j < 9; j++){
                                if(span_wordsList[j].length() > 6){
                                    shortlist = span_wordsList[j].subSequence(0,6);
                                    Log.d(TAG, "comparing " + sixText + " and " + shortlist);
                                    if(sixText.equals(shortlist)){
                                        Log.d(TAG, "passed comparison");
                                        buttonText = span_wordsList[j];
                                    }
                                }
                            }
                        }
                    }
                }
                text.setTitle(buttonText);

                popup.show();

                return true;
            }
        };

        //click function
        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!InitializedGame){
                    Toast need_init = Toast.makeText(main12x12.this ,
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


        int[] ids={R.id.b1_1, R.id.b1_2,R.id.b1_3, R.id.b1_4, R.id.b1_5,R.id.b1_6,R.id.b1_7, R.id.b1_8,R.id.b1_9, R.id.b1_10, R.id.b1_11,R.id.b1_12,
                R.id.b2_1, R.id.b2_2,R.id.b2_3, R.id.b2_4, R.id.b2_5,R.id.b2_6,R.id.b2_7, R.id.b2_8,R.id.b2_9, R.id.b2_10, R.id.b2_11,R.id.b2_12,
                R.id.b3_1, R.id.b3_2,R.id.b3_3, R.id.b3_4, R.id.b3_5,R.id.b3_6,R.id.b3_7, R.id.b3_8,R.id.b3_9, R.id.b2_10, R.id.b2_11,R.id.b2_12,
                R.id.b4_1, R.id.b4_2,R.id.b4_3, R.id.b4_4, R.id.b4_5,R.id.b4_6,R.id.b4_7, R.id.b4_8,R.id.b4_9, R.id.b4_10, R.id.b4_11,R.id.b4_12,
                R.id.b5_1, R.id.b5_2,R.id.b5_3, R.id.b5_4, R.id.b5_5,R.id.b5_6,R.id.b5_7, R.id.b5_8,R.id.b5_9, R.id.b5_10, R.id.b5_11,R.id.b5_12,
                R.id.b6_1, R.id.b6_2,R.id.b6_3, R.id.b6_4, R.id.b6_5,R.id.b6_6,R.id.b6_7, R.id.b6_8,R.id.b6_9, R.id.b6_10, R.id.b6_11,R.id.b6_12,
                R.id.b7_1, R.id.b7_2,R.id.b7_3, R.id.b7_4, R.id.b7_5,R.id.b7_6,R.id.b7_7, R.id.b7_8,R.id.b7_9, R.id.b7_10, R.id.b7_11,R.id.b7_12,
                R.id.b8_1, R.id.b8_2,R.id.b8_3, R.id.b8_4, R.id.b8_5,R.id.b8_6,R.id.b8_7, R.id.b8_8,R.id.b8_9, R.id.b8_10, R.id.b8_11,R.id.b8_12,
                R.id.b9_1, R.id.b9_2,R.id.b9_3, R.id.b9_4, R.id.b9_5,R.id.b9_6,R.id.b9_7, R.id.b9_8,R.id.b9_9, R.id.b9_10, R.id.b9_11,R.id.b9_12,
                R.id.b10_1, R.id.b10_2,R.id.b10_3, R.id.b10_4, R.id.b10_5,R.id.b10_6,R.id.b10_7, R.id.b10_8,R.id.b10_9, R.id.b10_10, R.id.b10_11,R.id.b10_12,
                R.id.b11_1, R.id.b11_2,R.id.b11_3, R.id.b11_4, R.id.b11_5,R.id.b11_6,R.id.b11_7, R.id.b11_8,R.id.b11_9, R.id.b11_10, R.id.b11_11,R.id.b11_12,
                R.id.b12_1, R.id.b12_2,R.id.b12_3, R.id.b12_4, R.id.b12_5,R.id.b12_6,R.id.b12_7, R.id.b12_8,R.id.b12_9, R.id.b12_10, R.id.b12_11,R.id.b12_12};



// loop through the array, find the button with respective id and set the listener
        for(int i=0; i<ids.length; i++){
            Button gbutton = (Button) findViewById(ids[i]);
            ((Button) gbutton).setOnClickListener(listener);
        }

        for(int i=0; i<ids.length; i++) {
            Button button = (Button) findViewById(ids[i]);
            button.setOnLongClickListener(longClickListener);
        }


    }//end of oncreate

    private Button SelectedButton; //button that user selects to insert


    //---------------------------l_numbers, checkFilledWord, wordsSplit----------------------------------------
    /*public void insertButtonOnClick(View w) {
        //user hits button to change text of selectedbutton
        if (listen_mode) {
            *//*
            For listen mode, all buttons are clickable,
            so I need to make it so that the user cannot change the word on the sudoku if the cell is a number
             *//*
            // text of input button is extracted
            *//* when inserting a new word into puzzle, check if right or wrong
             *  if it's right, make it green
             * *if wrong, put word to be red
             *//*
            //track the button that user selects
            String tmp = null;
            wordsSplit(list);
            if (SelectedButton != null) {
                CharSequence cellText = SelectedButton.getText();
                if (!(Arrays.asList(l_numbers).contains(cellText))) {
                    //If selected grid isn't a number, update the cell.
                    Button button = (Button) w;
                    // text of input button is extracted
                    CharSequence buttonText = button.getText();
                    if(buttonText.length() > 6){
                        buttonText = buttonText.subSequence(0,6)+"..";
                        System.out.println("Constrained to six letters");
                    }
                    Log.d(TAG, "buttonText length is" + buttonText.length());
                    SelectedButton.setText(buttonText);
                    //if is wrong, puts word to be red
                    if (!checkFilledWord(buttonText.toString())) {
                        SelectedButton.setTextColor(Color.parseColor("#FFFFC0CB"));
                        mistakeCount++;
                    } else {
                        //if it's right, makes it green
                        SelectedButton.setTextColor(Color.parseColor("#FF008577"));
                    }
                    //set the Selected Buttons Text as text from input button
                }
            }
        } else {
            //Normal mode
            if (SelectedButton != null) {
                Button button = (Button) w;
                // text of input button is extracted
                CharSequence buttonText = button.getText();
                *//* when inserting a new word into puzzle, check if right or wrong
                 *  if it's right, make it green
                 * *if wrong, put word to be red
                 *//*
                //track the button that user selects
                //if is wrong, puts word to be red
                if (!checkFilledWord(buttonText.toString())) {
                    SelectedButton.setBackgroundResource(R.drawable.unclicked_button);
                    if(buttonText.length() > 6){
                        buttonText = buttonText.subSequence(0,6) + "..";
                    }
                    SelectedButton.setText(buttonText);
                    SelectedButton.setTextColor(Color.parseColor("#FFFFC0CB"));
                    mistakeCount++;
                    //allow user to keep track of what they get wrong
                    //ask user whether they want to save to My words
                } else {
                    //if it's right, makes it green
                    SelectedButton.setBackgroundResource(R.drawable.unclicked_button);
                    if(buttonText.length() > 6){
                        buttonText = buttonText.subSequence(0,6) + "..";
                    }
                    SelectedButton.setText(buttonText);
                    SelectedButton.setTextColor(Color.parseColor("#FF008577"));
                }
                //set the Selected Buttons Text as text from input button
            }
        }
    }*/

    public void clearButtonOnClick(View z){
        if(SelectedButton != null) {
            SelectedButton.setText(null);
        }
    }
}
