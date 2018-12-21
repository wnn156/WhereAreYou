package com.example.jun.whereareyou.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private DatePicker datePicker;
    private TimePicker timePicker;
    private TextView place;
    private TextView friend_cnt;
    private TextView cancelTv;
    private TextView searchTv;
    private Button placeBtn;
    private Button add_friendBtn;
    private float x_pos;
    private float y_pos;
    private int REQUEST_CODE_P = 1;
    private int REQUEST_CODE_F = 2;
    private int promise_id;

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
        cancelTv = (TextView) findViewById(R.id.findPwDialogCancelTv);
        searchTv = (TextView) findViewById(R.id.findPwDialogFindTv);
        add_friendBtn = (Button) findViewById(R.id.promdia_friend_btn);
        friend_cnt = (TextView)findViewById(R.id.prom_cnt_text);

        me = getIntent().getParcelableExtra("USER");

        add_friendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(),PromFriendsActivity.class);
                intent.putExtra("USER",me);
                startActivityForResult(intent,REQUEST_CODE_F);
            }
        });
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        searchTv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String time = "" + datePicker.getYear() + "/" + datePicker.getMonth() + "/'" +
                        datePicker.getDayOfMonth() + " " + timePicker.getHour() + ":" +
                        timePicker.getMinute();
                ListViewChatItem item = new ListViewChatItem(nameEt.getText().toString(), place.getText().toString(),time,selectedList,promise_id);

                //디비에 추가
            }
        });

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);


        if(requestCode == REQUEST_CODE_P) {
            place.setText(data.getStringExtra("PLACE"));
            x_pos = data.getFloatExtra("X_POS", x_pos);
            y_pos = data.getFloatExtra("Y_POS", y_pos);
        }else if(requestCode == REQUEST_CODE_F){
            //selectedList.addAll((ArrayList<User>)data.getParcelableArrayListExtra("list"));
            selectedList = data.getParcelableArrayListExtra("list");
            friend_cnt.setText(Integer.toString(selectedList.size())+"명");
        }
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
                promise_id = Integer.parseInt(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
