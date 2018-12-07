package com.example.jun.whereareyou.Activity;

import android.content.Intent;
import android.database.DataSetObserver;
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

import com.example.jun.whereareyou.Adapter.FriendsListAdapter;
import com.example.jun.whereareyou.Data.FriendProfile;
import com.example.jun.whereareyou.R;

import java.util.ArrayList;

public class PromFriendsActivity extends AppCompatActivity {
    ArrayList<FriendProfile> friendsArrayList = new ArrayList<FriendProfile>();
    ArrayList<FriendProfile> selectedArrayList = new ArrayList<FriendProfile>();
    boolean[] isSelected;
    Toolbar promFriendToolbar;
    ListView friendsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prom_friends);

        initializeToolBar();
        initializeArrayList();
        initializeListView();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("KEY", selectedArrayList);
                setResult(RESULT_OK, intent);

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
        friendsArrayList.add(new FriendProfile("윤서원", "image_url1"));
        friendsArrayList.add(new FriendProfile("김동욱", "image_url2"));
        friendsArrayList.add(new FriendProfile("성경화", "image_url3"));
        friendsArrayList.add(new FriendProfile("안준영", "image_url4"));

        isSelected = new boolean[friendsArrayList.size()];
        for(int i = 0; i < isSelected.length; i++)
            isSelected[i] = false;

    }

    public void initializeListView() {
        friendsListView = (ListView) findViewById(R.id.prom_friend_listview);

        final FriendsListAdapter friendsListAdapter = new FriendsListAdapter(PromFriendsActivity.this, friendsArrayList);

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
}
