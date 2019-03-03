package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class GridAdapter extends BaseAdapter {
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

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final TextView textView = new TextView(mContext);
        if (sudoku.get(position).contains(".wrong")){
            String str = sudoku.get(position);
            str = str.replace(str.substring(str.length()-6), "");
            textView.setText(str);
            textView.setTextColor(Color.RED);
        }else{
            textView.setText(sudoku.get(position));
            textView.setTextColor(Color.parseColor("#FF008577"));

        }
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(new GridView.LayoutParams(144, 144));
        textView.setBackgroundResource(R.drawable.grid_items_border);



        return textView;
    }
}
