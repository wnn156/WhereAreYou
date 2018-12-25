package com.example.jun.whereareyou.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jun.whereareyou.Adapter.ChatAdapter;
import com.example.jun.whereareyou.Data.ChatDTO;
import com.example.jun.whereareyou.Data.ListViewChatItem;
import com.example.jun.whereareyou.Data.User;
import com.example.jun.whereareyou.Module.UpdateWorker;
import com.example.jun.whereareyou.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.daum.android.map.MapActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;


public class ChatActivity extends AppCompatActivity {


    Toolbar myToolbar; //액션바 없애서 채팅방에 툴바추가함

    private ChatAdapter adapter;
    private ListView chat_view;
    private EditText chat_edit;
    private Button chat_send;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private ListViewChatItem listViewChatItem;
    private User me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //툴바 생성
/////////////////////////////////////////////////////////////////////////////////
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
////////////////////////////////////////////////////////////////////////////////

        PeriodicWorkRequest.Builder UpdateBuilder =
                new PeriodicWorkRequest.Builder(UpdateWorker.class, 20,
                        TimeUnit.MINUTES);
// ...if you want, you can apply constraints to the builder here...

// Create the actual work object:
        PeriodicWorkRequest UpdateCheckWork = UpdateBuilder.build();
// Then enqueue the recurring task:
        WorkManager.getInstance().enqueue(UpdateCheckWork);

        // 위젯 ID 참조
        chat_view = (ListView) findViewById(R.id.chat_view);
        chat_edit = (EditText) findViewById(R.id.chat_edit);
        chat_send = (Button) findViewById(R.id.chat_sent);



        // 로그인 화면에서 받아온 채팅방 이름, 유저 이름 저장
        Intent intent = getIntent();
        listViewChatItem = intent.getParcelableExtra("ListViewChatItem");

        me = intent.getParcelableExtra("USER");


        String sMessage = "id="+listViewChatItem.getPromise_id();
        adapter = new ChatAdapter(this, R.layout.listitem_chat);
        System.out.println(sMessage);
        new ChatActivity.GetParticipantTask().execute("http://172.20.10.3:3000/get-participant?" + sMessage);




        getSupportActionBar().setTitle(listViewChatItem.getChat_name());//툴바명을 채팅방이름으로 변경

        System.out.println("CHATACTIVITY - LISTVIEWCHATITEM : " + listViewChatItem);
        System.out.println("CHATACTIVITY - PARTICIPANT : " + listViewChatItem.getparticipant());


    }

    private void addMessage(DataSnapshot dataSnapshot) {
        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
        chatDTO.setFirebaseKey(dataSnapshot.getKey());
        adapter.add(chatDTO);

        chat_view.smoothScrollToPosition(adapter.getCount());
    }

    private void removeMessage(DataSnapshot dataSnapshot) {
        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
        adapter.remove(chatDTO);
    }

    private void openChat(String chatName) {
        // 리스트 어댑터 생성 및 세팅

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child("CHAT").child(chatName).addChildEventListener(new ChildEventListener() {
            // 위에 이거 뭐임??
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addMessage(dataSnapshot);
                Log.e("LOG", "s:" + s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeMessage(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.list_menu, menu);

        return true;

    }



    /**

     * 메뉴 아이템을 클릭했을 때 발생되는 이벤트...

     * @param item

     * @return

     */

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {



        int id = item.getItemId();



        if( id == R.id.showMap ){
            Intent intent = new Intent(ChatActivity.this, MapsActivity.class);
            intent.putExtra("ListViewChatItem",listViewChatItem);
            //intent.putExtra("Participants",listViewChatItem.getparticipant());

            intent.putExtra("USER",me);
            startActivity(intent);
            return true;

        }
        return super.onOptionsItemSelected(item);

    }


    public class GetParticipantTask extends AsyncTask<String, String, String> {

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
                System.out.println("NULL...................................;;;");
                return;
            }

            try {

                JSONArray dataArr = new JSONArray(result);
                int cnt = 1;
                for (int i = 0; i < dataArr.length(); i++) {
                    adapter.addUser(new User(dataArr.getJSONObject(i)));
                    listViewChatItem.addParcicipant(dataArr.getJSONObject(i));
                }
                adapter.setMe(me);

                chat_view.setAdapter(adapter);
                // 채팅 방 입장
                openChat(listViewChatItem.getKey());

                // 메시지 전송 버튼에 대한 클릭 리스너 지정
                chat_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (chat_edit.getText().toString().equals(""))
                            return;

                        ChatDTO chat = new ChatDTO(me.getEmail(), chat_edit.getText().toString()); //ChatDTO를 이용하여 데이터를 묶는다.
                        chat.setTime(System.currentTimeMillis());
                        databaseReference.child("CHAT").child(listViewChatItem.getKey()).push().setValue(chat); // 데이터 푸쉬
                        chat_edit.setText(""); //입력창 초기화

                    }
                });
                System.out.println("GET CHATITEM : " + listViewChatItem);


            } catch (Exception e) {
                e.printStackTrace();
            }



        }

    }
}