package pe.geekadvice.utils;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import pe.geekadvice.zzleep.PaymentActivity;

public class ApiPost extends AsyncTask<String,String,String> {
    int opcion=0;
    PaymentActivity paymentActivity;
    public ApiPost(){
        super();
    }
    public ApiPost(PaymentActivity paymentActivity, int opcion){
        super();
        this.opcion=opcion;
        this.paymentActivity=paymentActivity;
    }
    @Override
    protected String doInBackground(String... params) {

        URL url;
        String response = "";
        try {
            url = new URL(params[0]);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            JSONObject data= new JSONObject(params[1]);

            String datosAEnviar="";
            if(opcion==0) {

                datosAEnviar += "name";
                datosAEnviar += "=";
                datosAEnviar += data.getString("name");
                datosAEnviar += "&";
                datosAEnviar += "address";
                datosAEnviar += "=";
                datosAEnviar += data.getString("address");
                datosAEnviar += "&";
                datosAEnviar += "image";
                datosAEnviar += "=";
                datosAEnviar += data.getString("image");
                datosAEnviar += "&";
                datosAEnviar += "type";
                datosAEnviar += "=";
                datosAEnviar += data.getString("type");
                datosAEnviar += "&";
                datosAEnviar += "user_id";
                datosAEnviar += "=";
                datosAEnviar += data.getString("user_id");
                datosAEnviar += "&";
                datosAEnviar += "lat";
                datosAEnviar += "=";
                datosAEnviar += data.getString("lat");
                datosAEnviar += "&";
                datosAEnviar += "lng";
                datosAEnviar += "=";
                datosAEnviar += data.getString("lng");
                datosAEnviar += "&";
                datosAEnviar += "range";
                datosAEnviar += "=";
                datosAEnviar += data.getString("range");
                datosAEnviar += "&";
                datosAEnviar += "sound_id";
                datosAEnviar += "=";
                datosAEnviar +=  data.getString("sound_id");
                datosAEnviar += "&";
                datosAEnviar += "alarm_id";
                datosAEnviar += "=";
                datosAEnviar += data.getString("alarma_id");
                //datosAEnviar = datosAEnviar.replaceAll(" ", "%20");
            }
            else if(opcion==1){
                datosAEnviar += "token_id";
                datosAEnviar += "=";
                datosAEnviar += data.getString("token_id");
                datosAEnviar += "&";
                datosAEnviar += "user_id";
                datosAEnviar += "=";
                datosAEnviar += data.getString("user_id");
                datosAEnviar += "&";
                datosAEnviar += "user_address";
                datosAEnviar += "=";
                datosAEnviar += data.getString("user_address");
                datosAEnviar += "&";
                datosAEnviar += "user_city";
                datosAEnviar += "=";
                datosAEnviar += data.getString("user_city");
                datosAEnviar += "&";
                datosAEnviar += "user_phone";
                datosAEnviar += "=";
                datosAEnviar += data.getString("user_phone");
            }
            else if(opcion==2){
/*
                datosAEnviar += "user_token";
                datosAEnviar += "=";
                datosAEnviar += data.getString("user_token");
                datosAEnviar += "&";*/
                datosAEnviar += "token_id";
                datosAEnviar += "=";
                datosAEnviar += data.getString("token_id");
                datosAEnviar += "&";
                datosAEnviar += "user_id";
                datosAEnviar += "=";
                datosAEnviar += data.getString("user_id");
                datosAEnviar += "&";
                datosAEnviar += "product_id";
                datosAEnviar += "=";
                datosAEnviar += data.getString("product_id");
            }
            writer.write(
                datosAEnviar
            );

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(opcion==1){
            paymentActivity.procesarCompra();
        }
        if(opcion==2){
            paymentActivity.exitoCompra();
        }
    }
}