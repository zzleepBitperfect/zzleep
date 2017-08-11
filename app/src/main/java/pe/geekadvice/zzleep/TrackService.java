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

public class TrackService extends Service implements LocationListener
{
	private LocationManager lm;
	private MainActivity ctx;
	private ArrayList<ZzleepPlace> destinies;
	private Boolean EnCamino=false;
	private int posicion=-1;
	private LatLng paradero;
	private LatLng miposicion;
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
        Toast.makeText(this, "He sido creado - meeeeeeeeeee:", Toast.LENGTH_SHORT).show();

        EnCamino = true;
        posicion = ctx.getPosicionParadero();
        destinies = ctx.getDestinies();
        if(posicion!=-1)
            paradero= new LatLng(Double.parseDouble(destinies.get(posicion).getLatitude()),Double.parseDouble(destinies.get(posicion).getLongitude()));
        startLocation();
		ctx.startAudio();
		//Toast.makeText(this, "Comando activado - flags:" + flags, Toast.LENGTH_SHORT).show();
		return super.onStartCommand(intent, flags, startId);
		//return START_STICKY;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		Toast.makeText(this, "He sido destrudio pero volveré :|", Toast.LENGTH_SHORT).show();
		EnCamino=false;
		ctx.stopAudio();
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		Toast.makeText(this, "Intentaron bidearme!!!", Toast.LENGTH_SHORT).show();
		throw new UnsupportedOperationException("Not yet implemented");
	}

	public void startLocation()
	{
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
		{
			Criteria criteria = new Criteria();
			String bestProvider = lm.getBestProvider(criteria, false);
			if (lm.getLastKnownLocation(bestProvider) != null)
				Toast.makeText(this, "Estas en camino!", Toast.LENGTH_SHORT).show();

			lm.requestLocationUpdates(bestProvider, 1000, 1F, this);
		} else {
			Toast.makeText(this, "No tengo permisos", Toast.LENGTH_SHORT).show();
		}
	}

	//Location
	@Override
	public void onLocationChanged(Location location)
	{
        //Toast.makeText(this, "changedLocation", Toast.LENGTH_SHORT).show();
		miposicion = new LatLng(location.getLatitude(), location.getLongitude());
		try {
			ctx.updateUserMarker();
		}catch (Exception e){
		}



		if(EnCamino && posicion!=-1) {
			Location location1 = new Location("");
			location1.setLatitude(miposicion.latitude);
			location1.setLongitude(miposicion.longitude);
			Location location2 = new Location("");
			location2.setLatitude(paradero.latitude);
			location2.setLongitude(paradero.longitude);
			Double distanceInMeters = new Double(location1.distanceTo(location2));
			((TextView)ctx.findViewById(R.id.km)).setText(Double.toString(distanceInMeters.intValue())+" metros");
			if (distanceInMeters < destinies.get(posicion).getRadio()+100){
				((TextView)ctx.findViewById(R.id.km)).setText("LLEGASTE!");
				ctx.startAlarm();
				destinies.get(posicion).setStatus(0);
				ctx.recargarParaderos(posicion);
				ctx.stopAudio();
			}
		}

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
