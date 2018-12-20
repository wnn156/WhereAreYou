package com.example.jun.whereareyou.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.jun.whereareyou.Activity.ChatActivity;
import com.example.jun.whereareyou.Adapter.ChatlistAdapter;
import com.example.jun.whereareyou.Data.ListViewChatItem;
import com.example.jun.whereareyou.Data.User;
import com.example.jun.whereareyou.Dialog.Prom_Dialog;
import com.example.jun.whereareyou.Dialog.Request_Dialog;
import com.example.jun.whereareyou.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    private ChatlistAdapter requestAdapter;
    private ChatlistAdapter adapter;

    private static User me = null;
    private ListView listView;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private ArrayList<ListViewChatItem> dataRequest;
    private ArrayList<ListViewChatItem> data;

    public static ChatFragment newInstance(){
        Bundle args = new Bundle();

        ChatFragment fragment = new ChatFragment();
        args.putParcelable("User",me);
        fragment.setArguments(args);
        return fragment;
    }

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        me = (User)getArguments().get("User");

        final ListView requestListView = view.findViewById(R.id.requestListView);

        listView = (ListView)view.findViewById(R.id.listView);
        data=new ArrayList<>();

        ListViewChatItem chat1=new ListViewChatItem("말해모앱","공대9호관","2018/12/03 18:30");
        ListViewChatItem chat2=new ListViewChatItem("디비조모임","융복관","2018/12/04 15:30");
        ListViewChatItem chat3=new ListViewChatItem("종프","공대9호관","2018/12/05 19:50");

        ArrayList<User> users = new ArrayList<>();
        users.add(new User("aa"));
        users.add(new User("bb"));
        users.add(new User("cc"));
        chat1.setKey("first");
        chat1.setUsers(users);
        chat2.setKey("first");
        chat2.setUsers(users);
        chat3.setKey("first");
        chat3.setUsers(users);

        data.add(chat1);
        data.add(chat2);
        data.add(chat3);

        adapter=new ChatlistAdapter(getActivity(),R.layout.chatlist_item,data);


        dataRequest = data;

        requestAdapter=new ChatlistAdapter(getActivity(),R.layout.chatlist_item,dataRequest);
        requestListView.setAdapter(requestAdapter);
        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Request_Dialog request_dialog = new Request_Dialog(getActivity());
                request_dialog.setDialogListener(new Request_Dialog.MyDialogListener() {  // MyDialogListener 를 구현
                    @Override
                    public void onPositiveClicked() {

                        ListViewChatItem listViewChatItem = dataRequest.get(position);
                        dataRequest.remove(position);
                        data.add(listViewChatItem);

                        requestAdapter.upDateList(dataRequest);
                        adapter.upDateList(data);

                        Log.d("Data", data.toString());
                        Log.d("dataRequest",dataRequest.toString());


                    }

                    @Override
                    public void onNegativeClicked() {
                        Log.d("MyDialogListener","onNegativeClicked");
                    }
                });
                request_dialog.show();
            }
        });

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("ListViewChatItem", data.get(position));
                intent.putExtra("chatID",data.get(position).getChat_name() );
                intent.putExtra("chatName",data.get(position).getKey());
                Log.d("chatID",data.get(position).toString());
                intent.putExtra("userName", me.getEmail());
                startActivity(intent);
            }
        });
        return view;
    }

    private void showChatList() {
        // 리스트 어댑터 생성 및 세팅
        final ArrayAdapter<String> adapter

                = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1);
        listView.setAdapter(adapter);

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child("CHAT").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("LOG", "dataSnapshot.getKey() : " + dataSnapshot.getKey());
                adapter.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
