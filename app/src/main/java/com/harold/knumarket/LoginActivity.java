package com.harold.knumarket;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.knumarket.harold.knu_market.R;
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


public class LoginActivity extends Activity {

    private EditText  username=null;
    private EditText  password=null;
    private TextView attempts;
//    private Button login;
//    int counter = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText)findViewById(R.id.editText1);
        password = (EditText)findViewById(R.id.editText2);
//        attempts = (TextView)findViewById(R.id.textView5);
//        attempts.setText(Integer.toString(counter));

        Button loginBtn = (Button)findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postLoading Login_attempt = new postLoading();
                String login_id = username.getText().toString();
                String login_pass = password.getText().toString();

                String url = Webserver_Url.getInstance().getUrl();
                Login_attempt.onPostExecute(url+"JSP/RequestLogin.jsp?login_id="+login_id+"&login_pass="+login_pass);
                //
            }
        });
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

            String categoryID = json.getString("category_id");
//            setDecodeCatID(categoryID);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //서버에 폰번,비번 보내기
    class postLoading extends AsyncTask<String, Void, String> {
        private String Url = Webserver_Url.getInstance().getUrl();
        private JSONArray jArray;
        private ViewPager mViewPager;
        @Override

        protected void onPostExecute(String result) {
            try {
                JSONObject json = null;
                jArray = new JSONArray(result);//JSON 데이터 형식으로 파싱
                updateView(jArray);//받아온 정보로 화면 표시
//                initializeViewPager(jArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //웹에서 정보 가져오는 부분
        @Override
        protected String doInBackground(String... urls)
        {
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

        public void login(View view)
        { //서버에 아이디(폰번), 비번 확인
            String str = doInBackground();

            String login_id = username.getText().toString();
            String login_pass = password.getText().toString();

            if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin"))
            {
                Toast.makeText(getApplicationContext(), "Redirecting...",
                        Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Wrong Credentials",
                        Toast.LENGTH_SHORT).show();
//                attempts.setBackgroundColor(Color.RED);
//                counter--;
//                attempts.setText(Integer.toString(counter));
//            if(counter==0)
//            {
//                login.setEnabled(false);
//            }
            }
        }
//        public  void initializeViewPager(JSONArray array){
//
//            ArrayList<String> imgUrls = new ArrayList<String>();
//            JSONObject object = null;
//
//            try {
//                object = array.getJSONObject(0);
//                for(int i = 0; i < 3 ; i++){
//                    imgUrls.add(object.getString("img"+(i+1)+"Url"));
//                /*if(!imgUrls.get(i).equals("null")) {
//                    p_detail.append("\n" + imgUrls.get(i));
//                }*/
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}

