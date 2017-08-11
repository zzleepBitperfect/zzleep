package pe.geekadvice.zzleep.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import pe.geekadvice.zzleep.MainActivity;
import pe.geekadvice.zzleep.R;

public class StopItemAdapterSinActivador extends ArrayAdapter<ZzleepPlace>
{

	String latitude;
	String longitude;
	public Double radio;
	public Integer type;
	public Integer status;
	Boolean attrActive = true;
	private ImageButton stopSwitch  ;
	ImageView imageType;
	ImageView iconAlarm;
	ImageView iconAudio;
	View parentStopButton;

	public StopItemAdapterSinActivador(Context context, int resource, List<ZzleepPlace> objects)
	{
		super(context, resource, objects);
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		// Get the data item for this position
		ZzleepPlace place = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_stop_item, parent, false);
		}
		// Lookup view for data population
		TextView tvName = (TextView) convertView.findViewById(R.id.stopTitle);
		TextView tvHome = (TextView) convertView.findViewById(R.id.stopAddress);
		stopSwitch = (ImageButton) convertView.findViewById(R.id.stopActive);
		iconAlarm = (ImageView) convertView.findViewById(R.id.iconAlarm);
		iconAudio = (ImageView) convertView.findViewById(R.id.ibAudio);
		parentStopButton = convertView.findViewById(R.id.parentStopButton);
		//iconAlarm

		stopSwitch.setAlpha(0.5F);
		stopSwitch.setImageResource(R.mipmap.switch_off);
		stopSwitch.setVisibility(View.INVISIBLE);

		// Populate the data into the template view using the data object
		tvName.setText(place.getName());
		tvHome.setText(place.getAddress());
		latitude = place.getLatitude();
		longitude = place.getLongitude();
		radio = place.getRadio();
		//TODO : replace this
		//type = place.getType();
		status = place.getStatus();

		if(status==1)
		{
			stopSwitch.setAlpha(0.5F);
			stopSwitch.setImageResource(R.mipmap.switch_off);
		} else{
			stopSwitch.setAlpha(1F);
			stopSwitch.setImageResource(R.mipmap.switch_on);
		}


		imageType = (ImageView) convertView.findViewById(R.id.iconStop);
		type = 1;
		switch (type) {
			case 1:
				imageType.setImageResource(R.mipmap.icon_house);
				break;
			case 2:
				imageType.setImageResource(R.mipmap.icon_work);
				break;
			case 3:
				imageType.setImageResource(R.mipmap.icon_college);
				break;
		}
		if (place.alarma != null) {
			int resourceId = convertView.getResources().getIdentifier(place.alarma.icon, "mipmap", "pe.geekadvice.zzleep");
			iconAlarm.setImageResource(resourceId);
		}

		// Return the completed view to render on screen
		stopSwitch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity auxMainA = (MainActivity) MainActivity.getInstance();
				auxMainA.recargarParaderos(position);
			}
		});
		if (place.alarma != null) {
			try {
				String tmpDir="/data/data/pe.geekadvice.zzleep/icon_"+place.alarma.name.replace(" ","_");
				java.io.File file = new java.io.File(tmpDir);
				if (!file.exists()) {
					URL url = new URL(place.alarma.icon);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setDoInput(true);
					connection.connect();
					InputStream input = connection.getInputStream();
					Bitmap myBitmap = BitmapFactory.decodeStream(input);
					iconAlarm.setImageBitmap(myBitmap);
				}else{
					File imgFile = new  File(tmpDir);
					iconAlarm.setImageURI(Uri.fromFile(imgFile));
				}

			}catch (Exception e){
				Log.v("GA-ZZ", e.toString());
			}
		}

		if (place.audio != null) {
			//int resourceId = convertView.getResources().getIdentifier(place.alarma.icon, "mipmap", "pe.geekadvice.zzleep");
			//iconAlarm.setImageResource(resourceId);
			try {
				String tmpDir="/data/data/pe.geekadvice.zzleep/icon_"+place.audio.name.replace(" ","_");
				java.io.File file = new java.io.File(tmpDir);
				if (!file.exists()) {
					URL url = new URL(place.audio.icon);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setDoInput(true);
					connection.connect();
					InputStream input = connection.getInputStream();
					Bitmap myBitmap = BitmapFactory.decodeStream(input);
					iconAudio.setImageBitmap(myBitmap);
				}else{
					File imgFile = new  File(tmpDir);
					iconAudio.setImageURI(Uri.fromFile(imgFile));
				}
			}catch (Exception e){
				Log.v("GA-ZZ", e.toString());
			}
		}
		return convertView;
	}
}
