package com.example.myapplication;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.Arrays;

import controller.MainActivity;

public class main6x6 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6x6);


//long click function to bring up popup text
        View.OnLongClickListener longClickListener = new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                if(!InitializedGame){
                    Toast need_init = Toast.makeText(MainActivity.this ,
                            R.string.not_initialized,Toast.LENGTH_LONG);
                    need_init.setGravity(Gravity.TOP, 0, 400);
                    need_init.show();
                    return true;
                }
                Button button = (Button) v;
                PopupMenu popup = new PopupMenu(MainActivity.this, (Button) v);

                popup.getMenuInflater().inflate(R.menu.popup_text, popup.getMenu());

                MenuItem text = popup.getMenu().getItem(0);

                CharSequence buttonText = button.getText();
                Log.d(TAG, "buttonText length is " + buttonText.length() );
                if(buttonText.length() > 6){
                    CharSequence sixText = buttonText.subSequence(0,6);
                    CharSequence shortlist;
                    if(fill_Eng){
                        if(button.getCurrentTextColor() == Color.parseColor("#000000")){
                            for(int i = 0; i < 9; i++){
                                if(span_wordsList[i].length() > 6){
                                    shortlist = span_wordsList[i].subSequence(0,6);
                                    Log.d(TAG, "comparing " + sixText + " and " + shortlist);
                                    if(sixText.equals(shortlist)){
                                        Log.d(TAG, "passed comparison");
                                        buttonText = span_wordsList[i];
                                    }
                                }

                            }
                        }
                        else{
                            for(int j = 0; j < 9; j++){
                                if(eng_wordsList[j].length() > 6){
                                    shortlist = eng_wordsList[j].subSequence(0,6);
                                    Log.d(TAG, "comparing " + sixText + " and " + shortlist);
                                    if(sixText.equals(shortlist)){
                                        Log.d(TAG, "passed comparison");
                                        buttonText = eng_wordsList[j];
                                    }
                                }
                            }
                        }
                    }
                    if(fill_Span){
                        if(button.getCurrentTextColor() == Color.parseColor("#000000")){
                            for(int i = 0; i < 9; i++){
                                if(eng_wordsList[i].length() > 6){
                                    shortlist = eng_wordsList[i].subSequence(0,6);
                                    Log.d(TAG, "comparing " + sixText + " and " + shortlist);
                                    if(sixText.equals(shortlist)){
                                        Log.d(TAG, "passed comparison");
                                        buttonText = eng_wordsList[i];
                                    }
                                }

                            }
                        }
                        else{
                            for(int j = 0; j < 9; j++){
                                if(span_wordsList[j].length() > 6){
                                    shortlist = span_wordsList[j].subSequence(0,6);
                                    Log.d(TAG, "comparing " + sixText + " and " + shortlist);
                                    if(sixText.equals(shortlist)){
                                        Log.d(TAG, "passed comparison");
                                        buttonText = span_wordsList[j];
                                    }
                                }
                            }
                        }
                    }
                }
                text.setTitle(buttonText);

                popup.show();

                return true;
            }
        };

        //click function
        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!InitializedGame){
                    Toast need_init = Toast.makeText(MainActivity.this ,
                            R.string.not_initialized,Toast.LENGTH_LONG);
                    need_init.setGravity(Gravity.TOP, 0, 400);
                    need_init.show();
                }


   /*     else if (mistakeCount >= 3){
                    Toast.makeText(MainActivity.this,"You have lost your game", Toast.LENGTH_LONG).show();
                }*/
                else {
                    Button test = (Button) v;
                    if(test.getCurrentTextColor() == Color.parseColor("#000000")){
                        return;
                    }

                    else if (SelectedButton != null) {
                        //if a button has already been selected change that button back to normal
                        SelectedButton.setBackgroundResource(R.drawable.unclicked_button);
                    }
                    SelectedButton = (Button) v;
                    SelectedButton.setBackgroundResource(R.drawable.clicked_button);
                    Log.d(TAG, "buttonText length is" );
                }
            }
        };


        int[] ids={R.id.b11, R.id.b12,R.id.b13, R.id.b14, R.id.b15,R.id.b16,
                R.id.b21, R.id.b22,R.id.b23, R.id.b24, R.id.b25,R.id.b26,
                R.id.b31, R.id.b32,R.id.b33, R.id.b34, R.id.b35,R.id.b36,
                R.id.b41, R.id.b42,R.id.b43, R.id.b44, R.id.b45,R.id.b46,
                R.id.b51, R.id.b52,R.id.b53, R.id.b54, R.id.b55,R.id.b56,
                R.id.b61, R.id.b62,R.id.b63, R.id.b64, R.id.b65,R.id.b66};



// loop through the array, find the button with respective id and set the listener
        for(int i=0; i<ids.length; i++){
            Button gbutton = (Button) findViewById(ids[i]);
            ((Button) gbutton).setOnClickListener(listener);
        }

        for(int i=0; i<ids.length; i++) {
            Button button = (Button) findViewById(ids[i]);
            button.setOnLongClickListener(longClickListener);
        }
    }//end of oncreate

    private Button SelectedButton; //button that user selects to insert

    public void insertButtonOnClick(View w) {
        //user hits button to change text of selectedbutton
        if (listen_mode) {
            /*
            For listen mode, all buttons are clickable,
            so I need to make it so that the user cannot change the word on the sudoku if the cell is a number
             */
            // text of input button is extracted
            /* when inserting a new word into puzzle, check if right or wrong
             *  if it's right, make it green
             * *if wrong, put word to be red
             */
            //track the button that user selects
            String tmp = null;
            wordsSplit(list);
            if (SelectedButton != null) {
                CharSequence cellText = SelectedButton.getText();
                if (!(Arrays.asList(l_numbers).contains(cellText))) {
                    //If selected grid isn't a number, update the cell.
                    Button button = (Button) w;
                    // text of input button is extracted
                    CharSequence buttonText = button.getText();
                    if(buttonText.length() > 6){
                        buttonText = buttonText.subSequence(0,6)+"..";
                        System.out.println("Constrained to six letters");
                    }
                    Log.d(TAG, "buttonText length is" + buttonText.length());
                    SelectedButton.setText(buttonText);
                    //if is wrong, puts word to be red
                    if (!checkFilledWord(buttonText.toString())) {
                        SelectedButton.setTextColor(Color.parseColor("#FFFFC0CB"));
                        mistakeCount++;
                    } else {
                        //if it's right, makes it green
                        SelectedButton.setTextColor(Color.parseColor("#FF008577"));
                    }
                    //set the Selected Buttons Text as text from input button
                }
            }
        } else {
            //Normal mode
            if (SelectedButton != null) {
                Button button = (Button) w;
                // text of input button is extracted
                CharSequence buttonText = button.getText();
                /* when inserting a new word into puzzle, check if right or wrong
                 *  if it's right, make it green
                 * *if wrong, put word to be red
                 */
                //track the button that user selects
                //if is wrong, puts word to be red
                if (!checkFilledWord(buttonText.toString())) {
                    SelectedButton.setBackgroundResource(R.drawable.unclicked_button);
                    if(buttonText.length() > 6){
                        buttonText = buttonText.subSequence(0,6) + "..";
                    }
                    SelectedButton.setText(buttonText);
                    SelectedButton.setTextColor(Color.parseColor("#FFFFC0CB"));
                    mistakeCount++;
                    //allow user to keep track of what they get wrong
                    //ask user whether they want to save to My words
                } else {
                    //if it's right, makes it green
                    SelectedButton.setBackgroundResource(R.drawable.unclicked_button);
                    if(buttonText.length() > 6){
                        buttonText = buttonText.subSequence(0,6) + "..";
                    }
                    SelectedButton.setText(buttonText);
                    SelectedButton.setTextColor(Color.parseColor("#FF008577"));
                }
                //set the Selected Buttons Text as text from input button
            }
        }
    }

    public void clearButtonOnClick(View z){
        if(SelectedButton != null) {
            SelectedButton.setText(null);
        }
    }
}
