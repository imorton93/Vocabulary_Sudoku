package controller;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import Model.WordsPairs;
import View.customGridAdapter;
import View.dialogListAdapter;
import com.example.myapplication.R;

public class Load_Pairs extends AppCompatActivity {


    private static final int READ_REQUEST_CODE = 100;
    private static final String WORDS_LIST = "wordslist";
    private static final String TAG = "LOAD_WORDS";

    ArrayList<WordsPairs> mPairs = new ArrayList<>();
    ArrayList<String> strings = null;
    private String message;
    Boolean loadMore = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_pairs);

        if (savedInstanceState != null) {
            mPairs = savedInstanceState.getParcelableArrayList(WORDS_LIST);
        }
        //set up ListView
        GridView girdview = (GridView)findViewById(R.id.grid_load);
        ArrayList<WordsPairs> arrayList= new ArrayList<>();
        if (mPairs.isEmpty()){
            isLargeScreen(arrayList);
        }else{
            for (int i = 0; i < mPairs.size(); i++){
                WordsPairs list=new WordsPairs();
                list.setENG(mPairs.get(i).getENG());
                list.setSPAN(mPairs.get(i).getSPAN());
                arrayList.add(list);
            }
        }
        girdview.setAdapter(new customGridAdapter(this,arrayList));



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
                Intent intent = new Intent(Load_Pairs.this, StartPage.class);
                startActivity(intent);
            }
        });

        //allow users to type in more words
        //more codes coming
        //store data into database
        final Button more = (Button)findViewById(R.id.more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMore = true;
                ArrayList<WordsPairs>arrayList= new ArrayList<>();

                for (int i = 0; i < 9; i++) {
                    WordsPairs list=new WordsPairs();
                    list.setENG("");
                    list.setSPAN("");
                    arrayList.add(list);
                }

                final GridView girdview = (GridView) findViewById(R.id.grid_load);
                girdview.setAdapter(new customGridAdapter(Load_Pairs.this,arrayList));

                isLargeScreen(arrayList);
            }
        });

        //Button to set up a Eng game with words that users type in
        //more codes coming
        //store data into database
        Button edit = (Button) findViewById(R.id.edit_file);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AlertDialog.Builder builder = new AlertDialog.Builder(Load_Pairs.this);
               builder.setCancelable(false);
               builder.setTitle("Words List");
                // add a radio button list
                final ArrayList<String> myList = new ArrayList<>();
                myList.add("default");
                File dir = new File(getFilesDir().getPath());
                File[] aList = dir.listFiles();
                if(aList != null){
                    for(File file: aList){
                        myList.add(file.getName());
                    }
                }
               ListView dialog_listview = new ListView(Load_Pairs.this);
               dialog_listview.setAdapter(new dialogListAdapter(Load_Pairs.this, myList));

               builder.setView(dialog_listview);
               builder.setNegativeButton("CANCEL", null);

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



        //Button save to save a file with uploaded words from users
        final Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUploadedFile(mPairs);
            }
        });
        //LOAD more pairs of words
        //need more codes

    }


    private void isLargeScreen(ArrayList<WordsPairs> arrayList){
        //Determine density
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int density = metrics.densityDpi;
        Log.d(TAG, "metrics.densityDpi of this device is " + density);

        if (density == DisplayMetrics.DENSITY_XHIGH)  {
            // on a large screen device ...
            int orientation = this.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                for (int i = 0; i < 18; i++) {
                    WordsPairs list=new WordsPairs();
                    list.setENG("");
                    list.setSPAN("");
                    arrayList.add(list);
                }
            }else{
                if ((getResources().getConfiguration().screenLayout &
                        Configuration.SCREENLAYOUT_SIZE_MASK) ==
                        Configuration.SCREENLAYOUT_SIZE_XLARGE){
                    for (int i = 0; i < 28; i++) {
                        WordsPairs list=new WordsPairs();
                        list.setENG("");
                        list.setSPAN("");
                        arrayList.add(list);
                    }
                }else{
                    for (int i = 0; i < 14; i++) {
                        WordsPairs list = new WordsPairs();
                        list.setENG("");
                        list.setSPAN("");
                        arrayList.add(list);
                    }
                }
            }
        }else{
            int orientation = this.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                for (int i = 0; i < 6; i++) {
                    WordsPairs list = new WordsPairs();
                    list.setENG("");
                    list.setSPAN("");
                    arrayList.add(list);
                }
            }else{
                for (int i = 0; i < 9; i++) {
                    WordsPairs list = new WordsPairs();
                    list.setENG("");
                    list.setSPAN("");
                    arrayList.add(list);
                }
            }
        }
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
                    ArrayList<WordsPairs>arrayList_sec= new ArrayList<>();
                    if (!loadMore){
                        mPairs.clear();
                    }
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
                        }
                        mPairs.add(new WordsPairs(words[0],words[1]));
                    }
                    isLargeScreen(arrayList);
                    GridView gridview = (GridView)findViewById(R.id.grid_load);
                    gridview.setAdapter(new customGridAdapter(this,arrayList));
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
    public void saveUploadedFile(final ArrayList<WordsPairs> words) {
        if (words.size() == 0){
            Toast.makeText(Load_Pairs.this,
                    "No words to save.", Toast.LENGTH_LONG).show();
        }else{
            final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_item, null);

            final EditText editText = (EditText) dialogView.findViewById(R.id.edt_file);
            Button save = (Button) dialogView.findViewById(R.id.buttonSave);
            Button cancel = (Button) dialogView.findViewById(R.id.buttonCancel);
            save.setText("SAVE");
            cancel.setText("CANCEL");
            dialogBuilder.setTitle("SAVE AS (0-9,a-z, A-Z)");

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBuilder.dismiss();
                }
            });
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // DO SOMETHINGS
                    String filename = editText.getText().toString();
                    if (filename.equals("")){
                        editText.setError("This field can not be blank");
                    }else{
                        //check if file already exists
                        File file = new File(getApplicationContext().getFilesDir(),filename);
                        if(file.exists()){
                            editText.setError("This field already exists");
                        }else{
                            //create a file located in internal storage
                            try {
                                FileOutputStream fOut = openFileOutput(filename, MODE_PRIVATE);
                                for (int i = 0; i < strings.size(); i++){
                                    String tmp = strings.get(i) + "\n";
                                    fOut.write(tmp.getBytes());
                                }
                                fOut.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(Load_Pairs.this,
                                    "Your file " + filename + " is successfully saved.", Toast.LENGTH_LONG).show();

                            dialogBuilder.dismiss();
                        }
                    }
                }
            });

            dialogBuilder.setView(dialogView);
            dialogBuilder.show();
        }

    }

    //store words while onDestroy
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putParcelableArrayList(WORDS_LIST,mPairs);
    }

}
