package com.example.jun.whereareyou.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jun.whereareyou.Activity.ChatActivity;
import com.example.jun.whereareyou.Activity.LoginActivity;
import com.example.jun.whereareyou.Activity.MainActivity;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    private ChatlistAdapter requestAdapter;
    private static ChatlistAdapter adapter;

    private static User me = null;
    private ListView listView;
    private TextView requestTextView;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private ListViewChatItem listViewChatItem;
    private ArrayList<ListViewChatItem> dataRequest;
    private static ArrayList<ListViewChatItem> data;

    public static ChatFragment newInstance(Context context, ListViewChatItem listViewChatItem, User user){
        Bundle args = new Bundle();

        ChatFragment fragment = new ChatFragment();
        if(data == null){
            data = new ArrayList<>();
        }
        data.add(listViewChatItem);
        System.out.println("Data : " + data);
        if(adapter == null){
            adapter = new ChatlistAdapter(context,R.layout.chatlist_item,data);
        }
        adapter.upDateList(data);
        me = user;
        args.putParcelable("USER",me);
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

        me = (User)getArguments().get("USER");

        final ListView requestListView = view.findViewById(R.id.requestListView);

        String sMessage = "memberId="+ me.getEmail();


        System.out.println(sMessage);
        new ChatFragment.GetPromiseTask().execute("http://172.20.10.3:3000/get-promise?" + sMessage);

        requestTextView = view.findViewById(R.id.requestTextView);
        listView = (ListView)view.findViewById(R.id.listView);
        data=new ArrayList<>();

        /*ListViewChatItem chat1=new ListViewChatItem("말해모앱","공대9호관","2018/12/03 18:30");
        ListViewChatItem chat2=new ListViewChatItem("디비조모임","융복관","2018/12/04 15:30");
        ListViewChatItem chat3=new ListViewChatItem("종프","공대9호관","2018/12/05 19:50");*/

        ArrayList<User> users = new ArrayList<>();
       /* users.add(new User("aa"));
        users.add(new User("bb"));
        users.add(new User("cc"));*/
       /* chat1.setKey("first");
        chat1.setUsers(users);
        chat2.setKey("first");
        chat2.setUsers(users);
        chat3.setKey("first");
        chat3.setUsers(users);

        data.add(chat1);
        data.add(chat2);
        data.add(chat3);*/

        adapter=new ChatlistAdapter(getActivity(),R.layout.chatlist_item,data);


        dataRequest = new ArrayList<>();
        dataRequest.addAll(data);

        requestAdapter=new ChatlistAdapter(getActivity(),R.layout.chatlist_item,dataRequest);
        if(dataRequest.size() >0)
            requestTextView.setVisibility(View.VISIBLE);
        else
            requestTextView.setVisibility(View.GONE);
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

                        if(dataRequest.size() >0)
                            requestTextView.setVisibility(View.VISIBLE);
                        else
                            requestTextView.setVisibility(View.GONE);

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



    public class GetPromiseTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);//url을 가져온다.
                    con = (HttpURLConnection) url.openConnection();
                    con.connect();//연결 수행

                    //입력 스트림 생성
                    InputStream stream = con.getInputStream();

                    //속도를 향상시키고 부하를 줄이기 위한 버퍼를 선언한다.
                    reader = new BufferedReader(new InputStreamReader(stream));

                    //실제 데이터를 받는곳
                    StringBuffer buffer = new StringBuffer();

                    //line별 스트링을 받기 위한 temp 변수
                    String line = "";

                    //아래라인은 실제 reader에서 데이터를 가져오는 부분이다. 즉 node.js서버로부터 데이터를 가져온다.
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    //다 가져오면 String 형변환을 수행한다. 이유는 protected String doInBackground(String... urls) 니까
                    return buffer.toString();

                    //아래는 예외처리 부분이다.
                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //종료가 되면 disconnect메소드를 호출한다.
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        //버퍼를 닫아준다.
                        if(reader != null){
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }//finally 부분
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("???");
            return null;
        }

        //doInBackground메소드가 끝나면 여기로 와서 텍스트뷰의 값을 바꿔준다.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result == null) {
                return;
            }

            try {

                JSONArray dataArr = new JSONArray(result);
                for (int i = 0; i < dataArr.length(); i++) {
                    JSONObject o = dataArr.getJSONObject(0);
                    dataRequest.add(new ListViewChatItem(o));
                }







            } catch (Exception e) {
                e.printStackTrace();
            }



        }

    }
}
