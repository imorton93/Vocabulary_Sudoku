package controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.example.myapplication.R;

import Model.DBHelper;
import View.GridAdapter;
import Model.WordsPairs;

import static android.app.PendingIntent.getActivity;
import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Words_Selection extends AppCompatActivity {
    private static final String TAG = "Words-Selection";
    private static final String PAGE = "Pages";
    private static final String WORDSLIST = "WORDSPAIRS";
    private static final String MESSAGE_LANGUAGE = "Message_Language";
    private static final String WORDS_COUNT = "Words_Count";
    private static final String GRID_PRE_POSITION = "Pre_Position";
    private static final String IS_SHOWN_FLAG = "isShowFlag";
    private static final String KEY_GRID_SIZE = "grid_size";

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
    TextView[] tv = null;
    private int wordsCount = 0;
    //words selected by users will go to MainActivity
    ArrayList<WordsPairs> wordpairs = new ArrayList<>();
    //private static String[] wordsList_eng = new String[9];
    //private static String[] wordsList_span = new String[9];
    //English or Spanish
    private String message;
    //keep track of whether button show/hide is activated
    int[] pre_pos;
    //show/hide wrong words from users
    Boolean isShown = false;
    private int gridSize;
    private boolean listen_mode = false; //Checks if the app is in listen comprehension mode

    DBHelper mDBHelper = new DBHelper(this);


    @SuppressLint("Assert")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.words_selection);

        //grid size
        gridSize = getIntent().getIntExtra(KEY_GRID_SIZE, 9);
        message = getIntent().getStringExtra(EXTRA_MESSAGE);
        listen_mode = getIntent().getBooleanExtra("LISTEN_MODE",false);
        tv = new TextView[gridSize];
        pre_pos = new int[gridSize];

        textViewSpawn();


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
        Uri uri;
        String filename = getIntent().getStringExtra("PICK_FILE");
        if (filename.equals("default")){
            //retrieve words from text file wordpairs
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.wordpairs);
        }else{
            //retrieve words from file uploaded by user
            uri = Uri.fromFile(getFileStreamPath(filename));
        }
        try {
            strings = readTextFromUri(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        numPages = strings.size()/36 + 1;
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
        }

        selectWords();
        intentStartGame();
    }


    public void textViewSpawn(){
        //declare TextView[] for showing words
        //programmatically spawn textview according to different size of Sudoku
        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid_layout);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int row = (int)Math.sqrt(gridSize);
        int col = gridSize/row;

        Log.d(TAG, "(getResources().getConfiguration().screenLayout &\n" +
                "                    Configuration.SCREENLAYOUT_SIZE_MASK) is " +(getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK));
        Log.d(TAG, "metrics.densityDpi " +metrics.densityDpi);
        Log.d(TAG, "(getResources().getConfiguration()) " +(getResources().getConfiguration().smallestScreenWidthDp));


        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayout.setColumnCount(col);
            gridLayout.setRowCount(row);
            for (int i = 0; i < gridSize; i++){
                tv[i] = new TextView(this);
                tv[i].setText("");
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                if ((getResources().getConfiguration().smallestScreenWidthDp) < 360){
                    params.width = 95;
                    params.height = 45;
                }else{
                    if (metrics.densityDpi < 400 && (getResources().getConfiguration().screenLayout &
                            Configuration.SCREENLAYOUT_SIZE_MASK) <= 2){
                        params.width = 155;
                        params.height = 65;
                    }else if (metrics.densityDpi >= 560) {
                        params.width = 295;
                        params.height = 125;
                    }else {
                        if (metrics.densityDpi < 200 && (getResources().getConfiguration().screenLayout &
                                Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE){
                            params.width = 125;
                            params.height = 55;
                        }else if (metrics.densityDpi < 300 && (getResources().getConfiguration().screenLayout &
                                Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE){
                            params.width = 145;
                            params.height = 65;
                        }else{
                            params.width = 225;
                            params.height = 95;
                        }
                    }
                }

                tv[i].setLayoutParams(params);
                tv[i].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
                tv[i].setBackgroundColor(Color.GRAY);
                tv[i].setTextColor(Color.WHITE);
                if ( metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {
                    tv[i].setTextSize(20);
                }
                if(metrics.densityDpi < 400 && (getResources().getConfiguration().screenLayout &
                        Configuration.SCREENLAYOUT_SIZE_MASK) <= 2){
                    tv[i].setTextSize(12);
                }
                gridLayout.addView(tv[i]);
            }
        }else{
            if ((getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK) ==
                    Configuration.SCREENLAYOUT_SIZE_XLARGE){
                gridLayout.setColumnCount(col);
                gridLayout.setRowCount(row);
                for (int i = 0; i < gridSize; i++){
                    tv[i] = new TextView(this);
                    tv[i].setText("");
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = 225;
                    params.height = 95;
                    tv[i].setLayoutParams(params);
                    tv[i].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
                    tv[i].setBackgroundColor(Color.GRAY);
                    tv[i].setTextColor(Color.WHITE);
                    tv[i].setTextSize(20);
                    gridLayout.addView(tv[i]);
                }
            }else{
                if (gridSize == 12 && metrics.densityDpi < 300 && (getResources().getConfiguration().screenLayout &
                        Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE){
                    gridLayout.setColumnCount(gridSize/2);
                }else{
                    gridLayout.setColumnCount(gridSize);
                }
                for (int i = 0; i < gridSize; i++) {
                    tv[i] = new TextView(this);
                    tv[i].setText("");
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    if ((getResources().getConfiguration().smallestScreenWidthDp) < 360){
                        params.width = 65;
                        params.height = 45;
                    }else{
                        if(gridSize == 12){
                            if (metrics.densityDpi < 200 && (getResources().getConfiguration().screenLayout &
                                    Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE){
                                params.width = 85;
                                params.height = 30;
                            }else if (metrics.densityDpi < 300 && (getResources().getConfiguration().screenLayout &
                                    Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE){
                                params.width = 115;
                                params.height = 45;
                            }else{
                                params.width = 145;
                                params.height = 90;
                            }
                        }else if(metrics.densityDpi < 400 && (getResources().getConfiguration().screenLayout &
                                Configuration.SCREENLAYOUT_SIZE_MASK) <= 2) {
                            params.width = 115;
                            params.height = 50;
                        }else if (metrics.densityDpi >= 560) {
                            params.width = 225;
                            params.height = 105;
                        }else{
                            if (metrics.densityDpi < 200 && (getResources().getConfiguration().screenLayout &
                                    Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE){
                                params.width = 95;
                                params.height = 35;
                            }else if (metrics.densityDpi < 300 && (getResources().getConfiguration().screenLayout &
                                    Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE){
                                params.width = 115;
                                params.height = 45;
                            }else{
                                params.width = 175;
                                params.height = 90;
                            }
                        }
                    }

                    tv[i].setLayoutParams(params);
                    tv[i].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
                    tv[i].setBackgroundColor(Color.GRAY);
                    tv[i].setTextColor(Color.WHITE);
                    if ( metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {
                        tv[i].setTextSize(17);
                    }
                    if(metrics.densityDpi < 400 && (getResources().getConfiguration().screenLayout &
                            Configuration.SCREENLAYOUT_SIZE_MASK) <= 2){
                        tv[i].setTextSize(11);
                    }
                    gridLayout.addView(tv[i]);
                }
            }
        }
    }

    public void intentStartGame(){
        //back to MainActivity
        Button mButton;
        mButton = (Button)findViewById(R.id.start_game);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wordsCount < gridSize){
                    Toast.makeText(Words_Selection.this, "You MUST select " +gridSize+" words to start a new game.",
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
        if (strings.size()-36*(pages-1) >= 36){
            int i = 0;
            while (i + (pages - 1) * 36 < 36 * pages){
                String[] words = strings.get(i+(pages-1)*36).split(",");
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
            for (int k = (pages-1)*36; k < strings.size();k++){
                String[] words = strings.get(k).split(",");
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
                if (wordsCount >= gridSize){
                    Toast.makeText(Words_Selection.this, "You already select " + gridSize + " words. ",
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
                        if (message.equals("ENG")){
                            Log.d(TAG,"STR STR STR STR span IS  "+ span_wordsList.get(position));
                        }else{
                            Log.d(TAG,"STR STR STR STR eng  IS  "+ eng_wordsList.get(position));
                        }
                        if (selectedItem.contains(".wrong")){
                            String str = selectedItem.replace(selectedItem.substring(selectedItem.length()-6), "");
                            Log.d(TAG,"STR STR STR STR IS  "+ str);
                            if (message.equals("ENG")){
                                Log.d(TAG,"STR STR STR STR span IS  "+ span_wordsList.get(position));
                            }else{
                                Log.d(TAG,"STR STR STR STR eng  IS  "+ eng_wordsList.get(position));
                            }
                            tv[wordsCount].setText(str);
                            //store what user choose
                            if (message.equals("ENG")){
                                wordpairs.add(new WordsPairs(str, span_wordsList.get(position)));
                            }else{
                                wordpairs.add(new WordsPairs(eng_wordsList.get(position), str));
                            }
                        }else{
                            tv[wordsCount].setText(selectedItem);

                            //store what user choose
                            if (message.equals("ENG")){
                                wordpairs.add(new WordsPairs(selectedItem, span_wordsList.get(position)));
                            }else{
                                wordpairs.add(new WordsPairs(eng_wordsList.get(position), selectedItem));
                            }
                        }
                        tv[wordsCount].setGravity(Gravity.CENTER);
                        //keep track of position in which words are selected
                        pre_pos[wordsCount] = position + (pages-1) * 36;
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
                    String tmp_translate;
                    if(message.equals("ENG")){
                        tmp_translate = wordpairs.get(wordsCount).getENG();
                    }else{
                        tmp_translate = wordpairs.get(wordsCount).getSPAN();
                    }
                    tv[wordsCount].setText("");
                    wordpairs.remove(wordsCount);
                    //wordsList_eng[wordsCount] = null;
                    //wordsList_span[wordsCount] = null;
                    int current_page = pre_pos[wordsCount]/36 + 1;
                    int current_pos = pre_pos[wordsCount] - (pages -1) * 36;
                    TextView undo_tv = (TextView) gridView.getChildAt(current_pos);
                    if (pages == current_page){
                        if(message.equals("SPAN")){
                            span_wordsList_gridview.set(current_pos, tmp);
                            eng_wordsList_gridview.set(current_pos, tmp_translate);
                        }else{
                            span_wordsList_gridview.set(current_pos, tmp_translate);
                            eng_wordsList_gridview.set(current_pos, tmp);
                        }
                        eng_wordsList_gridview.set(current_pos,tmp);
                        if (isShown){
                            if (lookForWrongWords(pre_pos[wordsCount] - (pages -1) * 36) ){
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
                    if (pages == numPages){
                        next.setEnabled(false);
                    }
                }
            }
        });

        if(pages <= 1){
            prev.setEnabled(false);
        }
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pages >= 1){
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
                    if(pages <= 1){
                        prev.setEnabled(false);
                    }
                }
            }
        });

        //show/hide previous wrong words
        //if user click show, it will color all wrong words in this page to be red
        Button button = (Button)findViewById(R.id.button_words);
        String bmsg = null;
        if (isShown){
            bmsg = "Hide Difficult Words";
        }else{
            bmsg = "Show Difficult Words";
        }
        button.setText(bmsg);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShown){
                    if (mDBHelper.getData().isEmpty()) {
                        isShown = false;
                        Toast.makeText(Words_Selection.this, "You DON'T have any words in this words list",
                                Toast.LENGTH_LONG).show();
                    }else{
                        isShown = true;
                        Toast.makeText(Words_Selection.this, "That words got wrong in your previous game will " +
                                "show in RED COLOR", Toast.LENGTH_LONG).show();
                        //make difficult words in front
                        /*    ArrayList<WordsPairs> wrongList = mDBHelper.getData();
                        ArrayList<String> wrong = new ArrayList<>();
                        for(int i = 0; i < wrongList.size();i++){
                            wrong.add(wrongList.get(i).getENG()+","+wrongList.get(i).getSPAN());
                        }
                        while (wrong.size() < strings.size()){
                            for(int i = 0; i < eng_wordsList.size(); i++){
                                if(!lookForWrongWords(i)){
                                    wrong.add(strings.get(i));
                                }
                            }
                        }
                        strings = wrong;
                        eng_wordsList.clear();
                        span_wordsList.clear();
                        eng_wordsList_gridview.clear();
                        span_wordsList_gridview.clear();
                        setWordsList(strings,pages);
                        switch (message){
                            case "SPAN":
                                gridView.setAdapter(new GridAdapter(span_wordsList_gridview, Words_Selection.this));
                                break;
                            case "ENG":
                                gridView.setAdapter(new GridAdapter(eng_wordsList_gridview, Words_Selection.this));
                                break;
                        }*/
                        int count = 0;
                        Button button = (Button)findViewById(R.id.button_words);
                        button.setText("Hide Difficult Words");
                        for (int i = 0; i < span_wordsList.size(); i++){
                            if (lookForWrongWords(i)){
                                final TextView textview = (TextView) gridView.getChildAt(i);
                                textview.setTextColor(Color.RED);
                                count++;
                            }
                        }
                        if (count == 0){
                            isShown = false;
                            Toast.makeText(Words_Selection.this, "You DON'T have any words in this words list",
                                    Toast.LENGTH_LONG).show();
                            Button b = (Button)findViewById(R.id.button_words);
                            b.setText("Show Difficult Words");
                        }
                    }
                }else{
                    isShown = false;
                    Button button = (Button)findViewById(R.id.button_words);
                    button.setText("Show Difficult Words");
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
           /* if (arrayList.get(i).getTotal() > 0){
                return true;
            } */

            for (int j = 0; j < arrayList.size(); j++) {
                if (arrayList.get(j).getSPAN().equals(span_wordsList.get(i))) {
                    if (arrayList.get(j).getTotal() > 0) {
                        return true;
                    }
                }
            }
        }else{
            /*
            if (arrayList.get(i).getTotal() > 0){
                return true;
            }
            */

            for (int j = 0; j < arrayList.size(); j++){
                if (arrayList.get(j).getENG().equals(eng_wordsList.get(i))) {
                    if (arrayList.get(j).getTotal() > 0) {
                        return true;
                    }
                }
            }

        }
        return false;

    }

    //make sure every word only collected once
    public Boolean isConflict(String item){
        for (int i = 0; i< wordsCount; i++){
            String tmp = tv[i].getText().toString();
            if (tmp.equals(item) || item.equals(tmp+".wrong")){
                return true;
            }
        }
        return false;
    }


    //pass words that user select to MainActivity
    private void setStartList(String msg, ArrayList<WordsPairs> words) {
        Intent data = new Intent();
        data.putExtra("LANGUAGE", msg);
        //data.putExtra("EXTRA_WORDS_LIST", lists);
        data.putParcelableArrayListExtra("EXTRA_WORDS_LIST", words);
        data.putExtra("LISTEN_MODE", listen_mode);
        for (int i = 0; i < gridSize; i++) {
            Log.d(TAG, "Words ing selection ENG and SPAN are " + words.get(i).getENG() + "  " +words.get(i).getSPAN());
            Log.d(TAG, "Words in selection LANGUAGE msg is " + msg);
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
