package com.harold.knumarket.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.harold.knumarket.User_Info;
import com.harold.knumarket.Webserver_Url;
import com.knumarket.harold.knu_market.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

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

//import com.harold.knumarket.Item_Comment.ItemComment;
//import com.harold.knumarket.Item_Comment.ItemCommentListAdapter;

public class PostActivity extends Activity {

    public static final int REQUEST_CODE_MAIN = 1001;
    public static final int REQUEST_CODE_MYPAGE = 1005;
    private int post_no;

    //private String Url = "http://192.168.1.10:5001/KNU_Market/"; //웹서버 URL
    //private String Url = "http://155.230.29.182:5001/KNU_Market/"; //웹서버 URL
    //private String Url = "http://121.151.119.125:5001/KNU_Market/"; //웹서버 URL
    //웹서버 url정보를 WebServer_Url클래스 하나로 관리함(싱글톤 패턴 사용)

    private String Url = Webserver_Url.getInstance().getUrl();

    private JSONArray jArray;
    private postLoading loadTask;
    private postLoading deleteTask;
    private ViewPager mViewPager;
    ListView listView1;
    //ItemCommentListAdapter adapter;
    private String seller_phoneNum;
    private String client_id;

    private class ImageViewAdapter extends PagerAdapter{

        Context context;
        ArrayList<String> imgUrls = new ArrayList<String>();

        private ImageViewAdapter(Context context,ArrayList<String> imgUrls) {
            this.context = context;
            this.imgUrls = imgUrls;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView imageView = new ImageView(context);

            if(!imgUrls.get(position).equals("null")) {

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
                imageLoader.displayImage(Url + "Image/" + imgUrls.get(position), imageView, options);
                //Toast.makeText(context.getApplicationContext(),imgUrls.get(position),Toast.LENGTH_SHORT).show();
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ((ViewPager) container).addView(imageView);
                return imageView;
            }
            else{
                imageView.setImageResource(R.drawable.ic_empty);
                return imageView;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView((ImageView) object);
        }

        @Override
        public int getCount() {
            int count = 0;
            for(int i=0; i <imgUrls.size(); i++){
                if(!imgUrls.get(i).equals("null")) {
                    count++;
                }
            }
            return count;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view ==((ImageView) object);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Intent intent = getIntent();
        post_no = intent.getExtras().getInt("post_no");
        mViewPager = (ViewPager) findViewById(R.id.post_viewpager);

        loadTask = new postLoading();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            loadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "JSP/RequestPost.jsp?post_no=" + post_no);
        }else {
            loadTask.execute("JSP/RequestPost.jsp?post_no=" + post_no);
        }

        // 댓글기능 -> 임시 삭제
        /*
        // 리스트뷰 객체 참조
        listView1 = (ListView) findViewById(R.id.commentList);

        // 어댑터 객체 생성
        adapter = new ItemCommentListAdapter(getApplicationContext());

        // 아이템 데이터 만들기
        Resources res = getResources();
        adapter.addItem(new ItemComment("KNU MARKET", "댓글 되요?"));
        adapter.addItem(new ItemComment("id test", "아아 id test"));
        adapter.addItem(new ItemComment("qwer",  "asdfqwer"));
        adapter.addItem(new ItemComment("Administrator", "댓글 확인했습니다")); // test

        // 리스트뷰에 어댑터 설정
        listView1.setAdapter(adapter);

        // 새로 정의한 리스너로 객체를 만들어 설정
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemComment curItem = (ItemComment) adapter.getItem(position);
                String[] curData = curItem.getData();
                //Toast.makeText(context, "Selected : " + curData[0], 1000).show();
                Toast.makeText(getApplicationContext(), "comment : " + curData[1], Toast.LENGTH_SHORT).show();

                Resources res = getResources();
            }
        });
        */
    }

    public void onClick(View v){

        int id = v.getId();
        Intent intent = null;

        switch (id){

            case R.id.btn_call:
                intent = new Intent( Intent.ACTION_DIAL );
                intent.setData( Uri.parse("tel:"+seller_phoneNum) );
                startActivity( intent );
                break;

            case R.id.btn_sms:
                intent = new Intent( Intent.ACTION_VIEW );
                intent.setData( Uri.parse("sms:"+seller_phoneNum) );
                startActivity( intent );
                break;

            //// insert button listener for MYPAGE
            case R.id.btn_zzim:
                //intent = new Intent(getBaseContext(), MyPageActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                //startActivityForResult(intent, REQUEST_CODE_MYPAGE);
                break;

            case R.id.btn_temp:
                User_Info user_info = User_Info.getUser_info();
                if(user_info.getClient_State()) {//현재 로그인 상태인지 확인
                    if(User_Info.getUser_info().getClient_Id().equals(client_id)){//유저가 작성자인지 확인

                        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(PostActivity.this);
                        alert_confirm.setMessage("해당 상품을 삭제 하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteTask = new postLoading();
                                        //상품 삭제 요청
                                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                            deleteTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"JSP/RemovePost.jsp?post_no="+post_no);
                                        }else {
                                            deleteTask.execute("JSP/RemovePost.jsp?post_no="+post_no);
                                        }
                                    }
                                }).setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        return;// 'No'
                                    }
                                });
                        AlertDialog alert = alert_confirm.create();
                        alert.show();
                    }
                    else{
                        //상품 등록한 유저가 아닐 경우
                        Toast.makeText(getApplicationContext(),"작성자만 삭제 가능합니다.",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    //로그인 하지 않은경우 로그인 화면으로 전환
                    intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    public  void initializeViewPager(JSONArray array){

        ArrayList<String> imgUrls = new ArrayList<String>();
        JSONObject object = null;

        try {
            object = array.getJSONObject(0);
            for(int i = 0; i < 3 ; i++){
                imgUrls.add(object.getString("img"+(i+1)+"Url"));
                /*if(!imgUrls.get(i).equals("null")) {
                    p_detail.append("\n" + imgUrls.get(i));
                }*/
            }
            ImageViewAdapter adapter = new ImageViewAdapter(this,imgUrls);
            mViewPager.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateView(JSONArray array) {

        TextView p_name = (TextView) findViewById(R.id.ItemEditText01);
        TextView p_price = (TextView) findViewById(R.id.ItemEditText03);
        TextView p_client = (TextView) findViewById(R.id.ItemText09);
        TextView p_profile = (TextView) findViewById(R.id.ItemText10);
        TextView p_detail = (TextView) findViewById(R.id.ItemEditText02);
        TextView p_keyword1 = (TextView) findViewById(R.id.ItemText07);
        TextView p_keyword2 = (TextView) findViewById(R.id.ItemText08);
        TextView p_keyword3 = (TextView) findViewById(R.id.ItemText03);

        try {
            JSONObject json = array.getJSONObject(0);
            p_name.setText(json.getString("name"));
            p_price.setText("[\\"+json.getString("price")+"]");

            client_id = json.getString("client");
            p_client.setText(client_id );

            //p_profile.setText(json.getString("profile"));
            p_detail.setText(json.getString("detail"));
            p_keyword1.setText(json.getString("keyword1"));
            p_keyword2.setText(json.getString("keyword2"));
            p_keyword3.setText(json.getString("keyword3"));

            seller_phoneNum = json.getString("seller_phone_num");

            TextView p_text1 = (TextView) findViewById(R.id.Buy);
            TextView p_text2 = (TextView) findViewById(R.id.Sell);
            Log.i("KNU_Market/Post_Act", "Sell=" + json.getString("product_state"));

            if(json.getString("product_state").equals("Sell")){
                p_text1.setTextColor(Color.parseColor("#82979797"));
                p_text1.setTextSize(15);
            }
            else{
                p_text2.setTextColor(Color.parseColor("#82979797"));
                p_text2.setTextSize(15);
            }

            String categoryID = json.getString("category_id");
            Log.i("KNU_Market/Post_Act", "category_code=" + categoryID);
            setDecodeCatID(categoryID);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
     }

    public void setDecodeCatID(String categoryID){

        Log.i("PostActivity setDecodeCatId","Category_ID = "+categoryID);
        TextView cat_hig =(TextView) findViewById(R.id.ItemText04); //대분류
        TextView cat_mid =(TextView) findViewById(R.id.ItemText05); //중분류
        TextView cat_low =(TextView) findViewById(R.id.ItemText06); //소분류

        int cat_high_Code = Character.getNumericValue(categoryID.charAt(0));
        int cat_mid_Code =  Character.getNumericValue(categoryID.charAt(1));
        int cat_low_Code = Character.getNumericValue(categoryID.charAt(2));

        Log.i("PostActivity setDecodeCatId","Category_ID = "+cat_high_Code+cat_mid_Code+cat_low_Code);

        switch(cat_high_Code){
            case 0:
                cat_hig.setText("중고서적");
                switch (cat_mid_Code){
                    case 0:
                        cat_mid.setText("전공");
                        switch (cat_low_Code){
                            case 0:
                                cat_low.setText("IT대");
                                break;
                            case 1:
                                cat_low.setText("공대");
                                break;
                            case 2:
                                cat_low.setText("경상대");
                                break;
                            case 3:
                                cat_low.setText("인문대");
                                break;
                        }
                        break;
                    case 1:
                        cat_mid.setText("교양");
                        switch (cat_low_Code){
                            case 0:
                                cat_low.setText("자연과학");
                                break;
                            case 1:
                                cat_low.setText("기초수학");
                                break;
                            case 2:
                                cat_low.setText("인문사회");
                                break;
                            case 3:
                                cat_low.setText("스포츠");
                                break;
                        }
                        break;
                    case 2:
                        cat_mid.setText("일반");
                        switch (cat_low_Code){
                            case 0:
                                cat_low.setText("소설");
                                break;
                            case 1:
                                cat_low.setText("자기계발");
                                break;
                            case 2:
                                cat_low.setText("전문서적");
                                break;
                        }
                        break;
                }
                break;
            case 1:
                cat_hig.setText("자취용품");
                switch (cat_mid_Code){
                    case 0:
                        cat_mid.setText("가전");
                        switch (cat_low_Code) {
                            case 0:
                                cat_low.setText("없음");
                                break;
                        }
                        break;
                    case 1:
                        cat_mid.setText("주방");
                        switch (cat_low_Code) {
                            case 0:
                                cat_low.setText("없음");
                                break;
                        }
                        break;
                    case 2:
                        cat_mid.setText("가구");
                        switch (cat_low_Code) {
                            case 0:
                                cat_low.setText("없음");
                                break;
                        }
                        break;
                }
                break;
            case 2:
                cat_hig.setText("패션잡화");
                switch (cat_mid_Code){
                    case 0:
                        cat_mid.setText("남성");
                        switch (cat_low_Code) {
                            case 0:
                                cat_low.setText("없음");
                                break;
                        }
                        break;
                    case 1:
                        cat_mid.setText("여성");
                        switch (cat_low_Code) {
                            case 0:
                                cat_low.setText("없음");
                                break;
                        }
                        break;
                }
                break;
            case 3:
                cat_hig.setText("기타");
                switch (cat_mid_Code){
                    case 0:
                        cat_mid.setText("없음");
                        switch (cat_low_Code) {
                            case 0:
                                cat_low.setText("없음");
                                break;
                        }
                        break;
                }
                break;
        }
    }

    class postLoading extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            try {
                Log.i("KNU_Market/Post result= ",result);
                if(result.contains("Remove Success")) {
                    Toast.makeText(getApplicationContext(),"삭제 완료",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();
                }
                else if(result.contains("Remove Fail")){
                    Toast.makeText(getApplicationContext(),"삭제 실패",Toast.LENGTH_SHORT).show();
                }
                else if(result.contains("[")){
                    JSONObject json = null;
                    jArray = new JSONArray(result);//JSON 데이터 형식으로 파싱
                    updateView(jArray);//받아온 정보로 화면 표시
                    initializeViewPager(jArray);
                }
                else if(result.contains("Apache")){
                    Toast.makeText(getApplicationContext(),"서버 오류",Toast.LENGTH_SHORT).show();
                }
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
            Log.i("KNU_Market/Post Url= ", Url);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

