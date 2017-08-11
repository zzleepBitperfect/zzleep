package pe.geekadvice.zzleep;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import pe.geekadvice.zzleep.adapters.ZzleepPlace;

public class ServiceGPS extends Service implements LocationListener
{
    private MainActivity ctx;
    private LocationManager lm;
    @Override
    public void onCreate()
    {
        super.onCreate();
        ctx=MainActivity.getInstance();

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            Criteria criteria = new Criteria();
            String bestProvider = lm.getBestProvider(criteria, false);
            lm.requestLocationUpdates(bestProvider, 1000, 1F, this);
        }
        startLocation();
        return super.onStartCommand(intent, flags, startId);
        //return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

    }
    public void startLocation()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            Criteria criteria = new Criteria();
            String bestProvider = lm.getBestProvider(criteria, false);
            if (lm.getLastKnownLocation(bestProvider) != null){}
                //Toast.makeText(this, "Estas en camino:" + lm.getLastKnownLocation(bestProvider).getLatitude(), Toast.LENGTH_SHORT).show();

            lm.requestLocationUpdates(bestProvider, 1000, 1F, this);
        } else {
            Toast.makeText(this, "No tengo permisos", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public IBinder onBind(Intent intent)
    {
        //Toast.makeText(this, "Intentaron bidearme!!!", Toast.LENGTH_SHORT).show();
        throw new UnsupportedOperationException("Not yet implemented");
    }


    //Location
    @Override
    public void onLocationChanged(Location location)
    {
        ctx.setUserMarker(new LatLng(location.getLatitude(),location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {

    }

    @Override
    public void onProviderDisabled(String provider)
    {

    }
}
