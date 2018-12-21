package com.example.jun.whereareyou.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.jun.whereareyou.Adapter.FriendListAdapter;
import com.example.jun.whereareyou.Data.User;
import com.example.jun.whereareyou.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    private static ArrayList<User> data;
    private static User me;
    public static FriendListAdapter adapter;

    public static FriendsFragment newInstance(User newUser){
        Bundle args = new Bundle();

        data.add(newUser);
        adapter.upDateList(data);
        FriendsFragment fragment = new FriendsFragment();
        args.putParcelable("User",me);
        fragment.setArguments(args);
        return fragment;
    }
    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);


        me = (User)getArguments().get("User");


        ListView listView = (ListView)view.findViewById(R.id.listView);
        data=new ArrayList<>();

        adapter=new FriendListAdapter(getActivity(),R.layout.friendlist_item,data);
        listView.setAdapter(adapter);

        return view;
    }

}
