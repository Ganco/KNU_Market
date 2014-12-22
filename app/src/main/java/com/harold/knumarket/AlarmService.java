package com.harold.knumarket;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.app.NotificationCompat.*;
import android.util.*;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.harold.knumarket.Activity.AlarmActivity;
import com.harold.knumarket.Activity.MainActivity;
import com.harold.knumarket.Activity.PostActivity;
import com.knumarket.harold.knu_market.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Comparator;

/**
 * Created by Gan on 2014-12-01.
 */
//public class AlarmService extends Service implements Runnable {
public class AlarmService extends Service {
    public static final String TAG = "AlarmService";
    private int count=0;
    protected boolean mRunning = false;
    private int newPostCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        //Thread myThread = new Thread(this);
        //myThread.start();

        // preference로 키워드정보 로딩
        User_Info userInfo = User_Info.getUser_info();
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        userInfo.LoadPreference(pref);
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
                    .setContentText(newPostCount + "개의 새 상품")    //
                    .setAutoCancel(true) // Notification을 누를 경우 자동으로 닫히도록 처리(여기서 Action을 눌렀을때는 닫히지 않습니다.)
                    .setVibrate(new long[]{1000, 1000})
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
            Intent intent = new Intent(getApplicationContext(),AlarmActivity.class);
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
        //final int time = intent.getIntExtra("time", 0);
//      Toast.makeText(this, "안녕~ 난 서비스 : "+time, 0).show();



        final int time = 6;         // n 초마다 run
        // handler 통한 Thread 이용
        new Thread(new Runnable() {
            private String Url;
            private JSONArray jArray;
            private postListLoading task;
            private int line_num = 0;
            @Override
            public void run() {
                mRunning = true;
                User_Info userInfo = User_Info.getUser_info();
                SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);

                while (mRunning) {
                    //SystemClock.sleep(time * 1000);
                    SystemClock.sleep(time * 1000);
                    //Thread.sleep(5000); // 5000 -> 5초

                    userInfo.LoadAlarmOnOff(pref);
                    if(userInfo.getAlarmOnOff() == false)       // 알람이 off면 계속 쉰다
                        continue;

                    try {
                        count++;
                        Log.i("KNU_Market/AlarmService", "count=" + count);

                        mHandler.sendEmptyMessage(0);       // 푸쉬알림 띄우기


                        // preference로 키워드정보 로딩
                        userInfo.LoadPreference(pref);
                        userInfo.LoadLastPostNo(pref);





                        // 각 keyword들로 각각 검색하기

                        // if not exist in list,
                        // add to list

                        // update post_no

                        // push alarm


                        Url = Webserver_Url.getInstance().getUrl();
                        //Log.i("KNU_Market/AlarmService","Url="+Url);

                        //작업 부분
                        boolean netStat = false;
                        netStat = checkNetStat();//네트워크 상태 확인
                        if(netStat){//WIFI나 데이터네트워크 사용 가능
                            if(true) {
                                task = new postListLoading();
                                ///////////////////////////////////////////////////////////////
                                task.execute("JSP/RequestKeyword.jsp?after_post_no="+userInfo.getLastPostNo());        // 쿼리 요청
                                ///////////////////////////////////////////////////////////////
                            }
                        }
                        else{//인터넷 연결 불가
                            Log.i("KNU_Market/AlarmService Error","Url="+Url + "/JSP/RequestKeyword.jsp?after_post_no="+userInfo.getLastPostNo());
                        }


                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }
            }
            public boolean checkNetStat() {
                try {
                    ConnectivityManager connMan;
                    connMan = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo[] netInfo = connMan.getAllNetworkInfo();

                    for (int i = 0; i < netInfo.length; i++) {
                        Log.d("TAG", netInfo[i].getTypeName() + " " + netInfo[i].isConnected());
                    }

                    if (netInfo[0].isConnected() || netInfo[1].isConnected()) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    //throw exception
                    return false;
                }
            }

            public void keywordCheck(JSONArray array){
                int product_count = 0;
                line_num = array.length();

                for(int i = 1; i <= line_num  ; i++){

                    JSONObject json = null;
                    try {
                        json = array.getJSONObject(product_count++);

                        //판매-구매 상품 상태 색상으로 구분
                        if(json.getString("product_state").equals("Sell")){
                        }
                        else{
                        }
                        json.getString("name");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            class postListLoading extends AsyncTask<String, Void, String> {
                @Override
                protected void onPostExecute(String result) {
                    try {
                        JSONObject json = null;
                        jArray = new JSONArray(result);//JSON 데이터 형식으로 파싱
                        keywordCheck(jArray);  //받아온 정보 처리
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //웹에서 정보 가져오는 부분
                @Override
                protected String doInBackground(String... urls) {

                    String str = "";
                    HttpResponse response;
                    HttpClient myClient = new DefaultHttpClient();
                    HttpPost myConnection = new HttpPost(Url+urls[0]);

                    try {
                        response = myClient.execute(myConnection);
                        str = EntityUtils.toString(response.getEntity(), "UTF-8");

                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //str = Url+urls[0];
                    return str;
                }
            }

            class JSONComparator implements Comparator<JSONObject> {

                public int compare(JSONObject a, JSONObject b){
                    //valA and valB could be any simple type, such as number, string, whatever
                    String valA = null;
                    String valB = null;
                    try {
                        valA = a.getString("post_no");
                        valB = b.getString("post_no");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return valA.compareTo(valB);
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


