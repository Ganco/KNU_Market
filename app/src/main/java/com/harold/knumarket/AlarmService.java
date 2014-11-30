package com.harold.knumarket;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Gan on 2014-12-01.
 */
public class AlarmService extends Service implements Runnable {

    public static final String TAG = "AlarmService";
    private int count=0;

    @Override
    public void onCreate() {
        super.onCreate();

        Thread myThread = new Thread(this);
        myThread.start();



        // preference로 키워드정보 로딩
        User_Info userInfo = User_Info.getUser_info();
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        //userInfo.LoadPreference(pref);
    }

    public void run() {
        while(true) {
            try {
                count++;

                Thread.sleep(5000); // 5000 -> 5초
                Log.i("KNU_Market/AlarmService", "count=" + count);


                // 각 keyword들로 각각 검색하기

                // if not exist in list,
                // add to list

                // update post_no

                // push alarm


            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
