package edu.wit.mobileapp.easybook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

/**
 * Created by haopan on 1/31/17.
 */
public class SearchItemAdapter extends ArrayAdapter<SearchItem> {
    private LayoutInflater mInflater;
    public SearchItemAdapter(Context context, int rid, List<SearchItem> list)
    {
        super(context, rid, list);
        mInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        SearchItem item=(SearchItem)getItem(position);
        View view= mInflater.inflate(R.layout.search_result_item,null);
        TextView name, author, aviability;
        name= (TextView)view.findViewById(R.id.result_book_name);
        name.setText(item.getBookName());
        author=(TextView)view.findViewById(R.id.result_author_name);
        author.setText(item.getAurthorName());
        aviability= (TextView)view.findViewById(R.id.avaiability);
        if(item.getAvaiability())
        {
            aviability.setText("Available");
            aviability.setTextColor(GREEN);
        }
        else
        {
            aviability.setText("Not Available");
            aviability.setTextColor(RED);
        }
        return view;
    }
}
