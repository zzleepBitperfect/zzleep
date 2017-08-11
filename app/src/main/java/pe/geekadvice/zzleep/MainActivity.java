package pe.geekadvice.zzleep;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.renderscript.Double4;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.VideoView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import pe.geekadvice.utils.ApiConnector;
import pe.geekadvice.utils.ApiDelete;
import pe.geekadvice.utils.ApiImg;
import pe.geekadvice.utils.ApiPost;
import pe.geekadvice.utils.DownloadTask;
import pe.geekadvice.utils.JsonTask;
import pe.geekadvice.utils.MusicTask;
import pe.geekadvice.zzleep.adapters.StopItemAdapter;
import pe.geekadvice.zzleep.adapters.ZzleepAlarm;
import pe.geekadvice.zzleep.adapters.ZzleepAudio;
import pe.geekadvice.zzleep.adapters.ZzleepAudioAdapter;
import pe.geekadvice.zzleep.adapters.ZzleepPlace;
import pe.geekadvice.zzleep.adapters.ZzleepPlaceAdapter;
import pe.geekadvice.zzleep.components.MainMenuView;
import pe.geekadvice.zzleep.components.StopCreate;
import pe.geekadvice.zzleep.components.StopItem;
import pe.geekadvice.zzleep.components.StopMenuEvents;
import pe.geekadvice.zzleep.components.StopMenuView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends FragmentActivity implements
		SeekBar.OnSeekBarChangeListener,
		AdapterView.OnItemClickListener,
		OnMapReadyCallback,
		ActivityCompat.OnRequestPermissionsResultCallback,
		GoogleMap.OnMapLongClickListener,
		GoogleMap.OnMapClickListener,
		View.OnClickListener,
		StopMenuEvents,
		GoogleApiClient.OnConnectionFailedListener,
		GoogleApiClient.ConnectionCallbacks,
		AdapterView.OnItemLongClickListener,
		com.google.android.gms.location.LocationListener {


	ImageView alarmIcon;
	ImageView audioIcon;
	ZzleepAlarm alarmaSelected;
	ZzleepAlarm alarmaDefault;

	private Double radioSelected;
	private GoogleMap mMap;
	private LocationManager lm;
	private Marker userMarker = null;
	private Marker targetMarker = null;
	private Circle targetArea = null;
	private AppConfig config;
	private Boolean serviceStarted = false;
	private EditText nameParadero;
	private static ArrayList<ZzleepPlace> destinies;
	private static ArrayList<ZzleepAlarm> alarmas;
	private ZzleepPlace dest;
	private ZzleepPlace placeSelected;
	private ZzleepPlace placeTransaccional;
	private StopMenuView menuStops;
	private MainMenuView menuZzleep;
	private StopCreate menuCreateStop;
	private SeekBar seekBar;
	private Boolean isOpenTutorial = true;
	private Double lat, lng;
	private FirebaseAuth mAuth;
	private FirebaseAuth.AuthStateListener mAuthListener;
	private FirebaseUser user = null;
	private StopItemAdapter destinyadapter;
	private MediaPlayer audioSelected = null;
	private MediaPlayer alarmSelected = null;
	private TextView radioStopMenu = null;

	//Luis Canales
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private final static String TAG = MainActivity.class.getSimpleName();

	private Location location;
	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;

	/*Mis variables ESteban*/
	private Button botonGuardar;
	private Boolean llaveCentrar = false;
	private Boolean llaveParadero = false;
	private JSONArray paraderosActuales;
	private ImageButton btAgregarParadero;
	private ImageView btLogout;
	public String urlRemove = "";
	public static MainActivity instancia;
	public boolean llaveCerrado = false;
	public VideoView vdoView;
	public TextView resetLocation;
	public Intent servicioTrack;
	public Intent servicioMarker;
	public static int posicionParadero = -1;
	private PowerManager powerManager;
	private PowerManager.WakeLock wakeLock;
	private ImageView iconGurdar;
	public static MainActivity getInstance() {
		return instancia;
	}

	private ImageButton btStopVideo;

	public void gotoLogin() {
		startActivity(new Intent(this, WelcomeActivity.class));
	}

	private ArrayList<ZzleepAudio> audiosArray;
	private ZzleepAudio currentAudio;
	private MediaPlayer audioMP;
	private MusicTask musicTask;
	private GridLayout menuTipo;
	private int datoTipoNuevo = 99;
	String tipoPlace = "";

	public void logOut() throws IOException {
		mAuth.signOut();
		user = null;
		destinies = new ArrayList<ZzleepPlace>();
		destinyadapter = new StopItemAdapter(this, 0, destinies);
		ListView lv = (ListView) findViewById(R.id.listUserStops);
		lv.setOnItemClickListener(this);
		lv.setOnItemLongClickListener(this);
		lv.setAdapter(destinyadapter);
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		gotoLogin();
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mAuth = FirebaseAuth.getInstance();
		user = FirebaseAuth.getInstance().getCurrentUser();

		mAuthListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				user = firebaseAuth.getCurrentUser();
			}
		};
		//Verificar sesión

		if (user != null) {
			//String idToken = task.getResult().getToken();
			JsonTask peticionAlarmas = new JsonTask(MainActivity.this, 7);
			JsonTask peticionParaderos = new JsonTask(MainActivity.this, 2);
			JsonTask peticionAudios = new JsonTask(MainActivity.this, 9);

			if (user.isAnonymous()) {
				peticionParaderos.execute("http://clientes.geekadvice.pe/zzleep-server/public/api/v1/places");
			} else {
				String aux_url2 = "http://clientes.geekadvice.pe/zzleep-server/public/api/v1/products?user_id=" + user.getUid();
				peticionAlarmas.execute(aux_url2);

				String aux_url3 = "http://clientes.geekadvice.pe/zzleep-server/public/api/v1/products?type=sound&user_id=" + user.getUid();
				peticionAudios.execute(aux_url3);

				String aux_url = "http://clientes.geekadvice.pe/zzleep-server/public/api/v1/places?user_id=" + user.getUid();
				peticionParaderos.execute(aux_url);


			}
			Log.v("GA-ZZ", "Usuario logueado: " + user.getDisplayName());

			init();
		} else {
			gotoLogin();
		}


		//stopService(new Intent(MainActivity.this, TrackService.class));
	}

	public void init() {
		((TextView)findViewById(R.id.track)).setText("Hola "+user.getDisplayName());
		alarmaDefault = new ZzleepAlarm("Gallo", "icon_alarma3", 3, 0, "", "mono");
		config = new AppConfig(getApplicationContext());
		menuStops = (StopMenuView) findViewById(R.id.menuStops);
		menuZzleep = (MainMenuView) findViewById(R.id.menuZzleep);
		menuCreateStop = (StopCreate) findViewById(R.id.menuCreateStop);
		nameParadero = (EditText) findViewById(R.id.textParadero);
		iconGurdar= (ImageView) findViewById(R.id.iconStopCreateMenu);
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		alarmIcon = (ImageView) findViewById(R.id.iconAlarm);
		botonGuardar = (Button) findViewById(R.id.btnSaveStop);
		audioIcon = (ImageView) findViewById(R.id.iconSoundAgregar);
		btAgregarParadero = (ImageButton) findViewById(R.id.btAgregar);
		resetLocation = (TextView) findViewById(R.id.btnResetLocation);
		btStopVideo = ((ImageButton) findViewById(R.id.btStopVideo));
		vdoView = (VideoView) findViewById(R.id.videoAlarma);
		Button btnBack4 = (Button) findViewById(R.id.btnBack4);
		Button btnBack3 = (Button) findViewById(R.id.btnBack3);
		radioStopMenu = (TextView) findViewById(R.id.tvRadio);
		btLogout = (ImageView) findViewById(R.id.btLogout);
		instancia = this;
		servicioTrack = new Intent(this, TrackService.class);
		powerManager = (PowerManager) getSystemService(POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakelockTag");
		menuTipo = (GridLayout) findViewById(R.id.menuTipo);
		servicioMarker = new Intent(this, ServiceGPS.class);
		menuTipo.setVisibility(View.INVISIBLE);
		((Button)findViewById(R.id.btnKms)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(getApplicationContext(), KilometrosActivity.class),11);
			}
		});
		((ImageButton) findViewById(R.id.imageButton1)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				datoTipoNuevo = 1;
				placeSelected.setType(Integer.toString(datoTipoNuevo));
				try {
					guardarParadero(placeSelected);
					if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
						// TODO: Consider calling
						//    ActivityCompat#requestPermissions
						// here to request the missing permissions, and then overriding
						//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
						//                                          int[] grantResults)
						// to handle the case where the user grants the permission. See the documentation
						// for ActivityCompat#requestPermissions for more details.
						return;
					}
					location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
					updateMapPoint(new LatLng(location.getLatitude(), location.getLongitude()));
					recargarParaderos(0);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				menuTipo.setVisibility(View.INVISIBLE);
			}
		});
		((ImageButton) findViewById(R.id.imageButton2)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				datoTipoNuevo = 2;
				placeSelected.setType(Integer.toString(datoTipoNuevo));
				try {
					guardarParadero(placeSelected);
					if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
						// TODO: Consider calling
						//    ActivityCompat#requestPermissions
						// here to request the missing permissions, and then overriding
						//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
						//                                          int[] grantResults)
						// to handle the case where the user grants the permission. See the documentation
						// for ActivityCompat#requestPermissions for more details.
						return;
					}
					location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
					updateMapPoint(new LatLng(location.getLatitude(), location.getLongitude()));
					recargarParaderos(1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				menuTipo.setVisibility(View.INVISIBLE);
			}
		});
		((ImageButton) findViewById(R.id.imageButton3)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				datoTipoNuevo = 3;
				placeSelected.setType(Integer.toString(datoTipoNuevo));
				try {
					guardarParadero(placeSelected);
					if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
						// TODO: Consider calling
						//    ActivityCompat#requestPermissions
						// here to request the missing permissions, and then overriding
						//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
						//                                          int[] grantResults)
						// to handle the case where the user grants the permission. See the documentation
						// for ActivityCompat#requestPermissions for more details.
						return;
					}
					location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
					updateMapPoint(new LatLng(location.getLatitude(), location.getLongitude()));
					recargarParaderos(2);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				menuTipo.setVisibility(View.INVISIBLE);
			}
		});
		((ImageButton) findViewById(R.id.imageButton4)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				datoTipoNuevo = 4;
				placeSelected.setType("");
				try {
					guardarParadero(placeSelected);
					if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
						// TODO: Consider calling
						//    ActivityCompat#requestPermissions
						// here to request the missing permissions, and then overriding
						//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
						//                                          int[] grantResults)
						// to handle the case where the user grants the permission. See the documentation
						// for ActivityCompat#requestPermissions for more details.
						return;
					}
					location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
					updateMapPoint(new LatLng(location.getLatitude(), location.getLongitude()));
					recargarParaderos(destinies.size() - 1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				menuTipo.setVisibility(View.INVISIBLE);
			}
		});
		((ImageButton) findViewById(R.id.imageButton5)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					alarmIcon.setImageResource(R.mipmap.btn_gallo);
					audioIcon.setImageResource(R.mipmap.btn_audio);
					menuCreateStop.setVisibility(View.INVISIBLE);
					placeTransaccional = placeSelected;
					addStopToMenu(placeTransaccional);
					if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
						// TODO: Consider calling
						//    ActivityCompat#requestPermissions
						// here to request the missing permissions, and then overriding
						//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
						//                                          int[] grantResults)
						// to handle the case where the user grants the permission. See the documentation
						// for ActivityCompat#requestPermissions for more details.
						return;
					}
					location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
					updateMapPoint(new LatLng(location.getLatitude(), location.getLongitude()));
					recargarParaderos(destinies.size()-1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				menuTipo.setVisibility(View.INVISIBLE);
			}
		});

		startService(servicioMarker);
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		inicializarGoogleApi();

//		menuStops.setOnStopMenuListener(this);
		findViewById(R.id.btnShowStops).setOnClickListener(this);
		findViewById(R.id.findPlace).setOnClickListener(this);
		findViewById(R.id.btnShowMenuZzleep).setOnClickListener(this);
		findViewById(R.id.btnPerfil).setOnClickListener(this);
		findViewById(R.id.btnAlarmList).setOnClickListener(this);
		findViewById(R.id.btnAudio).setOnClickListener(this);
		findViewById(R.id.iconAlarm).setOnClickListener(this);

		btAgregarParadero.setOnClickListener(this);
		btLogout.setOnClickListener(this);
		seekBar.setOnSeekBarChangeListener(this);
		btnBack3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				menuZzleep.setVisibility(View.INVISIBLE);
			}
		});
		btnBack4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				menuStops.setVisibility(View.INVISIBLE);
			}
		});
		audioIcon.setOnClickListener(this);
		btStopVideo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btStopVideo.setVisibility(View.INVISIBLE);
				if(vdoView.isPlaying()){
					vdoView.stopPlayback();
				}
				if(vdoView.getVisibility()==View.VISIBLE){
					vdoView.setVisibility(View.INVISIBLE);
				}
			}
		});
		findViewById(R.id.btnSaveStop).setOnClickListener(this);

		//Show tutorial
		if (!config.tutorialTaken()) {
			findViewById(R.id.tutorial).setVisibility(View.VISIBLE);
			findViewById(R.id.tutorial).setOnClickListener(this);
		} else {
			//TODO: Poner esto en una función de inicialización y sacar lo que está en el listener
			isOpenTutorial = false;
			if (userMarker != null) {
				userMarker.setAlpha(1);
				dropPinEffect(userMarker);
			}
		}
        destinies = new ArrayList<>();
        ZzleepPlace home= new ZzleepPlace("Casa", "Tocar para Añadir", "0", "0", 0.0, "", "1");
        ZzleepPlace school= new ZzleepPlace("Oficina", "Tocar para Añadir", "0", "0", 0.0, "", "2");
        ZzleepPlace work= new ZzleepPlace("Universidad", "Tocar para Añadir", "0", "0", 0.0, "", "3");
        destinies.add(home);
        destinies.add(school);
        destinies.add(work);
		destinyadapter = new StopItemAdapter(this, 0, destinies);

		//Asociamos el adaptador a la vista.
		ListView lv = (ListView) findViewById(R.id.listUserStops);
		lv.setOnItemClickListener(this);
		lv.setOnItemLongClickListener(this);
		lv.setAdapter(destinyadapter);
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);



	}

	private void inicializarGoogleApi() {
		if (mGoogleApiClient == null) {
			mGoogleApiClient = new GoogleApiClient.Builder(this)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.addApi(LocationServices.API)
					.build();

		}
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(10000);
		mLocationRequest.setFastestInterval(5000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
				.addLocationRequest(mLocationRequest);

		builder.setAlwaysShow(true);

		PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
				.checkLocationSettings(mGoogleApiClient, builder.build());
		result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
			@Override
			public void onResult(LocationSettingsResult result) {
				final Status status = result.getStatus();
				final LocationSettingsStates state = result
						.getLocationSettingsStates();
				switch (status.getStatusCode()) {
					case LocationSettingsStatusCodes.SUCCESS:

						break;
					case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
						// Location settings are not satisfied. But could be
						// fixed by showing the user
						// a dialog.
						try {
							// Show the dialog by calling
							// startResolutionForResult(),
							// and check the result in onActivityResult().
							status.startResolutionForResult(MainActivity.this, 1000);
						} catch (IntentSender.SendIntentException e) {
							// Ignore the error.
						}
						break;
					case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
						// Location settings are not satisfied. However, we have
						// no way to fix the
						// settings so we won't show the dialog.
						break;
				}
			}
		});
		//ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

		resetLocation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			updateUserMarker();
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
		mAuth.addAuthStateListener(mAuthListener);
		if (mGoogleApiClient != null)
			if (!mGoogleApiClient.isConnected())
				mGoogleApiClient.connect();


	}

	@Override
	public void onStop() {
		super.onStop();
		if (mAuthListener != null) {
			mAuth.removeAuthStateListener(mAuthListener);
		}
		if (audioSelected != null)
			audioSelected.stop();
		if (alarmSelected != null)
			alarmSelected.stop();

		if (mGoogleApiClient != null)
			if (mGoogleApiClient.isConnected())
				mGoogleApiClient.disconnect();

	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		this.getApplicationContext();
		mMap = googleMap;
		lm = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
		mMap.setOnMapLongClickListener(this);
		mMap.setOnMapClickListener(this);



		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			mMap.setMyLocationEnabled(true);
			startLocation();
		} else {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
		}

		if (Build.VERSION.SDK_INT >= 23) {
			if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
					== PackageManager.PERMISSION_GRANTED) {
				Log.v(TAG,"Permission is granted");
			} else {

				Log.v(TAG,"Permission is revoked");
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
			}
		}
		else { //permission is automatically granted on sdk<23 upon installation
			Log.v(TAG,"Permission is granted");
		}

		mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
			@Override
			public void onCameraMove() {
				if(llaveParadero){
					LatLng auxPos=mMap.getCameraPosition().target;
					targetMarker.setPosition(auxPos);
					placeSelected.setLatitude(Double.toString(auxPos.latitude));
					placeSelected.setLongitude(Double.toString(auxPos.longitude));

				}
			}
		});
	}

	public void startLocation() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

			location = LocationServices.FusedLocationApi.getLastLocation(
					mGoogleApiClient);
			if (location != null) {
				updateMapPoint(new LatLng(location.getLatitude(), location.getLongitude()));
			}
		} else {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
		}
	}

	public void getArrayStopItem(String result) {
		try {
			destinies = new ArrayList<>();
			ZzleepPlace home= new ZzleepPlace("Casa", "Tocar para Añadir", "0", "0", 0.0, "", "1");
			ZzleepPlace school= new ZzleepPlace("Oficina", "Tocar para Añadir", "0", "0", 0.0, "", "2");
			ZzleepPlace work= new ZzleepPlace("Universidad", "Tocar para Añadir", "0", "0", 0.0, "", "3");
			destinies.add(home);
			destinies.add(school);
			destinies.add(work);

			JSONObject auxStop = new JSONObject(result);

			JSONArray parentArray = auxStop.getJSONArray("data");
			//se recorre el json de paraderos
			for (int i = 0; i < parentArray.length(); i++) {
				JSONObject finalObject = parentArray.getJSONObject(i);
				String name = finalObject.getString("name");
				String lat = finalObject.getString("lat");
				String lng = finalObject.getString("lng");
				String address = finalObject.getString("address");
				String type = finalObject.getString("type");
				String image = finalObject.getString("image");

				Double range = 100D;

				if (!finalObject.get("range").equals(null)) {
					range = Double.parseDouble(finalObject.getString("range"));
				}
				ZzleepPlace destiny;
				destiny = new ZzleepPlace(name, address, lat, lng, range, type, image);
				//posible for para guardar alarma y audio
				if(!finalObject.get("alarma_id").equals(null)) {
					int alarmid = finalObject.getInt("alarma_id");
					if(alarmas!=null) {
						for (int k = 0; k < alarmas.size(); k++) {
							if (alarmas.get(k).id == alarmid) {
								destiny.alarma =alarmas.get(k);
								DownloadTask downloadTask= new DownloadTask(this);
								downloadTask.execute(destiny.alarma.video,"video_"+destiny.alarma.name.replace(" ","_"));
								break;
							}
						}
					}
				}
				if(!finalObject.get("sound_id").equals(null)) {
					int idAudio=finalObject.getInt("sound_id");

					for (int k = 0; k < audiosArray.size(); k++) {
						if (audiosArray.get(k).id == idAudio) {
							ZzleepAudio tmpAlarmData = audiosArray.get(k);
							destiny.audio = tmpAlarmData;
							break;
						}
					}
				}
				if (!finalObject.getString("id").equals(null)) {
					int id = finalObject.getInt("id");
					destiny.setId(id);
				}
				if(image.equals("1")){
					destinies.set(0,destiny);
				}else {
					if (image.equals("2")) {
						destinies.set(1, destiny);
					}else{
						if(image.equals("3")){
							destinies.set(2,destiny);
						}
						else{
							destinies.add(destiny);
						}
					}
				}
				createNewStop(destiny);

			}
			destinyadapter = new StopItemAdapter(this, 0, destinies);

			//Asociamos el adaptador a la vista.
			ListView lv = (ListView) findViewById(R.id.listUserStops);
			lv.setOnItemClickListener(this);
			lv.setOnItemLongClickListener(this);
			lv.setAdapter(destinyadapter);
			lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

		ZzleepPlace aux = (ZzleepPlace) parent.getAdapter().getItem(position);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Eliminar Paradero");
		builder.setMessage("¿Estás seguro que quieres eliminar el paradero?");

		urlRemove = "http://clientes.geekadvice.pe/zzleep-server/public/api/v1/places/" + aux.getId();

		builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				ApiDelete apiDelete = new ApiDelete();
				apiDelete.execute(urlRemove);
				eliminarParadero(position);
				dialog.dismiss();
			}
		});

		builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		placeSelected = (ZzleepPlace) parent.getAdapter().getItem(position);
		if(position<=2 && placeSelected.getLatitude().equals("0") && placeSelected.getLongitude().equals("0")){
			Integer aux = position+1;
			tipoPlace=aux.toString();
			openFindPlace();
			return;
		}
		LatLng latLng = new LatLng(Double.parseDouble(placeSelected.getLatitude()), Double.parseDouble(placeSelected.getLongitude()));
		tipoPlace="";
		if (targetMarker == null) {
			targetMarker = placeSelected.getMarker();
			targetArea = placeSelected.getCircle();
		} else {
			targetMarker.setPosition(latLng);
			targetArea.setCenter(latLng);
		}

		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, mMap.getCameraPosition().zoom));
	}


	public List<Address> getCurrentLocation() {
		List<Address> user = null;
		double lati = 1D;
		double lngt = 1D;
		LatLng ll;

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			mMap.setMyLocationEnabled(true);
		} else {
			Log.v("GA-ZZ", "Pedir permisos");
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
		}

		if (location == null) {
			Toast.makeText(this, "Location Not found", Toast.LENGTH_LONG).show();
		} else {
			Geocoder geocoder = new Geocoder(this);
			try {
				user = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
				lati = (double) user.get(0).getLatitude();
				lngt = (double) user.get(0).getLongitude();
				Log.v("GA-ZZ", " DDD lat: " + lati + ",  longitude: " + lngt);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return user;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == 1) {
			if (permissions.length == 1 && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				//startLocation();
			} else {
				Log.v("GA-ZZ", "Pedir permisos");
			}
		}
		if (requestCode == 2) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
				//resume tasks needing this permission
			}
		}
	}

	public void updateMapPoint(LatLng p) {
		float[] distance = new float[2];

		if (userMarker == null) {
			userMarker = mMap.addMarker(new MarkerOptions().position(p).flat(true).title("Estás aquí").icon(BitmapDescriptorFactory.fromResource(R.mipmap.user_pin)));
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p, 15F));
			//userMarker.setAlpha(0);
		} else {
			userMarker.setPosition(p);
			CameraPosition cameraPosition = CameraPosition.builder().target(userMarker.getPosition()).zoom(13).bearing(10).build();
			mMap.animateCamera(CameraUpdateFactory.newLatLng(p), 2000, null);
		}
/*
		if (targetArea != null) {
			try {
				if (audioSelected == null) {
					AssetFileDescriptor afd = getAssets().openFd("gallo.mp3");
					audioSelected = new MediaPlayer();
					audioSelected.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
					audioSelected.prepare();
					audioSelected.start();
				} else {
					if (!audioSelected.isPlaying()) {
						Log.v("GA-ZZ", "Reproduciendo... " + audioSelected.getDuration());
						audioSelected.start();
					}
				}


			} catch (IllegalStateException e) {
				Log.d("GA-ZZ", "IllegalStateException: " + e.getMessage());
			} catch (IOException e) {
				Log.d("GA-ZZ", "IOException: " + e.getMessage());
			} catch (IllegalArgumentException e) {
				Log.d("GA-ZZ", "IllegalArgumentException: " + e.getMessage());
			} catch (SecurityException e) {
				Log.d("GA-ZZ", "SecurityException: " + e.getMessage());
			}
			Location.distanceBetween(p.latitude, p.longitude, targetArea.getCenter().latitude, targetArea.getCenter().longitude, distance);
			/*
			if (distance[0] > targetArea.getRadius()) {
				((TextView) findViewById(R.id.track)).setText("Estás a " + (Math.round(distance[0] * 100) / 100) + "m");
			} else {
				if (!audioSelected.isPlaying()) {
					Log.v("GA-ZZ", "Reproduciendo... " + audioSelected.getDuration());
					audioSelected.stop();
					audioSelected = null;

				}
				try {
					if (alarmSelected == null) {
						AssetFileDescriptor afd = getAssets().openFd(placeSelected.alarma.audio);
						alarmSelected = new MediaPlayer();
						alarmSelected.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
						alarmSelected.prepare();
						alarmSelected.start();
					} else {
						if (!alarmSelected.isPlaying()) {
							Log.v("GA-ZZ", "Reproduciendo... " + alarmSelected.getDuration());
							alarmSelected.start();
						}
					}


				} catch (IllegalStateException e) {
					Log.d("GA-ZZ", "IllegalStateException: " + e.getMessage());
				} catch (IOException e) {
					Log.d("GA-ZZ", "IOException: " + e.getMessage());
				} catch (IllegalArgumentException e) {
					Log.d("GA-ZZ", "IllegalArgumentException: " + e.getMessage());
				} catch (SecurityException e) {
					Log.d("GA-ZZ", "SecurityException: " + e.getMessage());
				}
				((TextView) findViewById(R.id.track)).setText("Despierta!, quedan " + (Math.round(distance[0] * 100) / 100) + "m");
				if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
					lm.removeUpdates(this);
				}*/
//				Intent alarmScreen = new Intent(this, AlarmActivity.class);
//				startActivity(alarmScreen);
			//}
		//}


		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
				WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
				WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON, WindowManager.LayoutParams.FLAG_FULLSCREEN |
				WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
				WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

//		LatLng targetLocation = new LatLng(getCurrentLocation().latitude + 0.01D, getCurrentLocation().longitude + 0.001D);
//		mMap.addMarker(new MarkerOptions().position(targetLocation).title("Tu destino").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.mipmap.target_pin)));
//		mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
	}



	@Override
	public void onMapLongClick(LatLng latLng) {
	}

	@Override
	public void onMapClick(LatLng latLng) {
		if (menuCreateStop.getVisibility() == View.VISIBLE) {
			menuCreateStop.setVisibility(View.INVISIBLE);
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
			location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
			updateMapPoint(new LatLng(location.getLatitude(), location.getLongitude()));
			targetMarker.remove();
			targetMarker=null;
		}
		menuStops.setVisibility(View.INVISIBLE);
		menuZzleep.setVisibility(View.INVISIBLE);

		if(llaveParadero){
			targetMarker.setPosition(latLng);
            placeSelected.setLatitude(Double.toString(latLng.latitude));
            placeSelected.setLongitude(Double.toString(latLng.longitude));
		}
		if(vdoView.isPlaying()){
			vdoView.stopPlayback();
		}
		if(vdoView.getVisibility()==View.VISIBLE){
			vdoView.setVisibility(View.INVISIBLE);
		}
	}

	public void toggleMenuStop() {
		menuStops.setLayerType(View.LAYER_TYPE_HARDWARE, null);

		ObjectAnimator rotateAnim;

		if (menuStops.getVisibility() == View.VISIBLE) {
			rotateAnim = ObjectAnimator.ofFloat(menuStops, "alpha", 1, 0);
			ObjectAnimator rotateAnim5 = ObjectAnimator.ofFloat(menuStops, "translationY", 0, 100F);

			rotateAnim.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					Log.v("GA-ZZ", "Animación terminada");
					menuStops.setVisibility(View.INVISIBLE);
					menuStops.setLayerType(View.LAYER_TYPE_NONE, null);
				}
			});

			AnimatorSet as = new AnimatorSet();
			as.play(rotateAnim).with(rotateAnim5);
			as.setInterpolator(new AccelerateInterpolator(0.9F));
			as.setDuration(200);
			as.start();

		} else {
			menuStops.setVisibility(View.VISIBLE);

			ObjectAnimator rotateAnim2 = ObjectAnimator.ofFloat(menuStops, "alpha", 0, 1);
			rotateAnim = ObjectAnimator.ofFloat(menuStops, "translationY", 100F, 0);

			rotateAnim.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					Log.v("GA-ZZ", "Animación terminada");
					menuStops.setLayerType(View.LAYER_TYPE_NONE, null);
				}
			});

			AnimatorSet as = new AnimatorSet();
			as.play(rotateAnim).with(rotateAnim2);
			as.setInterpolator(new AccelerateInterpolator(0.12F));
			as.setDuration(200);
			as.start();
		}
	}

	public void toggleMenuZzleep() {
		menuZzleep.setLayerType(View.LAYER_TYPE_HARDWARE, null);

		ObjectAnimator rotateAnim;

		if (menuZzleep.getVisibility() == View.VISIBLE) {
			rotateAnim = ObjectAnimator.ofFloat(menuZzleep, "alpha", 1, 0);
			ObjectAnimator rotateAnim5 = ObjectAnimator.ofFloat(menuZzleep, "translationY", 0, 100F);

			rotateAnim.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					Log.v("GA-ZZ", "Animación terminada");
					menuZzleep.setVisibility(View.INVISIBLE);
					menuZzleep.setLayerType(View.LAYER_TYPE_NONE, null);
				}
			});

			AnimatorSet as = new AnimatorSet();
			as.play(rotateAnim).with(rotateAnim5);
			as.setInterpolator(new AccelerateInterpolator(0.9F));
			as.setDuration(200);
			as.start();

		} else {
			menuZzleep.setVisibility(View.VISIBLE);

			ObjectAnimator rotateAnim2 = ObjectAnimator.ofFloat(menuZzleep, "alpha", 0, 1);
			rotateAnim = ObjectAnimator.ofFloat(menuZzleep, "translationY", 100F, 0);

			rotateAnim.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					Log.v("GA-ZZ", "Animación terminada");
					menuZzleep.setLayerType(View.LAYER_TYPE_NONE, null);
				}
			});

			AnimatorSet as = new AnimatorSet();
			as.play(rotateAnim).with(rotateAnim2);
			as.setInterpolator(new AccelerateInterpolator(0.12F));
			as.setDuration(200);
			as.start();
		}
	}



/*ONCLICKGLOBAL*/
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.findPlace:
				tipoPlace="";
				openFindPlace();
				break;
			case R.id.btnShowStops:
				toggleMenuStop();
				if (menuZzleep.getVisibility() == View.VISIBLE)
					toggleMenuZzleep();
				break;
			case R.id.btnShowMenuZzleep:
				toggleMenuZzleep();
				if (menuStops.getVisibility() == View.VISIBLE)
					toggleMenuStop();
				break;
			case R.id.tutorial:
				findViewById(R.id.tutorial).setVisibility(View.INVISIBLE);
				isOpenTutorial = false;
				if (userMarker != null) {
					userMarker.setAlpha(1);
					dropPinEffect(userMarker);
				}
				break;
			case R.id.btnPerfil:
				openPerfil();
				break;
			case R.id.btnAlarmList:
				openAlarmList();
				break;
			case R.id.btnAudio:
				openAudio();
				break;
			case R.id.btnSaveStop:
                try {

                    placeSelected.setRadio((double)((SeekBar) findViewById(R.id.seekBar)).getProgress()+100D);
                    placeSelected.setLatitude(Double.toString(targetMarker.getPosition().latitude));
                    placeSelected.setLongitude(Double.toString(targetMarker.getPosition().longitude));
                    placeSelected.setName(nameParadero.getText().toString());
					placeSelected.setAlarma(alarmaSelected);
					placeSelected.setAudio(currentAudio);
                    ZzleepPlace placeTemporal = placeSelected;
					obtenerTipo(placeTemporal);
                } catch (JSONException e) {
                    e.printStackTrace();
                }break;
			case R.id.iconAlarm:
				openGetAlarmList();
				break;
			case R.id.btAgregar:
				setParadero();
				view.setVisibility(View.INVISIBLE);
				break;
			case R.id.btLogout:
				try {
					logOut();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case R.id.iconSoundAgregar:
				openAudio();
				break;
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
								  boolean fromUser) {
		radioSelected = Double.parseDouble(String.valueOf(progress));
		int aux=(radioSelected.intValue()+100);
		radioStopMenu.setText("Radio: "+String.valueOf(aux)+" metros");
	}


	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}
    private void openPerfil() {
        startActivity(new Intent(getApplicationContext(), PerfilZzleeperActivity.class));
    }

    private void openAlarmList() {
        Intent alarmScreen = new Intent(this, AlarmsList.class);
        alarmScreen.putExtra("opt", "buy");
        startActivityForResult(alarmScreen, 4);
    }/*
	private void hideCreateStop() {
		String name = nameParadero.getText().toString();
		String latitude = lat.toString();
		String longitude = lng.toString();
		String description = nameParadero.getText().toString();
		if (placeSelected == null) {
			dest = new ZzleepPlace(name, description, latitude, longitude, radioSelected, ZzleepPlace.TYPE_USER, null);
			destinies.add(dest);
		} else {
			placeSelected.setLatitude(latitude);
			placeSelected.setLongitude(longitude);
			placeSelected.setAddress(description);
			placeSelected.setRadio(radioSelected);
		}
		if (placeSelected.alarma == null) {
			if (alarmaSelected == null) {
				placeSelected.alarma = alarmaDefault;
			} else {
				placeSelected.alarma = alarmaSelected;
			}

		}
		destinyadapter.notifyDataSetChanged();
		alarmaSelected = null;
		LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));


		if (targetMarker == null) {
			targetMarker = mMap.addMarker(new MarkerOptions().draggable(true).position(latLng).title("Tu destino").icon(BitmapDescriptorFactory.fromResource(R.mipmap.target_pin)));
			targetArea = mMap.addCircle(new CircleOptions().center(latLng).radius(radioSelected).fillColor(0x33FF0000).strokeWidth(5F).strokeColor(0x99770000));
//			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16F));
		} else {
			targetMarker.setPosition(latLng);
			targetArea.setCenter(latLng);
		}

		if (!serviceStarted) {
			Intent serviceIntent = new Intent(MainActivity.this, TrackService.class);
			serviceIntent.putExtra("lat", targetArea.getCenter().latitude);
			serviceIntent.putExtra("lon", targetArea.getCenter().longitude);
			serviceIntent.putExtra("radio", targetArea.getRadius());
			startService(serviceIntent);
			serviceStarted = true;
		}

		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, mMap.getCameraPosition().zoom));
		menuCreateStop.setVisibility(View.INVISIBLE);
		placeSelected = null;
	}

*/

	private void openGetAlarmList() {
		Intent alarmScreen = new Intent(this, AlarmsList.class);

		alarmScreen.putExtra("opt", "create");

		startActivityForResult(alarmScreen, 4);

	}

	private void openAudio() {
		startActivityForResult(new Intent(getApplicationContext(), AudioActivity.class),5);
	}

	private void openCatalogo() {
//		startActivity(new Intent(getApplicationContext(), Catalogo.class));
	}

	private void openFindPlace() {

		menuStops.setVisibility(View.INVISIBLE);
		menuZzleep.setVisibility(View.INVISIBLE);
		Intent i = new Intent(this, PlaceActivity.class);
		String auxLati= Double.toString(userMarker.getPosition().latitude);
		String auxLngt= Double.toString(userMarker.getPosition().longitude);
		i.putExtra("lati",auxLati );
		i.putExtra("lngt",auxLngt );

		i.putExtra("img",tipoPlace);
		startActivityForResult(i, 3);
		return;

	}

	private void dropPinEffect(final Marker marker) {
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		final long duration = 1500;
		final Interpolator interpolator = new BounceInterpolator();
		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = Math.max(1 - interpolator.getInterpolation((float) elapsed / duration), 0);
				marker.setAnchor(0.5f, 1.0f + 14 * t);
				if (t > 0.0) {
					// Post again 15ms later.
					handler.postDelayed(this, 15);
				} else {
					marker.showInfoWindow();

				}
			}
		});
	}

	@Override
	public void onInteraction(View v, int action) {
		Log.d(TAG, "action : " + action);
		switch (action) {
			case 1: //CLICK ON icono alarma
//				openAlarmList();
				menuCreateStop.setVisibility(View.VISIBLE);
				break;
			case 2: // click en texto de busqueda

				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

			// The user has selected a place. Extract the name and address.
			final Place place = PlacePicker.getPlace(data, this);

			final CharSequence name = place.getName();
			final CharSequence address = place.getAddress();
			String attributions = PlacePicker.getAttributions(data);
			if (attributions == null) {
				attributions = "";
			}

			TextView txtPlace = (TextView) findViewById(R.id.txtSearchAddress);
			txtPlace.setText(name.toString() + " " + place.getLatLng().toString());
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 19F));
//			mViewAddress.setText(address);
//			mViewAttributions.setText(Html.fromHtml(attributions));

		} else if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
			String nameAct = data.getStringExtra("name");
			String addressAct = data.getStringExtra("address");
			String latAct = data.getStringExtra("lat");
			String lngAct = data.getStringExtra("lng");


			menuCreateStop.setVisibility(View.VISIBLE);
			lat = Double.parseDouble(latAct);
			lng = Double.parseDouble(lngAct);
			nameParadero.setText(nameAct, TextView.BufferType.EDITABLE);

		} else if (requestCode == 3 && resultCode == 20) {
			String nameAct = data.getStringExtra("name");
			String latitud = (data.getStringExtra("lat"));
			String longitud = (data.getStringExtra("lng"));
			String addressAct = data.getStringExtra("address");
			String auxType = data.getStringExtra("type");
			btAgregarParadero.setVisibility(View.VISIBLE);
			ZzleepPlace nuevo = new ZzleepPlace(nameAct, addressAct, latitud, longitud, 1000D, auxType, tipoPlace);
			placeSelected = nuevo;
			setMap(nuevo);
			llaveParadero = (true);



		} else if (requestCode == 4 && resultCode == Activity.RESULT_OK) {

			alarmaSelected = new ZzleepAlarm();
			alarmaSelected.name = data.getStringExtra("alarmName");
			alarmaSelected.icon = data.getStringExtra("alarmIcon");
			alarmaSelected.audio = data.getStringExtra("alarmAudio");
			alarmaSelected.video = data.getStringExtra("alarmVideo");
			alarmaSelected.id = data.getIntExtra("alarmId",0);
			alarmaSelected.status = 3;
			alarmaSelected.price = 0;
			if (placeSelected != null) {


				try {
					URL url = new URL(alarmaSelected.icon);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setDoInput(true);
					connection.connect();
					InputStream input = connection.getInputStream();
					Bitmap myBitmap = BitmapFactory.decodeStream(input);
					alarmIcon.setImageBitmap(myBitmap);
				}catch (Exception e){

				}

				/*placeSelected.alarma = alarmaSelected;
				int resourceId = getResources().getIdentifier(alarmaSelected.icon, "mipmap", "pe.geekadvice.zzleep");
				alarmIcon.setImageResource(resourceId);*/
			}
			try {
				alarmSelected=new MediaPlayer();
				alarmSelected.setDataSource(alarmaSelected.video);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (requestCode == 5 && resultCode == Activity.RESULT_OK) {
			int id = data.getIntExtra("audio_id",0);
			for (ZzleepAudio e:
				 audiosArray) {
					if(e.id==id){
						currentAudio=e;
					}
			}
			ApiImg auxApi = new ApiImg(audioIcon);
			auxApi.execute(currentAudio.getIcon());

		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}

	}

	private void obtenerTipo(ZzleepPlace nuevo) throws JSONException {
		switch (tipoPlace){
            case "1":
            case "2":
            case "3":
                try {
                    guardarParadero(nuevo);
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    updateMapPoint(new LatLng(location.getLatitude(), location.getLongitude()));
                    recargarParaderos(Integer.parseInt(tipoPlace)-1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                menuCreateStop.setVisibility(View.INVISIBLE);
                break;
            default:
                menuTipo.setVisibility(View.VISIBLE);
                menuCreateStop.setVisibility(View.INVISIBLE);
                break;
        }

	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		if (connectionResult.hasResolution()) {
			try {
				connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
			} catch (IntentSender.SendIntentException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
		}
	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		if (location == null) {
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            try {
                updateMapPoint(new LatLng(location.getLatitude(), location.getLongitude()));
            }catch (Exception e){

            }

		} else {
			if(placeSelected==null){
				placeSelected = new ZzleepPlace("Estas aquí","Home",Double.toString(location.getLatitude()),Double.toString(location.getLongitude()),100D,"user","ico");
			}
			if(llaveParadero) {
				setMap(placeSelected);
			}else{
				updateMapPoint(new LatLng(location.getLatitude(), location.getLongitude()));
			}
		}
	}

	@Override
	public void onConnectionSuspended(int i) {
	}


	public void setParadero() {
		String nameAct = placeSelected.getName();
		String addressAct = placeSelected.getAddress();
		menuCreateStop.setVisibility(View.VISIBLE);
		lat = Double.parseDouble(placeSelected.getLatitude());
		lng = Double.parseDouble(placeSelected.getLongitude());
		nameParadero.setText(nameAct, TextView.BufferType.EDITABLE);
		llaveParadero = false;
		iconGurdar.setVisibility(View.VISIBLE);
		botonGuardar.setBackgroundResource(R.mipmap.icon_save);
		switch (tipoPlace){
			case "1":
				iconGurdar.setImageResource(R.mipmap.icon_house);
				break;
			case "3":
				iconGurdar.setImageResource(R.mipmap.icon_college);
				break;
			case "2":
				iconGurdar.setImageResource(R.mipmap.icon_work);
				break;
			default:
				iconGurdar.setVisibility(View.GONE);
				//iconGurdar.set
				botonGuardar.setBackgroundResource(R.mipmap.go);
				break;
		}
	}

	public void setMap(ZzleepPlace placeAux) {

		placeSelected = placeAux;
		if (placeSelected.getStatus() == 1) {
			Double latitud = Double.parseDouble(placeSelected.getLatitude());
			Double longitud = Double.parseDouble(placeSelected.getLongitude());

			LatLng latLng = new LatLng(latitud, longitud);

			location = new Location("");//provider name is unnecessary
			location.setLatitude(latitud);//your coords of course
			location.setLongitude(longitud);

			if (targetMarker == null) {
				targetMarker = mMap.addMarker(new MarkerOptions().draggable(true).position(latLng).title(placeSelected.getName()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.target_pin)));
				//targetArea = mMap.addCircle(new CircleOptions().center(latLng).radius(placeSelected.getRadio()).fillColor(0x33FF0000).strokeWidth(5F).strokeColor(0x99770000));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16F));
			} else {
				targetMarker.setPosition(latLng);
				//targetArea.setCenter(latLng);
			}


			CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);

			mMap.moveCamera(center);


		} else {
			tipoPlace="";
			openFindPlace();
		}


	}

	private void guardarParadero(ZzleepPlace placeGuardar) throws JSONException {
		alarmIcon.setImageResource(R.mipmap.btn_gallo);
		audioIcon.setImageResource(R.mipmap.btn_audio);
		menuCreateStop.setVisibility(View.INVISIBLE);
        placeTransaccional=placeGuardar;
		if(!user.isAnonymous()) {

			String url = "https://clientes.geekadvice.pe/zzleep-server/public/api/v1/places";

			ApiPost apiPost = new ApiPost();
			try {
				if(user.isAnonymous()){
					apiPost.execute(url, placeTransaccional.getJSON("stop", user.getUid()));
				}else {
					apiPost.execute(url, placeTransaccional.getJSON("user", user.getUid()));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}



		}

		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}

		addStopToMenu(placeGuardar);
		location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		updateMapPoint(new LatLng(location.getLatitude(), location.getLongitude()));


	}
	private void createNewStop(ZzleepPlace destiny){
		LatLng latLng = new LatLng(Double.parseDouble(destiny.getLatitude()),Double.parseDouble(destiny.getLongitude()));
		destiny.setMarker(mMap.addMarker(new MarkerOptions().draggable(true).position(latLng).title(destiny.getName()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.target_pin))));
		destiny.setCircle(mMap.addCircle(new CircleOptions().center(latLng).radius(0d).fillColor(0x33FF0000).strokeWidth(5F).strokeColor(0x99770000)));
		targetArea = mMap.addCircle(new CircleOptions().center(latLng).radius(0).fillColor(0x33FF0000).strokeWidth(0F).strokeColor(0x99770000));
//		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16F));
	}
	private void addStopToMenu(ZzleepPlace destiny){
		if(destinies==null) {
			destinies = new ArrayList<>();
		}
		if(!destiny.getImage().equals("")){
			String aux = destiny.getImage();
			destinies.set(Integer.parseInt(aux)-1,destiny);
		}else {
			destinies.add(destiny);
		}
		destinyadapter = new StopItemAdapter(this, 0, destinies);
		ListView lv = (ListView) findViewById(R.id.listUserStops);
		lv.setOnItemClickListener(this);
		lv.setOnItemLongClickListener(this);
		lv.setOnItemLongClickListener(this);
		lv.setAdapter(destinyadapter);
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		createNewStop(destiny);
	}/*
	private void actualizarStopItems(){
		ListView lv = (ListView) findViewById(R.id.listUserStops);
		lv.setOnItemClickListener(this);
		lv.setOnItemLongClickListener(this);
		lv.setAdapter(destinyadapter);
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}*/
	public void recargarParaderos(int position) {
		double latitud = Double.parseDouble(destinies.get(position).getLatitude());
		double longitud= Double.parseDouble(destinies.get(position).getLongitude());
        double radio = destinies.get(position).getRadio();
		int estado=destinies.get(position).getStatus();

		LatLng latLng = new LatLng(latitud,longitud);
		for (int index=0;index<destinies.size();index++){
			if(index==position) {
				continue;
			}
			ZzleepPlace elemento = destinies.get(index);
			if(elemento.getLatitude().equals("0") && elemento.getLongitude().equals("0")){
				continue;
			}

			elemento.getCircle().remove();
			elemento.setCircle(mMap.addCircle(new CircleOptions().center(latLng).radius(0d).fillColor(0x33FF0000).strokeWidth(5F).strokeColor(0x99770000)));
			elemento.setStatus(1);
		}
		if(estado==1){
			if(destinies.get(position).getCircle()!=null){
				destinies.get(position).getCircle().remove();
			}
			destinies.get(position).setCircle(mMap.addCircle(new CircleOptions().center(latLng).radius(radio).fillColor(0x3334A853).strokeWidth(5F).strokeColor(0x9934A853)));
			destinies.get(position).setStatus(0);
			posicionParadero=position;
//VAS X KMS ZZLEEP
			Location location1 = location;
			LatLng paradero= latLng;
			Location location2 = new Location("");
			location2.setLatitude(paradero.latitude);
			location2.setLongitude(paradero.longitude);
			Double distanceInMeters = new Double(location1.distanceTo(location2));
			((TextView)findViewById(R.id.km)).setText("VAS "+Integer.toString(distanceInMeters.intValue()/1000)+" KMS ZZLEEP");
			alarmaSelected=destinies.get(position).getAlarma();
			currentAudio=destinies.get(position).getAudio();
			if (distanceInMeters < destinies.get(position).getRadio()+100){
				((TextView)findViewById(R.id.km)).setText("LLEGASTE!");
				startAlarm();
				destinies.get(position).setStatus(0);
                recargarParaderos(position);
			}else {
				startService(servicioTrack);
				wakeLock.acquire();
			}
		}else{
			destinies.get(position).getCircle().remove();
			destinies.get(position).setCircle(mMap.addCircle(new CircleOptions().center(latLng).radius(0d).fillColor(0x33FF0000).strokeWidth(5F).strokeColor(0x99770000)));
			destinies.get(position).setStatus(1);
			stopService(servicioTrack);
		}

		ListView lv = (ListView) findViewById(R.id.listUserStops);
		lv.setOnItemClickListener(this);
		lv.setOnItemLongClickListener(this);
		lv.setOnItemLongClickListener(this);
		lv.setAdapter(destinyadapter);
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}
	public void eliminarParadero(int position){
		destinies.get(position).getCircle().remove();
		destinies.get(position).getMarker().remove();

		ZzleepPlace home= new ZzleepPlace("Casa", "Tocar para Añadir", "0", "0", 0.0, "", "1");
		ZzleepPlace school= new ZzleepPlace("Oficina", "Tocar para Añadir", "0", "0", 0.0, "", "2");
		ZzleepPlace work= new ZzleepPlace("Universidad", "Tocar para Añadir", "0", "0", 0.0, "", "3");
		switch (position){
			case 0:
				destinies.set(position,home);
				break;
			case 1:
				destinies.set(position,school);
				break;
			case 2:
				destinies.set(position,work);
				break;
			default:
				destinies.remove(position);
				break;
		}
		ListView lv = (ListView) findViewById(R.id.listUserStops);
		lv.setOnItemClickListener(this);
		lv.setOnItemLongClickListener(this);
		lv.setOnItemLongClickListener(this);
		lv.setAdapter(destinyadapter);
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}




    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient != null)
            if (!mGoogleApiClient.isConnected())
                mGoogleApiClient.connect();
    }
    @Override
    protected void onPause() {
        super.onPause();
		/*
        if (mGoogleApiClient != null)
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
            }
*/
    }

	@Override
	protected void onDestroy() {
		stopService(servicioTrack);
		stopService(servicioMarker);
		try {
			wakeLock.release();
		}catch (Exception e){}
		super.onDestroy();
	}

	public ArrayList<ZzleepPlace> getDestinies() {
		return destinies;
	}
	public int getPosicionParadero() {
		return posicionParadero;
	}
	public void updateUserMarker(){
		LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) MainActivity.this);
		if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		try {
			updateMapPoint(new LatLng(location.getLatitude(), location.getLongitude()));
		}catch (Exception e){}
	}
	public void setUserMarker(LatLng posicion){
		try {
			userMarker.setPosition(posicion);
			location.setLongitude(posicion.longitude);
			location.setLatitude(posicion.latitude);
		}catch (Exception e){

		}
	}
	public void startAlarm(){
		try {
			if(alarmaSelected.video!=null && !vdoView.isPlaying())
			{
				//ViewGroup.LayoutParams params=vdoView.getLayoutParams();
				//params.width=666;
				//params.height=200;
				//vdoView.setLayoutParams(params);
				vdoView.setVisibility(View.VISIBLE);
				vdoView.setMediaController(new MediaController(this));

				//vdoView.setVideoURI(Uri.parse(alarmaSelected.video));
				vdoView.setVideoPath("/data/data/pe.geekadvice.zzleep/video_"+alarmaSelected.name.replace(" ","_"));
				if(audioMP!=null && audioMP.isPlaying()){
					audioMP.stop();
				}
				vdoView.start();
				btStopVideo.setVisibility(View.VISIBLE);


			}else{
				alarmSelected = MediaPlayer.create(this, R.raw.gallo);
				alarmSelected.start();
			}
		}catch (Exception e){
			alarmSelected = MediaPlayer.create(this, R.raw.gallo);
			alarmSelected.start();
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		setUserMarker(new LatLng(location.getLatitude(),location.getLongitude()));
	}

	@Override
	public void onBackPressed() {
		if (menuCreateStop.getVisibility() == View.VISIBLE) {
			menuCreateStop.setVisibility(View.INVISIBLE);
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
			location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
			updateMapPoint(new LatLng(location.getLatitude(), location.getLongitude()));
			targetMarker.remove();
			targetMarker=null;
		}
		menuStops.setVisibility(View.INVISIBLE);
		menuZzleep.setVisibility(View.INVISIBLE);

		if(vdoView.isPlaying()){
			vdoView.stopPlayback();
		}
		if(vdoView.getVisibility()==View.VISIBLE){
			vdoView.setVisibility(View.INVISIBLE);
		}

	}
	public void obtenerAlarmas(String result){
		try {
			alarmas= new ArrayList<>();
			JSONObject objJson = new JSONObject(result);
			JSONArray auxJson = objJson.getJSONArray("data");
			for(int i =0 ; i< auxJson.length();i++){
				JSONObject auxObj2 = auxJson.getJSONObject(i);

				String name=auxObj2.getString("name");
				String icon=auxObj2.getString("image");
				int status=auxObj2.getInt("is_owned");
				double price=auxObj2.getDouble("amount");
				String video=auxObj2.getString("preview");
				String audio=auxObj2.getString("preview");
				int id=auxObj2.getInt("id");
				ZzleepAlarm tmpAlarm= new ZzleepAlarm(name,icon,status,price,video,audio);
				tmpAlarm.id=id;
				alarmas.add(tmpAlarm);
				if(price<=0 || status==1) {
					DownloadTask downloadTask = new DownloadTask(this);
					downloadTask.execute(audio, "alarma_" + name.replace(" ", "_"));
				}
				DownloadTask downloadTask2= new DownloadTask(this);
				downloadTask2.execute(icon,"icon_"+name.replace(" ","_"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	public void obtenerAudios(String result) throws JSONException {
		audiosArray = new ArrayList<>();

		JSONObject aux= new JSONObject(result);
		JSONArray alarmas = aux.getJSONArray("data");
		String name;
		String icon;
		Integer status;
		Integer company_id;
		Double price;
		String preview;
		String audio;
		String description;
		Integer id;
		Double discount;
		Integer points;
		for (int i = 0; i < alarmas.length(); i++) {
			JSONObject auxAlarma = alarmas.getJSONObject(i);
			name=auxAlarma.getString("name");
			icon=auxAlarma.getString("image");
			description=auxAlarma.getString("description");
			status=auxAlarma.getInt("is_owned");
			company_id=auxAlarma.getInt("company_id");
			price=auxAlarma.getDouble("amount");
			discount=auxAlarma.getDouble("discount");
			points=auxAlarma.getInt("points");
			preview=auxAlarma.getString("preview");
			audio=auxAlarma.getString("product_file");
			id=auxAlarma.getInt("id");
			ZzleepAudio auxAlarm=new ZzleepAudio(id, name, icon, description, status,  price,  discount,  points,  preview, company_id);
			auxAlarm.id=id;
			audiosArray.add(auxAlarm);
			if(price<=0 || status==1) {
				DownloadTask downloadTask = new DownloadTask(this);
				downloadTask.execute(icon, "icon_" + name.replace(" ", "_"));
			}
			DownloadTask downloadTask2 = new DownloadTask(this);
			downloadTask2.execute(preview,"audio_"+name.replace(" ","_"));
		}

	}
	public void startAudio(){
		try {
			if(currentAudio!=null && currentAudio.getPreview().length()>0)
			{
				musicTask = new MusicTask(this,audioMP);
				musicTask.execute(currentAudio.getPreview());
			}
		}catch (Exception e){

		}

	}
	public void stopAudio(){
		try {
			if (audioMP!=null && audioMP.isPlaying())
				audioMP.stop();
		}catch (Exception e){

		}
	}
	public void setMusicTask(MediaPlayer tmpMusic){
		audioMP=tmpMusic;
	}
}


