package com.harold.knumarket.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.harold.knumarket.User_Info;
import com.harold.knumarket.Webserver_Url;
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
    private LoginTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText)findViewById(R.id.edit_c_id);
        password = (EditText)findViewById(R.id.edit_c_pw);

        Button loginBtn = (Button)findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = new LoginTask();
                String login_id = username.getText().toString();
                String login_pass = password.getText().toString();
                String url = Webserver_Url.getInstance().getUrl();
                task.execute(url+"JSP/RequestLogin.jsp?client_id="+login_id+"&client_pw="+login_pass);

            }
        });

        /*TextView registerScreen = (TextView) findViewById(R.id.link_to_login);
        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });*/
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

            Intent intent = new Intent(getBaseContext(),MainActivity.class);
            startActivity(intent);
            finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //서버에 폰번,비번 보내기
    class LoginTask extends AsyncTask<String, Void, String> {

        private String Url = Webserver_Url.getInstance().getUrl();
        private JSONArray jArray;

        @Override
        protected void onPostExecute(String result) {

            if (result.equals("false")) {
                Toast.makeText(getApplicationContext(), "일치하는 회원정보가 없습니다.", Toast.LENGTH_SHORT).show();
            }
            else {
                try {
                    jArray = new JSONArray(result);//JSON 데이터 형식으로 파싱
                    recordUserInfo(jArray);//받아온 정보로 화면 표시
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
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

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //str = Url+urls[0];
            return str;
        }
    }
}

