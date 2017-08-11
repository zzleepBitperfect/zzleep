package pe.geekadvice.zzleep.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import pe.geekadvice.utils.ApiImg;
import pe.geekadvice.zzleep.AlarmsList;
import pe.geekadvice.zzleep.MainActivity;
import pe.geekadvice.zzleep.R;

/**
 * Created by gerson on 18/12/16.
 */

public class ZzleepAlarmAdapter extends ArrayAdapter<ZzleepAlarm>
{
	ImageView alarmIcon;
	TextView alarmName;
	TextView alarmPrice;
	public String audio,video;
	public Integer status;
	private AlarmsList audioContext;
	private MainActivity mainContext;
	public ZzleepAlarmAdapter(AlarmsList context, int resource, List<ZzleepAlarm> objects)
	{
		super(context, resource, objects);
		this.audioContext=context;
	}
	public ZzleepAlarmAdapter(MainActivity context, int resource, List<ZzleepAlarm> objects)
	{
		super(context, resource, objects);
		this.mainContext=context;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		ZzleepAlarm alarm = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.alarm_item, parent, false);
		}
		// Lookup view for data population
		alarmIcon = (ImageView) convertView.findViewById(R.id.alarmIcon);
		alarmName = (TextView) convertView.findViewById(R.id.alarmName);
		alarmPrice = (TextView) convertView.findViewById(R.id.alarmPrice);
		//TextView tvHome = (TextView) convertView.findViewById(R.id.placeAddress);

		// Populate the data into the template view using the data object
		//int resourceId = convertView.getResources().getIdentifier(alarm.icon, "mipmap", "pe.geekadvice.zzleep");
		audio =alarm.audio;
		video= alarm.video;
		status= alarm.status;
		alarmName.setText(alarm.name);

		String tmpDir="/data/data/pe.geekadvice.zzleep/icon_"+alarm.name.replace(" ","_");
		File imgFile = new  File(tmpDir);
		if(imgFile.exists()) {
			alarmIcon.setImageURI(Uri.fromFile(imgFile));
		}else {
			ApiImg apiImg = new ApiImg(alarmIcon);
			apiImg.execute(alarm.icon);
		}
		switch (status)
		{
			case 0://gratis
				if(alarm.price.intValue() ==0) {
					alarmPrice.setText("Gratis");
				}else {
					alarmPrice.setText(alarm.price.toString());
				}
				break;
			case 1://comprado
				alarmPrice.setText("Comprado");
				break;
		}

		//tvHome.setText(audio.address);
		// Return the completed view to render on screen


		return convertView;
	}
}
