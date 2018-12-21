package com.example.jun.whereareyou.Module;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.jun.whereareyou.Activity.MainActivity;
import com.example.jun.whereareyou.Data.GpsInfo;

import javax.xml.transform.Result;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import static android.os.SystemClock.sleep;

public class UpdateWorker extends Worker {

    public UpdateWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {

        myCompress();
        sleep(500);

        return Result.success();
    }

    public void myCompress(){
        GpsInfo gps;

        gps = new GpsInfo(getApplicationContext());
        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // POST 하면 됨

            Log.d("LatLng", "Lat : " + latitude+ ", Lng : " + longitude);

        } else {
            // GPS 를 사용할수 없으므로
            gps.showSettingsAlert();
        }
    }
}