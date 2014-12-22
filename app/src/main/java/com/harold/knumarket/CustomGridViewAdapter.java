package com.harold.knumarket;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harold.knumarket.Activity.PostActivity;
import com.knumarket.harold.knu_market.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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

        mWidth = (screenWidth / 3) -10;
        //mHeight = mWidth*20/17;
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
            viewHolder.button.getLayoutParams().height = 320;

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

                    viewHolder.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Url = Webserver_Url.getInstance().getUrl();
                    Log.d(TAG,"Url="+Url+"Image/"+item.getFileUrls().get(0));
                    ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.displayImage(Url + "Image/" + item.getFileUrls().get(0), viewHolder.icon);

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

        viewHolder.name.setText(item.getProduct_name());
        viewHolder.price.setText("[\\" +item.getProduct_price() + "]");

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
