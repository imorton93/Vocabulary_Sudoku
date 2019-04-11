package controller;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

import java.util.ArrayList;

import Model.WordsPairs;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class finishPage extends AppCompatActivity {

    private static final String KEY_GRID_SIZE = "grid_size";

    public TextView Result;
    private Button[][] gridButton;
    private ArrayList<WordsPairs> mPairs;
    private String msg;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish9);
        int gridSize = getIntent().getIntExtra(KEY_GRID_SIZE, 9);
        if(gridSize == 4){
            setContentView(R.layout.activity_finish4);
        }
        else if(gridSize == 6){
            setContentView(R.layout.activity_finish6);
        }
        else if(gridSize == 12){
            setContentView(R.layout.activity_finish12);
        }

        /*if (gridSize != 9){
            int layoutID = getResources().getIdentifier("activity_main"+ gridSize, "layout", getPackageName());
            setContentView(layoutID);
        }*/

        final ArrayList<String> list=getIntent().getStringArrayListExtra(EXTRA_MESSAGE);
        mPairs = getIntent().getParcelableArrayListExtra("PLAYING_WORDS_LIST");
        msg = getIntent().getStringExtra("PLAYING_LANGUAGE");
        String[][] sudoku = new String[gridSize][gridSize];
        gridButton = new Button[gridSize][gridSize];//buttons b11-b99
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                String buttonID;
                if (gridSize == 12){
                    buttonID = "b" + (x+1) + "_"+ (y+1);
                }else {
                    buttonID = "b" + (x+1) + (y+1);
                }
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                gridButton[x][y] = findViewById(resID);
            }
            String wordButton = "button" + (x+1);
            int ID = getResources().getIdentifier(wordButton, "id", getPackageName());
        }
        int j = 0;
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                gridButton[x][y].setText(list.get(j));
                j++;
            }
        }

        Button sameSettings = (Button)findViewById(R.id.button1);
        sameSettings.setOnClickListener(listener1);

        Button newSettings = (Button)findViewById(R.id.button2);
        newSettings.setOnClickListener(listener2);

        Button mainMenu = (Button)findViewById(R.id.button3);
        mainMenu.setOnClickListener(listener3);


        Boolean result = getIntent().getExtras().getBoolean("result");

        Result  = (TextView)findViewById(R.id.sResult);
        if(result){
            Result.setText(getString(R.string.s_correct));

        }
        else{
            Result.setText(getString(R.string.s_incorrect));
        }
    }

    View.OnClickListener listener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent data = new Intent();
            data.putExtra("LANGUAGE", msg);
            data.putParcelableArrayListExtra("EXTRA_WORDS_LIST", mPairs);
            setResult(RESULT_OK, data);
            finish();
        }
    };

    View.OnClickListener listener2 = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            diffSettingsButtonOnClick();

        }
    };

    View.OnClickListener listener3 = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            mainMenuButtonOnClick();
        }
    };

    public void sameSettingsButtonOnClick(){

    }

    public void diffSettingsButtonOnClick(){
        //go back to set up page to choose new settings
        Intent start = new Intent(finishPage.this, SetUpPage.class );
        startActivity(start);
    }

    public void mainMenuButtonOnClick(){
        //go back to the start page
        Intent mainMenu = new Intent(finishPage.this, StartPage.class );
        startActivity(mainMenu);
    }

}
