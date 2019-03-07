package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
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

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Words_Selection extends AppCompatActivity {
    private static final String TAG = "Words-Selection";
    private static final String PAGE = "Pages";
    private static final String WORDSLIST = "WORDSPAIRS";
    private static final String WORDSLIST_ENG = "wordsList_eng";
    private static final String WORDSLIST_SPAN = "wordsList_span";
    private static final String MESSAGE_LANGUAGE = "Message_Language";
    private static final String WORDS_COUNT = "Words_Count";
    private static final String GRID_PRE_POSITION = "Pre_Position";
    private static final String IS_SHOWN_FLAG = "isShowFlag";

    //data structure
    ArrayList<String> eng_wordsList = new ArrayList<>();
    ArrayList<String> span_wordsList = new ArrayList<>();
    //for gridview
    ArrayList<String> eng_wordsList_gridview = new ArrayList<>();
    ArrayList<String> span_wordsList_gridview = new ArrayList<>();
    //original data from file or database
    ArrayList<String> strings = new ArrayList<>();
    //monitor pages which may slide by user
    private int numPages;
    private int pages = 1;
    private String selectedItem;
    final TextView[] tv = new TextView[9];
    private int wordsCount = 0;
    //words selected by users will go to MainActivity
    ArrayList<WordsPairs> wordpairs = new ArrayList<>();
    //private static String[] wordsList_eng = new String[9];
    //private static String[] wordsList_span = new String[9];
    //English or Spanish
    private String message;
    //keep track of whether button show/hide is activated
    int[] pre_pos = new int[9];
    //show/hide wrong words from users
    Boolean isShown = false;
    Boolean is_default;

    DBHelper mDBHelper = new DBHelper(this);


    @SuppressLint("Assert")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.words_selection);

        //declare TextView[] for showing words
        for (int x = 0; x < 9; x++) {
            String tvID = "tv" + (x + 1);
            int resID = getResources().getIdentifier(tvID, "id", getPackageName());
            tv[x] = findViewById(resID);
        }

        if (savedInstanceState != null) {
            pages = savedInstanceState.getInt(PAGE);
            Log.d(TAG, "PAGE IS  "+ pages);
            wordpairs = savedInstanceState.getParcelableArrayList(WORDSLIST);
           // wordsList_eng = savedInstanceState.getStringArray(WORDSLIST_ENG);
           // wordsList_span = savedInstanceState.getStringArray(WORDSLIST_SPAN);
            message = savedInstanceState.getString(MESSAGE_LANGUAGE);
            wordsCount = savedInstanceState.getInt(WORDS_COUNT);
            pre_pos = savedInstanceState.getIntArray(GRID_PRE_POSITION);
            isShown = savedInstanceState.getBoolean(IS_SHOWN_FLAG);
            //re-fill TextView with selected words
            for (int i = 0; i < wordsCount; i++){
                if (message.equals("SPAN")){
                    //tv[i].setText(wordsList_span[i]);
                    tv[i].setText(wordpairs.get(i).getSPAN());
                }else{
                    //tv[i].setText(wordsList_eng[i]);
                    tv[i].setText(wordpairs.get(i).getENG());
                }
                Log.d(TAG, "WORD LIST ENG IS "+wordpairs.get(i).getENG());
                Log.d(TAG, "WORD LIST SPAN IS "+wordpairs.get(i).getSPAN());
                //Log.d(TAG, "WORD LIST ENG IS "+wordsList_eng[i]);
               // Log.d(TAG, "WORD LIST SPAN IS "+wordsList_span[i]);
            }
        }
        TextView textView = (TextView)findViewById(R.id.tv_view);
        final GridView gridView = (GridView)findViewById(R.id.selection_view);

        //EXTRA_MESSAGE from Load_Pairs
        //words from users loading
        message = getIntent().getStringExtra(EXTRA_MESSAGE);
        if (message.equals("LOAD")){
            message = getIntent().getStringExtra("LANGUAGE");
            strings = getIntent().getStringArrayListExtra("LOAD_WORDS_LIST");
        }else{
            Uri uri;
            is_default = getIntent().getBooleanExtra("PICK_FILE", true);
            if (is_default){
                //retrieve words from text file wordpairs
                uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.wordpairs);
                try {
                    strings = readTextFromUri(uri);
                    for (int i = 0; i < strings.size(); i++){
                     //   Log.d(TAG,"STRINGS FROM UPLOAD FILE ARE " + strings.get(i));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                //retrieve words from file uploaded by user
                //uri = Uri.fromFile(getFileStreamPath("upload_wordspairs.txt"));
                ArrayList<WordsPairs> arrayList = mDBHelper.getImportedData();
                for (int i = 0; i < arrayList.size(); i++){
                    strings.add(arrayList.get(i).getENG()+","+arrayList.get(i).getSPAN());
                  //  Log.d(TAG, "mDBHELPER database has  " + arrayList.get(i).getENG()+"   "+
                      //      arrayList.get(i).getSPAN()+"  ");
                }
            }
        }
        numPages = strings.size()/30 + 1;
        setWordsList(strings,pages);

        final String msg;
        switch (message) {
            case "SPAN":
                msg = "Select Spanish words ";
                textView.setText(msg);
                gridView.setAdapter(new GridAdapter(span_wordsList_gridview, this));
                break;
            case "ENG":
                msg = "Select English words";
                textView.setText(msg);
                gridView.setAdapter(new GridAdapter(eng_wordsList_gridview, this));
                break;
            case "LOAD":

               break;
        }

        selectWords();
        intentStartGame();
    }


    public void intentStartGame(){
        //back to MainActivity
        Button mButton;
        mButton = (Button)findViewById(R.id.start_game);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wordsCount < 9){
                    Toast.makeText(Words_Selection.this, "You MUST select 9 words to start a new game.",
                            Toast.LENGTH_LONG).show();
                }else{
                    //setStartList(message, wordsList_eng,wordsList_span);
                    setStartList(message, wordpairs);
                }
            }
        });
    }

    //display words in every page
    //every page has 30 words
    //re-load words when user change page
    public void setWordsList(ArrayList<String> strings, int pages) {
        assert strings != null;

        //if has 30 words to get a complete page
        if (strings.size()-30*(pages-1) >= 30){
            int i = 0;
            while (i + (pages - 1) * 30 < 30 * pages){
                String[] words = strings.get(i+(pages-1)*30).split(",");
                if (words.length < 2){
                    break;
                }else{
                    eng_wordsList.add(words[0]);
                    span_wordsList.add(words[1]);
                    eng_wordsList_gridview.add(words[0]);
                    span_wordsList_gridview.add(words[1]);
                    if (isShown) {
                        if (lookForWrongWords(i)){
                            eng_wordsList_gridview.set(i, words[0]+".wrong");
                            span_wordsList_gridview.set(i, words[1]+".wrong");
                        }
                    }
                    //check whether has words selected before
                    //make it ""
                    for (int j = 0; j < wordsCount; j++){
                        //if (wordsList_eng[j].equals(words[0]) || wordsList_span[j].equals(words[1])){
                        if (wordpairs.get(j).getENG().equals(words[0]) || wordpairs.get(j).getSPAN().equals(words[1])){
                            eng_wordsList_gridview.set(i,"");
                            span_wordsList_gridview.set(i,"");
                        }
                    }
                    i++;
                }
            }
        }else{//display rest words if leaving less than 30 words
            int i = 0;
            for (int k = (pages-1)*30; k < strings.size();k++){
                String[] words = strings.get(k).split(",");
                if (words.length < 2){
                    break;
                }else{
                    eng_wordsList.add(words[0]);
                    span_wordsList.add(words[1]);
                    eng_wordsList_gridview.add(eng_wordsList.get(i));
                    span_wordsList_gridview.add(span_wordsList.get(i));
                    if (isShown) {
                        if (lookForWrongWords(i)){
                            eng_wordsList_gridview.set(i, words[0]+".wrong");
                            span_wordsList_gridview.set(i, words[1]+".wrong");
                        }
                    }
                    //check whether has words selected before
                    //make it ""
                    for (int j = 0; j < wordsCount; j++) {
                        //if (wordsList_eng[j].equals(words[0]) || wordsList_span[j].equals(words[1])) {
                        if (wordpairs.get(j).getENG().equals(words[0]) || wordpairs.get(j).getSPAN().equals(words[1])){
                            eng_wordsList_gridview.set(i,"");
                            span_wordsList_gridview.set(i,"");
                        }
                    }

                    i++;
                }
            }
        }

    }

    public void selectWords(){
        //gridview show words in the database
        //allow user to use
        final GridView gridView = (GridView)findViewById(R.id.selection_view);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //based on the position you have to get value
                if (wordsCount >= 9){
                    Toast.makeText(Words_Selection.this, "You already select 9 words. ",
                            Toast.LENGTH_LONG).show();
                }else{
                    //get Gridview ID
                    //retrieve TextView data through GridView ID
                    //pass data to TextView tv
                    //display to user which words they already select
                    //prevent user re-select same word
                    selectedItem = gridView.getItemAtPosition(position).toString();
                    if (!isConflict(selectedItem) && !selectedItem.equals("")){
                        //show which words selected by user in Textview
                        Log.d(TAG, "SELECTED ITEM IS "+ selectedItem);
                        if (selectedItem.contains(".wrong")){
                            String str = selectedItem.replace(selectedItem.substring(selectedItem.length()-6), "");
                            Log.d(TAG,"STR STR STR STR IS  "+ str);
                            tv[wordsCount].setText(str);
                            wordsList(message,str);
                        }else{
                            tv[wordsCount].setText(selectedItem);
                            wordsList(message,selectedItem);
                        }
                        tv[wordsCount].setGravity(Gravity.CENTER);
                        //keep track of position in which words are selected
                        pre_pos[wordsCount] = position + (pages-1) * 30;
                        wordsCount++;
                        TextView tv = (TextView) gridView.getChildAt(position);
                        tv.setText("");
                    }else{
                        Toast.makeText(Words_Selection.this,"You can't select a word twice.",
                                Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        //UNDO button can delete last word selected by user
        final Button undo = (Button) findViewById(R.id.undo);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wordsCount == 0){
                    Toast.makeText(Words_Selection.this, "There is no words selected now. ",
                            Toast.LENGTH_LONG).show();
                }else{
                    wordsCount--;
                    String tmp = tv[wordsCount].getText().toString();
                    tv[wordsCount].setText("");
                    wordpairs.remove(wordsCount);
                    //wordsList_eng[wordsCount] = null;
                    //wordsList_span[wordsCount] = null;
                    int current_page = pre_pos[wordsCount]/30 + 1;
                    int current_pos = pre_pos[wordsCount] - (pages -1) * 30;
                    TextView undo_tv = (TextView) gridView.getChildAt(current_pos);
                    if (pages == current_page){
                        eng_wordsList_gridview.set(current_pos,tmp);
                        span_wordsList_gridview.set(current_pos, tmp);
                        if (isShown){
                            if (lookForWrongWords(pre_pos[wordsCount] - (pages -1) * 30) ){
                                if (tmp.contains(".wrong")){
                                    undo_tv.setText(tmp.replace(tmp.substring(tmp.length()-6), ""));
                                    undo_tv.setTextColor(Color.RED);
                                }else{
                                    undo_tv.setText(tmp);
                                    undo_tv.setTextColor(Color.RED);
                                }
                            }else{
                                undo_tv.setText(tmp);
                                undo_tv.setTextColor(Color.parseColor("#FF008577"));
                            }
                        }else{
                            undo_tv.setText(tmp);
                            undo_tv.setTextColor(Color.parseColor("#FF008577"));
                        }
                        Log.d(TAG, "PAGE is  " + pages);
                        Log.d(TAG, "TMP IN UNDO IS "+ tmp);
                    }
                }
            }
        });

        final Button next = (Button) findViewById(R.id.next);
        final Button prev = (Button) findViewById(R.id.prev);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pages < numPages){
                    prev.setEnabled(true);
                    eng_wordsList.clear();
                    span_wordsList.clear();
                    eng_wordsList_gridview.clear();
                    span_wordsList_gridview.clear();
                    pages++;
                    setWordsList(strings,pages);
                    switch (message){
                        case "SPAN":
                            gridView.setAdapter(new GridAdapter(span_wordsList_gridview, Words_Selection.this));
                            break;
                        case "ENG":
                            gridView.setAdapter(new GridAdapter(eng_wordsList_gridview, Words_Selection.this));
                            break;
                    }
                }else{
                    next.setEnabled(false);
                }
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pages <= 1){
                    prev.setEnabled(false);
                }else {
                    next.setEnabled(true);
                    eng_wordsList.clear();
                    span_wordsList.clear();
                    eng_wordsList_gridview.clear();
                    span_wordsList_gridview.clear();
                    pages--;
                    setWordsList(strings,pages);
                    switch (message){
                        case "SPAN":
                            gridView.setAdapter(new GridAdapter(span_wordsList_gridview, Words_Selection.this));
                            break;
                        case "ENG":
                            gridView.setAdapter(new GridAdapter(eng_wordsList_gridview, Words_Selection.this));
                            break;
                    }
                }
            }
        });

        //show/hide previous wrong words
        //if user click show, it will color all wrong words in this page to be red
        Button button = (Button)findViewById(R.id.button_words);
        String bmsg = null;
        if (isShown){
            bmsg = "Hide My words";
        }else{
            bmsg = "Show My words";
        }
        button.setText(bmsg);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShown){
                    if (mDBHelper.getData().isEmpty()) {
                        isShown = false;
                        Toast.makeText(Words_Selection.this, "You DON'T have any words in My WORDS",
                                Toast.LENGTH_LONG).show();
                    }else{
                        isShown = true;
                        Button button = (Button)findViewById(R.id.button_words);
                        button.setText("Hide My words");
                        for (int i = 0; i < span_wordsList.size(); i++){
                            if (lookForWrongWords(i)){
                                TextView tv = (TextView) gridView.getChildAt(i);
                                tv.setTextColor(Color.RED);
                            }
                        }
                    }
                }else{
                    isShown = false;
                    Button button = (Button)findViewById(R.id.button_words);
                    button.setText("Show My words");
                    for (int i = 0; i < span_wordsList.size(); i++){
                        TextView tv = (TextView) gridView.getChildAt(i);
                        tv.setTextColor(Color.parseColor("#FF008577"));
                    }
                }
            }
        });

    }

    public boolean lookForWrongWords(int i){
        ArrayList<WordsPairs> arrayList = mDBHelper.getData();
        if (message.equals("SPAN")){
            for (int j = 0; j < arrayList.size(); j++){
                if (arrayList.get(j).getSPAN().equals(span_wordsList.get(i))){
                    return true;
                }
            }
        }else{
            for (int j = 0; j < arrayList.size(); j++){
                if (arrayList.get(j).getENG().equals(eng_wordsList.get(i))){
                    return true;
                }
            }
        }
        return false;
    }

    //make sure every word only collected once
    public Boolean isConflict(String item){
        for (int i = 0; i< wordsCount; i++){
            String tmp = tv[i].getText().toString();
            if (tmp.equals(item) || item.contains(tmp)){
                return true;
            }
        }
        return false;
    }

    //store what user choose
    public void wordsList(String msg, String item){
        switch (msg) {
            case "SPAN":
                String eng = null;
                //wordsList_span[wordsCount] = item;
                for (int i = 0; i < span_wordsList.size(); i++) {
                    if (item.equals(span_wordsList.get(i))) {
                        //wordsList_eng[wordsCount] = eng_wordsList.get(i);
                        eng = eng_wordsList.get(i);
                    }
                }
                wordpairs.add(new WordsPairs(eng, item));
                break;
            case "ENG":
                String span = null;
                //wordsList_eng[wordsCount] = item;
                for (int i = 0; i < eng_wordsList.size(); i++) {
                    if (item.equals(eng_wordsList.get(i))) {
                        //wordsList_span[wordsCount] = span_wordsList.get(i);
                        span = span_wordsList.get(i);
                    }
                }
                wordpairs.add(new WordsPairs(item, span));
                break;
        }
    }

    //pass words that user select to MainActivity
    private void setStartList(String msg, ArrayList<WordsPairs> words) {
        Intent data = new Intent();
        String[] lists = new String[9];
       /* for (int i = 0; i < eng.length; i++){
            String tmp = eng[i] + "-" + span[i];
            lists[i] = tmp;
            Log.d(TAG, "TMP is    " + lists[i]);
        }*/

        data.putExtra("LANGUAGE", msg);
        //data.putExtra("EXTRA_WORDS_LIST", lists);
        data.putExtra("EXTRA_WORDS_LIST", words);
        for (int i = 0; i < 9; i++) {
            Log.d(TAG, "Words ing selection ENG and SPAN are " + words.get(i).getENG() + "  " +words.get(i).getSPAN()); Log.d(TAG, "Words from selection LANGUAGE msg is " + msg);
        }
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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(PAGE,pages);
        savedInstanceState.putParcelableArrayList(WORDSLIST, wordpairs);
        savedInstanceState.putString(MESSAGE_LANGUAGE,message);
        savedInstanceState.putInt(WORDS_COUNT,wordsCount);
        savedInstanceState.putIntArray(GRID_PRE_POSITION, pre_pos);
        savedInstanceState.putBoolean(IS_SHOWN_FLAG, isShown);
    }

    @Override public void onDestroy() {
        mDBHelper.close();
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}
