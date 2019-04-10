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

public class finishPage extends AppCompatActivity {

    private static final String KEY_GRID_SIZE = "grid_size";

    public TextView Result;




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
