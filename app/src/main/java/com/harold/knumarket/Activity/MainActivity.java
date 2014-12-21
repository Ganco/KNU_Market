package com.harold.knumarket.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.TabHost;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.harold.knumarket.AlarmService;
import com.harold.knumarket.BackPressCloseHandler;
import com.harold.knumarket.User_Info;
import com.harold.knumarket.Webserver_Url;
import com.harold.knumarket.categories.Category_Book;
import com.harold.knumarket.categories.Category_Fashion;
import com.harold.knumarket.categories.Category_ListView;
import com.harold.knumarket.categories.Category_Living;
import com.harold.knumarket.fragment.Fragment_section1;
import com.harold.knumarket.fragment.Fragment_section2;
import com.harold.knumarket.fragment.Fragment_section3;
import com.knumarket.harold.knu_market.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MainActivity extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

    private TabHost mTabHost;
    private ViewPager mViewPager;
    private MyAdapter mPagerAdapter;
    private HashMap<String, TabInfo> mapTabInfo =
            new HashMap<String, MainActivity .TabInfo>();
    List<Fragment> fragments;

    private static final int REQUEST_CODE_ADDPOST = 1004;
    public static final int REQUEST_CODE_MYPAGE = 1005;
    private boolean urlInputFlag = false;

    private AlertDialog.Builder builder;
    private DialogInterface mPopupDlg = null;
    private BackPressCloseHandler backPressCloseHandler;

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
                intent = new Intent(getBaseContext(), addPostActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(intent, REQUEST_CODE_ADDPOST);
                //finish();
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
                intent = new Intent(getBaseContext(), ConfigActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
            case R.id.btn_search:
                intent = new Intent(getBaseContext(), SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
            case R.id.btn_zzim:
                break;
            case R.id.btn_alarm:
                intent = new Intent(getBaseContext(), AlarmActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;


            case R.id.btn_f000:
                intent = new Intent(getBaseContext(), Category_Book.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("category_no", "000");
                startActivity(intent);
                break;

            case R.id.btn_f100:
                intent = new Intent(getBaseContext(), Category_Living.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("category_no", "100");
                startActivity(intent);
                break;

            case R.id.btn_f200:
                intent = new Intent(getBaseContext(), Category_Fashion.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("category_no", "200");
                startActivity(intent);
                break;

            case R.id.btn_f300:
                intent = new Intent(getBaseContext(), Category_ListView.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("category_no", "300");
                startActivity(intent);
                break;
           // */
        }
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 뒤로가기 2연속 체크
        backPressCloseHandler = new BackPressCloseHandler(this);

        //Intent intent = new Intent(this, AlarmService.class);
        //startService(intent);
        // 알람 서비스 시작 -> 로그온 정보를 알람activity가 계속 유지하게되는 현상


        // preference로 키워드정보 로딩
        // onStart -> 메인Activity 볼때마다 강제갱신된다. onCreate에 넣는게 맞는듯
        User_Info userInfo = User_Info.getUser_info();
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        userInfo.LoadPreference(pref);
        //

        ///*  // 서버 IP 입력받는 팝업창
        //다이얼로그를 통해 어플 실행 시작시 서버URL을 입력받아 실행함(개발용 코드)
        builder = new AlertDialog.Builder(this);
        builder.setTitle("서버 IP 주소 입력");
        final EditText url_Input = new EditText(this);
        //url_Input.setText("211.51.176.248");//내 방 공유기 외부 아이피 주소
        url_Input.setText("155.230.29.162");
        //url_Input.setText("220.94.32.180");
        builder.setView(url_Input);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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
        mPopupDlg = builder.show();
        //다이얼로그를 통해 어플 실행 시작시 서버URL을 입력받아 실행함(개발용 코드)
        //*/


        /*  // IP주소 입력 팝업창 없이 바로 연결
        Webserver_Url.getInstance().setUrl("211.51.176.248");
        Webserver_Url.getInstance().setUrl("220.94.32.180");

        Log.i("KNU_Market/Main Url= ", Webserver_Url.getInstance().getUrl());
        // Initialise the TabHost
        initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }
        // Initialise ViewPager
        initializeViewPager();
        //*/

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();

        ImageLoader.getInstance().init(config);
    }
    @Override
    protected void onStart(){
        super.onStart();

        /*
        // preference로 키워드정보 로딩 -> 여기 넣으면 메인화면 부를때마다 pref파일 값으로 강제갱신
        User_Info userInfo = User_Info.getUser_info();
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        userInfo.LoadPreference(pref);
        //*/
    }
    @Override
    protected void onStop(){
        super.onStop();

        // preference로 키워드정보 저장
        User_Info userInfo = User_Info.getUser_info();

        SharedPreferences pref = getSharedPreferences("pref",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        userInfo.SavePreference(editor);

        if(mPopupDlg != null){
            mPopupDlg.dismiss();
        }
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
        //outState.putString("tab", mTabHost.getCurrentTabTag());
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
