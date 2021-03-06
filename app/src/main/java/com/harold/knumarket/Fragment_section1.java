package com.harold.knumarket;

import com.knumarket.harold.knu_market.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import java.text.CollationElementIterator;
import java.util.Comparator;

public class Fragment_section1 extends Fragment {

    public static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = "Fragment_section1";
    private JSONArray jArray;
    private postListLoading task;
    //private String Url = "http://192.168.0.4:5001/KNU_Market/"; //웹서버 URL
    //private String Url = "http://192.168.1.10:5001/KNU_Market/"; //웹서버 URL
    //private String Url = "http://155.230.29.182:5001/KNU_Market/"; //웹서버 URL
    private String Url = "http://211.51.176.248:5001/KNU_Market/"; //웹서버 URL

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_section1, container, false);

        //작업 부분
        boolean netStat = false;
        netStat = checkNetStat();//네트워크 상태 확인
        if(netStat){//WIFI나 데이터네트워크 사용 가능
            //TextView textView = (TextView) findViewById(R.id.textView1);
            //textView.setText("인터넷 연결 가능");
            if(true) {
                task = new postListLoading();
                task.execute("JSP/RequestMainList.jsp");
            }
        }
        else{//인터넷 연결 불가
            //TextView textView = (TextView) getActivity().findViewById(R.id.frag1_textView);
            //textView.setText("인터넷 연결 불가능");
            Button refresh = new Button(getActivity());
            refresh.setText("새로고침");
            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity().getApplicationContext(), "새로고침 클릭", Toast.LENGTH_SHORT).show();
                    //onResume();
                }
            });
            ((LinearLayout)getActivity().findViewById(R.id.linear_horiz)).addView(refresh);
        }
        //작업 부분
        //Bundle args = getArguments();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //웹서버에서 받아온 정보(상품명,가격,이미지파일명)를 출력
    public void updateView(JSONArray array){

        int product_count = 0;
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
                    json = array.getJSONObject(product_count++);

                    //임시로 웹뷰 사용 -> 이미지 버튼 형식으로 바꿔야함
                    //WebView webView = (WebView) new WebView(getActivity());
                    //webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
                    //webView.loadUrl(Url+"Image/"+json.getString("imgUrl"));
                    //new_linearLayout.addView(webView);
                    //이미지만 웹뷰로 출력
                    LinearLayout p_button = (LinearLayout) new LinearLayout(getActivity());
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                            (screenWidth/3)-10, 320);
                    param.setMargins(5,5,5,5);
                    p_button.setLayoutParams(param);
                    p_button.setPadding(0, 0, 0, 0);
                    p_button.setOrientation(LinearLayout.VERTICAL);
                    p_button.setBackgroundColor(Color.LTGRAY);
                    p_button.setGravity(Gravity.FILL);
                    p_button.setId(json.getInt("post_no"));//Post의 번호를 각 버튼의 ID값으로 사용
                    p_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(getActivity().getApplicationContext(),"Post_no:"+v.getId(),Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(),PostActivity.class);
                            intent.putExtra("post_no",v.getId());
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
                    p_name.setGravity(Gravity.FILL);
                    p_button.setGravity(Gravity.FILL);

                    ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.displayImage(Url+"Image/"+json.getString("imgUrl"), p_img);
                    p_name.setText(json.getString("name"));
                    p_name.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                    p_price.setText("[\\"+json.getString("price")+"]");
                    p_price.setGravity(Gravity.CENTER | Gravity.BOTTOM);

                    p_button.addView(p_img);
                    p_button.addView(p_name);
                    p_button.addView(p_price);
                    //Button button = (Button) new Button(getActivity());
                    //button.setText(json.getString("name")+"\n"+json.getString("price"));
                    //button.setWidth(screenWidth/3);
                    new_linearLayout.addView(p_button);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            LinearLayout linearLayout_vertic = (LinearLayout)getActivity().findViewById(R.id.linear_vertic);
            linearLayout_vertic.addView(new_linearLayout);
        }
    }

    class postListLoading extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json = null;
                jArray = new JSONArray(result);//JSON 데이터 형식으로 파싱
                updateView(jArray);//받아온 정보로 화면 표시
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

    class JSONComparator implements Comparator<JSONObject>{

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
            //if your value is numeric:
            //if(valA > valB)
            //    return 1;
            //if(valA < valB)
            //    return -1;
            //return 0;
        }
    }
}
