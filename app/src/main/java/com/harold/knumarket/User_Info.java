package com.harold.knumarket;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Harold on 2014-11-20.
 */
public class User_Info {

    private String client_No;
    private String client_Id;
    private boolean client_State;

    public boolean isClient_State() {
        return client_State;
    }

    public void setClient_State(boolean client_State) {
        this.client_State = client_State;
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

    private String phone_No;
    private String profile;
    private String addition;
    public ArrayList<String> client_keyword;
    private static User_Info user_info = null;

    public User_Info() {
        this.client_No = null;
        this.client_Id = null;
        this.phone_No = null;
        this.client_keyword = new ArrayList<String>(5);
        this.profile = null;
        this.addition = null;
        this.client_State = false;
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
    public void setClient_keywordList(ArrayList<String> stringList) {
        user_info.client_keyword = stringList;
    }
    public void setClient_keyword(String keyword, int index) {
        //client_keyword.set(index, keyword);
        user_info.getClient_keyword().set(index, keyword);
    }

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


            user_info.getClient_keyword().add(0,"키워드1");
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
    public void SavePostList(SharedPreferences.Editor editor)
    {
        SavePreference(editor);

        // to-do
    }
    public void LoadPostList(SharedPreferences pref)
    {

        // to-do
    }

}
