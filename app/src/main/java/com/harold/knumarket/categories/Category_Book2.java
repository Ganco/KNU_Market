package com.harold.knumarket.categories;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.harold.knumarket.Activity.AlarmActivity;
import com.harold.knumarket.Activity.ConfigActivity;
import com.harold.knumarket.Activity.MainActivity;
import com.harold.knumarket.Activity.MyPageActivity;
import com.harold.knumarket.Activity.SearchActivity;
import com.harold.knumarket.Activity.ZzimActivity;
import com.harold.knumarket.Activity.addPostActivity;
import com.knumarket.harold.knu_market.R;

/**
 * Created by Gan on 2014-11-30.
 */
public class Category_Book2 extends Activity{



    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_book2);

    }

    public void onClick(View v){

        int id = v.getId();
        Intent intent = null;

        switch (id){

            case R.id.btn_029:
                intent = new Intent(getBaseContext(), Category_ListView.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("category_no","029");
                startActivity(intent);
                break;

            case R.id.btn_020:
                intent = new Intent(getBaseContext(), Category_ListView.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("category_no","020");
                startActivity(intent);
                break;

            case R.id.btn_021:
                intent = new Intent(getBaseContext(), Category_ListView.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("category_no","021");
                startActivity(intent);
                break;

            case R.id.btn_022:
                intent = new Intent(getBaseContext(), Category_ListView.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("category_no","022");
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
                finish();
                break;

            //// insert button listener for MYPAGE
            case R.id.btn_myPage:
                intent = new Intent(getBaseContext(), MyPageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
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
                finish();
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
