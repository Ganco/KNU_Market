package com.harold.knumarket;

/**
 * Created by Harold on 2014-11-11.
 */

/*
웹서버의 URL를 저장하는 클래스, 하나의 객체만 생성되며 프로젝트 전역에서 사용가능
MainActivity에서 다이얼로그를 통해 서버의 Url 정보를 입력받음
 */
public class Webserver_Url {

    private String Url;

    public Webserver_Url(String url) {
        Url = url;
    }
    public String getUrl() {
        return Url;
    }
    public void setUrl(String url) {
        Url = "http://"+url+":5001/KNU_Market/";
    }

    private static Webserver_Url instance = null;
    public static synchronized Webserver_Url getInstance(){
        if(null == instance){
            instance = new Webserver_Url(null);
        }
        return instance;
    }
}
