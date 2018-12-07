package com.example.jun.whereareyou.Activity;

import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity {

    public EditText etEmail;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btnRegist = (Button) findViewById(R.id.btnRegist);
        Button btnLogin = findViewById(R.id.login);
        etEmail = (EditText) findViewById(R.id.etEmail);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setEmail(etEmail.getText().toString());
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
                finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String email;

        Log.d("RESULT", requestCode + "");
        Log.d("RESULT", resultCode + "");
        Log.d("RESULT", data + "");

        email = data.getStringExtra("email");
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            Toast.makeText(LoginActivity.this, "회원가입을 완료했습니다!", Toast.LENGTH_SHORT).show();
            etEmail.setText(email);

        }

        user.setEmail(email);

    }
}
