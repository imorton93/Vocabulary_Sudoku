package com.example.myapplication;

        import android.content.Intent;
        import android.graphics.Color;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.Gravity;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.Toast;

        import java.util.ArrayList;
        import java.util.Random;

        import static android.provider.AlarmClock.EXTRA_MESSAGE;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "CMPT276-1191E1-Delta";
    private static final int[] Button_ids = { //ID's for the 9 big buttons
            R.id.button1,
            R.id.button2,
            R.id.button3,
            R.id.button4,
            R.id.button5,
            R.id.button6,
            R.id.button7,
            R.id.button8,
            R.id.button9,
    };

    private static final Button[][] gridButton = new Button[9][9];//buttons b11-b99
    String[] span_words;
    String[] eng_words;
    private static SudokuGenerator initialGame = new SudokuGenerator();
    String[][] Sudoku = new String[9][9];
    private static SudokuChecker resultCheck = new SudokuChecker();
    private Button mfinButton;
    private boolean InitializedGame = false;
    String[] eng_wordsList = new String[9];
    String[] span_wordsList = new String[9];




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initial gameGrid
        //show words in button
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                String buttonID = "b" + (y+1) + (x+1);
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                gridButton[x][y] = (Button) findViewById(resID);
            }
        }
        //store words from String Resources
        span_words = getResources().getStringArray(R.array.Span_words);
        eng_words = getResources().getStringArray(R.array.Eng_words);
        //in case, order of String from String Resources may change
        //store in local variables
        for (int i = 0; i < 9; i++){
            span_wordsList[i] = span_words[i];
            eng_wordsList[i] = eng_words[i];
        }
        //gameInitial
        //finish Button
        finButton();
    }

    public void getGameGrid(String[] words){
        InitializedGame = true;
        Sudoku = initialGame.generateGrid(words);

        double remainingGrids = 81;
        double remainingHoles = 50; //set up a num to determine how many words to hide
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                gridButton[x][y].setText(Sudoku[x][y]);
                gridButton[x][y].setClickable(false);
                double makingHole = remainingHoles/remainingGrids;  //randomly hide some words
                if(Math.random() <= makingHole) {
                    gridButton[x][y].setText(null);
                    gridButton[x][y].setClickable(true);
                    remainingHoles--;
                }
                remainingGrids--;
            }
        }

    }

    public void finButton(){
        //finish button listening action
        //By clicking finish button, will check the correctness of Sudoku
        //display whether right or wrong
        mfinButton = (Button)findViewById(R.id.fin_button);
        mfinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[][] checkSudoku = new String[9][9];
                String[][] originalSudoku = new String[9][9];
                for (int y = 0; y < 9; y++) {
                    for (int x = 0; x < 9; x++) {
                        //pass words in grid to a Sting[][] Sudoku in order to check correctness
                        CharSequence temp = gridButton[x][y].getText();
                        checkSudoku[x][y] = temp + "";
                        originalSudoku[x][y] = Sudoku[x][y];
                    }
                }
                checkAnswer(checkSudoku, originalSudoku);
            }
        });
    }

    //check sudoku correctness
    public void checkAnswer(String[][] Sudoku, String[][] originalSudoku){
        String msg;
        if (resultCheck.sudokuCheck(Sudoku, eng_wordsList,span_wordsList)){
            msg = "Congratulation! Sudoku is correct!";
        }else{
            msg = "Sudoku is incorrect, try again!";
        }
        Toast result = Toast.makeText(MainActivity.this,msg,Toast.LENGTH_LONG);
        result.setGravity(Gravity.TOP, 0, 400);
        result.show();
        //only for test
        //intent the 9*9 Grid Sudoku to a new page
        Intent intent = new Intent(MainActivity.this, SudokuDisplay.class);
        ArrayList<String> words = new ArrayList<String>();
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                words.add(originalSudoku[x][y]);
            }
        }
        intent.putStringArrayListExtra(EXTRA_MESSAGE,words);
        intent.putExtra(EXTRA_MESSAGE,words);
        startActivity(intent);
    }


    private Button SelectedButton; //button that user selects to insert

    public void gridButtonOnClick(View v){
        //user hits one of the grid blocks to insert a word
        if(!InitializedGame){
            Toast.makeText(MainActivity.this ,
                    R.string.not_initialized,Toast.LENGTH_LONG).show();
        }
        else {
            if (SelectedButton != null) {
                //if a button has already been selected change that button back to normal
                SelectedButton.setBackgroundResource(R.drawable.unclicked_button);
                SelectedButton.setTextColor(Color.parseColor("#FF008577"));
            }
            SelectedButton = (Button) v;
            SelectedButton.setBackgroundResource(R.drawable.clicked_button);
            SelectedButton.setTextColor(Color.parseColor("#FFFFFF"));
            //SelectedButton.setText("clicked");
        }
    }

    public void insertButtonOnClick(View w){
        //user hits button to change text of selectedbutton
        if(SelectedButton != null) {
            Button button = (Button) w;
            // text of input button is extracted
            CharSequence buttonText = button.getText();
            /* when clicking a English word and filling into the cell,
            the English word will be automatically translated to Spanish
            *  vice versa
            * *
            */
            for (int i = 0; i < 9; i++) {
                if (buttonText.equals(eng_wordsList[i])){
                    buttonText = ""+span_wordsList[i];
                }else if (buttonText.equals(span_wordsList[i])){
                    buttonText =""+eng_wordsList[i];
                }
            }
            //set the Selected Buttons Text as text from input button
            SelectedButton.setText(buttonText);
        }
    }

    public void clearButtonOnClick(View z){
        if(SelectedButton != null) {
            SelectedButton.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Button mButton1 = (Button) findViewById(R.id.button1);
        Button mButtons;
        int i;
        switch (item.getItemId()){
            case R.id.fill_Span:
                Log.d(TAG,"User chooses to fill in Spanish");
                //The 9 buttons will display Spanish. works.
                //mButton1.setText(R.string.span_1);
                Toast result1 = Toast.makeText(MainActivity.this,
                        "User chooses to fill in Spanish",Toast.LENGTH_LONG);
                if (InitializedGame == false) {
                    result1.setGravity(Gravity.TOP, 0, 400);
                    result1.show();
                    getGameGrid(eng_words); //After choosing "fill in Spanish", start a new game with Spanish
                    for (i = 0; i < 9; i++) {
                        mButtons = findViewById(Button_ids[i]);
                        mButtons.setText(span_words[i]);
                    }
                }
                else{
                    //Make warning dialog appear
                    Toast.makeText(MainActivity.this,R.string.cant_init,Toast.LENGTH_LONG).show();
                }
                return true;

            case R.id.fill_Eng:
                Log.d(TAG,"User chooses to fill in English");
                //The 9 buttons will display English
                //mButton1.setText(R.string.eng_1);
                Toast result2 = Toast.makeText(MainActivity.this,
                        "User chooses to fill in English",Toast.LENGTH_LONG);
                if (InitializedGame == false) {
                    result2.setGravity(Gravity.TOP, 0, 400);
                    result2.show();
                    getGameGrid(span_words); //After choosing "fill in Spanish", start a new game with English
                    for (i = 0; i < 9; i++) {
                        mButtons = findViewById(Button_ids[i]);
                        mButtons.setText(eng_words[i]);
                    }
                }
                else {
                    //make dialog here
                    //Temporary Toast
                    Toast.makeText(MainActivity.this,R.string.cant_init,Toast.LENGTH_LONG).show();
                }
                return true;

            case R.id.display_words:

                Log.d(TAG,"User chooses to see word pairs");
                Intent display_w = new Intent(this, Display_Words.class);
                this.startActivity(display_w);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}


