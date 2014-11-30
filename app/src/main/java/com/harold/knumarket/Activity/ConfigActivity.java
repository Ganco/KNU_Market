package com.harold.knumarket.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.harold.knumarket.User_Info;
import com.knumarket.harold.knu_market.R;

import java.util.ArrayList;

/**
 * Created by Gan on 2014-11-28.
 */
public class ConfigActivity extends Activity {

    public static final int REQUEST_CODE_MAIN = 1001;
    private ArrayList<String> userKeyword;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);



    }
    @Override
    public void onStart() {
        super.onStart();

        Log.i("KNU_Market/Config_Act", "before user_info=");
        User_Info userInfo = User_Info.getUser_info();
        userInfo.getClient_keyword();

        /*
        Log.i("KNU_Market/Config_Act", "keyword1=" + userInfo.getClient_keyword().get(0));
        Log.i("KNU_Market/Config_Act", "keyword2=" + userInfo.getClient_keyword().get(1));
        Log.i("KNU_Market/Config_Act", "keyword3=" + userInfo.getClient_keyword().get(2));
        Log.i("KNU_Market/Config_Act", "keyword4=" + userInfo.getClient_keyword().get(3));
        Log.i("KNU_Market/Config_Act", "keyword5=" + userInfo.getClient_keyword().get(4));
        ///*/


        ///*
        EditText keyword1 = (EditText) findViewById(R.id.keyword1);
        EditText keyword2 = (EditText) findViewById(R.id.keyword2);
        EditText keyword3 = (EditText) findViewById(R.id.keyword3);
        EditText keyword4 = (EditText) findViewById(R.id.keyword4);
        EditText keyword5 = (EditText) findViewById(R.id.keyword5);

        Log.i("KNU_Market/Config_Act", "finish findViewById");

        //*/

        ///*
        keyword1.setText(userInfo.getClient_keyword().get(0));
        keyword2.setText(userInfo.getClient_keyword().get(1));
        keyword3.setText(userInfo.getClient_keyword().get(2));
        keyword4.setText(userInfo.getClient_keyword().get(3));
        keyword5.setText(userInfo.getClient_keyword().get(4));

        Log.i("KNU_Market/Config_Act", "finish setText");
        //*/

    }

    @Override
    public void onPause()
    {
        super.onPause();
        ///*
        User_Info userInfo = User_Info.getUser_info();
        //userKeyword = userInfo.getClient_keyword();


        EditText keyword1 = (EditText) findViewById(R.id.keyword1);
        EditText keyword2 = (EditText) findViewById(R.id.keyword2);
        EditText keyword3 = (EditText) findViewById(R.id.keyword3);
        EditText keyword4 = (EditText) findViewById(R.id.keyword4);
        EditText keyword5 = (EditText) findViewById(R.id.keyword5);

        userInfo.setClient_keyword(keyword1.getText().toString(),0);
        userInfo.setClient_keyword(keyword2.getText().toString(),1);
        userInfo.setClient_keyword(keyword3.getText().toString(),2);
        userInfo.setClient_keyword(keyword4.getText().toString(),3);
        userInfo.setClient_keyword(keyword5.getText().toString(),4);

        //*/

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
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(intent, REQUEST_CODE_MAIN);
                break;

            //// insert button listener for MYPAGE
            case R.id.btn_myPage:
                intent = new Intent(getBaseContext(), MyPageActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                //startActivityForResult(intent, REQUEST_CODE_MYPAGE);
                break;

            case R.id.btn_config:
                break;
            case R.id.btn_search:
                intent = new Intent(getBaseContext(), SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
            case R.id.btn_zzim:
                break;
            case R.id.btn_alarm:
                intent = new Intent(getBaseContext(), AlarmActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
        }
    }

}