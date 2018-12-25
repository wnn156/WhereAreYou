package com.example.jun.whereareyou.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

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

public class LoginActivity extends AppCompatActivity {

    public EditText etEmail;
    User user = new User();
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Intent intent = getIntent();

        Button btnRegist = (Button) findViewById(R.id.btnRegist);
        Button btnLogin = findViewById(R.id.login);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etEmail = (EditText) findViewById(R.id.etEmail);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sMessage = "memberId="+ etEmail.getText().toString() + "&password=" + etPassword.getText().toString();


                System.out.println(sMessage);
                new JSONTask().execute("http://172.20.10.3:3000/login?" + sMessage);
            }
        });

        btnRegist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), RegistActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, 1000);
            }

        });
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
            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();//실패
            if(result == null) {
                Toast.makeText(getApplicationContext(), "ID가 없거나 비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();//실패
                return;
            }



            try {


                JSONArray dataArr = new JSONArray(result);
                JSONObject datas = dataArr.getJSONObject(0);
                //Toast.makeText(getApplicationContext(), datas.toString(), Toast.LENGTH_SHORT).show();//실패
                User u = new User(datas);
                Log.d("datas",datas.toString());
                Log.d("USER",u.toString());
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("LOGIN", "login");
                intent.putExtra("USER",u);

                Toast.makeText(getApplication(),"로그인에 성공하였습니다.",Toast.LENGTH_SHORT).show();

                startActivity(intent);// 인텐트로 다음 화면으로 넘기면서 result도 넘겨야 함


            } catch (Exception e) {
                e.printStackTrace();
            }



        }

    }
}
