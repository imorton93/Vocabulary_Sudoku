package Model;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

public class customGridAdapter extends BaseAdapter {
    public ArrayList<WordsPairs> words = new ArrayList<WordsPairs>();
    private LayoutInflater mInflater;
    Context mContext;

    public customGridAdapter(Context context, ArrayList<WordsPairs> items) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.words = items;
    }

    public int getCount() {
        return words.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item, parent, false);
            holder.mPairs = words.get(position);
            holder.edit1 = (EditText) convertView.findViewById(R.id.item_edit1);
            holder.edit2 = (EditText) convertView.findViewById(R.id.item_edit2);
            holder.txt1 = (TextView) convertView.findViewById(R.id.txt1);
            holder.txt2 = (TextView) convertView.findViewById(R.id.txt2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        convertView.setTag(holder);

        setTextChangeListener(holder);

        setupItem(holder, position);

        setupItemView(convertView, parent);


        return convertView;
    }

    private void setupItemView(View convertView, ViewGroup parent) {
        //Calculation of TextView Size - density independent.
        Display display = ((Activity)mContext).getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        Resources resources = mContext.getResources();

        int  height = ((GridView) parent).getHeight();
        int orientation = mContext.getResources().getConfiguration().orientation;
        if ( metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {
            // on a large screen device ...
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // In landscape
                convertView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)height/9));
            } else {
                // In portrait
                if ((mContext.getResources().getConfiguration().screenLayout &
                        Configuration.SCREENLAYOUT_SIZE_MASK) ==
                        Configuration.SCREENLAYOUT_SIZE_XLARGE){
                    convertView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)height/14));
                }else{
                    convertView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)height/14));
                }
            }
        }else {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // In landscape
                convertView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)height/6));
            } else {
                convertView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) height / 9));
            }
        }

    }


    class ViewHolder {
        WordsPairs mPairs;
        EditText edit1;
        EditText edit2;
        TextView txt1;
        TextView txt2;
    }

    private void setupItem(ViewHolder holder, int position) {
        holder.edit1.setText(words.get(position).getENG());
        holder.edit1.setId(position);
        holder.edit1.setTextColor(Color.parseColor("#000000"));
        holder.edit1.setEnabled(false);

        holder.edit2.setText(words.get(position).getSPAN());
        holder.edit2.setId(position);
        holder.edit2.setTextColor(Color.parseColor("#000000"));
        holder.edit2.setEnabled(false);

    }

    private void setTextChangeListener(final ViewHolder holder) {

        holder.edit1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    int position = v.getId();
                    String tmp = ((EditText) v).getText().toString();
                    words.get(position).setENG(tmp);

                    if (!TextUtils.isEmpty(holder.edit1.getText().toString().trim()) &&
                            TextUtils.isEmpty(holder.edit2.getText().toString())){
                        holder.edit2.setError("This field can not be blank");
                    }else{
                        holder.edit2.setError(null);
                    }

                }
            }
        });


        holder.edit2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    int position = v.getId();
                    String tmp = ((EditText) v).getText().toString();
                    words.get(position).setSPAN(tmp);

                    if (!TextUtils.isEmpty(holder.edit2.getText().toString().trim())  &&
                            TextUtils.isEmpty(holder.edit1.getText().toString())){
                        holder.edit1.setError("This field can not be blank");
                    }else{
                        holder.edit1.setError(null);
                    }
                }
            }
        });
    }

}
