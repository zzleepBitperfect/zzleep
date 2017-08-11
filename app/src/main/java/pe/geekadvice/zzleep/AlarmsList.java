package pe.geekadvice.zzleep;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.DialogInterface;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.culqi.checkout.Card;
import com.culqi.checkout.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.Serializable;
import java.util.ArrayList;

import pe.geekadvice.utils.DownloadTask;
import pe.geekadvice.utils.JsonTask;
import pe.geekadvice.zzleep.adapters.ZzleepAlarm;
import pe.geekadvice.zzleep.adapters.ZzleepAlarmAdapter;
import pe.geekadvice.zzleep.adapters.ZzleepAudio;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AlarmsList extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener
{

	String chooseOption;
	int valueButton;
	View mViewGroup;
	VideoView vdoView;
	ArrayList<ZzleepAlarm> arrayList;
	ZzleepAlarm alarmSelected;
	String optionSelected;
	ZzleepAlarmAdapter alarmAdapter;
	GridLayout popup;
	TextView nombre;
	TextView costo;
	ImageButton btContinuar;
	ProgressBar spinner;

	private MediaPlayer mp = null;
	private FirebaseUser user = null;
	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarms_list);
		mViewGroup = findViewById(R.id.viewsContainer);
		vdoView = (VideoView) findViewById(R.id.videoViewRec);
		vdoView.setMediaController(new MediaController(this));
		nombre=(TextView) findViewById(R.id.txNombre);
		costo=(TextView) findViewById(R.id.txCosto);
		btContinuar=(ImageButton) findViewById(R.id.btContinuar);
		Bundle data = getIntent().getExtras();
		optionSelected =data.getString("opt");
		popup=(GridLayout) findViewById(R.id.popupcompra);
		mViewGroup.setVisibility(View.GONE);
		btContinuar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popup.setVisibility(View.INVISIBLE);
				iniciarCompra();
			}
		});
		init();
	}
	@Override
	protected void onStop()
	{
		super.onStop();
		if(mp != null)
			mp.stop();
	}

	private void init()
	{
		spinner = (ProgressBar)findViewById(R.id.progressBar1);
		spinner.setVisibility(View.VISIBLE);
		popup.setVisibility(View.INVISIBLE);
		findViewById(R.id.btnBack).setOnClickListener(this);
		arrayList = new ArrayList<>();
		user = FirebaseAuth.getInstance().getCurrentUser();
		String url ="http://clientes.geekadvice.pe/zzleep-server/public/api/v1/products?type=alarm&user_id=" + user.getUid();
		JsonTask pedir_alarmas = new JsonTask(AlarmsList.this,5);
		pedir_alarmas.execute(url);

/*
		arrayList.add(new ZzleepAlarm("Saico","icon_alarma1",1,0,"","mono"));//name,nameimagen,status(gratis 1,comprar 2,comprado 3),precio,videoname,audio
		arrayList.add(new ZzleepAlarm("Mono","icon_alarma2",2,5.50,"","mono"));
		arrayList.add(new ZzleepAlarm("Gallo","icon_alarma3",3,0,"","gallo"));
		arrayList.add(new ZzleepAlarm("Saico","icon_alarma1",1,0,"","mono"));
		arrayList.add(new ZzleepAlarm("Mono","icon_alarma2",3,0,"","mono"));
		arrayList.add(new ZzleepAlarm("Gallo","icon_alarma3",3,0,"","mono"));
		arrayList.add(new ZzleepAlarm("Melcochita","para_boton",1,0,"melco_son2","zzleep_son_melcochita.wav"));

*/

	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		alarmSelected= (ZzleepAlarm) parent.getAdapter().getItem(position);
		Integer status=alarmSelected.status;
		String option="Comprar";
		if(status==1 || alarmSelected.price.doubleValue()==0.0)
		{
			option="Usar";
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Alarma");
		builder.setMessage("¿Qué deseas hacer?");
		builder.setPositiveButton("Vista previa",new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int id){
				try {
					previewAlarm(alarmSelected.audio,alarmSelected.video);
				} catch (IOException e) {
					e.printStackTrace();
				}
				mViewGroup.setVisibility(View.VISIBLE);
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(option,new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int id){
				if(alarmSelected.status== 1 || alarmSelected.price.equals(0.0))
				{
					useAlarm();
				}
				else{
					buyAlarm();
				}

				dialog.dismiss();
			}
		});
		AlertDialog alert =builder.create();
		alert.show();

	}
	private void useAlarm()
	{
		Intent resultData = new Intent();
		resultData.putExtra("alarmName",alarmSelected.name);
		resultData.putExtra("alarmIcon",alarmSelected.icon);
		resultData.putExtra("alarmAudio",alarmSelected.audio);
		resultData.putExtra("alarmVideo",alarmSelected.video);
		resultData.putExtra("alarmId",alarmSelected.id);
		setResult(Activity.RESULT_OK, resultData);
		finish();

	}
	private void previewAlarm(String _audio,String _video) throws IOException {
		if(mp != null)
			mp.stop();
//		if (!mp.isPlaying()) {
//			Log.v("GA", "Reproduciendo... " + mp.getDuration());
//			mp.start();
//		}

		/*
		int sound_id = getResources().getIdentifier(_audio, "raw",
				this.getPackageName());
		if(sound_id != 0) {
			mp = MediaPlayer.create(this,sound_id);
			mp.start();

		}
*/
		//mp = MediaPlayer.create(this, R.raw.MelcoSon2);
		/*
		*/
		if(_video!="")
		{Uri path =Uri.parse(_video);
			vdoView.setVisibility(View.VISIBLE);
			vdoView.setVideoURI(path);
			vdoView.start();
		}else{
			vdoView.setVisibility(View.INVISIBLE);
		}

	}

	private void buyAlarm()
	{
		popup.setVisibility(View.VISIBLE);
		nombre.setText(alarmSelected.name);
		String tmp="S/.";
		tmp+= alarmSelected.price.toString();
		costo.setText(tmp);
		/*
		if(alarmSelected.price.intValue()!=0) {
			Intent alarmScreen = new Intent(this, PaymentActivity.class);
			alarmScreen.putExtra("nombre", alarmSelected.name);
			alarmScreen.putExtra("precio", alarmSelected.price.toString());
			alarmScreen.putExtra("userid",user.getUid());
			String temp= FirebaseInstanceId.getInstance().getToken();
			alarmScreen.putExtra("userToken",temp);
			if(alarmSelected.id==-1){
				Toast.makeText(this, "Error de ID", Toast.LENGTH_LONG).show();
				return;
			}
			alarmScreen.putExtra("id",alarmSelected.id);
			startActivity(alarmScreen);
		}else{
			useAlarm();
		}

		return;
		*/

	}

	@Override
	public void onClick(View v)
	{

		valueButton=0;
	switch (v.getId())
	{
		case R.id.btnBack:
			/*Intent alarmScreen = new Intent(this, MainActivity.class);
			startActivity(alarmScreen);*/
			finish();
			break;
	/*	case R.id.alarma1:
			valueButton=1;
			break;
		case R.id.alarma2:
			valueButton=2;
			break;
		case R.id.alarma3:
			valueButton=3;
			break;
		case R.id.alarma4:
			valueButton=4;
			break;
		case R.id.alarma5:
			valueButton=5;
			break;
		case R.id.alarma6:
			valueButton=7;
			break;
		case R.id.alarma7:
			valueButton=7;
			break;*/
		case R.id.btnStop:
			mViewGroup.setVisibility(View.GONE);
			if(vdoView.isPlaying())
			{
				vdoView.stopPlayback();
			}/*
			if(){
				mp.stop();
			}*/


			break;
	}




	}

	public void obtener_alarmas(String data) throws JSONException {
		JSONObject aux= new JSONObject(data);
		JSONArray alarmas = aux.getJSONArray("data");
		 String name;
		 String icon;
		 Integer status;
		 Number price;
		 String video;
		 String audio;
		 Integer id;
		for (int i = 0; i < alarmas.length(); i++) {
			JSONObject auxAlarma = alarmas.getJSONObject(i);
			name=auxAlarma.getString("name");
			icon=auxAlarma.getString("image");
			status=auxAlarma.getInt("is_owned");
			price=auxAlarma.getDouble("amount");
			video=auxAlarma.getString("preview");
			audio=auxAlarma.getString("product_file");
			id=auxAlarma.getInt("id");
			ZzleepAlarm auxAlarm=new ZzleepAlarm(name,icon,status,price,video,audio);
			auxAlarm.id=id;
			arrayList.add(auxAlarm);
			DownloadTask tmpDownload = new DownloadTask(this);
			tmpDownload.execute(video,name);
		}
		alarmAdapter = new ZzleepAlarmAdapter(this,0, arrayList);
		//Asociamos el adaptador a la vista.
		if (arrayList.size()==0 && data.contains("ERROR")){
			Toast.makeText(this, "Error:" + data, Toast.LENGTH_LONG).show();
		}
		GridView lv = (GridView) findViewById(R.id.gridview);
		lv.setOnItemClickListener(this);
		lv.setAdapter(alarmAdapter);
		findViewById(R.id.btnStop).setOnClickListener(this);
		spinner.setVisibility(View.GONE);
	}
	public void iniciarCompra(){
		if(alarmSelected.price.intValue()!=0) {
			Intent alarmScreen = new Intent(this, PaymentActivity.class);
			alarmScreen.putExtra("nombre", alarmSelected.name);
			alarmScreen.putExtra("precio", alarmSelected.price.toString());
			alarmScreen.putExtra("userid",user.getUid());
			String temp= FirebaseInstanceId.getInstance().getToken();
			alarmScreen.putExtra("userToken",temp);
			if(alarmSelected.id==-1){
				Toast.makeText(this, "Error de ID", Toast.LENGTH_LONG).show();
				return;
			}
			alarmScreen.putExtra("id",alarmSelected.id);
			startActivity(alarmScreen);
		}else{
			useAlarm();
		}
		init();
		return;
	}
}
