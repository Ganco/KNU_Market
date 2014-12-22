package com.harold.knumarket.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harold.knumarket.Activity.PostActivity;
import com.harold.knumarket.CustomGridViewAdapter;
import com.harold.knumarket.Post_DTO;
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

public class Fragment_section1 extends Fragment implements AbsListView.OnScrollListener {

    private static final String TAG = "Frag_sec1";
    private static final String SORT = "ASC";
    private JSONArray jArray;
    private postListLoading task;
    private GridView mGridView;
    private CustomGridViewAdapter mAdapterGrid;
    private boolean mIsLoading;

    //웹서버 url정보를 WebServer_Url클래스 하나로 관리함(싱글톤 패턴 사용)
    private String Url;

    public boolean checkNetStat() {
        try {
            ConnectivityManager connMan;
            connMan = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = connMan.getAllNetworkInfo();

            for (int i = 0; i < netInfo.length; i++) {
                Log.d("TAG",netInfo[i].getTypeName() + " " + netInfo[i].isConnected());
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

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Post_DTO item = (Post_DTO)mAdapterGrid.getItem(position);
            Intent intent = new Intent(getActivity(), PostActivity.class);
            intent.putExtra("post_no", item.getPost_no());
            startActivity(intent);
        }
    };

    public void requestAllPost(int start, int amount, String sort){
        task = new postListLoading();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"JSP/RequestMainList.jsp?s="+start+"&a="+amount+"&d="+sort);
        }else {
            task.execute("JSP/RequestMainList.jsp?s="+start+"&a="+amount+"&d="+sort);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //View rootView = inflater.inflate(R.layout.fragment_section1, container, false);
        View rootView = inflater.inflate(R.layout.fragment_section1, container, false);

        mAdapterGrid = new CustomGridViewAdapter(getActivity().getApplicationContext(), null, R.layout.grid_item);
        mGridView = (GridView)((ViewGroup)rootView).findViewById(R.id.gridView1);

        Log.d(TAG, "Before setAdapter");
        mGridView.setAdapter(mAdapterGrid);
        Log.d(TAG, "After setAdapter");
        mGridView.setOnItemClickListener(mItemClickListener);
        mGridView.setOnScrollListener(this);

        boolean netStat = false;
        netStat = checkNetStat();//네트워크 상태 확인
        if(netStat){//WIFI나 데이터네트워크 사용 가능
            if(true) {
                requestAllPost(1, 12, SORT);
            }
        }
        else{//네트워크 연결 불가

        }
        // mode : a(All), p(Popular), r(Recommend)
        //DDMHelper.RequestGoodsByCategory(this, mHandler, 100, "p", id, 0, 30);

        Bundle extras = getActivity().getIntent().getExtras();
        if ( extras != null ) {
            netStat = checkNetStat();//네트워크 상태 확인
            //WIFI나 데이터네트워크 사용 가능
            if(netStat){
                requestAllPost(1, 12, SORT);
            }
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(task != null) {
            task.cancel(true);
        }
        //Toast.makeText(getActivity().getApplicationContext(),"Task status = "+String.valueOf(task.isCancelled()),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        Log.d(TAG,"onScroll(): mAdapterGrid.getCount()="+mAdapterGrid.getCount());
        if(mAdapterGrid.getCount() < 12)
            return;

        int loadedItems = firstVisibleItem + visibleItemCount;
        if( (loadedItems == totalItemCount) && !mIsLoading && mAdapterGrid.getCount() >= 1){
            Log.d(TAG,"onScroll(): loaded:"+loadedItems+"/total:"+totalItemCount+"IsLoading:"+mIsLoading );
            mIsLoading = true;
            requestAllPost(mAdapterGrid.getCount()+1, 12, SORT);
            //DDMHelper.RequestGoodsByCategory(this, mHandler, 100, "p", mCategoryId, mAdapterGrid.getCount(), 30);
        }
    }

    //웹서버에서 받아온 정보(상품명,가격,이미지파일명)를 출력
    public void updateView(JSONArray array){

        int product_count = array.length()-1;
        int line_num;

        if(array.length() == 0){
            line_num = 0;
        }
        else{
            line_num = (array.length()/3)+1;
        }

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;

        for(int i = 1; i <= line_num  ; i++){
            LinearLayout new_linearLayout = new LinearLayout(getActivity());
            //new_linearLayout.setWeightSum(1);
            for(int j = 0; j < 3; j++){
                JSONObject json = null;
                try {
                    if(product_count >= 0) {
                        json = array.getJSONObject(product_count--);
                        //임시로 웹뷰 사용 -> 이미지 버튼 형식으로 바꿔야함
                        //WebView webView = (WebView) new WebView(getActivity());
                        //webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
                        //webView.loadUrl(Url+"Image/"+json.getString("imgUrl"));
                        //new_linearLayout.addView(webView);
                        //이미지만 웹뷰로 출력
                        LinearLayout p_button = (LinearLayout) new LinearLayout(getActivity());
                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                (screenWidth / 3) - 10, 320);
                        param.setMargins(5, 5, 5, 5);
                        p_button.setLayoutParams(param);
                        p_button.setPadding(0, 0, 0, 0);
                        p_button.setOrientation(LinearLayout.VERTICAL);

                        if(json.getString("product_state").equals("Sell")){
                            p_button.setBackgroundColor(Color.parseColor("#C3FF7961"));
                        }
                        else{
                            p_button.setBackgroundColor(Color.parseColor("#B2CCFF"));
                        }

                        p_button.setGravity(Gravity.FILL);
                        p_button.setId(json.getInt("post_no"));//Post의 번호를 각 버튼의 ID값으로 사용
                        p_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(getActivity().getApplicationContext(),"Post_no:"+v.getId(),Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), PostActivity.class);
                                intent.putExtra("post_no", v.getId());
                                startActivity(intent);
                            }
                        });

                        ImageView p_img = (ImageView) new ImageView(getActivity());
                        LinearLayout.LayoutParams img_param =
                                //new LinearLayout.LayoutParams(200,235);
                                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 235);
                        p_img.setLayoutParams(img_param);
                        //p_img.setImageDrawable(null);
                        p_img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        TextView p_name = (TextView) new TextView(getActivity());
                        TextView p_price = (TextView) new TextView(getActivity());

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
                        imageLoader.displayImage(Url + "Image/" + json.getString("imgUrl"), p_img, options, new ImageLoadingListener() {
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
                        p_name.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                        p_price.setText("[\\" + json.getString("price") + "]");
                        p_price.setGravity(Gravity.CENTER | Gravity.BOTTOM);

                        p_button.addView(p_img);
                        p_button.addView(p_name);
                        p_button.addView(p_price);
                        //Button button = (Button) new Button(getActivity());
                        //button.setText(json.getString("name")+"\n"+json.getString("price"));
                        //button.setWidth(screenWidth/3);
                        new_linearLayout.addView(p_button);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //LinearLayout linearLayout_vertic = (LinearLayout)getActivity().findViewById(R.id.f_sec1_linear_vertic);
            //linearLayout_vertic.addView(new_linearLayout);
        }
    }

    public boolean handleMessage(JSONArray array) {
        // TODO Auto-generated method stub
        //if (msg.what == 100) {
        //JSONObject json = new JSONObject((String) msg.obj);
        //Log.d(TAG, (String) msg.obj);
        //if (json.getInt("success") == 1) {
        ArrayList<Post_DTO> items = new ArrayList<Post_DTO>();
        int count = array.length();
        if (count > 0) {
            JSONArray rows = array;
            Log.d(TAG, "rows.length() = " + rows.length());
            for (int i = 0; i < count; i++) {
                JSONObject row = null;
                try {
                    row = rows.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Post_DTO item = new Post_DTO();
                item.fromJSONObject(row);
                items.add(item);
            }

            if (mAdapterGrid.getCount() > 0) {
                int found = 0;
                Post_DTO last = (Post_DTO) mAdapterGrid.getItem(mAdapterGrid.getCount() - 1);
                for (int i = 0; i < items.size(); i++) {
                    Post_DTO item = items.get(i);
                    if (last.equals(item)) {
                        found = i + 1;
                        break;
                    }
                }

                for (int i = 0; i < found; i++)
                    items.remove(0);
            }

            boolean bFirst = mAdapterGrid.getCount() == 0;
            mAdapterGrid.addItems(items);
            mAdapterGrid.notifyDataSetChanged();
            if (bFirst) {
                //mGridView.setAnimation(AnimationUtil.Alpha(0.0f, 1.0f, 500));
            }
            mIsLoading = false;
        } else {
            mIsLoading = true;
        }

        if (mAdapterGrid.getCount() > 0) {
            // mNoMoreItem.setVisibility(View.GONE);
        } else {
            //mNoMoreItem.setVisibility(View.VISIBLE);
        }
        return true;
    }

    class postListLoading extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json = null;
                Log.i("KNU_Market/Frgmt_sec1 - onPostExcute()","result="+result);
                jArray = new JSONArray(result);//JSON 데이터 형식으로 파싱
                handleMessage(jArray);
                //updateView(jArray);//받아온 정보로 화면 표시
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //웹에서 정보 가져오는 부분
        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            Url = Webserver_Url.getInstance().getUrl();
            HttpResponse response;
            HttpClient myClient = new DefaultHttpClient();
            HttpPost myConnection = new HttpPost(Url+urls[0]);
            Log.i("KNU_Market/Frgmt_sec1 - doInBackground()","Url="+Url+urls[0]);

            try {
                response = myClient.execute(myConnection);
                result = EntityUtils.toString(response.getEntity(), "UTF-8");
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //str = Url+urls[0];
            return result;
        }
    }
}
