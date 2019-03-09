package Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.myapplication.R;

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

        //Calculation of TextView Size - density independent.
        Resources r = Resources.getSystem();
        float px;
        if (getCount() ==  81){
             px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, r.getDisplayMetrics());
        }else{
             px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 55, r.getDisplayMetrics());
        }
        textView.setLayoutParams(new GridView.LayoutParams((int)px, (int)px));
       // textView.setLayoutParams(new GridView.LayoutParams(144, 144));
        textView.setBackgroundResource(R.drawable.grid_items_border);

        return textView;
    }
}
