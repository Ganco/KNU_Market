package com.harold.knumarket.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.harold.knumarket.AndroidUploader;
import com.harold.knumarket.Post_DTO;
import com.harold.knumarket.User_Info;
import com.knumarket.harold.knu_market.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.R.layout.simple_spinner_dropdown_item;
import static com.harold.knumarket.Webserver_Url.getInstance;
import static com.knumarket.harold.knu_market.R.array.cat_low_etc;
import static com.knumarket.harold.knu_market.R.array.cat_low_fashion;
import static com.knumarket.harold.knu_market.R.array.cat_low_general;
import static com.knumarket.harold.knu_market.R.array.cat_low_goods;
import static com.knumarket.harold.knu_market.R.array.cat_low_libreal;
import static com.knumarket.harold.knu_market.R.array.cat_low_major;
import static com.knumarket.harold.knu_market.R.array.cat_mid_books;
import static com.knumarket.harold.knu_market.R.array.cat_mid_etc;
import static com.knumarket.harold.knu_market.R.array.cat_mid_fashion;
import static com.knumarket.harold.knu_market.R.array.cat_mid_goods;

public class addPostActivity extends Activity {

    //카메라,갤러리에서 이미지 가져올때 필요한 변수들
    private Bitmap bm = null;
    private ImageView img_btn;
    private static final double SCALE = 0.8;
    private int TAKE_CAMERA = 1;
    private int TAKE_GALLARY = 2;
    private int IMG_BTN_1 =1;
    private int IMG_BTN_2 =2;
    private int IMG_BTN_3 =3;
    private Uri mImageCaptureUri;

    //카테고리 Spinner객체 변수
    private static Spinner cat_high;
    private static Spinner cat_mid;
    private static Spinner cat_low;

    //비동기식 웹 업로드를 위한 객체
    private postUploading uploadTask;

    //불러온 이미지 파일 객체를 임시저장하는 배열
    private ArrayList<File> imgFiles = new ArrayList<File>(3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //상품 등록 전 로그인 여부 확인
        User_Info user_info = User_Info.getUser_info();
        if(user_info.getClient_State()) {
            setContentView(R.layout.activity_add_post);
            String serverUrl = getInstance().getUrl();
            initializeSpinner();
        }
        else{
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void initializeSpinner(){

        //NoDefaultSpinner cat_high = (NoDefaultSpinner) findViewById(R.id.cat_high);
        cat_high = (Spinner) findViewById(R.id.cat_high);
        cat_mid = (Spinner) findViewById(R.id.cat_mid);
        cat_low= (Spinner) findViewById(R.id.cat_low);

        ArrayAdapter<CharSequence> cat_high_adapter = ArrayAdapter.createFromResource(this,
                R.array.category_upper, R.layout.spinner_item);
        cat_high_adapter.setDropDownViewResource(simple_spinner_dropdown_item);
        cat_high.setAdapter(cat_high_adapter);
        cat_high.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final ArrayAdapter<CharSequence> cat_mid_adapter = null;
                switch (position) {
                    case 0://대분류 0번 선택
                        //Toast.makeText(getApplicationContext(),"selected Postion="+position, Toast.LENGTH_SHORT).show();
                        setSpinnerAdapter(cat_mid, cat_mid_adapter, cat_mid_books);
                        cat_mid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                ArrayAdapter<CharSequence> cat_low_adapter = null;
                                switch (position){
                                    case 0://중분류 0번 선택
                                        setSpinnerAdapter(cat_low,cat_low_adapter,cat_low_major);
                                        cat_low.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                if(position == Spinner.INVALID_POSITION)
                                                    cat_low.setSelection(0);
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {

                                            }
                                        });
                                        break;
                                    case 1://중분류 1번 선택
                                        setSpinnerAdapter(cat_low,cat_low_adapter,cat_low_libreal);
                                        cat_low.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                if(position == Spinner.INVALID_POSITION)
                                                    cat_low.setSelection(0);
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {

                                            }
                                        });
                                        break;
                                    case 2://중분류 2번 선택
                                        setSpinnerAdapter(cat_low,cat_low_adapter,cat_low_general);
                                        cat_low.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                if(position == Spinner.INVALID_POSITION)
                                                    cat_low.setSelection(0);
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {

                                            }
                                        });
                                        break;
                                    case Spinner.INVALID_POSITION://중분류 미섵택시 0번 선택
                                        cat_mid.setSelection(0);
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 1://대분류 1번 선택
                        //Toast.makeText(getApplicationContext(),"selected Postion="+position, Toast.LENGTH_SHORT).show();
                        setSpinnerAdapter(cat_mid,cat_mid_adapter,cat_mid_goods);
                        cat_mid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                ArrayAdapter<CharSequence> cat_low_adapter = null;
                                switch (position) {
                                    case 0://중분류 0번 선택
                                        setSpinnerAdapter(cat_low, cat_low_adapter, cat_low_goods);
                                        break;
                                    case Spinner.INVALID_POSITION://중분류 미섵택시 0번 선택
                                        cat_mid.setSelection(0);
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 2://대분류 2번 선택
                        //Toast.makeText(getApplicationContext(),"selected Postion="+position, Toast.LENGTH_SHORT).show();
                        setSpinnerAdapter(cat_mid,cat_mid_adapter,cat_mid_fashion);
                        cat_mid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                ArrayAdapter<CharSequence> cat_low_adapter = null;
                                switch (position) {
                                    case 0://중분류 0번 선택
                                        setSpinnerAdapter(cat_low, cat_low_adapter, cat_low_fashion);
                                        break;
                                    case Spinner.INVALID_POSITION://중분류 미섵택시 0번 선택
                                        cat_mid.setSelection(0);
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 3://대분류 3번 선택
                        //Toast.makeText(getApplicationContext(),"selected Postion="+position, Toast.LENGTH_SHORT).show();
                        setSpinnerAdapter(cat_mid,cat_mid_adapter,cat_mid_etc);
                        cat_mid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                ArrayAdapter<CharSequence> cat_low_adapter = null;
                                switch (position) {
                                    case 0:
                                        setSpinnerAdapter(cat_low, cat_low_adapter, cat_low_etc);
                                        break;
                                    case Spinner.INVALID_POSITION:
                                        cat_mid.setSelection(0);
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case Spinner.INVALID_POSITION://대분류 미선택시 0번 선택
                        cat_high.setSelection(0);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //cat_high.setSelection(0);
        //cat_mid.setSelection(0);
        //cat_low.setSelection(0);
    }

    public void onClick(View v) {

        int id = v.getId();
        Intent intent = null;

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
                Post_DTO postData = setPostContents();
                if(postData != null) {

                    Log.i("add_Post setPostContents()",
                                     postData.getClient_id()+"\n"
                                    +postData.getProduct_name()+"\n"
                                    +postData.getCategory_id()+"\n"
                                    +postData.getProduct_price()+"\n"
                                    +postData.getProduct_detail()+"\n");

                    for(int i = 0 ; i < postData.getPost_keyword().size() ; i++){
                        Log.i("add_Post setPostContents()",postData.getPost_keyword().get(i)+"\n");
                    }

                    for(int i = 0 ; i < postData.getImgFiles().size() ; i++){
                        Log.i("add_Post setPostContents()",postData.getImgFiles().get(i).getName()+"\n");
                    }

                    uploadTask = new postUploading();//AsyncTack 객체 생성

                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        try {
                            Log.i("AddPost/Onclick-executeOnExecutor()", "함수 실행 전");
                            //Log.i("AddPost/Onclick", "status = " + uploadTask.getStatus());
                            //Log.i("AddPost/Onclick","status = "+uploadTask.get());
                            uploadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, postData);
                            //testAsyncTask task = new testAsyncTask();
                            //task.execute();
                        }
                        catch (NullPointerException e) {
                            Log.i("AddPost/Onclick-executeOnExecutor()", e.toString());
                            e.printStackTrace();
                        }
                    }
                    else {
                        try {
                            uploadTask.execute(postData);
                        } catch (NullPointerException e) {
                            Log.i("AddPost/Onclick-execute()", e.toString());
                            e.printStackTrace();
                        }
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Upload 실패",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_home:
                intent = new Intent(getBaseContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

    @Override //startActivityForResult의 데이터 후 처리
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

            //갤러리에서 받아온 이미지를 처리
            if(requestCode == TAKE_GALLARY) {
                Log.i("add_Post-onActivityResult","TAKE_GALLARY"+"/img_btn"+id);
                try {

                    bm = MediaStore.Images.Media.getBitmap(getContentResolver(), intent.getData());
                    bm = Bitmap.createScaledBitmap(bm, (int)(bm.getWidth()*SCALE) ,(int)(bm.getHeight()*SCALE),true);

                    //FILE Array에 임시 저장
                    String file_path = Environment.getExternalStorageDirectory().getPath();
                    String tempFileName ="tmp"+System.currentTimeMillis();

                    File f = SaveBitmapToFileCache(bm, file_path,tempFileName);

                    if(imgFiles.size() <3){
                        imgFiles.add(f);
                        //Toast.makeText(getApplicationContext(),"imgFile Array Size="+imgFiles.size(),Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //Toast.makeText(getApplicationContext(), "imgFile Array Full~!!", Toast.LENGTH_SHORT).show();
                    }

                    //Bitmap bm = (Bitmap) intent.getExtras().get("data");
                    if (id != 1) {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((screenWidth / 2), 200);
                        params.setMargins(3, 3, 3, 3);
                        img_btn.setLayoutParams(params);
                    }
                    img_btn.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    /*if(bm.getHeight() < bm.getWidth()){
                        bm = imgRotate(bm);
                    }*/
                    setThumbnailImage(f.getPath(),img_btn, "GALLARY");
                    Log.d("addPostActivity : onActivityResult() Gallary =>",f.getPath());
                    //img_btn.setImageBitmap(bm);

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
                //FILE Array에 임시 저장
                if(imgFiles.size() < 3) {
                    imgFiles.add(new File(img_path));
                    //Toast.makeText(getApplicationContext(),"imgFile Array Size="+imgFiles.size(),Toast.LENGTH_SHORT).show();
                }
                else{
                    //Toast.makeText(getApplicationContext(),"imgFile Array Full~!!",Toast.LENGTH_SHORT).show();
                }

                Log.i("add_Post-url","path="+img_path+", url="+mImageCaptureUri);
                bm = BitmapFactory.decodeFile(img_path);
                bm = Bitmap.createScaledBitmap(bm, (int)(bm.getWidth()*SCALE) ,(int)(bm.getHeight()*SCALE),true);

                if (id != 1) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((screenWidth / 2), 200);
                    params.setMargins(3, 3, 3, 3);
                    img_btn.setLayoutParams(params);
                }
                img_btn.setScaleType(ImageView.ScaleType.CENTER_CROP);

                /*if(bm.getHeight() < bm.getWidth()){
                    bm = imgRotate(bm);
                }*/

                Log.d("addPostActivity : onActivityResult() Camera =>", img_path);
                setThumbnailImage(img_path, img_btn, "CAMERA");

                //img_btn.setImageBitmap(bm);

                // 임시 파일 삭제
                File f = new File(mImageCaptureUri.getPath());
                /*if(f.exists()){
                    f.delete();
                }*/
            }
        }
    }

    public void setSpinnerAdapter(Spinner spinner ,ArrayAdapter<CharSequence> adapter,int textArrayResId){
        adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                textArrayResId, R.layout.spinner_item);
        adapter.setDropDownViewResource(simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private Post_DTO setPostContents(){
        Post_DTO post_DTO = null;

        EditText editName = (EditText) findViewById(R.id.edit_p_name);
        EditText editPrice = (EditText) findViewById(R.id.edit_p_price);
        editPrice.setKeyListener(new KeyListener() {
            @Override
            public int getInputType() {
                return 0;
            }
            @Override
            public boolean onKeyDown(View view, Editable text, int keyCode, KeyEvent event) {
                //Toast.makeText(getApplicationContext(),"onKeyDown="+text.toString(),Toast.LENGTH_SHORT).show();
                return false;
            }
            @Override
            public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
                return false;
            }
            @Override
            public boolean onKeyOther(View view, Editable text, KeyEvent event) {
                return false;
            }
            @Override
            public void clearMetaKeyState(View view, Editable content, int states) {
            }
        });
        EditText editDetail = (EditText) findViewById(R.id.edit_p_detail);
        EditText editKeyword_1 = (EditText) findViewById(R.id.editText3);
        EditText editKeyword_2 = (EditText) findViewById(R.id.editText5);
        EditText editKeyword_3 = (EditText) findViewById(R.id.editText6);
        ArrayList<String> postKeword = new ArrayList<String>(3);
        postKeword.add(editKeyword_1.getText().toString());
        postKeword.add(editKeyword_2.getText().toString());
        postKeword.add(editKeyword_3.getText().toString());

        String categoryID = String.valueOf(cat_high.getSelectedItemPosition())+
                String.valueOf(cat_mid.getSelectedItemPosition())+
                String.valueOf(cat_low.getSelectedItemPosition());

        String postState;
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.addPost_radioGroup);
        RadioButton rbtn = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        postState = rbtn.getTag().toString();

        /*Toast.makeText(getApplicationContext(),
                String.valueOf(cat_high.getSelectedItemPosition())+
                String.valueOf(cat_mid.getSelectedItemPosition())+
                String.valueOf(cat_low.getSelectedItemPosition()), Toast.LENGTH_SHORT).show();*/

        if(User_Info.getUser_info().getClient_Id() != null) {
             try {
                post_DTO = new Post_DTO(
                        User_Info.getUser_info().getClient_Id(),
                        categoryID,
                        editName.getText().toString(),
                        editPrice.getText().toString(),
                        editDetail.getText().toString(),
                        postKeword,
                        imgFiles,
                        postState
                );
                if(post_DTO != null){
                    Log.i("AddPost/setContents()", "post_dto 생성완료");
                    return post_DTO;
                }
                else {
                    //Toast.makeText(getApplicationContext(),"Post_DTO 생성실패",Toast.LENGTH_SHORT).show();
                    return null;
                }
            }
            catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
        else{
            //Toast.makeText(getApplicationContext(),"User_id is Null",Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private Bitmap imgRotate(Bitmap bmp){
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
        bmp.recycle();

        return resizedBitmap;
    }

    private void setThumbnailImage(String orgImagePath, ImageView mThumbnailImage, String source) {
        // 회전 각도 취득
        int degrees = GetExifOrientation(orgImagePath);

        // 이미지 취득 옵션 설정 (사이즈 정보 취득)
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        // 원본 이미지 정보 취득
        BitmapFactory.decodeFile(orgImagePath, options);

        // 리사이즈 비율 취득
        int sampleSize = getSampliSize(options.outWidth, options.outHeight);

        // 이미지 취득 옵션 설정 (사이즈 정보 취득 해제, 리사이즈)
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;

        // 리사이즈된 원본 이미지 취득
        Bitmap orgImage = BitmapFactory.decodeFile(orgImagePath, options);

        if(source.equals("CAMERA")){
            // 회전한 이미지 취득
            orgImage = GetRotatedBitmap(orgImage, degrees);
        }

        if (orgImage != null) {
            // 섬네일 이미지 표시
            mThumbnailImage.setImageBitmap(orgImage);
        }
    }

    private int getSampliSize(int width, int height) {
        // 화면 크기 취득
        Display currentDisplay = getWindowManager().getDefaultDisplay();

        float dw = currentDisplay.getWidth();
        float dh = currentDisplay.getHeight();

        // 가로/세로 축소 비율 취득
        int widthtRatio = (int) Math.ceil(width / dw);
        int heightRatio = (int) Math.ceil(height / dh);

        // 초기 리사이즈 비율
        int sampleSize = 1;

        // 가로 세로 비율이 화면보다 큰경우에만 처리
        if (widthtRatio > 1 && height > 1) {
            if (widthtRatio > heightRatio) {
                // 가로 축소 비율이 큰 경우
                sampleSize = widthtRatio;
            } else {
                // 세로 축소 비율이 큰 경우
                sampleSize = heightRatio;
            }
        }

        return sampleSize;
    }

    private int GetExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(filepath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            if (orientation != -1) {
                switch(orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }

        return degree;
    }

    private Bitmap GetRotatedBitmap(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);

            try {
                Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);

                if (bitmap != b2) {
                    bitmap.recycle();
                    bitmap = b2;
                }
            } catch (OutOfMemoryError e) {
                // 메모리 부족에러시, 원본을 반환
            }
        }

        return bitmap;
    }

    private File SaveBitmapToFileCache(Bitmap bitmap, String strFilePath, String fileName) {

        File file = new File(strFilePath);
        // If no folders
        if (!file.exists()) {
            file.mkdirs();
            // Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        }

        File fileCacheItem = new File(strFilePath,fileName);
        OutputStream out = null;

        try{
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            return fileCacheItem;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    class postUploading extends AsyncTask<Post_DTO, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("postUploading","AsyncTask Ready!");
        }

        @Override
        protected String doInBackground(Post_DTO... post_DTOs) {
            Log.i("postUploading","AsyncTask Start!");
            AndroidUploader uploader = new AndroidUploader();
            ///try{
                AndroidUploader.ReturnCode returnCode = uploader.uploadPost(post_DTOs[0]);
                return returnCode.toString();
            /*}
            catch (Exception e){
                e.printStackTrace();
                return null;
            }*/
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("postUploading","AsyncTask Ended!");
            //Toast.makeText(getApplicationContext(),"returnCode="+s,Toast.LENGTH_SHORT).show();
            if(s.equals("http201")){
                Toast.makeText(getApplicationContext(),"상품 등록 완료~!",Toast.LENGTH_SHORT).show();
                goBackToMainActivity();
            }
        }
    }

    public void goBackToMainActivity(){
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bm != null){
            bm.recycle();
            bm = null;
        }
    }

 }
