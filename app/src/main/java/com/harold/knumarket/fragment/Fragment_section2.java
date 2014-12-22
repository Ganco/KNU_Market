package com.harold.knumarket.fragment;

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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Comparator;

public class Fragment_section2 extends Fragment implements AbsListView.OnScrollListener {

    private static final String TAG = "Frag_sec2";
    private static final String SORT = "DESC";
    private static final int NUM_OF_LOAD_ITEM = 3;

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
        View rootView = null;
        boolean netStat = false;
        netStat = checkNetStat();//네트워크 상태 확인

        if(netStat) {
            rootView = inflater.inflate(R.layout.fragment_section2, container, false);
            mAdapterGrid = new CustomGridViewAdapter(getActivity().getApplicationContext(), null, R.layout.grid_item);
            mGridView = (GridView) ((ViewGroup) rootView).findViewById(R.id.gridView2);
            mGridView.setAdapter(mAdapterGrid);
            mGridView.setOnItemClickListener(mItemClickListener);
            mGridView.setOnScrollListener(this);

            requestAllPost(1, 9, SORT);
            // mode : a(All), p(Popular), r(Recommend)
            //DDMHelper.RequestGoodsByCategory(this, mHandler, 100, "p", id, 0, 30);
            Bundle extras = getActivity().getIntent().getExtras();
            if (extras != null) {
                requestAllPost(1, 9, SORT);
            }
            return rootView;
        }
        else{
            //rootView = inflater.inflate(R.layout.fragment_section1, container, false);
            return rootView;
        }
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

        Log.d(TAG, "onScroll(): mAdapterGrid.getCount()=" + mAdapterGrid.getCount());
        Log.d(TAG, "onScroll(): first index=" +firstVisibleItem+" visible count="+visibleItemCount);
        int loadedItems = firstVisibleItem + visibleItemCount;
        Log.d(TAG,"onScroll(): loaded/total:"+loadedItems+"/"+totalItemCount+"-IsLoading:"+mIsLoading );

        if(mAdapterGrid.getCount() < NUM_OF_LOAD_ITEM)
            return;

        if( (loadedItems == totalItemCount) && !mIsLoading && mAdapterGrid.getCount() >= 1){
            mIsLoading = true;
            requestAllPost(mAdapterGrid.getCount()+1, NUM_OF_LOAD_ITEM, SORT);
            //DDMHelper.RequestGoodsByCategory(this, mHandler, 100, "p", mCategoryId, mAdapterGrid.getCount(), 30);
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
