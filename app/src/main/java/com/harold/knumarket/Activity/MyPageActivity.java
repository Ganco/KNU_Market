package com.harold.knumarket.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.harold.knumarket.User_Info;
import com.knumarket.harold.knu_market.R;

/**
 * Created by Gan on 2014-10-10.
 */
public class MyPageActivity extends Activity {

    public static final int REQUEST_CODE_MAIN = 1001;
    private EditText client_Id;
    private EditText phone_No;
    private EditText profile;
    private EditText addition;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_mypage);
        client_Id = (EditText) findViewById(R.id.profileTextEdit02);
        phone_No = (EditText) findViewById(R.id.profileTextEdit03);
        profile = (EditText) findViewById(R.id.profileTextEdit01);
        addition = (EditText) findViewById(R.id.profileTextEdit04);
        //마이페이지 진입 전 로그인 여부 확인
        User_Info user_info = User_Info.getUser_info();
        if(user_info.getClient_State()) {
            final EditText mEditText01 = (EditText) findViewById(R.id.profileTextEdit01);
            // 프로필 입력창이 3줄이상 넘어가지 않게 이벤트 처리
            mEditText01.addTextChangedListener(new TextWatcher() {
                String previousString = "";

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    previousString = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (mEditText01.getLineCount() >= 4) {
                        mEditText01.setText(previousString);
                        mEditText01.setSelection(mEditText01.length());
                    }
                }
            });
        }
        else{
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
            //finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        //Log.i("KNU_Market/Config_Act", "before user_info=");
        User_Info userInfo = User_Info.getUser_info();
        userInfo.getClient_keyword();


        client_Id.setText(userInfo.getClient_Id());
        phone_No.setText(userInfo.getPhone_No());
        profile.setText(userInfo.getProfile());
        addition.setText(userInfo.getAddition());

    }
    @Override
    public void onPause(){
        super.onPause();
        User_Info userInfo = User_Info.getUser_info();
        //userKeyword = userInfo.getClient_keyword();

        EditText client_Id = (EditText) findViewById(R.id.profileTextEdit02);
        EditText phone_No = (EditText) findViewById(R.id.profileTextEdit03);
        EditText profile = (EditText) findViewById(R.id.profileTextEdit01);
        EditText addition = (EditText) findViewById(R.id.profileTextEdit04);

        userInfo.setClient_Id(client_Id.getText().toString());
        userInfo.setPhone_No(phone_No.getText().toString());
        userInfo.setProfile(profile.getText().toString());
        userInfo.setAddition(addition.getText().toString());

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
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivityForResult(intent, REQUEST_CODE_MAIN);
                startActivity(intent);
                finish();
                break;

            //// insert button listener for MYPAGE
            case R.id.btn_myPage:
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
                intent = new Intent(getBaseContext(), ZzimActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
            case R.id.btn_alarm:
                intent = new Intent(getBaseContext(), AlarmActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_goMyPost:
                intent = new Intent(getBaseContext(), MyPostActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
                break;

        }
    }

}
