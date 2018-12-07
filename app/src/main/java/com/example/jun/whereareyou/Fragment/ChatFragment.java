package com.example.jun.whereareyou.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.jun.whereareyou.Activity.ChattingActivity;
import com.example.jun.whereareyou.Adapter.ChatlistAdapter;
import com.example.jun.whereareyou.Data.ListViewChatItem;
import com.example.jun.whereareyou.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        ListView listView = (ListView)view.findViewById(R.id.listView);
        final ArrayList<ListViewChatItem> data=new ArrayList<>();

        ListViewChatItem chat1=new ListViewChatItem("말해모앱","공대9호관","2018/12/03 18:30");
        ListViewChatItem chat2=new ListViewChatItem("디비조모임","융복관","2018/12/04 15:30");
        ListViewChatItem chat3=new ListViewChatItem("종프","공대9호관","2018/12/05 19:50");

        data.add(chat1);
        data.add(chat2);
        data.add(chat3);

        ChatlistAdapter adapter=new ChatlistAdapter(getActivity(),R.layout.chatlist_item,data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChattingActivity.class);
                intent.putExtra("ListViewChatItem", data.get(position));
                startActivity(intent);
            }
        });
        return view;
    }

}
