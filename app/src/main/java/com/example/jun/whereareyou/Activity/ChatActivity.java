package com.example.jun.whereareyou.Activity;

import android.content.Intent;
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

import com.example.jun.whereareyou.Adapter.ChatAdapter;
import com.example.jun.whereareyou.Data.ChatDTO;
import com.example.jun.whereareyou.Data.ListViewChatItem;
import com.example.jun.whereareyou.Module.UpdateWorker;
import com.example.jun.whereareyou.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.daum.android.map.MapActivity;

import java.util.concurrent.TimeUnit;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;


public class ChatActivity extends AppCompatActivity {


    Toolbar myToolbar; //액션바 없애서 채팅방에 툴바추가함
    private String CHAT_ID;
    private String CHAT_NAME;
    private String USER_NAME;
    private ChatAdapter adapter;
    private ListView chat_view;
    private EditText chat_edit;
    private Button chat_send;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private ListViewChatItem listViewChatItem;
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
        CHAT_ID = intent.getStringExtra("chatID"); //준영이 이거 chatname에서 chat id로 변경함 name이름 쓸라고 바꿨디
        USER_NAME = intent.getStringExtra("userName");
        CHAT_NAME = intent.getStringExtra("chatName");

        getSupportActionBar().setTitle(CHAT_NAME);//툴바명을 채팅방이름으로 변경

        adapter = new ChatAdapter(this, R.layout.listitem_chat,listViewChatItem.getUsers());
        chat_view.setAdapter(adapter);
        // 채팅 방 입장
        openChat(CHAT_ID);

        // 메시지 전송 버튼에 대한 클릭 리스너 지정
        chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chat_edit.getText().toString().equals(""))
                    return;

                ChatDTO chat = new ChatDTO(USER_NAME, chat_edit.getText().toString()); //ChatDTO를 이용하여 데이터를 묶는다.
                chat.setTime(System.currentTimeMillis());
                databaseReference.child("CHAT").child(CHAT_ID).push().setValue(chat); // 데이터 푸쉬
                chat_edit.setText(""); //입력창 초기화

            }
        });
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
            startActivity(intent);
            return true;

        }
        return super.onOptionsItemSelected(item);

    }
}