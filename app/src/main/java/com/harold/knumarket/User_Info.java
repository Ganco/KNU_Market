package com.harold.knumarket;

import java.util.ArrayList;

/**
 * Created by Harold on 2014-11-20.
 */
public class User_Info {

    private String client_No;
    private String client_Id;
    private String phone_No;
    private ArrayList<String> client_keyword;
    private static User_Info user_info = null;

    public User_Info() {
        this.client_No = "0";
        this.client_Id = "Test_User1";
        this.phone_No = "01071229615";
        this.client_keyword = null;
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

    public ArrayList<String> getClient_keyword() {
        return client_keyword;
    }

    public static synchronized User_Info getUser_info(){
        if(null == user_info){
            user_info = new User_Info();
        }
        return user_info;
    }
}
