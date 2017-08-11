package pe.geekadvice.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import android.content.Context;

import pe.geekadvice.zzleep.AlarmsList;
import pe.geekadvice.zzleep.AudioActivity;
import pe.geekadvice.zzleep.ListaParaderos;
import pe.geekadvice.zzleep.MainActivity;
import pe.geekadvice.zzleep.PlaceActivity;
import pe.geekadvice.zzleep.R;
import pe.geekadvice.zzleep.RegisterActivity;
import pe.geekadvice.zzleep.adapters.ZzleepPlace;
import pe.geekadvice.zzleep.adapters.ZzleepPlaceAdapter;
import pe.geekadvice.zzleep.components.StopItem;

/**
 * Created by Carlos on 20/12/16.
 */

public class JsonTask extends AsyncTask<String,String,ArrayList<String>> {

    private MainActivity  activity;
    private PlaceActivity plactivity;
    private RegisterActivity registerActivity;
    private AudioActivity audio;
    private AlarmsList alarmsList;
    private ListaParaderos listaParaderos;
    private int option;

    public  JsonTask(MainActivity activity,int option ){
        this.activity= activity;
        this.option=option;
    }
    public  JsonTask(PlaceActivity activity,int option ){
        this.plactivity= activity;
        this.option=option;
    }
    public  JsonTask(RegisterActivity activity, int option ){
        this.registerActivity= activity;
        this.option=option;
    }
    public JsonTask(AlarmsList activity, int option){
        this.alarmsList=activity;
        this.option=option;
    }
    public  JsonTask(ListaParaderos activity, int option ){
        this.listaParaderos= activity;
        this.option=option;
    }
    public  JsonTask(AudioActivity activity, int option ){
        this.audio= activity;
        this.option=option;
    }

    @Override
    protected ArrayList<String> doInBackground(String... params){
        ArrayList<String> resultado= new ArrayList<>();
        HttpURLConnection connection = null;
        BufferedReader reader =null;
        try {
            String auxUrl = params[0];
            auxUrl = auxUrl.replaceAll(" ", "%20");
            URL url = new URL(auxUrl);
            Log.v("GA", url.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            int status = connection.getResponseCode();
            if(status != 200)
                return resultado;
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();

            String line ="";
            while((line = reader.readLine()) !=null){
                buffer.append(line);

            }
            Log.v("GA", buffer.toString());
            if(params.length>1){
                resultado.add(buffer.toString());
                url = new URL(params[1]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                status = connection.getResponseCode();
                if(status != 200)
                    return resultado;
                stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                buffer = new StringBuffer();

                String line2 ="";
                while((line2 = reader.readLine()) !=null){
                    buffer.append(line2);

                }
                resultado.add(buffer.toString());
                return resultado;
            }
            try{

                reader.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            resultado.add(buffer.toString());
            return resultado;
        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(connection !=null) {
                connection.disconnect();
            }
        }

        // Convert response to string using String Builder
        return null;
    }
    @Override
    protected void onPostExecute(ArrayList<String> resultado) {
        super.onPostExecute(resultado);
        if (resultado != null) {
            if (resultado.size() > 0) {
                String result = resultado.get(0);
                if (result.length() > 2)
                    switch (option) {
                        case 1:
                            plactivity.getPlaceJson(result);
                            break;
                        case 2:
                            activity.getArrayStopItem(result);
                            break;
                        case 3:
                            if (resultado.size() > 1) {
                                String result2 = resultado.get(1);
                                plactivity.getPosiblePlaces(result2, result);
                            } else {
                                plactivity.getPosiblePlaces(result, "");
                            }
                            break;
                        case 4:
                            break;
                        case 5:
                            try {
                                alarmsList.obtener_alarmas(result);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 6:
                            listaParaderos.getArrayStopItem(result);
                            break;
                        case 7:
                            activity.obtenerAlarmas(result);
                            break;
                        case 8 :
                            try {
                                audio.obtenerAudiosArray(result);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 9 :
                            try {
                                activity.obtenerAudios(result);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
            }
        }
    }

    }

