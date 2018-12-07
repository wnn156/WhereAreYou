package com.example.jun.whereareyou.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jun.whereareyou.Data.ListViewFriendItem;
import com.example.jun.whereareyou.R;

import java.util.ArrayList;

public class FriendListAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private ArrayList<ListViewFriendItem> data;
    private int layout;
    public FriendListAdapter(Context context, int layout, ArrayList<ListViewFriendItem> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }
    @Override
    public int getCount(){return data.size();}
    @Override
    public String getItem(int position){return data.get(position).getUser_id();}
    @Override
    public long getItemId(int position){return position;}
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }
        ListViewFriendItem listviewitem=data.get(position);
        TextView user_id=(TextView) convertView.findViewById(R.id.user_id);
        user_id.setText(listviewitem.getUser_id());
        TextView credit_rate=(TextView)convertView.findViewById(R.id.credit_rate);
        credit_rate.setText(listviewitem.getCredit_rate());


        return convertView;
    }
}
