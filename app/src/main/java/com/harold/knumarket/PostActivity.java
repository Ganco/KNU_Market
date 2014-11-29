package com.harold.knumarket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.harold.knumarket.Item_Comment.ItemComment;
import com.harold.knumarket.Item_Comment.ItemCommentListAdapter;
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
    private postLoading task;
    private ViewPager mViewPager;
    ListView listView1;
    ItemCommentListAdapter adapter;


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

        task = new postLoading();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"JSP/RequestPost.jsp?post_no="+post_no);
        }else {
            task.execute("JSP/RequestPost.jsp?post_no="+post_no);
        }
        //TextView textView = (TextView) findViewById(R.id.Post_textView);
        //textView.setText("post_no :"+post_no);



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


        /*
        //// insert button listener for MYPAGE ////
        Button btnMypage = (Button) findViewById(R.id.button6);
        btnMypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MyPageActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(intent, REQUEST_CODE_MYPAGE);
            }
        });
        ////
        //// insert button listener for Main
        Button btnMain = (Button) findViewById(R.id.button4);
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(intent, REQUEST_CODE_MAIN);
            }
        });
        ////
        */
    }

    public void onClick(View v){

        int id = v.getId();
        Intent intent = null;

        switch (id){

            case R.id.btn_call:
               // intent = new Intent(getBaseContext(), addPostActivity.class);
               // startActivity(intent);
                //finish();
                break;

            case R.id.btn_sms:
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
        TextView p_detail = (TextView) findViewById(R.id.ItemEditText02);

        try {
            JSONObject json = array.getJSONObject(0);
            p_name.setText(json.getString("name"));
            p_price.setText("[\\"+json.getString("price")+"]");
            p_client.setText(json.getString("client"));
            p_detail.setText(json.getString("detail"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
     }

    class postLoading extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json = null;
                jArray = new JSONArray(result);//JSON 데이터 형식으로 파싱
                updateView(jArray);//받아온 정보로 화면 표시
                initializeViewPager(jArray);
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

