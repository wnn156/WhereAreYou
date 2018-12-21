package com.example.jun.whereareyou.Activity;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.jun.whereareyou.Data.ListViewChatItem;
import com.example.jun.whereareyou.R;

public class AddPromActivity extends AppCompatActivity {

    private TextInputEditText nameEt;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private TextView place;
    private TextView cancelTv;
    private TextView searchTv;
    private Button placeBtn;
    private float x_pos;
    private float y_pos;
    private int REQUEST_CODE_P = 1;
    //private Button addFriendsBtn;

    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prom);

        nameEt = (TextInputEditText) findViewById(R.id.promdia_title_edit);
        datePicker = findViewById(R.id.promdia_datepicker);
        timePicker = findViewById(R.id.promdia_timepicker);
        place = findViewById(R.id.promdia_place_text);
        cancelTv = (TextView) findViewById(R.id.findPwDialogCancelTv);
        searchTv = (TextView) findViewById(R.id.findPwDialogFindTv);

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
                ListViewChatItem item = new ListViewChatItem(nameEt.getText().toString(), place.getText().toString(),time);


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


        if(requestCode == REQUEST_CODE_P){
            place.setText(data.getStringExtra("PLACE"));
            x_pos =data.getFloatExtra("X_POS",x_pos);
            y_pos =data.getFloatExtra("Y_POS",y_pos);
        }
    }
}
