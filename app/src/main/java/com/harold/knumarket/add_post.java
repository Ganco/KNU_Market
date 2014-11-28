package com.harold.knumarket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class add_post extends Activity {

    private ImageView img_btn;
    private static final double SCALE = 0.4;
    private int TAKE_CAMERA = 1;
    private int TAKE_GALLARY = 2;
    private int IMG_BTN_1 =1;
    private int IMG_BTN_2 =2;
    private int IMG_BTN_3 =3;
    private Uri mImageCaptureUri;

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

                // 임시로 사용할 파일의 경로를 생성
                String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

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

            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_add_post);
            String tag = "img_btn"+id;
            img_btn = (ImageView) linearLayout.findViewWithTag(tag);
            Bitmap bm = null;

            //갤러리에서 받아온 이미지를 처리
            if(requestCode == TAKE_GALLARY) {
                Log.i("add_Post-onActivityResult","TAKE_GALLARY"+"/img_btn"+id);
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

                // 크롭이 된 이후의 이미지를 넘겨 받습니다.
                // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
                // 임시 파일을 삭제합니다.

                String img_path = mImageCaptureUri.getPath();
                Log.i("add_Post-url","path="+img_path+", url="+mImageCaptureUri);
                bm = BitmapFactory.decodeFile(img_path);
                bm = Bitmap.createScaledBitmap(bm, (int)(bm.getWidth()*SCALE) ,(int)(bm.getHeight()*SCALE),true);

                if (id != 1) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((screenWidth / 2), 200);
                    params.setMargins(3, 3, 3, 3);
                    img_btn.setLayoutParams(params);
                }
                img_btn.setScaleType(ImageView.ScaleType.CENTER_CROP);

                /*
                //AUIL 이미지 옵션 설정
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.drawable.ic_empty) // 로딩중 이미지 설정
                        .showImageForEmptyUri(R.drawable.ic_empty) // Uri주소가 잘못되었을경우(이미지없을때)
                        .showImageOnFail(R.drawable.ic_error) // 로딩 실패시
                        .resetViewBeforeLoading(false)  // 로딩전에 뷰를 리셋하는건데 false로 하세요 과부하!
                        .delayBeforeLoading(0) // 로딩전 딜레이라는데 필요한일이 있을까요..?ㅋㅋ
                        .cacheInMemory(true) // 메모리케시 사용여부   (사용하면 빨라지지만 많은 이미지 캐싱할경우 outOfMemory Exception발생할 수 있어요)
                        .cacheOnDisc(true) // 디스크캐쉬를 사용여부(사용하세요왠만하면)
                                //.preProcessor(...) // 비트맵 띄우기전에 프로세스 (BitmapProcessor이라는 인터페이스를 구연하면 process(Bitmap image)라는 메소드를 사용할 수 있어요. 처리하실게 있으면 작성하셔서 이안에 넣어주시면 됩니다.)
                                //.postProcessor(...) // 비트맵 띄운후 프로세스 (위와같이 BitmapProcessor로 처리)
                        .considerExifParams(false) // 사진이미지의 회전률 고려할건지
                        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // 스케일타입설정   (일부밖에없습니다. 제가 centerCrop이 없어서 라이브러리 다 뒤져봤는데 없더라구요. 다른방법이 있습니다. 아래 설명해드릴게요.)
                        .bitmapConfig(Bitmap.Config.ARGB_8888) // 이미지 컬러방식
                        .build();

                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(String.valueOf(mImageCaptureUri),img_btn,options);
                */
                img_btn.setImageBitmap(bm);

                // 임시 파일 삭제
                File f = new File(mImageCaptureUri.getPath());
                if(f.exists()){
                    f.delete();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((BitmapDrawable)img_btn.getDrawable()).getBitmap().recycle();
    }

    class addPost extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
