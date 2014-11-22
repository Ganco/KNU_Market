package com.harold.knumarket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.knumarket.harold.knu_market.R;

import java.io.FileNotFoundException;
import java.io.IOException;

public class add_post extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
    }

    public void onClick(View v) {

        int id = v.getId();

        switch(id){
            case R.id.img_btn1:
                getImage(id);
                break;
            case R.id.img_btn2:
                getImage(id);
                break;
            case R.id.img_btn3:
                getImage(id);
                break;
            case R.id.btn_addPost:
                break;
        }
    }
    private void getImage(final int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("이미지 가져오기");
        builder.setPositiveButton("촬영", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.i("add_Post getImage","R.id(Int)="+id);
                startActivityForResult(intent, id);
            }
        });
        builder.setNegativeButton("갤러리", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = Uri.parse("content://media/external/images/media");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                Log.i("add_Post getImage","R.id(Int)="+id);
                startActivityForResult(intent, id);
            }
        });
        builder.show();
    }

    //////////////////////////// 선택 하면 리턴값 받기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        //String r_id = intent.getExtras().getString("R_id_val");
        Log.i("add_Post-onActivityResult","R.id="+requestCode);

        if (resultCode == RESULT_OK)
        {
            //if(requestCode == 1){ // 1 은 위에서 startActivityForResult(intent, 1);
                ImageView img_btn = (ImageView)findViewById(requestCode);
                Bitmap bm = null;
                try {
                    bm = MediaStore.Images.Media.getBitmap(getContentResolver(),intent.getData());
                    bm = Bitmap.createScaledBitmap(bm, 100, 100, true);
                    img_btn.setImageBitmap(bm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            //}
        }
    }
}
