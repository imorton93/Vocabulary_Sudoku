package com.example.myapplication;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    private Button SelectedButton; //button that user selects to insert

    public void gridButtonOnClick(View v){
        //user hits one of the grid blocks to insert a word
        if(SelectedButton != null){
            SelectedButton.setBackgroundResource(R.drawable.unclicked_button);
            SelectedButton.setTextColor(Color.parseColor("#000000"));
        }
        SelectedButton = (Button) v;
        SelectedButton.setBackgroundResource(R.drawable.clicked_button);
        SelectedButton.setTextColor(Color.parseColor("#FFFFFF"));
        //SelectedButton.setText("clicked");
    }

    public void insertButtonOnClick(View w){
        //user hits button to change text of selectedbutton
        Button button = (Button) w;
        // text of input button is extracted
        CharSequence buttonText = button.getText();
        //set the Selected Buttons Text as text from input button
        SelectedButton.setText(buttonText);
    }

    public void clearButtonOnClick(View z){
        SelectedButton.setText("");
    }
}
