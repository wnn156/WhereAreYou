package com.example.jun.whereareyou.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jun.whereareyou.Activity.LoginActivity;
import com.example.jun.whereareyou.Activity.MainActivity;
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

public class AddFriends_Dialog extends Dialog implements View.OnClickListener{
    private static final int LAYOUT = R.layout.addfriends_dialog;

    public interface MyDialogListener {
        public void onPositiveClicked(User user);
        public void onNegativeClicked();
    }
    private MyDialogListener dialogListener;

    private Context context;

    private EditText textview_user_ID;
    private TextView user_info;
    private TextView cancelTv;
    private TextView searchTv;

    private User me;
    User newUser;
    private String userID;

    public AddFriends_Dialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public AddFriends_Dialog(Context context, User me){
        super(context);
        this.context = context;
        this.me = me;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        textview_user_ID = (EditText) findViewById(R.id.user_id);

        cancelTv = (TextView) findViewById(R.id.findPwDialogCancelTv);
        searchTv = (TextView) findViewById(R.id.findPwDialogFindTv);

        cancelTv.setOnClickListener(this);
        searchTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.findPwDialogCancelTv:
                cancel();
                break;
            case R.id.findPwDialogFindTv:
               //user_id로 검색후 user_id와 일치하는 유저 닉네임 등등 반환.
                String sMessage = "memberId="+ textview_user_ID.getText().toString();
                new AddFriends_Dialog.JSONTask().execute("http://172.20.10.3:3000/search-friend?" + sMessage);
                break;
        }
    }
    public void setDialogListener(MyDialogListener dialogListener){
        this.dialogListener = dialogListener;
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
            if(result == null) {
                return;
            }
            try {
                JSONArray dataArr = new JSONArray(result);

                JSONObject datas = dataArr.getJSONObject(0);

                newUser = new User(datas);


                Request_Dialog request_dialog = new Request_Dialog(getContext(), newUser);
                request_dialog.setDialogListener(new Request_Dialog.MyDialogListener() {
                    @Override
                    public void onPositiveClicked() {
                        String sMessage = "memberId="+ me.getEmail() + "&friendId=" + newUser.getEmail();
                        new AddFriends_Dialog.AddFriendTask().execute("http://172.20.10.3:3000/add-friend?" + sMessage);

                    }

                    @Override
                    public void onNegativeClicked() {

                    }
                });
                request_dialog.show();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public class AddFriendTask extends AsyncTask<String, String, String> {

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
            System.out.println("ADDFRIEND " + result);
            if(result.equals("undefined")) {
                Toast.makeText(getContext(),"이미 등록된 친구입니다!", Toast.LENGTH_SHORT).show();
                dismiss();
            }

            else {
                try {
                    //다음화면
                    dialogListener.onPositiveClicked(newUser);
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        }

    }

}