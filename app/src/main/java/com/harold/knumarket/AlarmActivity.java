package com.harold.knumarket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.knumarket.harold.knu_market.R;

/**
 * Created by Gan on 2014-11-28.
 */
public class AlarmActivity extends Activity {

    public static final int REQUEST_CODE_MAIN = 1001;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);



    }

    public void onClick(View v){

        int id = v.getId();
        Intent intent = null;

        switch (id){

            case R.id.btn_goAddPost:
                intent = new Intent(getBaseContext(), add_post.class);
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
                intent = new Intent(getBaseContext(), ConfigActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
            case R.id.btn_search:
                intent = new Intent(getBaseContext(), SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
            case R.id.btn_zzim:
                break;
            case R.id.btn_alarm:
                break;
        }
    }

}