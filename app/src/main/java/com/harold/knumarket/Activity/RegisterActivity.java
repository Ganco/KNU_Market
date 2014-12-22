package com.harold.knumarket.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.io.IOException;

/**
* Created by Administrator on 2014-12-01.
        */
public class RegisterActivity extends Activity {

    public static final int REQUEST_CODE_MAIN = 1001;

    private String login_id;
    private String login_num;
    private String login_pass;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);
        TextView btnRegister = (Button) findViewById(R.id.btnRegister);
        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {


                postLoading Login_attempt = new postLoading();



                EditText reg_phone = (EditText) findViewById(R.id.reg_email);
                EditText reg_id = (EditText) findViewById(R.id.reg_fullname);
                EditText reg_password = (EditText) findViewById(R.id.reg_password);

                login_id = reg_id.getText().toString();
                login_num = reg_phone.getText().toString();
                login_pass = reg_password.getText().toString();

                String url = Webserver_Url.getInstance().getUrl();
                Login_attempt.execute("JSP/RequestRegister.jsp?client_id=" + login_id + "&client_pw=" + login_pass + "&client_phoneNum=" + login_num);

                Log.i("KNU_Market/Register", url + "JSP/RequestRegister.jsp?client_id=" + login_id + "&client_pw=" + login_pass + "&client_phoneNum=" + login_num);


                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                //startActivityForResult(intent, REQUEST_CODE_MAIN);
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

            if (result.contains("false")) {
                Toast.makeText(getApplicationContext(), "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
            }
            else if(result.contains("true")) {
                Log.i("KNU_Market/Register result=s ", result);
                User_Info user_info = User_Info.getUser_info();
                user_info.setClient_State(true);

                user_info.setPhone_No(login_num);
                user_info.setClient_Id(login_id);

                Log.i("KNU_Market/Register phone_num= ", user_info.getPhone_No());
                Log.i("KNU_Market/Register client_id= ", user_info.getClient_Id());

                SharedPreferences pref = getSharedPreferences("pref",Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                user_info.SavePreference(editor);


                Toast.makeText(getApplicationContext(), "회원가입되었습니다~.", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(getBaseContext(),MainActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
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