package com.example.jun.whereareyou.Activity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.jun.whereareyou.Adapter.FriendListAdapter;
import com.example.jun.whereareyou.Data.User;
import com.example.jun.whereareyou.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class PromFriendsActivity extends AppCompatActivity {
    ArrayList<User> friendsArrayList = new ArrayList<User>();
    ArrayList<User> selectedArrayList = new ArrayList<User>();
    int id = 9;
    boolean[] isSelected;
    Toolbar promFriendToolbar;
    ListView friendsListView;
    User me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prom_friends);

        me = getIntent().getParcelableExtra("USER");
        initializeToolBar();
        initializeArrayList();


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent i = new Intent(getApplication(),AddPromActivity.class);
                i.putParcelableArrayListExtra("list",selectedArrayList);
                //i.putExtra("list",selectedArrayList);


                setResult(RESULT_OK,i);
                finish();
                return true;
            }
        });

        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                finish();
                return true;
            }
        });

        return true;
    }

    public void printSelectedList() {
        for(int i = 0; i < selectedArrayList.size(); i++)
            Log.i(""+i, selectedArrayList.get(i).getName());

    }

    public void initializeToolBar() {
        promFriendToolbar = (Toolbar) findViewById(R.id.prom_friends_toolbar);
        setSupportActionBar(promFriendToolbar);
        getSupportActionBar().setTitle("친구 추가");
    }

    public void initializeArrayList() {
        Log.i("DD", "DDDD");

        // 얘를 바꾸고
        new getFriendToServer().execute("http://172.20.10.3:3000/get-friend?memberId=" + me.getEmail());
    }

    public void initializeListView() {
        friendsListView = (ListView) findViewById(R.id.prom_friend_listview);
        final FriendListAdapter friendsListAdapter = new FriendListAdapter(this,R.layout.friendlist_item, friendsArrayList);


        friendsListView.setAdapter(friendsListAdapter);

        friendsListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(!isSelected[i]) {
                    isSelected[i] = true;
                    selectedArrayList.add(friendsArrayList.get(i));
                    view.setBackgroundColor(0xffcccccc);
                } else {
                    isSelected[i] = false;
                    selectedArrayList.remove(friendsArrayList.get(i));
                    view.setBackgroundColor(0xffffffff);
                }
            }
        });
    }

    // 클래스 이름 바꾸고
    public class getFriendToServer extends AsyncTask<String, String, String> {

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
            try {
                JSONArray friendListJson = new JSONArray(result);
                Log.i("RESULT", result);

                // 이거 바꾸면 됨
                for(int i  = 0; i < friendListJson.length(); i++) {
                    JSONObject o = friendListJson.getJSONObject(i);
                    User user = new User(o);

                    friendsArrayList.add(user);
                }

                isSelected = new boolean[friendsArrayList.size()];
                for(int i = 0; i < isSelected.length; i++)
                    isSelected[i] = false;

                initializeListView();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}