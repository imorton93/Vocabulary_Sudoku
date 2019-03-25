package View;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;

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

        //Calculation of TextView Size - density independent.
        Display display = ((Activity)mContext).getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        Resources resources = mContext.getResources();

        View gridview = (GridView) parent;
        int  width = gridview.getWidth();
        int  height = gridview.getHeight();
        int orientation = mContext.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            textView.setLayoutParams(new GridView.LayoutParams((int)width/9,(int)height/4));
        } else {
            // In portrait
            textView.setLayoutParams(new GridView.LayoutParams((int)width/6,(int)height/6));
        }


        textView.setBackgroundResource(R.drawable.grid_items_border);

        return textView;

    }
}
