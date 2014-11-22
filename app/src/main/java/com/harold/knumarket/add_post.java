package com.harold.knumarket;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.knumarket.harold.knu_market.R;

public class add_post extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
    }

    public void onClick(View v) {

        int id = v.getId();

        switch(id){
            case R.id.btn_addPost:
                break;
        }
    }
}
