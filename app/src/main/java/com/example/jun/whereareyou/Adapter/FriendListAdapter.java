package com.example.jun.whereareyou.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jun.whereareyou.Data.ListViewChatItem;
import com.example.jun.whereareyou.Data.User;
import com.example.jun.whereareyou.R;

import java.util.ArrayList;

public class FriendListAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private ArrayList<User> data;
    private int layout;
    public FriendListAdapter(Context context, int layout, ArrayList<User> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }
    @Override
    public int getCount(){return data.size();}
    @Override
    public String getItem(int position){return data.get(position).getEmail();}
    @Override
    public long getItemId(int position){return position;}
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        System.out.println(data);
        
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }
        User user=data.get(position);
        TextView user_id=(TextView) convertView.findViewById(R.id.user_id);
        user_id.setText(user.getName());
        TextView phone_number = (TextView) convertView.findViewById(R.id.phone_num);
        phone_number.setText(user.getPhone_number());
        TextView credit_rate=(TextView)convertView.findViewById(R.id.credit_rate);
        credit_rate.setText(Double.toString(user.getScore()));


        return convertView;
    }
    public void upDateList(ArrayList<User> users){
        this.data = users;
        notifyDataSetChanged();
    }
}
