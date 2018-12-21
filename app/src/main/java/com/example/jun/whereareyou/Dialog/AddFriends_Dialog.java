package com.example.jun.whereareyou.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jun.whereareyou.Data.ListViewChatItem;
import com.example.jun.whereareyou.Data.User;
import com.example.jun.whereareyou.R;

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

    private String userID;
    private int flag = 0;

    public AddFriends_Dialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public AddFriends_Dialog(Context context, String userID){
        super(context);
        this.context = context;
        this.userID = userID;
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

        if(userID != null){
            textview_user_ID.setText(userID);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.findPwDialogCancelTv:
                cancel();
                break;
            case R.id.findPwDialogFindTv:
                flag = 0;
               //user_id로 검색후 user_id와 일치하는 유저 닉네임 등등 반환.
                User user = new User();

                Request_Dialog request_dialog = new Request_Dialog(getContext(), user);
                request_dialog.setDialogListener(new Request_Dialog.MyDialogListener() {
                    @Override
                    public void onPositiveClicked() {
                        flag = 1;
                    }

                    @Override
                    public void onNegativeClicked() {

                    }
                });
                request_dialog.show();
                if(flag == 1) {
                    dialogListener.onPositiveClicked(user);
                    dismiss();
                }
                else{

                }
                break;
        }
    }
    public void setDialogListener(MyDialogListener dialogListener){
        this.dialogListener = dialogListener;
    }

}