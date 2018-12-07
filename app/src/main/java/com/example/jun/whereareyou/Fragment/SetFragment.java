package com.example.jun.whereareyou.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.jun.whereareyou.Activity.MainActivity;
import com.example.jun.whereareyou.Activity.PromFriendsActivity;
import com.example.jun.whereareyou.Adapter.FriendsListAdapter;
import com.example.jun.whereareyou.Data.FriendProfile;
import com.example.jun.whereareyou.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SetFragment extends Fragment {
    Toolbar promToolbar;
    ListView friendListView;
    TextView cntTextView;

    String dateStr = "";
    String timeStr = "";
    TextView dateTextView;
    final static int RES_CODE = 1000;
    ArrayList<FriendProfile> friendsArrayList = new ArrayList<FriendProfile>();

    public SetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dateTextView = (TextView) getActivity().findViewById(R.id.prom_start_time_text);
        cntTextView = (TextView) getActivity().findViewById(R.id.prom_cnt_text);

//        setToolbar();
        setDateTimePicker();


        friendListView = (ListView) getActivity().findViewById(R.id.prom_set_friend_list);

        Button friendBtn = (Button) getActivity().findViewById(R.id.prom_friend_btn);
        friendBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), PromFriendsActivity.class);
                startActivityForResult(intent, RES_CODE);

            }
        });
    }

    public void setListView() {
        final FriendsListAdapter friendsListAdapter = new FriendsListAdapter(getActivity(), friendsArrayList);
        friendListView.setAdapter(friendsListAdapter);
    }

//    public void setToolbar() {
//        promToolbar = (Toolbar) getActivity().findViewById(R.id.set_prom_toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(promToolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("설정");
//    }

    public void setDateTimePicker() {
        DatePicker datePicker = (DatePicker) getActivity().findViewById(R.id.prom_datepicker);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    dateStr = year + "년 " + monthOfYear + "월 " + dayOfMonth + "일";

                    if(!dateStr.equals("") && !timeStr.equals(""))
                        dateTextView.setText(dateStr + " " + timeStr);
                }
            });
        }

        TimePicker timePicker = (TimePicker) getActivity().findViewById(R.id.prom_timepicker);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                timeStr = hourOfDay + "시 " + minute + "분";

                if(!dateStr.equals("") && !timeStr.equals(""))
                    dateTextView.setText(dateStr + " " + timeStr);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == getActivity().RESULT_OK) {
            if(requestCode == RES_CODE) {
                friendsArrayList = data.getParcelableArrayListExtra("KEY");
                printSelectedList();
                setListView();
                cntTextView.setText(friendsArrayList.size() + "명");
            }

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void printSelectedList() {
        for(int i = 0; i < friendsArrayList.size(); i++)
            Log.i(""+i, friendsArrayList.get(i).getName());

    }
}
