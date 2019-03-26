package View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import com.example.myapplication.R;


public class dialogListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> mList = new ArrayList<>();
    private LayoutInflater mInflater;

    public dialogListAdapter(Context context, ArrayList<String> list){
        this.mList = list;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item, parent, false);
            holder.file = (TextView) convertView.findViewById(R.id.file_textview);
            holder.delete = (Button) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        convertView.setTag(holder);

        setTextChangeListener(holder, position);

        setupItem(holder, position);


        return convertView;
    }

    private void setTextChangeListener(final ViewHolder holder, final int position) {
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File dir = mContext.getFilesDir();
                File file = new File(dir, mList.get(position));
                boolean deleted = file.delete();
                if(deleted){
                    mList.remove(position);
                    notifyDataSetChanged();
                }
            }
        });
    }

    private void setupItem(ViewHolder holder, int position) {
        holder.file.setText(mList.get(position));
        holder.delete.setText("DELETE");
        if(mList.get(position).equals("default")){
            holder.delete.setEnabled(false);
            holder.delete.setVisibility(View.GONE);
        }
    }


    class ViewHolder {
        TextView file;
        Button delete;
    }
}
