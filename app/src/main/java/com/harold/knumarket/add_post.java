package com.harold.knumarket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.knumarket.harold.knu_market.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class add_post extends Activity {

    private int TAKE_CAMERA = 1;
    private int TAKE_GALLARY = 2;
    private int IMG_BTN_1 =1;
    private int IMG_BTN_2 =2;
    private int IMG_BTN_3 =3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        //NoDefaultSpinner cat_high = (NoDefaultSpinner) findViewById(R.id.cat_high);
        Spinner cat_high = (Spinner) findViewById(R.id.cat_high);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_upper, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cat_high.setAdapter(adapter);
        cat_high.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public class CustomSpinnerAdapter extends ArrayAdapter{

        public CustomSpinnerAdapter(Context context, int resource) {
            super(context, resource);
        }
    }

    public void onClick(View v) {

        int id = v.getId();

        switch(id){
            case R.id.img_btn1:
                getImage(IMG_BTN_1);
                break;
            case R.id.img_btn2:
                getImage(IMG_BTN_2);
                break;
            case R.id.img_btn3:
                getImage(IMG_BTN_3);
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
                File f = new File(android.os.Environment
                        .getExternalStorageDirectory(), "temp.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //Log.i("add_Post getImage(카메라)", "R.id(Int)=" + id);
                //intent.putExtra("r_id", id);

                String r_id = String.valueOf(id);
                String requestCode = String.valueOf(TAKE_CAMERA);
                Log.i("add_Post getImage(카메라)","request_code="+Integer.valueOf(requestCode+r_id));
                startActivityForResult(intent, Integer.valueOf(requestCode+r_id));
            }
        });
        builder.setNegativeButton("갤러리", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = Uri.parse("content://media/external/images/media");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                String r_id = String.valueOf(id);
                String requestCode = String.valueOf(TAKE_GALLARY);
                Log.i("add_Post getImage(갤러리)","request_code="+Integer.valueOf(requestCode+r_id));
                startActivityForResult(intent, Integer.valueOf(requestCode + r_id));
            }
        });
        builder.show();
    }

    //////////////////////////// 선택 하면 리턴값 받기
    @Override
    protected void onActivityResult(int customRequestCode, int resultCode, Intent intent) {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;

        Log.i("add_Post-onActivityResult","request_code="+customRequestCode);
        int requestCode = customRequestCode/10;
        int id = customRequestCode-(requestCode*10);

        if (resultCode == RESULT_OK){

            //갤러리에서 받아온 이미지를 처리
            if(requestCode == TAKE_GALLARY) {
                Log.i("add_Post-onActivityResult","TAKE_GALLARY"+"/img_btn"+id);

                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_add_post);
                String tag = "img_btn"+id;
                ImageView img_btn = (ImageView) linearLayout.findViewWithTag(tag);
                Bitmap bm = null;
                try {
                    bm = MediaStore.Images.Media.getBitmap(getContentResolver(), intent.getData());
                    //Bitmap bm = (Bitmap) intent.getExtras().get("data");
                    if (id != 1) {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((screenWidth / 2), 200);
                        params.setMargins(3, 3, 3, 3);
                        img_btn.setLayoutParams(params);
                    }
                    img_btn.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img_btn.setImageBitmap(bm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //카메라에서 받아온 이미지를 처리
            else if(requestCode == TAKE_CAMERA){
                Log.i("add_Post-onActivityResult","TAKE_CAMERA"+"/img_btn"+id);
            }
        }
    }

}
