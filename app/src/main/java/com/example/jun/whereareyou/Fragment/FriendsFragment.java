package com.example.jun.whereareyou.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.jun.whereareyou.Adapter.FriendListAdapter;
import com.example.jun.whereareyou.Data.ListViewFriendItem;
import com.example.jun.whereareyou.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {


    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        ListView listView = (ListView)view.findViewById(R.id.listView);
        ArrayList<ListViewFriendItem> data=new ArrayList<>();

        ListViewFriendItem friend1=new ListViewFriendItem("김동욱","5");
        ListViewFriendItem friend2=new ListViewFriendItem("윤서원","3");
        ListViewFriendItem friend3=new ListViewFriendItem("성경화","2");
        ListViewFriendItem friend4=new ListViewFriendItem("안준영","3");

        data.add(friend1);
        data.add(friend2);
        data.add(friend3);
        data.add(friend4);

        FriendListAdapter adapter=new FriendListAdapter(getActivity(),R.layout.friendlist_item,data);
        listView.setAdapter(adapter);

        return view;
    }

}
