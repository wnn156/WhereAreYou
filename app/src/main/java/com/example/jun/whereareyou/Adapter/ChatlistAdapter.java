package com.example.jun.whereareyou.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jun.whereareyou.Data.ListViewChatItem;
import com.example.jun.whereareyou.R;

import java.util.ArrayList;

public class ChatlistAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private ArrayList<ListViewChatItem> data;
    private int layout;
    public ChatlistAdapter(Context context, int layout, ArrayList<ListViewChatItem> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }
    @Override
    public int getCount(){return data.size();}
    @Override
    public String getItem(int position){return data.get(position).getChat_name();}
    @Override
    public long getItemId(int position){return position;}
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }
        ListViewChatItem listviewitem=data.get(position);
        TextView chat_name=(TextView) convertView.findViewById(R.id.chat_name);
        chat_name.setText(listviewitem.getChat_name());
        TextView place=(TextView)convertView.findViewById(R.id.place);
        place.setText(listviewitem.getPlace());
        TextView time=(TextView)convertView.findViewById(R.id.time);
        time.setText(listviewitem.getTime());
        return convertView;
    }

    public void upDateList(ArrayList<ListViewChatItem> listViewChatItem){
        this.data = listViewChatItem;
        notifyDataSetChanged();
    }
}
