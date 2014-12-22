package com.harold.knumarket;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Harold on 2014-11-28.
 */
public class AndroidUploader {

    private String serverUrl;
    public enum ReturnCode { noPicture, unknown, http201, http400, http401, http403, http404, http500};
    private DataOutputStream dataStream = null;
    static String CRLF = "\r\n";
    static String twoHyphens = "--";
    static String boundary = "*****b*o*u*n*d*a*r*y*****";
    private String TAG = "멀티파트 테스트";

    public ReturnCode uploadPost(Post_DTO post_DTO){

        Log.i("AndroidUploader uploadPost","file Array Size = "+ post_DTO.getImgFiles().size());
        if (post_DTO.getImgFiles().size() != 0) {

            try {
                serverUrl = Webserver_Url.getInstance().getUrl();
                String savePostUrl = serverUrl+"JSP/SavePost.jsp";
                //FileInputStream fileInputStream = new FileInputStream(uploadFile);
                URL connectURL = new URL(savePostUrl);
                Log.i("AndroidUploader uploadPost","server postUpload Url  = "+ savePostUrl);

                HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
                //conn.setChunkedStreamingMode(1024);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");

                //conn.setRequestProperty("User-Agent", "myFileUploader");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.connect();


                dataStream = new DataOutputStream(conn.getOutputStream());
                writeFormField("Client_id", post_DTO.getClient_id());
                writeFormField("Category_id", post_DTO.getCategory_id());
                writeFormField("Product_name", post_DTO.getProduct_name());
                writeFormField("Product_price", post_DTO.getProduct_price());
                writeFormField("Product_detail", post_DTO.getProduct_detail());
                writeFormField("Product_state", post_DTO.getProduct_state());

                for (int i = 0; i < post_DTO.getPost_keyword().size(); i++)
                    writeFormField("keyword"+(i+1), post_DTO.getPost_keyword().get(i));

                ArrayList<FileInputStream> fileInputStreams = new ArrayList<FileInputStream>(3);
                for (int i = 0; i < post_DTO.getImgFiles().size(); i++)
                    fileInputStreams.add(new FileInputStream(post_DTO.getImgFiles().get(i)));

                for (int i = 0; i < post_DTO.getImgFiles().size(); i++) {
                    dataStream.flush();
                    writeFileField("file" + (i + 1) + "Name", post_DTO.getClient_id() + System.currentTimeMillis()+".jpg", "image/jpg", fileInputStreams.get(i));
                    fileInputStreams.get(i).close();
                }

                // final closing boundary line
                dataStream.writeBytes(twoHyphens + boundary + twoHyphens + CRLF);


                dataStream.flush();
                dataStream.close();
                dataStream = null;

                Log.d("업로드 테스트", "***********전송완료***********");

                String response = getResponse(conn);
                //String response = conn.getResponseMessage();

                Log.d("업로드 테스트","response : "+response);

                int responseCode = conn.getResponseCode();

                if (response.contains("Upload Success"))
                    return ReturnCode.http201;
                else
                    // for now assume bad name/password
                    return ReturnCode.http401;
            } catch (MalformedURLException mue) {
                Log.e(TAG, "error: " + mue.getMessage(), mue);
                return ReturnCode.http400;
            } catch (IOException ioe) {
                Log.e(TAG, "error: " + ioe.getMessage(), ioe);
                return ReturnCode.http500;
            } catch (Exception e) {
                Log.e(TAG, "error: " + e.getMessage(), e);
                return ReturnCode.unknown;
            }
        }
        else{
            return ReturnCode.noPicture;
        }
    }

    private void writeFormField(String fieldName, String fieldValue)  {
        try{
            dataStream.writeBytes(twoHyphens + boundary + CRLF);
            dataStream.writeBytes("Content-Disposition: form-data; name=\"" + fieldName + "\"" + CRLF);
            dataStream.writeBytes(CRLF);
            //dataStream.writeBytes(fieldValue);
            dataStream.write(fieldValue.getBytes("UTF-8"));
            dataStream.writeBytes(CRLF);
            Log.e(TAG, "AndroidUploader.writeFormField DataStream"+ fieldName+":"+ fieldValue+ " 입력 완료");

        }catch(Exception e){
            //System.out.println("AndroidUploader.writeFormField: got: " + e.getMessage());
            Log.e(TAG, "AndroidUploader.writeFormField: " + e.getMessage());
        }
    }

    private void writeFileField(String fieldName, String fieldValue, String type, FileInputStream fis) {

        try {
            // opening boundary line
            dataStream.writeBytes(twoHyphens + boundary + CRLF);
            dataStream.writeBytes("Content-Disposition: form-data; name=\""
                    + fieldName
                    + "\";filename=\""
                    + fieldValue
                    + "\""
                    + CRLF);

            dataStream.writeBytes("Content-Type: " + type +  CRLF);
            dataStream.writeBytes(CRLF);

            // create a buffer of maximum size
            int bytesAvailable = fis.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];

            // read file and write it into form...
            int bytesRead = fis.read(buffer, 0, bufferSize);
            while (bytesRead > 0){
                dataStream.write(buffer, 0, bytesRead);
                bytesAvailable = fis.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fis.read(buffer, 0, bufferSize);
                dataStream.flush();
            }
            // closing CRLF
            dataStream.writeBytes(CRLF);
            Log.e(TAG, "AndroidUploader.writeFileField DataStream"+ fieldName+":"+ fieldValue+ "입력 완료");

        }
        catch(Exception e){
            //System.out.println("GeoPictureUploader.writeFormField: got: " + e.getMessage());
            Log.e(TAG, "AndroidUploader.writeFileField: got: " + e.getMessage());
        }

    }

    private String getResponse(HttpURLConnection conn)	{

        try{
            DataInputStream dis = new DataInputStream(conn.getInputStream());
            byte[] data = new byte[1024];
            int len = dis.read(data, 0, 1024);

            dis.close();
            int responseCode = conn.getResponseCode();

            if (len > 0)
                return new String(data, 0, len);
            else
                return "";
        }
        catch(Exception e){
            //System.out.println("AndroidUploader: "+e);
            Log.e(TAG, "AndroidUploader: "+e);
            return "";
        }
    }
    /**

     *  this mode of reading response no good either

     */
    private String getResponseOrign(HttpURLConnection conn)	    {

        InputStream is = null;

        try {
            is = conn.getInputStream();
            // scoop up the reply from the server
            int ch;
            StringBuffer sb = new StringBuffer();
            while( ( ch = is.read() ) != -1 ) {
                sb.append( (char)ch );
            }
            return sb.toString();   // TODO Auto-generated method stub
        }
        catch(Exception e){
            //System.out.println("GeoPictureUploader: biffed it getting HTTPResponse");
            Log.e(TAG, "AndroidUploader: "+e);
        }
        finally {
            try {
                if (is != null)
                    is.close();
            }
            catch (Exception e) {}
        }
        return "";
    }
}
