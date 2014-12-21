package com.harold.knumarket;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.app.NotificationCompat.*;
import android.util.*;
import android.widget.*;

import com.harold.knumarket.Activity.MainActivity;
import com.knumarket.harold.knu_market.R;

/**
 * Created by Gan on 2014-12-01.
 */
//public class AlarmService extends Service implements Runnable {
public class AlarmService extends Service {
    public static final String TAG = "AlarmService";
    private int count=0;
    protected boolean mRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        //Thread myThread = new Thread(this);
        //myThread.start();

        // preference로 키워드정보 로딩
        User_Info userInfo = User_Info.getUser_info();
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        //userInfo.LoadPreference(pref);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
//               switch(msg.what) {}
//               Toast.makeText(getApplicationContext(), "알림!", 0).show();
            NotificationManager manager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(AlarmService.this);
            builder.setSmallIcon(R.drawable.icon)
                    .setContentTitle("새 상품이 등록되었습니다")
                    .setContentText("n 개의 새 상품")    // user_info의 전역변수 사용
                    .setAutoCancel(true) // Notification을 누를 경우 자동으로 닫히도록 처리(여기서 Action을 눌렀을때는 닫히지 않습니다.)
                    .setVibrate(new long[]{1000,2000})
                    .setTicker("새 상품이 등록되었습니다")   // Notification이 표시되면서 화면에 잠시 노출되는 내용.
                    .setProgress(0, 0, false); // Removes the progress bar

            /*
            // autoCancel : 한번 누르면 알림바에서 사라진다.
            // vibrate : 쉬고, 울리고, 쉬고, 울리고... 밀리세컨
            // 진동이 되려면 AndroidManifest.xml에 진동 권한을 줘야 한다..bigPicture(photo) // bigPicture의 이미지 파일은 Bitmap으로 처리되어야 합니다.
            .bigLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_gallery)) // 화면이 펼쳐진 상태에서 보여질 아이콘을 Bitmap으로 처리해줍니다.
             .setStyle(new NotificationCompat.BigPictureStyle() // BitPictureStyle을 적용하는 코드
            .addAction(android.R.drawable.ic_menu_share, getResources().getString(R.string.share), sendPendingIntent); // 하단의 공유 버튼
            */


            // 알람 클릭시 Activity를 화면에 띄운다.
            // alarm activity로 띄우면 preference 동작 유무 고려해야
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext()
                    , 0
                    , intent
                    //, Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    , PendingIntent.FLAG_UPDATE_CURRENT);



            // Notification을 눌렀을 경우 처리되는 Intent
            builder.setContentIntent(pIntent);
            manager.notify(1, builder.build());
        };
    };


    // 제일 중요한 메서드! (서비스 작동내용을 넣어준다.)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("service", "onStartCommand 실행");
        final int time = intent.getIntExtra("time", 0);
//          Toast.makeText(this, "안녕~ 난 서비스 : "+time, 0).show();

        // handler 통한 Thread 이용
        new Thread(new Runnable() {

            @Override
            public void run() {
                mRunning = true;
                while (mRunning) {
                    SystemClock.sleep(time * 1000);
                    mHandler.sendEmptyMessage(0);
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

        }).start();

        return START_STICKY_COMPATIBILITY;
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRunning = false;
        count = 0;
    }
}
