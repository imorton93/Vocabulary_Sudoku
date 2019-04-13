package controller;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

public class SetUpPage extends AppCompatActivity {
    private static final String KEY_fill_Eng = "fill_Eng";
    private static final String KEY_fill_Span = "fill_Span";
    private static final String KEY_Listen = "Listen_Mode";
    private static final String KEY_GRID_SIZE = "grid_size";

    Boolean listen_mode = false;
    Boolean fill_English = false;
    Boolean fill_Spanish = false;
    Switch listen_switch = null;
    CheckBox fill_eng = null;
    CheckBox fill_span = null;
    CheckBox three_by_three = null;
    CheckBox two_by_three = null;
    CheckBox three_by_four = null;
    CheckBox two_by_two = null;
    TextView cannot_start = null;
    Button start = null;
    Boolean t3by3 = false;
    Boolean t2by2 = false;
    Boolean t2by3 = false;
    Boolean t3by4 = false;
    int gridSize = 9;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.setuppage);

        cannot_start = findViewById(R.id.cannot_start);
        start = findViewById(R.id.start_button);
        start.setVisibility(View.GONE);
        if ((fill_English || fill_Spanish) && (t3by3 || t2by2 || t2by3 || t3by4)) {
            start.setVisibility(View.VISIBLE);
            cannot_start.setVisibility(View.GONE);
        } else {
            cannot_start.setVisibility(View.VISIBLE);
            start.setVisibility(View.GONE);
        }


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sudoku = new Intent(SetUpPage.this, MainActivity.class);
                sudoku.putExtra(KEY_fill_Eng, fill_English);
                sudoku.putExtra(KEY_fill_Span, fill_Spanish);
                sudoku.putExtra(KEY_Listen, listen_mode);
                sudoku.putExtra(KEY_GRID_SIZE, gridSize);
                startActivity(sudoku);
            }
        });

        /*
        three_by_three = findViewById(R.id.n3by3);
        three_by_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fill_English || fill_Spanish) {
                    Intent n9by9 = new Intent(SetUpPage.this, MainActivity.class);
                    n9by9.putExtra(KEY_fill_Eng, fill_English);
                    n9by9.putExtra(KEY_fill_Span, fill_Spanish);
                    n9by9.putExtra(KEY_Listen, listen_mode);
                    n9by9.putExtra(KEY_GRID_SIZE, 9);
                    startActivity(n9by9);
                }
                else{
                    Toast need_check = Toast.makeText(SetUpPage.this ,
                            R.string.not_checked,Toast.LENGTH_LONG);
                    need_check.setGravity(Gravity.TOP, 0, 400);
                    need_check.show();
                }
            }
        });

        two_by_two = findViewById(R.id.t2by2);
        if ( two_by_two != null) {
            two_by_two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Start activity for intent page with two by three layout
                    if (fill_English || fill_Spanish) {
                        Intent t2by2 = new Intent(SetUpPage.this, MainActivity.class);
                        t2by2.putExtra(KEY_fill_Eng, fill_English);
                        t2by2.putExtra(KEY_fill_Span, fill_Spanish);
                        t2by2.putExtra(KEY_Listen, listen_mode);
                        t2by2.putExtra(KEY_GRID_SIZE, 4);
                        startActivity(t2by2);
                    }
                    else{
                        Toast need_check = Toast.makeText(SetUpPage.this ,
                                R.string.not_checked,Toast.LENGTH_LONG);
                        need_check.setGravity(Gravity.TOP, 0, 400);
                        need_check.show();
                    }
                }
            });
        }

        two_by_three = findViewById(R.id.t2by3);
        two_by_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start activity for intent page with two by three layout
                if (fill_English || fill_Spanish) {
                    Intent t2by3 = new Intent(SetUpPage.this, MainActivity.class);
                    t2by3.putExtra(KEY_fill_Eng, fill_English);
                    t2by3.putExtra(KEY_fill_Span, fill_Spanish);
                    t2by3.putExtra(KEY_Listen, listen_mode);
                    t2by3.putExtra(KEY_GRID_SIZE, 6);
                    startActivity(t2by3);
                }
                else{
                    Toast need_check = Toast.makeText(SetUpPage.this ,
                            R.string.not_checked,Toast.LENGTH_LONG);
                    need_check.setGravity(Gravity.TOP, 0, 400);
                    need_check.show();
                }
            }
        });

        three_by_four = findViewById(R.id.t3by4);
        if (three_by_four != null) {
            three_by_four.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Start activity for intent page with three by four layout
                    //Determine density
                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    int density = metrics.densityDpi;

                    if (density == DisplayMetrics.DENSITY_XHIGH) {
                        three_by_four.setVisibility(View.VISIBLE);
                    }else {
                        three_by_four.setVisibility(View.GONE);
                    }

                    if (fill_English || fill_Spanish) {
                        Intent t3by4 = new Intent(SetUpPage.this, MainActivity.class);
                        t3by4.putExtra(KEY_fill_Eng, fill_English);
                        t3by4.putExtra(KEY_fill_Span, fill_Spanish);
                        t3by4.putExtra(KEY_Listen, listen_mode);
                        t3by4.putExtra(KEY_GRID_SIZE, 12);
                        startActivity(t3by4);
                    }
                    else{
                        Toast need_check = Toast.makeText(SetUpPage.this ,
                                R.string.not_checked,Toast.LENGTH_LONG);
                        need_check.setGravity(Gravity.TOP, 0, 400);
                        need_check.show();
                    }
                }
            });
        }
*/

        three_by_three = findViewById(R.id.n3by3);
        three_by_three.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    t3by3 = true;
                    gridSize = 9;
                    if(fill_Spanish || fill_English){
                        start.setVisibility(View.VISIBLE);
                        cannot_start.setVisibility(View.GONE);
                    }
                    if (t2by2) {
                        two_by_two.toggle();
                        t2by2 = false;
                    } else if (t2by3) {
                        two_by_three.toggle();
                        t2by3 = false;
                    } else if (t3by4) {
                        three_by_four.toggle();
                        t3by4 = false;
                    }
                }
                else{
                    t3by3 = false;
                    if (!(t3by4 || t2by2 || t2by3)) {
                        cannot_start.setVisibility(View.VISIBLE);
                        start.setVisibility(View.GONE);
                    }
                }
            }
        });

        two_by_two = findViewById(R.id.t2by2);
            two_by_two.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        t2by2 = true;
                        gridSize = 4;
                        if(fill_Spanish || fill_English){
                            start.setVisibility(View.VISIBLE);
                            cannot_start.setVisibility(View.GONE);
                        }
                        if (t3by3) {
                            three_by_three.toggle();
                            t3by3 = false;
                        } else if (t2by3) {
                            two_by_three.toggle();
                            t2by3 = false;
                        } else if (t3by4) {
                            three_by_four.toggle();
                            t3by4 = false;
                        }
                    }
                    else{
                        t2by2 = false;
                        if (!(t3by3 || t3by4 || t2by3)) {
                            cannot_start.setVisibility(View.VISIBLE);
                            start.setVisibility(View.GONE);
                        }
                    }
                }
            });

        two_by_three = findViewById(R.id.t2by3);
        two_by_three.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    t2by3 = true;
                    gridSize = 6;
                    if(fill_Spanish || fill_English){
                        start.setVisibility(View.VISIBLE);
                        cannot_start.setVisibility(View.GONE);
                    }
                    if (t2by2) {
                        two_by_two.toggle();
                        t2by2 = false;
                    } else if (t3by3) {
                        three_by_three.toggle();
                        t3by3 = false;
                    } else if (t3by4) {
                        three_by_four.toggle();
                        t3by4 = false;
                    }
                }
                else{
                    t2by3 = false;
                    if (!(t3by4 || t2by2 || t3by3)) {
                        cannot_start.setVisibility(View.VISIBLE);
                        start.setVisibility(View.GONE);
                    }
                }
            }
        });


        //Determine density
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int density = metrics.densityDpi;

        three_by_four = findViewById(R.id.t3by4);
        if (three_by_four != null) {
            if ((getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE ) {
                three_by_four.setVisibility(View.VISIBLE);
            } else {
                three_by_four.setVisibility(View.GONE);
            }
        }
        if (three_by_four != null) {
            three_by_four.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        t3by4 = true;
                        gridSize = 12;
                        if(fill_Spanish || fill_English){
                            start.setVisibility(View.VISIBLE);
                            cannot_start.setVisibility(View.GONE);
                        }
                        if (t2by2) {
                            two_by_two.toggle();
                            t2by2 = false;
                        } else if (t3by3) {
                            three_by_three.toggle();
                            t3by3 = false;
                        } else if (t2by3) {
                            two_by_three.toggle();
                            t2by3 = false;
                        }
                    }
                    else{
                        t3by4 = false;
                        if (!(t3by3 || t2by2 || t2by3)) {
                            cannot_start.setVisibility(View.VISIBLE);
                            start.setVisibility(View.GONE);
                        }
                    }
                }
            });
        }


        listen_switch = findViewById(R.id.listen_switch);
        listen_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    listen_mode = true;
                }
                else{
                    listen_mode = false;
                }
            }
        });


        fill_eng = findViewById(R.id.fill_eng);
        fill_eng.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked){
                    fill_English = true;
                   // fill_span.setSelected(false);
                    if (t3by3 || t3by4 || t2by3 || t2by2) {
                        start.setVisibility(View.VISIBLE);
                        cannot_start.setVisibility(View.GONE);
                    }
                    if (fill_Spanish) {
                        fill_span.toggle();
                        fill_Spanish = false;
                    }
                }
                else{
                    fill_English = false;
                    if(!fill_Spanish){
                        cannot_start.setVisibility(View.VISIBLE);
                        start.setVisibility(View.GONE);
                    }
                }
            }

         });


        fill_span = findViewById(R.id.fill_span);
        fill_span.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked){
                    fill_Spanish = true;
                    //fill_eng.setSelected(false);
                    if (t3by3 || t3by4 || t2by3 || t2by2) {
                        start.setVisibility(View.VISIBLE);
                        cannot_start.setVisibility(View.GONE);
                    }
                    if (fill_English) {
                        fill_eng.toggle();
                        fill_English = false;
                    }
                }
                else{
                    fill_Spanish = false;
                    if(!fill_English){
                        cannot_start.setVisibility(View.VISIBLE);
                        start.setVisibility(View.GONE);
                    }
                }
            }
        });



    }

    @Override
    public void onBackPressed(){
        Intent startmenu = new Intent(SetUpPage.this, StartPage.class);
        startActivity(startmenu);
        return;

    }


}
