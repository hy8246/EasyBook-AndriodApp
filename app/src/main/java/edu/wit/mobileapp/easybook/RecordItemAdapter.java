package edu.wit.mobileapp.easybook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by haopan on 3/3/17.
 */

public class RecordItemAdapter extends ArrayAdapter<RecordItem> {
    private LayoutInflater mInflater;
    public RecordItemAdapter(Context context, int rid, List<RecordItem> list)
    {
        super(context,rid,list);
        mInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

    }
    public View getView(int position, View convertView, ViewGroup parent)
    {
        RecordItem item=(RecordItem)getItem(position);
        View view= mInflater.inflate(R.layout.list_item,null);
        TextView name;
        name= (TextView)view.findViewById(R.id.book_name_txtfiled);
        name.setText(item.getBookName());
        TextView day_to_return;
        day_to_return = (TextView) view.findViewById(R.id.day_to_return_txt);
        day_to_return.setText(item.getDay_to_return());
        return view;
    }
}
