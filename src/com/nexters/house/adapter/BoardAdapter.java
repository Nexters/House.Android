package com.nexters.house.adapter;

import java.util.ArrayList;

import com.nexters.house.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BoardAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater mInflater;
    ArrayList<String> mArrayList;

    public BoardAdapter() {
        super();
    }

    public BoardAdapter(Context context, ArrayList<String> arrayList) {
        super();
        this.mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mArrayList = arrayList;
    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_board, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.board_content);
        textView.setText(mArrayList.get(position));

//        TextView heartTextView = (TextView) convertView.findViewById(R.id.heart_btn);
//        heartTextView.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "Heart Btn", Toast.LENGTH_SHORT).show();
//            }
//        });

        return convertView;
    }
}
