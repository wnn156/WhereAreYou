package com.example.jun.whereareyou.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.jun.whereareyou.Activity.SearchMapActivity;
import com.example.jun.whereareyou.Data.ListViewChatItem;
import com.example.jun.whereareyou.R;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class Prom_Dialog extends Dialog implements View.OnClickListener{
    private static final int LAYOUT = R.layout.prom_dialog;

    public interface MyDialogListener {
        public void onPositiveClicked(ListViewChatItem item);
        public void onNegativeClicked();
    }
    private MyDialogListener dialogListener;

    private Context context;

    private TextInputEditText nameEt;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private TextView place;
    private TextView cancelTv;
    private TextView searchTv;
    private Button placeBtn;
    private int REQUEST_CODE_P = 1;
    //private Button addFriendsBtn;

    private String name;
    private Activity activity;

    public Prom_Dialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public Prom_Dialog(Context context,Activity activity){
        super(context);
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        nameEt = (TextInputEditText) findViewById(R.id.promdia_title_edit);
        datePicker = findViewById(R.id.promdia_datepicker);
        timePicker = findViewById(R.id.promdia_timepicker);
        place = findViewById(R.id.promdia_place_text);
        cancelTv = (TextView) findViewById(R.id.findPwDialogCancelTv);
        searchTv = (TextView) findViewById(R.id.findPwDialogFindTv);

        cancelTv.setOnClickListener(this);
        searchTv.setOnClickListener(this);

        if(name != null){
            nameEt.setText(name);
        }

        placeBtn = (Button) findViewById(R.id.promdia_place_btn);

        placeBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent_p = new Intent(activity, SearchMapActivity.class);
                activity.startActivityForResult(intent_p,REQUEST_CODE_P);

            }
        });
    }


    @Override
    public void onClick(View v) {

    }
    public void setDialogListener(MyDialogListener dialogListener){
        this.dialogListener = dialogListener;
    }


}