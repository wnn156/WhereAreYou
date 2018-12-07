package com.example.jun.whereareyou.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.example.jun.whereareyou.R;

import net.daum.mf.map.api.MapView;

public class ChattingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

    }
    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.list_menu, menu);

        return true;

    }



    /**

     * 메뉴 아이템을 클릭했을 때 발생되는 이벤트...

     * @param item

     * @return

     */

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {



        int id = item.getItemId();



        if( id == R.id.showMap ){
            Intent intent = new Intent(ChattingActivity.this, MapActivity.class);
            startActivity(intent);
            return true;

        }
        return super.onOptionsItemSelected(item);

    }

}
