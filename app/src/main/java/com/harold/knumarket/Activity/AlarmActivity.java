package com.harold.knumarket.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harold.knumarket.User_Info;
import com.harold.knumarket.Webserver_Url;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by Gan on 2014-11-28.
 */
public class AlarmActivity extends Activity {

    private ArrayList<String> userKeyword;

    public static final int REQUEST_CODE_MAIN = 1001;
    public static final int REQUEST_CODE_MYPAGE = 1005;

    public static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = "Search";
    private JSONArray jArray;
    private postListLoading task;
    private int line_num = 0;
    //웹서버 url정보를 WebServer_Url클래스 하나로 관리함(싱글톤 패턴 사용)
    private String Url;
    private static int keywordCount = 0;
    Map m = new TreeMap();
    private postListLoading task2;
    private postListLoading task3;
    private postListLoading task4;
    private postListLoading task5;
    private boolean isTasking = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
    }
    @Override
    public void onStart() {
        super.onStart();

        User_Info userInfo = User_Info.getUser_info();
        userKeyword = userInfo.getClient_keyword();

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        userInfo.LoadAlarmPosts(pref);

        Url = Webserver_Url.getInstance().getUrl();
        Log.i("KNU_Market/Alarm_Act", "Url=" + Url);

        if(line_num != 0) {
            LinearLayout linearLayout_vertic = (LinearLayout) findViewById(R.id.list_vertical);
            linearLayout_vertic.removeAllViews();   //기존의 검색된 리스트들을 모두 삭제하고, 새로고침
        }

        //작업 부분
        boolean netStat = false;
        netStat = checkNetStat();//네트워크 상태 확인
        if(netStat){//WIFI나 데이터네트워크 사용 가능
            if(true) {
                task = new postListLoading();
                task2 = new postListLoading();
                task3 = new postListLoading();
                task4 = new postListLoading();
                task5 = new postListLoading();
                ///////////////////////////////////////////////////////////////

                keywordCount = 0;
                m.clear();

                /*
                task.execute("JSP/RequestSearch.jsp?search_keyword="+userKeyword.get(0));
                while(keywordCount < 1);
                task2.execute("JSP/RequestSearch.jsp?search_keyword="+userKeyword.get(1));
                while(keywordCount < 2);
                task3.execute("JSP/RequestSearch.jsp?search_keyword="+userKeyword.get(2));
                while(keywordCount < 3);
                task4.execute("JSP/RequestSearch.jsp?search_keyword="+userKeyword.get(3));
                while(keywordCount < 4);
                task5.execute("JSP/RequestSearch.jsp?search_keyword="+userKeyword.get(4));

                String key1 = userKeyword.get(0);
                String key2 = userKeyword.get(1);
                String key3 = userKeyword.get(2);
                String key4 = userKeyword.get(3);
                String key5 = userKeyword.get(4);

                Log.i("KNU_Market/Alarm_Act", "keyword1=" + userKeyword.get(0));
                Log.i("KNU_Market/Alarm_Act", "keyword2=" + userKeyword.get(1));
                Log.i("KNU_Market/Alarm_Act", "keyword3=" + userKeyword.get(2));
                Log.i("KNU_Market/Alarm_Act", "keyword4=" + userKeyword.get(3));
                Log.i("KNU_Market/Alarm_Act", "keyword5=" + userKeyword.get(4));
                //*/


                Set<String> alarmPosts;
                alarmPosts = userInfo.getAlarmPosts();

                int alarmPostCount = userInfo.getAlarmPostCount();
                int j=0;
                for(Iterator i = alarmPosts.iterator(); i.hasNext(); ) {
                    new postListLoading().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"JSP/RequestPost.jsp?post_no="+i.next());
                    //task(j).execute("JSP/RequestPost.jsp?post_no="+i.next());
                    j++;
                }
                //updateView();


                /*
                if(key1.length() != 0 && !key1.contains(" "))
                    task.execute("JSP/RequestSearch.jsp?search_keyword="+key1);
                else
                    keywordCount++;

                if(key2.length() != 0 && !key2.contains(" "))
                    task2.execute("JSP/RequestSearch.jsp?search_keyword="+key2);
                else
                    keywordCount++;

                //if(key3 != null && key3 != "" && key3 != " ")
                if(key3.length() != 0 && !key3.contains(" "))
                    task3.execute("JSP/RequestSearch.jsp?search_keyword="+key3);
                else
                    keywordCount++;

                if(key4.length() != 0 && !key4.contains(" "))
                    task4.execute("JSP/RequestSearch.jsp?search_keyword="+key4);
                else
                    keywordCount++;

                if(key5.length() != 0 && !key5.contains(" "))
                    task5.execute("JSP/RequestSearch.jsp?search_keyword="+key5);
                else {
                    Log.i("KNU_Market/Alarm_Act", "keywordCount0=" + keywordCount);
                    keywordCount++;
                    //updateView(jArray);
                }
                   // keywordCount++;
                //*/


               // while(keywordCount < 5);    // wait for threads


                //task.execute("JSP/RequestMainList.jsp");        // 지금은 메인화면 코드
                // 검색할 키워드를 서버의 jsp에 보내는 코드 만들어야 //
                ///////////////////////////////////////////////////////////////
            }
        }
        else{//인터넷 연결 불가
            Button refresh = new Button(getBaseContext());
            refresh.setText("새로고침");
            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getApplicationContext(), "새로고침 클릭", Toast.LENGTH_SHORT).show();
                    //onResume();
                }
            });
            ((LinearLayout)findViewById(R.id.list_vertical)).addView(refresh);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
    //웹서버에서 받아온 정보(상품명,가격,이미지파일명)를 출력
    synchronized public void updateView(){

        int product_count = 0;

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;


        Set set = m.keySet();
        line_num = set.size();
        Object []hmKeys = set.toArray();



        for(int i = 0; i < line_num  ; i++){
            LinearLayout new_linearLayout = new LinearLayout(getBaseContext());
            //new_linearLayout.setWeightSum(1);
            // for(int j = 0; j < 3; j++){
            JSONObject json = null;
            try {
                //json = (JSONObject)hmKeys[i-1];


                //Object key = hmKeys[i-1];

                Integer key = (Integer)hmKeys[i];
                json = (JSONObject)m.get(key);
                Log.i("KNU_Market/Alarm_Act_updateView", "key=" + key);
                Log.i("KNU_Market/Alarm_Act_updateView", "catch post_no=" + json.getInt("post_no"));




                //json = array.getJSONObject(product_count++);

                //임시로 웹뷰 사용 -> 이미지 버튼 형식으로 바꿔야함
                //WebView webView = (WebView) new WebView(getActivity());
                //webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
                //webView.loadUrl(Url+"Image/"+json.getString("imgUrl"));
                //new_linearLayout.addView(webView);
                //이미지만 웹뷰로 출력
                LinearLayout p_button = (LinearLayout) new LinearLayout(getBaseContext());
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        (screenWidth)-10, 200);
                param.setMargins(5,5,5,5);
                p_button.setLayoutParams(param);
                p_button.setPadding(0, 0, 0, 0);
                p_button.setOrientation(LinearLayout.HORIZONTAL);

                if(json.getString("product_state").equals("Sell")){
                    p_button.setBackgroundColor(Color.parseColor("#C3FF7961"));
                }
                else{
                    p_button.setBackgroundColor(Color.parseColor("#B2CCFF"));
                }
               //p_button.setBackgroundColor(Color.LTGRAY);
                p_button.setGravity(Gravity.FILL);
                p_button.setId(json.getInt("post_no"));//Post의 번호를 각 버튼의 ID값으로 사용
                p_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getActivity().getApplicationContext(),"Post_no:"+v.getId(),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getBaseContext(),PostActivity.class);
                        intent.putExtra("post_no",v.getId());
                        startActivity(intent);
                    }
                });

                ImageView p_img = (ImageView) new ImageView(getBaseContext());
                LinearLayout.LayoutParams img_param =
                        //new LinearLayout.LayoutParams(200,235);
                        new LinearLayout.LayoutParams(200, 200);
                p_img.setLayoutParams(img_param);
                //p_img.setImageDrawable(null);
                p_img.setScaleType(ImageView.ScaleType.CENTER_CROP);




                LinearLayout p_box = (LinearLayout) new LinearLayout(getBaseContext());
                LinearLayout.LayoutParams box_param = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                box_param.setMargins(5,5,5,5);
                p_box.setLayoutParams(param);
                p_box.setOrientation(LinearLayout.VERTICAL);
                p_box.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

                TextView p_name = (TextView) new TextView(getBaseContext());
                TextView p_price = (TextView) new TextView(getBaseContext());

                p_name.setTextSize(15);
                p_price.setTextSize(15);
                p_name.setTextColor(Color.BLACK);
                p_price.setTextColor(Color.BLACK);
                p_name.setGravity(Gravity.FILL);
                p_button.setGravity(Gravity.FILL);

                //AUIL ImageLoader 사용
                //AUIL 이미지 옵션 설정
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.drawable.ic_empty) // 로딩중 이미지 설정
                        .showImageForEmptyUri(R.drawable.ic_empty) // Uri주소가 잘못되었을경우(이미지없을때)
                        .showImageOnFail(R.drawable.ic_error) // 로딩 실패시
                        .resetViewBeforeLoading(false)  // 로딩전에 뷰를 리셋하는건데 false로 하세요 과부하!
                        .delayBeforeLoading(0) // 로딩전 딜레이라는데 필요한일이 있을까요..?ㅋㅋ
                        .cacheInMemory(true) // 메모리케시 사용여부   (사용하면 빨라지지만 많은 이미지 캐싱할경우 outOfMemory Exception발생할 수 있어요)
                        .cacheOnDisc(true) // 디스크캐쉬를 사용여부(사용하세요왠만하면)
                                //.preProcessor(...) // 비트맵 띄우기전에 프로세스 (BitmapProcessor이라는 인터페이스를 구연하면 process(Bitmap image)라는 메소드를 사용할 수 있어요. 처리하실게 있으면 작성하셔서 이안에 넣어주시면 됩니다.)
                                //.postProcessor(...) // 비트맵 띄운후 프로세스 (위와같이 BitmapProcessor로 처리)
                        .considerExifParams(false) // 사진이미지의 회전률 고려할건지
                        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // 스케일타입설정   (일부밖에없습니다. 제가 centerCrop이 없어서 라이브러리 다 뒤져봤는데 없더라구요. 다른방법이 있습니다. 아래 설명해드릴게요.)
                        .bitmapConfig(Bitmap.Config.ARGB_8888) // 이미지 컬러방식
                        .build();

                ImageLoader imageLoader = ImageLoader.getInstance();
                Log.i("KNU_Market/Alarm_Act_updateView", "Url=" + Url+"Image/"+json.getString("img1Url"));
                imageLoader.displayImage(Url+"Image/"+json.getString("img1Url"), p_img, options, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                    }
                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                    }
                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    }
                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });
                //AUIL ImageLoader 사용

                p_name.setText(json.getString("name"));
                p_price.setText("[\\"+json.getString("price")+"]");

                /*
                p_price.setGravity(Gravity.LEFT);
                p_name.setGravity(Gravity.LEFT);
                param.setMargins(10,5,5,5);
                p_name.setLayoutParams(param);
                p_price.setLayoutParams(param);
                //*/

                p_box.addView(p_name);
                p_box.addView(p_price);

                p_button.addView(p_img);
                p_button.addView(p_box);

                //Button button = (Button) new Button(getActivity());
                //button.setText(json.getString("name")+"\n"+json.getString("price"));
                //button.setWidth(screenWidth/3);
                new_linearLayout.addView(p_button);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //}
            LinearLayout linearLayout_vertic = (LinearLayout)findViewById(R.id.list_vertical);
            linearLayout_vertic.addView(new_linearLayout);
        }
        Log.i("KNU_Market/Alarm_Act", "///////////// END UPDATE VIEW //////////////");
    }

    public void mergeKeyword(JSONArray jArray) {

        JSONObject json = null;
        int product_count = 0;

        //line_num = jArray.length();
        line_num = 1;
        for(int i = 1; i <= line_num  ; i++) {
            try {
                json = jArray.getJSONObject(product_count++);
                m.put(json.getInt("post_no"), json);
                Log.i("KNU_Market/Alarm_Act", "catch post_no=" + json.getInt("post_no"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        keywordCount++;
        Log.i("KNU_Market/Alarm_Act", "keywordCount2=" + keywordCount);

        User_Info userInfo = User_Info.getUser_info();
        Log.i("KNU_Market/Alarm_Act", "AlarmPostCount=" + userInfo.getAlarmPostCount());
        if(keywordCount == userInfo.getAlarmPostCount()) {
            keywordCount = 0;
            updateView();
        }

        /*
        keywordCount++;
        if(keywordCount == 5) {
            JSONArray jArray2 = null;
            for(int i=0; i < m.size(); i++) {
                jArray2.
            }
            updateView(jArray);
        }//*/
    }

    class postListLoading extends AsyncTask<String, Void, String> {

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
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json = null;
                jArray = new JSONArray(result);//JSON 데이터 형식으로 파싱
                //updateView(jArray);//받아온 정보로 화면 표시
                mergeKeyword(jArray); // thread 5개에서 각각의 키워드로 json 받아와서 json pool에 merge

            } catch (JSONException e) {
                e.printStackTrace();
                //keywordCount++;     // no results makes exception?
            }
        }
    }

    public void onClick(View v){

        int id = v.getId();
        Intent intent = null;

        switch (id){

            case R.id.btn_goAddPost:
                intent = new Intent(getBaseContext(), addPostActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
                break;

            case R.id.btn_home:
                intent = new Intent(getBaseContext(), MainActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                //startActivityForResult(intent, REQUEST_CODE_MAIN);
                startActivity(intent);
                finish();
                break;

            //// insert button listener for MYPAGE
            case R.id.btn_myPage:
                intent = new Intent(getBaseContext(), MyPageActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                //startActivityForResult(intent, REQUEST_CODE_MYPAGE);
                startActivity(intent);
                finish();
                break;

            case R.id.btn_config:
                intent = new Intent(getBaseContext(), ConfigActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_search:
                intent = new Intent(getBaseContext(), SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_zzim:
                break;
            case R.id.btn_alarm:
                break;
        }
    }

}