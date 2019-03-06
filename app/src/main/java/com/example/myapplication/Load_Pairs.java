package com.example.myapplication;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static java.lang.Integer.max;

public class Load_Pairs extends AppCompatActivity {


    private static final int READ_REQUEST_CODE = 100;
    private static final String ENG_WORDS_LIST = "englishlist";
    private static final String SPAN_WORDS_LIST = "spanishlist";
    private static final String TAG = "LOAD_WORDS";

    ArrayList<String> eng_wordsList = new ArrayList<>();
    ArrayList<String> span_wordsList = new ArrayList<>();
    ArrayList<String> strings = null;
    private String message;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_pairs);

        if (savedInstanceState != null) {
            eng_wordsList = savedInstanceState.getStringArrayList(ENG_WORDS_LIST);
            span_wordsList = savedInstanceState.getStringArrayList(SPAN_WORDS_LIST);
        }

        //set up ListView
        ArrayList<WordsPairs> arrayList= new ArrayList<>();
        final ListView listView = (ListView)findViewById(R.id.words_eng_list);
        if (eng_wordsList.isEmpty()){
            for (int i = 0; i < 9; i++) {
                WordsPairs list=new WordsPairs();
                list.setENG("");
                list.setSPAN("");
                arrayList.add(list);
            }

        }else{
            for (int i = 0; i < eng_wordsList.size(); i++){
                WordsPairs list=new WordsPairs();
                list.setENG(eng_wordsList.get(i));
                list.setSPAN(span_wordsList.get(i));
                arrayList.add(list);
            }
        }
        listView.setAdapter(new listArrayAdapter(this,arrayList));


        final String msg;
        TextView textView = (TextView)findViewById(R.id.tv_view);
        msg = "Type words into cells";
        textView.setText(msg);
        Button button_load = (Button)findViewById(R.id.button_load);
        button_load.setText("Load Words from a Chapter of Book");
        button_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ask to open a file to get data
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent , READ_REQUEST_CODE);
            }
        });

        //back to MainActivity
        Button back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Load_Pairs.this, MainActivity.class);
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
                listView.setAdapter(new listArrayAdapter(Load_Pairs.this,arrayList));
            }
        });

        //Button to set up a Eng game with words that users type in
        //more codes coming
        //store data into database
        Button back_fill_span = (Button) findViewById(R.id.back_fill_span);
        back_fill_span.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = "SPAN";
                setWordsList(message,eng_wordsList,span_wordsList);
            }
        });

        //Button to set up a Span game with words that users type in
        //more codes coming
        //store data into database
        Button back_fill_eng = (Button) findViewById(R.id.back_fill_eng);
        back_fill_eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = "ENG";
                setWordsList(message,eng_wordsList,span_wordsList);
            }
        });

        //Button save to save a file with uploaded words from users
        final Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUploadedFile(eng_wordsList,span_wordsList);
            }
        });
        //LOAD more pairs of words
        //need more codes

    }

    //pass words that user select to Words_Selection
    private void setWordsList(String msg, ArrayList<String> eng, ArrayList<String> span) {
        Intent data = new Intent(this,Words_Selection.class);
        ArrayList<String> lists = new ArrayList<>();
        for (int i = 0; i < eng.size(); i++){
            String tmp = eng.get(i) + "," + span.get(i);
            lists.add(tmp);
            Log.d(TAG, "TMP is    " + lists.get(i));
        }
        data.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        data.putExtra(EXTRA_MESSAGE, "LOAD");
        //pass "ENG" or "SPAN" back
        data.putExtra("LANGUAGE", msg);
        //pass what user loaded
        data.putStringArrayListExtra("LOAD_WORDS_LIST", lists);
        Log.d(TAG, "DATA is    " + data.getStringArrayListExtra("LOAD_WORDS_LIST"));
        startActivity(data);
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

    //read file
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        // The ACTION_VIEW intent was sent with the request code
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
                String mimeType = null;
                //get external file's extensive
                //make sure the file is .txt or .csv
                //if not, set an Error
                assert uri != null;
                if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
                    ContentResolver cr = this.getContentResolver();
                    mimeType = cr.getType(uri);
                } else {
                    String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                            .toString());
                    mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                            fileExtension.toLowerCase());
                }
                Log.d(TAG, "FILE EXTENSION IS " + uri);

                Log.d(TAG, "MIMIETYPE IS " + mimeType);
                assert mimeType != null;
                if (mimeType.equals("text/csv") || mimeType.equals("text/plain")){
                    try {
                        strings = readTextFromUri(uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //ListView initial
                    //set up a new ListView
                    ArrayList<WordsPairs>arrayList= new ArrayList<>();

                    for (int i = 0; i < strings.size(); i++){
                        WordsPairs list=new WordsPairs();
                        String[] words = strings.get(i).split(",");
                        //read line to get words list
                        //check if data is ok to assign to variables
                        if (words.length != 2){
                            Toast.makeText(this, "Can't Read This File, Make Sure It is From the Chapter of Book.",
                                    Toast.LENGTH_LONG).show();
                            for (int k = 0; k < 9; k++) {
                                list.setENG("");
                                list.setSPAN("");
                                arrayList.add(list);
                            }
                            break;
                        }else{
                            list.setENG(words[0]);
                            Log.d(TAG,"WORD is "+words[0]);
                            list.setSPAN(words[1]);
                            arrayList.add(list);
                            eng_wordsList.add(words[0]);
                            span_wordsList.add(words[1]);
                        }
                    }

                    ListView listView = (ListView)findViewById(R.id.words_eng_list);
                    listView.setAdapter(new listArrayAdapter(this,arrayList));
                }else{
                    Toast.makeText(this, "Can't Open this type of file",
                            Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    //save what users upload
    //initial a alert dialog to make sure if user wants to save these words
    //open database
    //save words into a table
    public void saveUploadedFile(final ArrayList<String> eng, final ArrayList<String> span) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do you want to save your loaded words?");
        alertDialogBuilder.setPositiveButton(
                "yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //create a file located in internal storage
                      /*  try {
                            FileOutputStream fOut = openFileOutput("upload_wordspairs.txt",MODE_PRIVATE);
                            for (int i = 0; i < strings.size(); i++){
                                String tmp = strings.get(i) + "\n";
                                fOut.write(tmp.getBytes());
                            }
                            fOut.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/

                        if (eng.size() == 0 || span.size() == 0){
                            Toast.makeText(Load_Pairs.this,
                                    "Cannot save.", Toast.LENGTH_LONG).show();
                        }else{
                            //save to database
                            //
                            DBHelper mDB = new DBHelper(Load_Pairs.this);
                           // mDB.onDestroy();
                            for (int i = 0; i < Math.min(eng.size(),span.size()); i++){
                                if (eng.get(i).isEmpty() || span.get(i).isEmpty()){
                                    Toast.makeText(Load_Pairs.this,
                                            "Cannot save.", Toast.LENGTH_LONG).show();
                                }else{
                                    if (!mDB.hasImported(new WordsPairs(eng.get(i),span.get(i),0))){
                                        mDB.importWord(new WordsPairs(eng.get(i),span.get(i),0));
                                    }
                                }
                            }
                            Toast.makeText(Load_Pairs.this,
                                    "Your file is saved.", Toast.LENGTH_LONG).show();
                            mDB.close();
                        }
                        DBHelper mDB = new DBHelper(Load_Pairs.this);

                        ArrayList<WordsPairs> arrayList = mDB.getImportedData();
                        for (int i = 0; i < arrayList.size(); i++){
                            Log.d(TAG, "mDBHELPER database has  " + arrayList.get(i).getENG()+"   "+
                                    arrayList.get(i).getSPAN()+"  ");
                        }
                    }
                });
                alertDialogBuilder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //store words while onDestroy
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putStringArrayList(ENG_WORDS_LIST,eng_wordsList);
        savedInstanceState.putStringArrayList(SPAN_WORDS_LIST,span_wordsList);
    }

}
