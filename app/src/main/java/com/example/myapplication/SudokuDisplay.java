package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;


import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class SudokuDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sudoku_display);

        final Button tv_view1;
        final ArrayList<String> list=getIntent().getStringArrayListExtra(EXTRA_MESSAGE);
        String text = "The Correct Sudoku Vocabulary:" + System.getProperty("line.separator") + System.getProperty("line.separator");
       // textView.setText(Text);

        final GridView gridView = (GridView)findViewById(R.id.sudoku_view);
        gridView.setAdapter(new GridAdapter(list,this));

    }

}


class GridAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<String> sudoku = new ArrayList<String>();

    // Constructor
    public GridAdapter(ArrayList<String> list, Context c) {
        this.mContext = c;
        this.sudoku = list;
    }

    @Override
    public int getCount() {
        return sudoku.size();
    }

    @Override
    public Object getItem(int position) {
        return sudoku.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TextView textView = new TextView(mContext);
        textView.setText(sudoku.get(position));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.parseColor("#FF008577"));
        textView.setLayoutParams(new GridView.LayoutParams(144, 144));
        textView.setBackgroundResource(R.drawable.grid_items_border);
        return textView;
    }
}
