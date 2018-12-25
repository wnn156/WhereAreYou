package com.example.jun.whereareyou.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jun.whereareyou.Data.ListViewChatItem;
import com.example.jun.whereareyou.Data.User;
import com.example.jun.whereareyou.R;
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

public class AddPromActivity extends AppCompatActivity {

    private TextInputEditText nameEt;
    Toolbar toolbar;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private TextView place;
    private TextView friend_cnt;
    private Button placeBtn;
    private Button add_friendBtn;
    private double latitude;
    private double longitude;
    private int REQUEST_CODE_P = 1;
    private int REQUEST_CODE_F = 2;
    private int promise_id;
    private String time;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    ArrayList<User> selectedList;
    User me;
    //private Button addFriendsBtn;

    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_prom);
        new AddPromActivity.JSONTask().execute("http://172.20.10.3:3000/get-promise-id?");
        nameEt = (TextInputEditText) findViewById(R.id.promdia_title_edit);
        datePicker = findViewById(R.id.promdia_datepicker);
        timePicker = findViewById(R.id.promdia_timepicker);
        place = findViewById(R.id.promdia_place_text);
        add_friendBtn = (Button) findViewById(R.id.promdia_friend_btn);
        friend_cnt = (TextView)findViewById(R.id.prom_cnt_text);

        me = getIntent().getParcelableExtra("USER");

        initializeToolBar();
        add_friendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(),PromFriendsActivity.class);
                intent.putExtra("USER",me);
                startActivityForResult(intent,REQUEST_CODE_F);
            }
        });

        /*searchTv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String time = "" + datePicker.getYear() + "/" + datePicker.getMonth() + "/'" +
                        datePicker.getDayOfMonth() + " " + timePicker.getHour() + ":" +
                        timePicker.getMinute();
                ListViewChatItem item = new ListViewChatItem(nameEt.getText().toString(), place.getText().toString(),time,selectedList,promise_id);

                //디비에 추가
            }
        });*/

        if(name != null){
            nameEt.setText(name);
        }

        placeBtn = (Button) findViewById(R.id.promdia_place_btn);

        placeBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent_p = new Intent(getApplication(), SearchMapActivity.class);
                startActivityForResult(intent_p,REQUEST_CODE_P);

            }
        });
    }

    public void initializeToolBar() {
        toolbar = (Toolbar) findViewById(R.id.prom_friends_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("약속 추가");
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);


        if(requestCode == REQUEST_CODE_P) {
            place.setText(data.getStringExtra("PLACE"));
            latitude = data.getDoubleExtra("X_POS", latitude);
            longitude = data.getDoubleExtra("Y_POS", longitude);
        }else if(requestCode == REQUEST_CODE_F){
            //selectedList.addAll((ArrayList<User>)data.getParcelableArrayListExtra("list"));
            selectedList = data.getParcelableArrayListExtra("list");
            friend_cnt.setText(Integer.toString(selectedList.size())+"명");
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                time = "" + datePicker.getYear() + datePicker.getMonth() +
                        datePicker.getDayOfMonth() + timePicker.getHour() +
                        timePicker.getMinute();

                String key = databaseReference.child("CHAT").push().getKey();





                ListViewChatItem listViewChatItem = new ListViewChatItem(nameEt.getText().toString(), place.getText().toString(), time, promise_id, latitude, longitude, key);
                listViewChatItem.setKey(key);

                String sMessage = "id=" + promise_id + "&prom_name=" + nameEt.getText().toString() + "&time=" + time+ "&place=" +place.getText().toString()+"&x=" + latitude+ "&y=" + longitude + "&chatKey=" + key;
                System.out.println("PROMISE : QUERY="+sMessage);
                new AddPromActivity.AddPromise().execute("http://172.20.10.3:3000/add-promise?" + sMessage);


                selectedList.add(0,me);
                for(int i = 0; i < selectedList.size(); i++) {
                    sMessage = "promId=" + promise_id + "&friendId=" + selectedList.get(i).getEmail();
                    new AddPromActivity.AddPartTask().execute("http://172.20.10.3:3000/join-promise?" + sMessage);
                }

                Intent intent = new Intent(getApplication(),MainActivity.class);
                intent.putExtra("USER",me);
                intent.putExtra("ListViewChatItem",listViewChatItem);
                setResult(1,intent);
                Toast.makeText(getApplication(),"약속이 추가되었습니다.",Toast.LENGTH_SHORT);
                startActivity(intent);
                finish();

                //Toast.makeText(getApplication(),"확인 버튼 실행.",Toast.LENGTH_SHORT);

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

    public class JSONTask extends AsyncTask<String, String, String> {

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
                System.out.println("GET PROMISE ID : " + result);
                promise_id = Integer.parseInt(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public class AddPartTask extends AsyncTask<String, String, String> {

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
                Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();//실패
                return;
            }
            Toast.makeText(getApplication(),result,Toast.LENGTH_SHORT);

        }
    }

    public class AddPromise extends AsyncTask<String, String, String> {

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

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
