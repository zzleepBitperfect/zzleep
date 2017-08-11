package pe.geekadvice.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by yaguar on 05/06/2017.
 */

public class JsonURL {
    public JsonURL(){

    }
    public String read(String urlObtener){
        HttpURLConnection connection = null;
        BufferedReader reader =null;
            try {
            URL url = new URL(urlObtener);
            Log.v("GA", url.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            int status = connection.getResponseCode();
            if(status != 200)
                return "";
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();

            String line ="";
            while((line = reader.readLine()) !=null){
                buffer.append(line);

            }
            Log.v("GA", buffer.toString());

            return buffer.toString();
        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(connection !=null) {
                connection.disconnect();
            }
        }
        return "";
    }
}
