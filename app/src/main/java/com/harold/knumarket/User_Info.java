package com.harold.knumarket;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Harold on 2014-11-20.
 */
public class User_Info {

    private String client_No;
    private String client_Id;
    private boolean client_State;
    private int LastPostNo;
    private boolean alarmOnOff;
    private String phone_No;
    private String profile;
    private String addition;
    public ArrayList<String> client_keyword;
    public Set<String> alarmPosts;
    public Set<String> zzimPosts;
    //private int alarmPostCount;
    private static User_Info user_info = null;

    public void setLastPostNo(int lastPostNo) {
        if(lastPostNo > LastPostNo)
            LastPostNo = lastPostNo;
    }

    public int getLastPostNo() { return LastPostNo; }
    //public void setAlarmPostCount(int AlarmPostCount) { alarmPostCount = AlarmPostCount; }
    //public int getAlarmPostCount() { return alarmPostCount; }
    public void setClient_State(boolean clientState) { client_State = clientState; }

    public boolean getClient_State() { return client_State; }

    public boolean getAlarmOnOff() {
        return alarmOnOff;
    }

    public void setAlarmOnOff(boolean alarm_OnOff) {
        this.alarmOnOff = alarm_OnOff;
    }

    public void setPhone_No(String phone_No) {
        this.phone_No = phone_No;
    }

    public void setClient_Id(String client_Id) {
        this.client_Id = client_Id;
    }

    public void setClient_No(String client_No) {
        this.client_No = client_No;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public User_Info() {
        this.client_No = null;
        this.client_Id = null;
        this.phone_No = null;
        this.client_keyword = new ArrayList<String>(5);
        this.profile = null;
        this.addition = "";
        this.client_State = false;
        this.alarmOnOff = false;
        this.alarmPosts = new TreeSet<String>();
        this.zzimPosts = new TreeSet<String>();
       // this.alarmPostCount = 0;
        //this.client_keyword = null;
    }

    public User_Info(String client_No, String client_Id, String phone_No, ArrayList<String> client_keyword) {
        this.client_No = client_No;
        this.client_Id = client_Id;
        this.phone_No = phone_No;
        this.client_keyword = client_keyword;
    }

    public String getClient_Id() {
        return client_Id;
    }

    public String getClient_No() {
        return client_No;
    }

    public String getPhone_No() {
        return phone_No;
    }

    public ArrayList<String> getClient_keyword() { return user_info.client_keyword; }

    public void setClient_keywordList(ArrayList<String> stringList) { user_info.client_keyword = stringList; }

    public void setClient_keyword(String keyword, int index) {
        //client_keyword.set(index, keyword);
        user_info.getClient_keyword().set(index, keyword);
    }

    public Set<String> getAlarmPosts() { return user_info.alarmPosts; }

    public void setAlarmPosts(Set<String> stringSet) { user_info.alarmPosts = stringSet; }

    public Set<String> getZzimPosts() { return user_info.zzimPosts; }

    public void setZzimPosts(Set<String> stringSet) { user_info.zzimPosts = stringSet; }

    public static synchronized User_Info getUser_info(){
        if(null == user_info){
            user_info = new User_Info();

           // Log.i("KNU_Market/User_Info", "after user_info=");
            //user_info.setClient_keyword("test1",0);
           // user_info.setClient_keyword("test4",3);


            //Log.i("KNU_Market/User_Info", "add user_info=");
            //user_info.getClient_keyword().add(0,"test1");
           // Log.i("KNU_Market/User_Info", "set user_info=");
            //user_info.setClient_keyword("test4",3);


            user_info.getClient_keyword().add(0,"");
            user_info.getClient_keyword().add(1,"");
            user_info.getClient_keyword().add(2,"");
            user_info.getClient_keyword().add(3,"");
            user_info.getClient_keyword().add(4,"");
            //user_info.setClient_keyword("test4",5);
            //Log.i("KNU_Market/User_Info", "finish user_info=");
        }
        return user_info;
    }

    public void SavePreference(SharedPreferences.Editor editor)
    {
        // preference로 키워드정보 저장

        editor.putString("client_No", client_No);
        editor.putString("client_Id", client_Id);
        editor.putString("phone_No", phone_No);
        editor.putString("profile", profile);
        editor.putString("addition", addition);
        editor.putString("client_keyword1", client_keyword.get(0));
        editor.putString("client_keyword2", client_keyword.get(1));
        editor.putString("client_keyword3", client_keyword.get(2));
        editor.putString("client_keyword4", client_keyword.get(3));
        editor.putString("client_keyword5", client_keyword.get(4));

        editor.commit();
    }
    public void LoadPreference(SharedPreferences pref)
    {
        // preference로 키워드정보 로딩

        client_No = pref.getString("client_No", "");
        client_Id = pref.getString("client_Id", "");
        phone_No = pref.getString("phone_No", "");
        profile = pref.getString("profile", "");
        addition = pref.getString("addition", "");
        client_keyword.set(0, pref.getString("client_keyword1", ""));
        client_keyword.set(1, pref.getString("client_keyword2", ""));
        client_keyword.set(2, pref.getString("client_keyword3", ""));
        client_keyword.set(3, pref.getString("client_keyword4", ""));
        client_keyword.set(4, pref.getString("client_keyword5", ""));
    }
    public void SaveAlarmPosts(SharedPreferences.Editor editor)
    {
        editor.putStringSet("alarmPosts", alarmPosts);
        editor.commit();
        Log.i("KNU_Market/User_Info", "alarmPosts Saved");

    }
    public void LoadAlarmPosts(SharedPreferences pref)
    {
        alarmPosts = pref.getStringSet("alarmPosts",new TreeSet<String>());
        /*  // 테스트용 데이터
        if(alarmPostCount == 0) {
            alarmPosts.add("33");
            alarmPosts.add("54");
            alarmPosts.add("57");
            alarmPosts.add("56");
            alarmPostCount = 4;
        }*/

        for(Iterator i = alarmPosts.iterator(); i.hasNext(); ) {
            Log.i("KNU_Market/User_Info", "alarmPostNum=" + i.next());
        }
    }
    public void SaveZzimPosts(SharedPreferences.Editor editor)
    {
        editor.putStringSet("zzimPosts", zzimPosts);
        editor.commit();
        Log.i("KNU_Market/User_Info", "zzimPosts Saved");

    }
    public void LoadZzimPosts(SharedPreferences pref)
    {
        zzimPosts = pref.getStringSet("zzimPosts",new TreeSet<String>());
        /*  // 테스트용 데이터
        if(alarmPostCount == 0) {
            alarmPosts.add("33");
            alarmPosts.add("54");
            alarmPosts.add("57");
            alarmPosts.add("56");
            alarmPostCount = 4;
        }*/

        for(Iterator i = zzimPosts.iterator(); i.hasNext(); ) {
            Log.i("KNU_Market/User_Info", "zzimPosts=" + i.next());
        }
    }
    public void SaveLastPostNo(SharedPreferences.Editor editor)
    {
        editor.putInt("LastPostNo", LastPostNo);
        editor.commit();
    }
    public void LoadLastPostNo(SharedPreferences pref)
    {
        LastPostNo = pref.getInt("LastPostNo", 0);
    }
    public void SaveAlarmOnOff(SharedPreferences.Editor editor)
    {
        editor.putBoolean("alarmOnOff", alarmOnOff);
        editor.commit();
    }
    public void LoadAlarmOnOff(SharedPreferences pref)
    {
        alarmOnOff = pref.getBoolean("alarmOnOff", false);  // defualt는 false
    }
}
