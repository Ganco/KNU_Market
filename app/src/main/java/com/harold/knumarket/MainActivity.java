package com.harold.knumarket;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import com.knumarket.harold.knu_market.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MainActivity extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

    private TabHost mTabHost;
    private ViewPager mViewPager;
    private MyAdapter mPagerAdapter;
    private HashMap<String, TabInfo> mapTabInfo =
            new HashMap<String, MainActivity .TabInfo>();
    List<Fragment> fragments;

    public static final int REQUEST_CODE_MYPAGE = 1005;
    private boolean urlInputFlag = false;

    public boolean isUrlInputFlag() {
        return urlInputFlag;
    }

    public void setUrlInputFlag(boolean urlInputFlag) {
        this.urlInputFlag = urlInputFlag;
    }

    private class TabInfo {
        private String tag;
        private Class<?> clss;
        private Bundle args;
        private Fragment fragment;
        TabInfo(String tag, Class<?> clazz, Bundle args) {
            this.tag = tag;
            this.clss = clazz;
            this.args = args;
        }
    }

    class TabFactory implements TabHost.TabContentFactory {

        private final Context mContext;

        public TabFactory(Context context) {
            mContext = context;
        }
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }

    public void onClick(View v){

        int id = v.getId();
        Intent intent = null;

        switch (id){

            case R.id.btn_goAddPost:
                intent = new Intent(getBaseContext(), add_post.class);
                startActivity(intent);
                finish();
                break;

            case R.id.btn_home:
                break;

            //// insert button listener for MYPAGE
            case R.id.btn_myPage:
                intent = new Intent(getBaseContext(), MyPageActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(intent, REQUEST_CODE_MYPAGE);
                break;

            case R.id.btn_config:
                break;
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //다이얼로그를 통해 어플 실행 시작시 서버URL을 입력받아 실행함(개발용 코드)
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("서버 IP 주소 입력");
        //builder.setMessage("Message");
        final EditText url_Input = new EditText(this);
        url_Input.setText("211.51.176.248");
        builder.setView(url_Input);
        builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Webserver_Url.getInstance().setUrl(url_Input.getText().toString());

                // Initialise the TabHost
                initialiseTabHost(savedInstanceState);
                if (savedInstanceState != null) {
                    mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
                }
                // Initialise ViewPager
                initializeViewPager();
            }
        });
        builder.show();
        //다이얼로그를 통해 어플 실행 시작시 서버URL을 입력받아 실행함(개발용 코드)

         //Android Universal Image Loader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCacheExtraOptions(480, 800)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // 마켓에 포팅하실땐 빼주세요.
                .build();
        ImageLoader.getInstance().init(config);

        /*
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub) // 로딩중 이미지 설정
                .showImageForEmptyUri(R.drawable.ic_empty) // Uri주소가 잘못되었을경우(이미지없을때)
                .showImageOnFail(R.drawable.ic_error) // 로딩 실패시
                .resetViewBeforeLoading(false)  // 로딩전에 뷰를 리셋하는건데 false로 하세요 과부하!
                .delayBeforeLoading(1000) // 로딩전 딜레이라는데 필요한일이 있                                     을까요..?ㅋㅋ
                .cacheInMemory(true) // 메모리케시 사용여부   (사용하면 빨라지지만 많은 이미지 캐싱할경우 outOfMemory Exception발생할 수 있어요)
                .cacheOnDisc(true) // 디스크캐쉬를 사용여부(사용하세요왠만하면)
                //.preProcessor(...) // 비트맵 띄우기전에 프로세스 (BitmapProcessor이라는 인터페이스를 구연하면 process(Bitmap image)라는 메소드를 사용할 수 있어요. 처리하실게 있으면 작성하셔서 이안에 넣어주시면 됩니다.)
                //.postProcessor(...) // 비트맵 띄운후 프로세스 (위와같이 BitmapProcessor로 처리)
                .considerExifParams(false) // 사진이미지의 회전률 고려할건지
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // 스케일타입설정   (일부밖에없습니다. 제가 centerCrop이 없어서 라이브러리 다 뒤져봤는데 없더라구요. 다른방법이 있습니다. 아래 설명해드릴게요.)
                .bitmapConfig(Bitmap.Config.ARGB_8888) // 이미지 컬러방식
                .build();
         */
    }

    private void initialiseTabHost(Bundle args) {

        mTabHost = (TabHost)findViewById(R.id.tabHost);
        mTabHost.setup();
        TabInfo tabInfo = null;
        MainActivity.AddTab(
                this,
                this.mTabHost,
                this.mTabHost.newTabSpec("Tab 1").setIndicator("최신순"),
                ( tabInfo = new TabInfo("Tab 1", Fragment_section1.class, args))
        );
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        MainActivity.AddTab(
                this,
                this.mTabHost,
                this.mTabHost.newTabSpec("Tab 2").setIndicator("오래된순"),
                ( tabInfo = new TabInfo("Tab 2", Fragment_section2.class, args))
        );
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        MainActivity.AddTab(
                this,
                this.mTabHost,
                this.mTabHost.newTabSpec("Tab 3").setIndicator("카테고리"),
                ( tabInfo = new TabInfo("Tab 3", Fragment_section3.class, args))
        );
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        // Default to first tab
        //this.onTabChanged("Tab1");
        //
        mTabHost.setOnTabChangedListener(this);
    }

    private static void AddTab(MainActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        // Attach a Tab view factory to the spec
        tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    private void initializeViewPager() {

        //List<Fragment> fragments = new Vector<Fragment>();
        fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, Fragment_section1.class.getName()));
        fragments.add(Fragment.instantiate(this, Fragment_section2.class.getName()));
        fragments.add(Fragment.instantiate(this, Fragment_section3.class.getName()));
        this.mPagerAdapter  = new MyAdapter(super.getSupportFragmentManager(), fragments);
        //
        this.mViewPager = (ViewPager)super.findViewById(R.id.pager);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.setOnPageChangeListener(this);
    }

    private class MyAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        public MyAdapter(FragmentManager fm, List<Fragment> fragments ) {
            super(fm);
            this.fragments = fragments;
        }
        @Override
        public Fragment getItem(int i) {
            return this.fragments.get(i);
        }
        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHost.getCurrentTabTag());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int position) {
        this.mTabHost.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onTabChanged(String tag) {
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
    }

   }
