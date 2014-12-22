package com.harold.knumarket;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Harold on 2014-11-28.
 */
public class Post_DTO {

    private int post_no;
    private String client_id;
    private String category_id;
    private String product_name;
    private String product_price;
    private String product_detail;
    private ArrayList<String> post_keyword;
    private ArrayList<File> upLoadFiles = new ArrayList<File>(3);
    private  ArrayList<String > fileUrls = new ArrayList<String>(3);
    private String product_state;

    public Post_DTO(String client_id, String category_id, String product_name,
                        String product_price, String product_detail,
                        ArrayList<String> post_keyword,
                        ArrayList<File> imgFiles, String product_state) {
            this.client_id = client_id;
            this.category_id = category_id;
            this.product_name = product_name;
            this.product_price = product_price;
            this.product_detail = product_detail;
            this.post_keyword = post_keyword;
            this.upLoadFiles = imgFiles;
            this.product_state = product_state;
        }

    public Post_DTO(){
    }

    public ArrayList<String> getFileUrls() {
        return fileUrls;
    }

    public void setFileUrls(ArrayList<String> fileUrls) {
        this.fileUrls = fileUrls;
    }

    public String getProduct_state() {
        return product_state;
    }

    public String getClient_id() {
            return client_id;
        }

    public String getCategory_id() {
            return category_id;
        }

    public String getProduct_name() {
            return product_name;
        }

    public String getProduct_price() {
            return product_price;
        }

    public String getProduct_detail() {
            return product_detail;
        }

    public ArrayList<String> getPost_keyword() {
            return post_keyword;
        }

    public ArrayList<File> getImgFiles() {
            return upLoadFiles;
        }

    public void fromJSONObject(JSONObject obj) {
        try {
            product_name = obj.getString("name");
            product_price = obj.getString("price");
            fileUrls.add(obj.getString("imgUrl"));
            product_state = obj.getString("product_state");
            post_no = obj.getInt("post_no");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getPost_no() {
        return post_no;
    }
}
