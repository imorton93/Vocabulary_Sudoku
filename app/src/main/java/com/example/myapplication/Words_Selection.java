package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Words_Selection extends AppCompatActivity {
    private static final String TAG = "Words-Selection";
    private static final int READ_REQUEST_CODE = 100;

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
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.wordpairs);
        ArrayList<String> strings = null;
        try {
            strings = readTextFromUri(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert strings != null;
        for (int i = 0; i < strings.size(); i++){
            String[] words = strings.get(i).split("-");
            eng_wordsList.add(words[0]);
            span_wordsList.add(words[1]);
            Log.d(TAG,"LIST ENG is "+eng_wordsList.get(i));
            Log.d(TAG,"LIST SPAN is "+span_wordsList.get(i));
        }

        TextView textView = (TextView)findViewById(R.id.tv_view);
        Button button = (Button)findViewById(R.id.button_words);
        final GridView gridView = (GridView)findViewById(R.id.selection_view);
        message = getIntent().getStringExtra(EXTRA_MESSAGE);
        final String msg;
        String bmsg = "Select from wrong words";
        switch (message) {
            case "SPAN":
                msg = "Select Spanish words ";
                textView.setText(msg);
                button.setText(bmsg);
                gridView.setAdapter(new GridAdapter(span_wordsList, this));
                selectWords();
                intentStartGame();
                break;
            case "ENG":
                msg = "Select English words";
                textView.setText(msg);
                button.setText(bmsg);
                gridView.setAdapter(new GridAdapter(eng_wordsList, this));
                selectWords();
                intentStartGame();
                break;
            default:
                setContentView(R.layout.load_pairs);
                msg = "Type words into cells";
                textView.setText(msg);
                Button button_load = (Button)findViewById(R.id.button_load);
                button_load.setText("Load Words from a Chapter of Book");
                button_load.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("*/*");
                        startActivityForResult(Intent.createChooser(intent,"select file") , READ_REQUEST_CODE);
                    }
                });

                ArrayList<WordsPairs>arrayList= new ArrayList<>();

                for (int i = 0; i < 9; i++) {
                    WordsPairs list=new WordsPairs();
                    list.setENG("");
                    list.setSPAN("");
                    arrayList.add(list);
                }

                final ListView listView = (ListView)findViewById(R.id.words_eng_list);
                listView.setAdapter(new listArrayAdapter(this,arrayList));

                //back to MainActivity
                Button back = (Button)findViewById(R.id.back);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Words_Selection.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

                //allow users to type in more words
                //more codes coming
                //store data into database
                Button more = (Button)findViewById(R.id.more);
                more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<WordsPairs>arrayList= new ArrayList<>();

                        for (int i = 0; i < 9; i++) {
                            WordsPairs list=new WordsPairs();
                            list.setENG("");
                            list.setSPAN("");
                            arrayList.add(list);
                        }

                        final ListView listView = (ListView)findViewById(R.id.words_eng_list);
                        listView.setAdapter(new listArrayAdapter(Words_Selection.this,arrayList));
                    }
                });

                //Button to set up a Eng game with words that users type in
                //more codes coming
                //store data into database
                Button back_fill_span = (Button) findViewById(R.id.back_fill_span);
                back_fill_span.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setContentView(R.layout.words_selection);
                        message = "SPAN";
                        final GridView gridView = (GridView)findViewById(R.id.selection_view);
                        gridView.setAdapter(new GridAdapter(span_wordsList, Words_Selection.this));
                        selectWords();
                        intentStartGame();
                    }
                });

                //Button to set up a Span game with words that users type in
                //more codes coming
                //store data into database
                Button back_fill_eng = (Button) findViewById(R.id.back_fill_eng);
                back_fill_eng.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setContentView(R.layout.words_selection);
                        message = "ENG";
                        final GridView gridView = (GridView)findViewById(R.id.selection_view);
                        gridView.setAdapter(new GridAdapter(eng_wordsList, Words_Selection.this));
                        selectWords();
                        intentStartGame();
                    }
                });
                //LOAD more pairs of words

                //need more codes
                break;
        }

    }

    public void intentStartGame(){
        //back to MainActivity
        Button mButton;
        mButton = (Button)findViewById(R.id.start_game);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wordsCount < 9){
                    Toast warn = Toast.makeText(Words_Selection.this, "You MUST select 9 words to start a new game.",Toast.LENGTH_LONG);
                    warn.setGravity(Gravity.TOP, 0, 400);
                    warn.show();
                }else{
                    setWordsList(message, wordsList_eng,wordsList_span);
                }
            }
        });

    }

    public void selectWords(){
        for (int x = 0; x < 9; x++) {
            String tvID = "tv" + (x + 1);
            int resID = getResources().getIdentifier(tvID, "id", getPackageName());
            tv[x] = (TextView) findViewById(resID);
        }
        //gridview show words in the database
        //allow user to use
        final GridView gridView = (GridView)findViewById(R.id.selection_view);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //based on the position you have to get value
                if (wordsCount >= 9){
                    Toast warn2 = Toast.makeText(Words_Selection.this, "You already select 9 words. ", Toast.LENGTH_LONG);
                    warn2.setGravity(Gravity.TOP, 0, 400);
                    warn2.show();
                }else{
                    //get Gridview ID
                    //retrieve TextView data through GridView ID
                    //pass data to TextView tv
                    //display to user which words they already select
                    //prevent user re-select same word
                    selectedItem = gridView.getItemAtPosition(position).toString();
                    if (!isConflict(selectedItem)){
                        wordsList(message,selectedItem);
                        fillTextView(selectedItem);
                        TextView tv = (TextView) gridView.getChildAt(position);
                        tv.setBackgroundColor(Color.RED);
                        tv.setText("");
                    }else{
                        Toast warn3 = Toast.makeText(Words_Selection.this,"You can't select a word twice.",Toast.LENGTH_LONG);
                        warn3.setGravity(Gravity.TOP, 0, 400);
                        warn3.show();
                    }

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

    //read file
    private ArrayList<String> readTextFromUri(Uri uri) throws IOException {
        ArrayList<String> strings = new ArrayList<>();
        InputStream inputStream = getContentResolver().openInputStream(uri);
        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
        String eachline = null;
        try {
            eachline = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (eachline != null) {
            strings.add(eachline);
            try {
                eachline = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return strings;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                ArrayList<String> fileContent = new ArrayList<>();
                try {
                    fileContent = readTextFromUri(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "Uri: " + uri.toString());

                if(fileContent.size() < 9){
                    Toast.makeText(this, "Can't Start a Game With Less 9 Words",
                    Toast.LENGTH_LONG).show();
                }else{
                    //ListView initial
                    ArrayList<WordsPairs>arrayList= new ArrayList<>();

                    eng_wordsList.clear();
                    span_wordsList.clear();

                    for (int i = 0; i < fileContent.size(); i++){
                        WordsPairs list=new WordsPairs();
                        String[] words = fileContent.get(i).split("\\s+");
                        if (words.length == 1){
                            Toast.makeText(this, "Can't Read This Flie, Make Sure It is From the Chapter of Book.",
                                    Toast.LENGTH_LONG).show();
                            list.setENG("");
                            list.setSPAN("");
                            arrayList.add(list);
                        }else{
                            list.setENG(words[0]);
                            list.setSPAN(words[1]);
                            arrayList.add(list);
                        }
                        eng_wordsList.add(words[0]);
                        span_wordsList.add(words[1]);
                    }

                    ListView listView = (ListView)findViewById(R.id.words_eng_list);
                    listView.setAdapter(new listArrayAdapter(this,arrayList));

                }
            }
        }
    }

}
