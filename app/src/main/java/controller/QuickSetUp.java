package controller;

import android.content.Intent;
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
import android.widget.Toast;

import com.example.myapplication.R;

public class QuickSetUp extends AppCompatActivity {
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
    Button nine_by_nine = null;
    Button two_by_three = null;
    Button three_by_four = null;
    Button two_by_two = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setuppage);

        nine_by_nine = findViewById(R.id.n3by3);
        nine_by_nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fill_English || fill_Spanish) {
                    //Call the quickSetUp class
                    Intent q_n9by9 = new Intent(QuickSetUp.this, QuickNbyBActivity.class);
                    q_n9by9.putExtra(KEY_fill_Eng, fill_English);
                    q_n9by9.putExtra(KEY_fill_Span, fill_Spanish);
                    q_n9by9.putExtra(KEY_Listen, listen_mode);
                    startActivity(q_n9by9);
                }
                else{
                    Toast need_check = Toast.makeText(QuickSetUp.this ,
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
                        Intent t2by2 = new Intent(QuickSetUp.this, QuickNbyBActivity.class);
                        t2by2.putExtra(KEY_fill_Eng, fill_English);
                        t2by2.putExtra(KEY_fill_Span, fill_Spanish);
                        t2by2.putExtra(KEY_Listen, listen_mode);
                        t2by2.putExtra(KEY_GRID_SIZE, 4);
                        startActivity(t2by2);
                    }
                    else{
                        Toast need_check = Toast.makeText(QuickSetUp.this ,
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
                    Intent t2by3 = new Intent(QuickSetUp.this, QuickNbyBActivity.class);
                    t2by3.putExtra(KEY_fill_Eng, fill_English);
                    t2by3.putExtra(KEY_fill_Span, fill_Spanish);
                    t2by3.putExtra(KEY_Listen, listen_mode);
                    t2by3.putExtra(KEY_GRID_SIZE, 6);
                    startActivity(t2by3);
                }
                else{
                    Toast need_check = Toast.makeText(QuickSetUp.this ,
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
                        Intent t3by4 = new Intent(QuickSetUp.this, QuickNbyBActivity.class);
                        t3by4.putExtra(KEY_fill_Eng, fill_English);
                        t3by4.putExtra(KEY_fill_Span, fill_Spanish);
                        t3by4.putExtra(KEY_Listen, listen_mode);
                        t3by4.putExtra(KEY_GRID_SIZE, 12);
                        startActivity(t3by4);
                    }else {
                        three_by_four.setVisibility(View.GONE);
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
                    if (fill_Spanish) {
                        fill_span.toggle();
                        fill_Spanish = false;
                    }
                }
                else{
                    fill_English = false;
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
                    if (fill_English) {
                        fill_eng.toggle();
                        fill_English = false;
                    }
                }
                else{
                    fill_Spanish = false;
                }
            }
        });



    }


}
