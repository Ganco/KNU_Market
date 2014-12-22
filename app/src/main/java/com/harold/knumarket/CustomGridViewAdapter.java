package com.harold.knumarket;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.knumarket.harold.knu_market.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Harold on 2014-12-20.
 */
public class CustomGridViewAdapter extends BaseAdapter {

    private static final String TAG = "CustomGridViewAdapter";
    private boolean mIsScrolling;
    private Context mContext;
    private ArrayList<Post_DTO> mItems;
    private int mRowId;
    private static LayoutInflater inflater=null;
    private int mWidth, mHeight;

    //웹서버 url정보를 WebServer_Url클래스 하나로 관리함(싱글톤 패턴 사용)
    private String Url;

    public CustomGridViewAdapter(Context context, ArrayList<Post_DTO> mItems,int mRowId) {

        Log.d(TAG, " CustomGridViewAdapter()");

        this.mContext = context;
        this.mItems = mItems;
        this.mRowId = mRowId;
        mIsScrolling = false;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 파라메터랑 멤버변수 이름이 같으면 모호해지지
        if(this.mItems == null){
            this.mItems = new ArrayList<Post_DTO>();
         }

        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHight = metrics.heightPixels;

        mWidth = (screenWidth / 3) -10;
        mHeight = 320;
    }

    public void clearItems(){
        mItems.clear();
    }

    public void addItems(ArrayList<Post_DTO> items) {
        for(int i=0 ; i< items.size() ; i++)
            Log.d(TAG, "addItems()"+items.get(i).getProduct_name());
        mItems.addAll(items);
    }

    public void setScrolling(boolean scrolling)
    {
        mIsScrolling = scrolling;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder{
        public int position;
        public LinearLayout button;
        public ImageView icon;
        public TextView name;
        public TextView price;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View v = convertView;
        if ( convertView == null ){
            v = inflater.inflate(mRowId, null);

            viewHolder = new ViewHolder();
            viewHolder.button = (LinearLayout)v.findViewById(R.id.grid_item_linear);
            viewHolder.icon = (ImageView)v.findViewById(R.id.grid_imageView);
            viewHolder.name = (TextView)v.findViewById(R.id.grid_textView1);
            viewHolder.price = (TextView)v.findViewById(R.id.grid_textView2);
            viewHolder.icon.getLayoutParams().width = this.mWidth;
            viewHolder.icon.getLayoutParams().height = 235;
            viewHolder.button.getLayoutParams().width = this.mWidth;
            viewHolder.button.getLayoutParams().height = this.mHeight;

            v.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)v.getTag();
        }

        Post_DTO item = mItems.get(position);

        Log.d(TAG, "getView():"+item.getProduct_name());

        viewHolder.position = position;
        if ( !mIsScrolling ){
            Log.d(TAG, "getView(): mIsScrolling="+mIsScrolling);

            if ( item.getFileUrls() != null && item.getFileUrls().size() > 0 ){
                Log.d(TAG, "getView(): getImgFiles()="+item.getFileUrls()
                        +"size="+item.getFileUrls().size());

                String url = item.getFileUrls().get(0);
                if ( !url.equals(viewHolder.icon.getTag()) ){
                    Log.d(TAG, "getView():url="+url+"viewHolder.icon.getTag()="+viewHolder.icon.getTag());

                    Drawable drawable = viewHolder.icon.getBackground();
                    viewHolder.icon.setImageDrawable(null);

                    if (drawable instanceof BitmapDrawable) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        bitmap.recycle();
                    }

                    viewHolder.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Url = Webserver_Url.getInstance().getUrl();
                    Log.d(TAG,"Url="+Url+"Image/"+item.getFileUrls().get(0));

                    //AUIL 이미지 옵션 설정
                    DisplayImageOptions options = new DisplayImageOptions.Builder()
                            //.showImageOnLoading(R.drawable.ic_empty) // 로딩중 이미지 설정
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
                    imageLoader.displayImage(Url + "Image/" + item.getFileUrls().get(0), viewHolder.icon, options);

                }
                viewHolder.icon.setTag(url);
            }
        }
        else {
            //viewHolder.icon.setImageResource(R.drawable.ic_empty);
            viewHolder.icon.setBackgroundColor(Color.WHITE);
            viewHolder.button.setBackgroundColor(Color.WHITE);
        }
//        String addr = item.mb_seller_addr.split("-")[0];
//        viewHolder.name.setText(String.format("[%s]%s", addr, item.mb_seller_name));
//        viewHolder.desc.setText(item.gd_name);
//        viewHolder.price.setText(String.format("%,d %s", item.gd_price, mContext.getString(R.string.common_price_unit)));

        if(item.getProduct_state().equals("Sell")){
            viewHolder.button.setBackgroundColor(Color.parseColor("#C3FF7961"));
        }
        else{
            viewHolder.button.setBackgroundColor(Color.parseColor("#B2CCFF"));
        }

        viewHolder.name.setTextSize(13);
        viewHolder.price.setTextSize(13);

        viewHolder.name.setGravity(Gravity.CENTER|Gravity.BOTTOM);
        viewHolder.price.setGravity(Gravity.CENTER|Gravity.BOTTOM);

        viewHolder.name.setTextColor(Color.BLACK);
        viewHolder.price.setTextColor(Color.BLACK);
        viewHolder.name.setText(item.getProduct_name());
        viewHolder.price.setText(item.getProduct_price() + "원");

        return v;
    }

    private static class ThumbnailTask extends AsyncTask<String, Void, Bitmap> {
        private Context mContext;
        private int mPosition;
        private ViewHolder mHolder;

        public ThumbnailTask(Context context, int position, ViewHolder holder) {
            mContext = context;
            mPosition = position;
            mHolder = holder;
            //mHolder.icon.setImageResource(R.drawable.myshop_img_default);
            mHolder.icon.setBackgroundColor(Color.WHITE);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (mHolder.position == mPosition) {
                if ( bitmap != null ){
                    //mHolder.icon.setImageBitmap(bitmap);
                    //Animation fadeInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
                    //mHolder.icon.startAnimation(fadeInAnimation);
                    mHolder.icon.setBackgroundDrawable(new BitmapDrawable(bitmap));
                }
            }
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            // TODO Auto-generated method stub
            //return BitmapUtil.DownloadImageCache(mContext, urls[0]);//DownloadImageCache(mContext, urls[0]);
            return null;
        }

        private Bitmap DownloadImage(String url) {
            Bitmap bmp =null;
            try{
                URL ulrn = new URL(url);
                HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
                InputStream is = con.getInputStream();
                bmp = BitmapFactory.decodeStream(is);

                if (null != bmp)
                    return bmp;

            }catch(Exception e){}
            return bmp;
        }

        private Bitmap DownloadImageCache(Context context, String url) {
            String path = context.getCacheDir().getPath();
            String filename = path + "/" + url.replace('/', '_').replace(':', '_');
            Bitmap bmp = BitmapFactory.decodeFile(filename);

            if ( bmp == null ){
                bmp = DownloadImage(url);
                try {
                    File file = new File(filename);
                    FileOutputStream fos = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 70, fos);
                    fos.close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return bmp;
        }
    }
}

