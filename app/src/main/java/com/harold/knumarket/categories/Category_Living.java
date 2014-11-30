package com.harold.knumarket.categories;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.harold.knumarket.AlarmActivity;
import com.harold.knumarket.ConfigActivity;
import com.harold.knumarket.MainActivity;
import com.harold.knumarket.MyPageActivity;
import com.harold.knumarket.SearchActivity;
import com.harold.knumarket.addPostActivity;
import com.knumarket.harold.knu_market.R;

/**
 * Created by Gan on 2014-11-30.
 */
public class Category_Living extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_living);

    }

    public void onClick(View v){

        int id = v.getId();
        Intent intent = null;

        switch (id){

            case R.id.btn_199:
                intent = new Intent(getBaseContext(), Category_ListView.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("category_no","199");
                startActivity(intent);
                break;

            case R.id.btn_100:
                intent = new Intent(getBaseContext(), Category_ListView.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("category_no","100");
                startActivity(intent);
                break;

            case R.id.btn_110:
                intent = new Intent(getBaseContext(), Category_ListView.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("category_no","110");
                startActivity(intent);
                break;

            case R.id.btn_120:
                intent = new Intent(getBaseContext(), Category_ListView.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("category_no","120");
                startActivity(intent);
                break;
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
                break;

            //// insert button listener for MYPAGE
            case R.id.btn_myPage:
                intent = new Intent(getBaseContext(), MyPageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
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
                intent = new Intent(getBaseContext(), AlarmActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
        }
    }
}
