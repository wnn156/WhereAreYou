package com.example.jun.whereareyou.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.jun.whereareyou.Data.GpsInfo;
import com.example.jun.whereareyou.Data.ListViewChatItem;
import com.example.jun.whereareyou.Dialog.Prom_Dialog;
import com.example.jun.whereareyou.Fragment.ChatFragment;
import com.example.jun.whereareyou.Fragment.FriendsFragment;
import com.example.jun.whereareyou.Fragment.SetFragment;
import com.example.jun.whereareyou.Module.UpdateWorker;
import com.example.jun.whereareyou.R;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.concurrent.TimeUnit;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    private ListViewChatItem item;
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;
    // GPSTracker class
    private static GpsInfo gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PeriodicWorkRequest.Builder UpdateBuilder =
                new PeriodicWorkRequest.Builder(UpdateWorker.class, 12,
                        TimeUnit.HOURS);
// ...if you want, you can apply constraints to the builder here...

// Create the actual work object:
        PeriodicWorkRequest UpdateCheckWork = UpdateBuilder.build();
// Then enqueue the recurring task:
        WorkManager.getInstance().enqueue(UpdateCheckWork);


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);

        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);

        callPermission();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                anim();
                Toast.makeText(this, "Floating Action Button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab1:
                anim();
                Prom_Dialog prom_dialog = new Prom_Dialog(this);
                prom_dialog.setDialogListener(new Prom_Dialog.MyDialogListener() {  // MyDialogListener 를 구현
                    @Override
                    public void onPositiveClicked(ListViewChatItem item) {
                        setResult(item);
                    }

                    @Override
                    public void onNegativeClicked() {
                        Log.d("MyDialogListener","onNegativeClicked");
                    }
                });
                prom_dialog.show();
                break;
            case R.id.fab2:
                anim();
                Toast.makeText(this, "Button2", Toast.LENGTH_SHORT).show();
                break;
        }

    }
    public void anim() {

        if (isFabOpen) {
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
        } else {
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
        }
    }

    private void setResult(ListViewChatItem item){
        this.item = item;
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            if (position == 0)
                return new FriendsFragment();
            else if(position==1)
                return new ChatFragment();
            else
                return new SetFragment();
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_ACCESS_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            isAccessFineLocation = true;

        } else if (requestCode == PERMISSIONS_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            isAccessCoarseLocation = true;
        }

        if (isAccessFineLocation && isAccessCoarseLocation) {
            isPermission = true;
        }
    }

    // 전화번호 권한 요청
    private void callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
    }


}
