package com.harold.knumarket.Activity;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.harold.knumarket.User_Info;
import com.harold.knumarket.Webserver_Url;
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

/**
* Created by Administrator on 2014-12-01.
        */
public class RegisterActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);
        TextView btnRegister = (Button) findViewById(R.id.btnRegister);
        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
<<<<<<< HEAD
                // Closing registration screen
                // Switching to Login Screen/closing activity_register screen
=======
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
>>>>>>> ef50035e47143a72291123fa862ce5420a70f6a6
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {


                postLoading Login_attempt = new postLoading();



                EditText reg_phone = (EditText) findViewById(R.id.reg_email);
                EditText reg_id = (EditText) findViewById(R.id.reg_fullname);
                EditText reg_password = (EditText) findViewById(R.id.reg_password);

                String login_id = reg_id.getText().toString();
                String login_num = reg_phone.getText().toString();
                String login_pass = reg_password.getText().toString();

                String url = Webserver_Url.getInstance().getUrl();
                Login_attempt.execute("JSP/RequestRegister.jsp?client_id=" + login_id + "&client_pw=" + login_pass + "&client_phoneNum=" + login_num);

                Log.i("KNU_Market/Register", url + "JSP/RequestRegister.jsp?client_id=" + login_id + "&client_pw=" + login_pass + "&client_phoneNum=" + login_num);





                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();

            }
        });

    }
    //서버에 폰번,비번 보내기
    class postLoading extends AsyncTask<String, Void, String> {
        private String Url = Webserver_Url.getInstance().getUrl();
        private JSONArray jArray;

        @Override
        protected void onPostExecute(String result) {

            if (result.equals("false")) {
                Toast.makeText(getApplicationContext(), "등록실패", Toast.LENGTH_SHORT).show();
            }
            else { /*
                try {
                    jArray = new JSONArray(result);//JSON 데이터 형식으로 파싱
                    //recordUserInfo(jArray);//받아온 정보로 화면 표시
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "등록실패", Toast.LENGTH_SHORT).show();
                }
                */
                Log.i("KNU_Market/Register result - ", result);
                User_Info user_info = User_Info.getUser_info();
                user_info.setClient_State(true);
                Toast.makeText(getApplicationContext(), "등록", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

        public void recordUserInfo(JSONArray array) {

            try {

                JSONObject obj = array.getJSONObject(0);
                User_Info user_info = User_Info.getUser_info();

                ArrayList<String> tempArray = new ArrayList<String>();
                tempArray.add(obj.getString("c_keyword1"));
                tempArray.add(obj.getString("c_keyword2"));
                tempArray.add(obj.getString("c_keyword3"));
                tempArray.add(obj.getString("c_keyword4"));
                tempArray.add(obj.getString("c_keyword5"));

                user_info.setClient_keywordList(tempArray);
                user_info.setPhone_No(obj.getString("c_phone_num"));
                user_info.setAddition(obj.getString("profile_image_loc"));

                //User를 로그인 상태로 변경
                user_info.setClient_State(true);


                Toast.makeText(getApplicationContext(), "등록", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(),MainActivity.class);
                startActivity(intent);
                finish();

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
            HttpPost myConnection = new HttpPost(Url + urls[0]);

            try {
                response = myClient.execute(myConnection);
                str = EntityUtils.toString(response.getEntity(), "UTF-8");
                Log.i("KNU_Market/Register - ", str);

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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}