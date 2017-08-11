package pe.geekadvice.zzleep;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import pe.geekadvice.utils.DownloadTask;
import pe.geekadvice.utils.JsonTask;
import pe.geekadvice.utils.MusicTask;
import pe.geekadvice.utils.SeekBarTask2;
import pe.geekadvice.zzleep.adapters.ZzleepAlarm;
import pe.geekadvice.zzleep.adapters.ZzleepAlarmAdapter;
import pe.geekadvice.zzleep.adapters.ZzleepAudio;
import pe.geekadvice.zzleep.adapters.ZzleepAudioAdapter;
import pe.geekadvice.zzleep.adapters.ZzleepPlace;
import pe.geekadvice.zzleep.adapters.ZzleepPlaceAdapter;

public class AudioActivity extends AppCompatActivity
{
	ArrayList<ZzleepAudio> audios;
	public ZzleepAudio currentAudio;
	MediaPlayer mp;
	SeekBar currentSeekBar;
	MusicTask musicTask;
	GridLayout popup;
	TextView nombre;
	TextView costo;
    ProgressBar spinner;
	ImageButton btContinuar;
	private FirebaseUser user = null;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio);
		popup= (GridLayout) findViewById(R.id.popupcompraAudio);
		nombre=(TextView) findViewById(R.id.txNombre);
		costo=(TextView) findViewById(R.id.txCosto);
		btContinuar=(ImageButton) findViewById(R.id.btContinuar);
		btContinuar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				iniciarCompra();
				popup.setVisibility(View.INVISIBLE);
			}
		});
		init();
	}
	void init(){
		user = FirebaseAuth.getInstance().getCurrentUser();
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);

        JsonTask obtenerAudios=new JsonTask(this,8);
		obtenerAudios.execute("http://clientes.geekadvice.pe/zzleep-server/public/api/v1/products?type=sound&user_id="+user.getUid());
		ZzleepAudioAdapter adaptador = new ZzleepAudioAdapter(this,0, audios);
		findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	public void obtenerAudiosArray(String data) throws JSONException {
		audios = new ArrayList<>();

		JSONObject aux= new JSONObject(data);
		JSONArray alarmas = aux.getJSONArray("data");
		String name;
		String icon;
		Integer status;
		Integer company_id;
		Double price;
		String video;
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
			video=auxAlarma.getString("preview");
			audio=auxAlarma.getString("product_file");
			id=auxAlarma.getInt("id");
			ZzleepAudio auxAlarm=new ZzleepAudio(id, name, icon, description, status,  price,  discount,  points,  video, company_id);
			auxAlarm.id=id;
			audios.add(auxAlarm);
			DownloadTask tmpDownload = new DownloadTask(this);
			tmpDownload.execute(video,"audio_"+name.replace(" ","_"));
		}
		ZzleepAudioAdapter adaptador = new ZzleepAudioAdapter(this,0, audios);

		//Asociamos el adaptador a la vista.

		ListView lv = (ListView) findViewById(R.id.listAudios);
		lv.setAdapter(adaptador);
        spinner.setVisibility(View.GONE);

    }
	public void usarAudio(ZzleepAudio audioEnviar){
		Integer status=audioEnviar.getStatus();
		if(status==1){
			if(mp!=null && mp.isPlaying()){
				mp.stop();
			}
			Intent resultData = new Intent();
			resultData.putExtra("audio_id",audioEnviar.id);
			setResult(Activity.RESULT_OK, resultData);
			finish();
		}else{
			nombre.setText(audioEnviar.getName());
			costo.setText("S./"+audioEnviar.getPrice().toString());
			popup.setVisibility(View.VISIBLE);
			currentAudio=audioEnviar;
		}

	}

	public void startPreview(ZzleepAudio audioIniciar, SeekBar auxSeekBar){
		try {
			if(audioIniciar!=null && audioIniciar.preview.length()>0)
			{
				if(currentAudio==null){
					currentAudio=audioIniciar;
					musicTask = new MusicTask(this,mp);
					musicTask.execute(currentAudio.getPreview());

				}else {
					if(currentAudio.getPreview().equals(audioIniciar.getPreview())){
						if(mp.isPlaying()) {
							mp.pause();
						}else{
							mp.start();
						}
					}else{
						if(mp.isPlaying()) {
							mp.stop();
							mp.release();
						}
						currentAudio=audioIniciar;
						musicTask = new MusicTask(this,mp);
						musicTask.execute(currentAudio.getPreview());

					}
				}


				currentSeekBar=auxSeekBar;

				currentSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
						try {
							if (fromUser & mp != null)
								mp.seekTo(mp.getDuration() * progress / 100);
						}catch (Exception e){

						}
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {

					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {

					}
				});
				SeekBarTask2 auxSB= new SeekBarTask2(this);
				auxSB.execute();
			}
		}catch (Exception e) {

		}
	}
	public void setCurrentAudio(MediaPlayer audioNuevo){
		this.mp=audioNuevo;
	}
	public MediaPlayer getMp(){
		return this.mp;
	}
	public void setValueSB(int value){
		currentSeekBar.setProgress(value);
	}
	public boolean getMpPlaying(){
		return mp.isPlaying();
	}
	public int mpPosition(){
		return mp.getCurrentPosition();
	}
	public int mpDuration(){
		return mp.getDuration();
	}
	public void iniciarCompra(){
		if(currentAudio.price.intValue()!=0) {
			Intent alarmScreen = new Intent(this, PaymentActivity.class);
			alarmScreen.putExtra("nombre", currentAudio.name);
			alarmScreen.putExtra("precio", currentAudio.price.toString());
			alarmScreen.putExtra("userid",user.getUid());
			String temp= FirebaseInstanceId.getInstance().getToken();
			alarmScreen.putExtra("userToken",temp);
			if(currentAudio.id==-1){
				Toast.makeText(this, "Error de ID", Toast.LENGTH_LONG).show();
				return;
			}
			alarmScreen.putExtra("id",currentAudio.id);
			startActivity(alarmScreen);
		}else{
			usarAudio(currentAudio);
		}
		init();
		return;
	}
}
