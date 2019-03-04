package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;


public class listArrayAdapter extends BaseAdapter {
    public ArrayList<WordsPairs> words = new ArrayList<WordsPairs>();
    private LayoutInflater mInflater;
    Context mContext;

    public listArrayAdapter(Context context, ArrayList<WordsPairs> items) {
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

        return convertView;
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