package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Words_Selection extends AppCompatActivity {
    private static final String TAG = "Words-Selection";
    public static final String EXTRA_WORDS_LIST = "com.example.myapplication.words_list";

    ArrayList<String> eng_wordsList = new ArrayList<>();
    ArrayList<String> span_wordsList = new ArrayList<>();
    private String selectedItem;
    final TextView[] tv = new TextView[9];
    private int wordsCount = 0;
    private static String[] wordsList_eng = new String[9];
    private static String[] wordsList_span = new String[9];
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.words_selection);

        //retrieve words from text file wordpairs
        InputStream inputStream = getResources().openRawResource(R.raw.wordpairs);
        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
        String eachline = null;
        try {
            eachline = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (eachline != null) {
            // `the words in the file are separated by -`, so to get each words
            String[] words = eachline.split("-");
            eng_wordsList.add(words[0]);
            span_wordsList.add(words[1]);
            try {
                eachline = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        TextView textView = (TextView)findViewById(R.id.tv_view);
        final GridView gridView = (GridView)findViewById(R.id.selection_view);
        message = getIntent().getStringExtra(EXTRA_MESSAGE);
        String msg;
        switch (message) {
            case "SPAN":
                msg = "Select Spanish words ";
                textView.setText(msg);
                gridView.setAdapter(new GridAdapter(span_wordsList, this));
                break;
            case "ENG":
                msg = "Select English words";
                textView.setText(msg);
                gridView.setAdapter(new GridAdapter(eng_wordsList, this));
                break;
            default:
                msg = "Load more words";
                textView.setText(msg);
                //LOAD more pairs of words
                //need more codes
                break;
        }

        for (int x = 0; x < 9; x++) {
            String tvID = "tv" + (x + 1);
            int resID = getResources().getIdentifier(tvID, "id", getPackageName());
            tv[x] = (TextView) findViewById(resID);
        }

        //gridview show words in the database
        //allow user to use
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //based on the position you have to get value
                if (wordsCount >= 9){
                    Toast.makeText(Words_Selection.this, "You already select 9 words. ", Toast.LENGTH_LONG).show();
                }else{
                    selectedItem = gridView.getItemAtPosition(position).toString();
                    if (!isConflict(selectedItem)){
                        wordsList(message,selectedItem);
                        fillTextView(selectedItem);
                        TextView tv = (TextView) gridView.getChildAt(position);
                        tv.setBackgroundColor(Color.RED);
                        tv.setText("");
                    }else{
                        Toast.makeText(Words_Selection.this,"You can't select a word twice.",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        //back to MainActivity
        Button mButton;
        mButton = (Button)findViewById(R.id.start_game);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wordsCount < 9){
                    Toast.makeText(Words_Selection.this, "You MUST select 9 words to start a new game.",Toast.LENGTH_LONG).show();
                }else{
                    setWordsList(message, wordsList_eng,wordsList_span);
                }
            }
        });

    }

    //show which words selected by user in Textview
    public void fillTextView(String item){
        tv[wordsCount].setText(item);
        wordsCount++;
    }

    //make sure every word only collected once
    public Boolean isConflict(String item){
        for (int i = 0; i< wordsCount; i++){
            if (tv[i].getText().equals(item)){
                return true;
            }
        }
        return false;
    }

    //store what user choose
    public void wordsList(String msg, String item){
        switch (msg) {
            case "SPAN":
                wordsList_span[wordsCount] = item;
                for (int i = 0; i < span_wordsList.size(); i++) {
                    if (item.equals(span_wordsList.get(i))) {
                        wordsList_eng[wordsCount] = eng_wordsList.get(i);
                    }
                }
                break;
            case "ENG":
                wordsList_eng[wordsCount] = item;
                for (int i = 0; i < eng_wordsList.size(); i++) {
                    if (item.equals(eng_wordsList.get(i))) {
                        wordsList_span[wordsCount] = span_wordsList.get(i);
                    }
                }
                break;
        }
    }

    //pass words that user select to MainActivity
    private void setWordsList(String msg, String[] eng, String[] span) {
        Intent data = new Intent();
        String[] lists = new String[9];
        for (int i = 0; i < eng.length; i++){
            String tmp = eng[i] + "-" + span[i];
            lists[i] = tmp;
            Log.d(TAG, "TMP is    " + lists[i]);
        }
        data.putExtra("LANGUAGE", msg);
        data.putExtra("EXTRA_WORDS_LIST", lists);
        setResult(RESULT_OK, data);
        finish();
    }


}


