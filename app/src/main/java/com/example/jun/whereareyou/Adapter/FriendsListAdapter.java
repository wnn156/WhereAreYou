package com.example.jun.whereareyou.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jun.whereareyou.Data.FriendProfile;
import com.example.jun.whereareyou.R;

import java.util.ArrayList;

public class FriendsListAdapter extends BaseAdapter {
    Context context;
    ArrayList<FriendProfile> friendsArrayList;


    public FriendsListAdapter(Context context, ArrayList<FriendProfile> friendsArrayList) {
        this.context = context;
        this.friendsArrayList = friendsArrayList;
    }

    @Override
    public int getCount() {
        return this.friendsArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.friendsArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.freinds_item,null);
            ImageView friendImageView = (ImageView)view.findViewById(R.id.friendImageView);
            TextView friendTextView = (TextView)view.findViewById(R.id.friendTextView);
            friendTextView.setText(friendsArrayList.get(i).getName());
        }
        return view;
    }
}
