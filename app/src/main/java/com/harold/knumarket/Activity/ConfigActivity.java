package com.harold.knumarket.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.harold.knumarket.User_Info;
import com.knumarket.harold.knu_market.R;

import java.util.ArrayList;

/**
 * Created by Gan on 2014-11-28.
 */
public class ConfigActivity extends Activity {

    public static final int REQUEST_CODE_MAIN = 1001;
    //private ArrayList<String> userKeyword;
    private Switch swc; // 스위치 객체
    private EditText keyword1;
    private EditText keyword2;
    private EditText keyword3;
    private EditText keyword4;
    private EditText keyword5;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        User_Info userInfo = User_Info.getUser_info();

        keyword1 = (EditText) findViewById(R.id.keyword1);
        keyword2 = (EditText) findViewById(R.id.keyword2);
        keyword3 = (EditText) findViewById(R.id.keyword3);
        keyword4 = (EditText) findViewById(R.id.keyword4);
        keyword5 = (EditText) findViewById(R.id.keyword5);

        swc = (Switch)findViewById(R.id.alarmOnOff);
        SharedPreferences pref = getSharedPreferences("pref",Activity.MODE_PRIVATE);
        userInfo.LoadAlarmOnOff(pref);
        swc.setChecked(userInfo.getAlarmOnOff());

        if(!swc.isChecked()) {
            keyword1.setEnabled(false);
            keyword2.setEnabled(false);
            keyword3.setEnabled(false);
            keyword4.setEnabled(false);
            keyword5.setEnabled(false);
            keyword1.setTextColor(Color.parseColor("#82979797"));   // 회색
            keyword2.setTextColor(Color.parseColor("#82979797"));
            keyword3.setTextColor(Color.parseColor("#82979797"));
            keyword4.setTextColor(Color.parseColor("#82979797"));
            keyword5.setTextColor(Color.parseColor("#82979797"));
        }

        // 객체 설정, 리스너 부착
        swc.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean isChecking) {
                String str = String.valueOf(isChecking); // boolean - String 변환
                User_Info userInfo = User_Info.getUser_info();

                // 상태가 on, off인 경우에 알맞게 토스트를 띄움
                if(isChecking) {
                    Toast.makeText(getApplication(), "키워드 알람을 설정합니다", Toast.LENGTH_SHORT).show();
                    loadKeyword();
                    keyword1.setEnabled(true);
                    keyword2.setEnabled(true);
                    keyword3.setEnabled(true);
                    keyword4.setEnabled(true);
                    keyword5.setEnabled(true);
                    keyword1.setTextColor(Color.parseColor("#ff000000"));   // 검은색
                    keyword2.setTextColor(Color.parseColor("#ff000000"));
                    keyword3.setTextColor(Color.parseColor("#ff000000"));
                    keyword4.setTextColor(Color.parseColor("#ff000000"));
                    keyword5.setTextColor(Color.parseColor("#ff000000"));

                    SharedPreferences pref = getSharedPreferences("pref",Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    userInfo.SaveLastPostNo(editor);    // 마지막 글번호 설정
                    userInfo.setAlarmOnOff(true);
                    userInfo.SaveAlarmOnOff(editor);    //
                }
                else {
                    Toast.makeText(getApplication(), "키워드 알람을 끕니다", Toast.LENGTH_SHORT).show();
                    saveKeyword();
                    keyword1.setEnabled(false);
                    keyword2.setEnabled(false);
                    keyword3.setEnabled(false);
                    keyword4.setEnabled(false);
                    keyword5.setEnabled(false);
                    keyword1.setTextColor(Color.parseColor("#82979797"));   // 회색
                    keyword2.setTextColor(Color.parseColor("#82979797"));
                    keyword3.setTextColor(Color.parseColor("#82979797"));
                    keyword4.setTextColor(Color.parseColor("#82979797"));
                    keyword5.setTextColor(Color.parseColor("#82979797"));

                    SharedPreferences pref = getSharedPreferences("pref",Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    userInfo.setAlarmOnOff(false);
                    userInfo.SaveAlarmOnOff(editor);    //
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        loadKeyword();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        saveKeyword();
    }

    public void loadKeyword()
    {
        User_Info userInfo = User_Info.getUser_info();

        keyword1 = (EditText) findViewById(R.id.keyword1);
        keyword2 = (EditText) findViewById(R.id.keyword2);
        keyword3 = (EditText) findViewById(R.id.keyword3);
        keyword4 = (EditText) findViewById(R.id.keyword4);
        keyword5 = (EditText) findViewById(R.id.keyword5);

        keyword1.setText(userInfo.getClient_keyword().get(0));
        keyword2.setText(userInfo.getClient_keyword().get(1));
        keyword3.setText(userInfo.getClient_keyword().get(2));
        keyword4.setText(userInfo.getClient_keyword().get(3));
        keyword5.setText(userInfo.getClient_keyword().get(4));
    }

    public void saveKeyword()
    {
        User_Info userInfo = User_Info.getUser_info();
        //userKeyword = userInfo.getClient_keyword();

        keyword1 = (EditText) findViewById(R.id.keyword1);
        keyword2 = (EditText) findViewById(R.id.keyword2);
        keyword3 = (EditText) findViewById(R.id.keyword3);
        keyword4 = (EditText) findViewById(R.id.keyword4);
        keyword5 = (EditText) findViewById(R.id.keyword5);

        userInfo.setClient_keyword(keyword1.getText().toString(),0);
        userInfo.setClient_keyword(keyword2.getText().toString(),1);
        userInfo.setClient_keyword(keyword3.getText().toString(),2);
        userInfo.setClient_keyword(keyword4.getText().toString(),3);
        userInfo.setClient_keyword(keyword5.getText().toString(),4);

        //*/
        // 키워드 저장
        SharedPreferences pref = getSharedPreferences("pref",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        userInfo.SavePreference(editor);
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
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                //startActivityForResult(intent, REQUEST_CODE_MAIN);
                startActivity(intent);
                finish();
                break;

            //// insert button listener for MYPAGE
            case R.id.btn_myPage:
                intent = new Intent(getBaseContext(), MyPageActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                //startActivityForResult(intent, REQUEST_CODE_MYPAGE);
                startActivity(intent);
                finish();
                break;

            case R.id.btn_config:
                break;
            case R.id.btn_search:
                intent = new Intent(getBaseContext(), SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_zzim:
                break;
            case R.id.btn_alarm:
                intent = new Intent(getBaseContext(), AlarmActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
                break;
        }
    }

}