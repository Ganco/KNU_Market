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
import com.harold.knumarket.Activity.addPostActivity;
import com.knumarket.harold.knu_market.R;

/**
 * Created by Gan on 2014-11-30.
 */
public class Category_Book0 extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_book0);

    }

    public void onClick(View v){

        int id = v.getId();
        Intent intent = null;

        switch (id){

            case R.id.btn_009:
                intent = new Intent(getBaseContext(), Category_ListView.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("category_no","009");
                startActivity(intent);
                break;

            case R.id.btn_000:
                intent = new Intent(getBaseContext(), Category_ListView.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("category_no","000");
                startActivity(intent);
                break;

            case R.id.btn_001:
                intent = new Intent(getBaseContext(), Category_ListView.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("category_no","001");
                startActivity(intent);
                break;

            case R.id.btn_002:
                intent = new Intent(getBaseContext(), Category_ListView.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("category_no","002");
                startActivity(intent);
                break;

            case R.id.btn_003:
                intent = new Intent(getBaseContext(), Category_ListView.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("category_no","003");
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
