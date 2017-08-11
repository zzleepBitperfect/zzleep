package pe.geekadvice.Culqi;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import pe.geekadvice.zzleep.PaymentActivity;

/**
 * Created by YaguarRuna on 14/07/2017.
 */

public class TokenAux extends AsyncTask<String,String,String> {
    int opcion=0;
    PaymentActivity paymentActivity;
    public TokenAux(){
        super();
    }
    public TokenAux(PaymentActivity paymentActivity, int opcion){
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
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Authorization","Bearer "+params[2]);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            JSONObject data= new JSONObject(params[1]);

            String datosAEnviar=data.toString();



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
}
