package com.lebig4.serverConnection;

/**
 * Created by TheAnPheel on 2018-01-20.
 */

import android.annotation.TargetApi;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class PostRequest {

    private final String SERVER_URL = "http://ec2-18-216-96-61.us-east-2.compute.amazonaws.com/analyse.php";
    public enum Direction {
        upload, download
    }
    private Direction direction;
    private String params;

    public PostRequest(Direction upOrDown, String params){
        this.direction = upOrDown;
        this.params = params;
        generateDir();
    }

    private void generateDir(){
        params = "id="+direction.toString() + "&"+params;
    }

    @TargetApi(19)
    public String excutePost()
    {
        URL url;
        byte[] max = this.params.getBytes( StandardCharsets.UTF_8 );
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(SERVER_URL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(max.length));
            connection.setRequestProperty("Content-Language", "en-US");
            //connection.setRequestProperty("id", "test");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.write(max);
            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
    }
}
