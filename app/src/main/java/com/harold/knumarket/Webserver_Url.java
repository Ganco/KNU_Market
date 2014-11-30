package com.harold.knumarket;

/**
 * Created by Harold on 2014-11-11.
 */

/*
웹서버의 URL를 저장하는 클래스, 하나의 객체만 생성되며 프로젝트 전역에서 사용가능
MainActivity에서 다이얼로그를 통해 서버의 Url 정보를 입력받음
 */
public class Webserver_Url {

    private String Url = "http://211.51.176.248:5001/KNU_Market/";

    public Webserver_Url(String url) {
        Url = url;
    }

    public Webserver_Url() {
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = "http://"+url+":5001/KNU_Market/";
    }

    //싱글톤 패턴 -> 무조건 하나의 객체만 존재함
    private static Webserver_Url instance = new Webserver_Url();
    public static synchronized Webserver_Url getInstance(){
        if(instance == null){
            instance = new Webserver_Url();
        }
        return instance;
    }
}
