package com.example.myapplication;

import org.junit.Test;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.widget.Button;

import android.view.View;

import java.util.Locale;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
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
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


import static org.junit.Assert.*;

public class MainActivityTest {
    /*
   // private Context context =  MainActivity.getApplicationContext();
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
    private static final String KEY_assigned = "Assigned";

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
    */


    @Test
    public void onCreate() {

    }

    @Test
    public void setInitialGame() {

        MainActivity main = new MainActivity();

        String msg1 = main.getmsg();
        int[] Button_ids = main.getButtonids();
        String[] Eng_wordlist = main.getEng_wordsList();
        String[] Span_wordlist = main.getSpan_wordsList();
        String[] list = main.getList();
        main.setInitialGame(msg1,list);
        Button[] mainButtons = main.getMainButtons();
        int i;
        switch (msg1){
            case "SPAN":
                for (i = 0; i < 9; i++) {
                    assertEquals( mainButtons[i].getText()+"", Span_wordlist[i]);
                }
                break;
            case "ENG":
                for (i = 0; i < 9; i++) {
                    assertEquals( mainButtons[i].getText()+"", Eng_wordlist[i]);
                }
                break;
        }


    }

    @Test
    public void setListenInitialGame() {
    }

    @Test
    public void wordsSplit() {
    }

    @Test
    public void getGameGrid() {
    }

    @Test
    public void getListenGameGrid() {
    }

    @Test
    public void finButton() {
    }

    @Test
    public void checkAnswer() {
    }

    @Test
    public void gridButtonOnClick() {
    }

    @Test
    public void insertButtonOnClick() {
    }

    @Test
    public void clearButtonOnClick() {
    }

    @Test
    public void addMyWords() {
    }

    @Test
    public void onCreateOptionsMenu() {
    }

    @Test
    public void onOptionsItemSelected() {
    }

    @Test
    public void onSaveInstanceState() {
    }

    @Test
    public void onActivityResult() {
    }
}