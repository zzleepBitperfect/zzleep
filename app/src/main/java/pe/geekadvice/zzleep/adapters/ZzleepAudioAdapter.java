package pe.geekadvice.zzleep.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import pe.geekadvice.utils.ApiImg;
import pe.geekadvice.utils.DownloadTask;
import pe.geekadvice.zzleep.AudioActivity;
import pe.geekadvice.zzleep.R;

/**
 * Created by gerson on 18/12/16.
 */

public class ZzleepAudioAdapter extends ArrayAdapter<ZzleepAudio>
{

	private AudioActivity context;


	public ZzleepAudioAdapter(AudioActivity context, int resource, List<ZzleepAudio> objects)
	{
		super(context, resource, objects);
		this.context=context;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		// Get the data item for this position
		final ZzleepAudio audio = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.audio_item, parent, false);
		}
		// Lookup view for data population
		TextView tvName = (TextView) convertView.findViewById(R.id.audioName);
		TextView tvPrecio = (TextView) convertView.findViewById(R.id.audioPrice);
		final ImageView audioButton = (ImageView) convertView.findViewById(R.id.iconPlayAudio);
		ImageView iconAudio = (ImageView) convertView.findViewById(R.id.iconAudio);
		final SeekBar seekBar = (SeekBar) convertView.findViewById(R.id.seekbartimeline);
		seekBar.setMax(100);
		iconAudio.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				context.usarAudio(audio);
			}
		});
		String tmpDir="/data/data/pe.geekadvice.zzleep/icon_"+audio.name.replace(" ","_");
		File imgFile = new  File(tmpDir);
		if(imgFile.exists()) {
			iconAudio.setImageURI(Uri.fromFile(imgFile));
		}else {
			DownloadTask downloadTask = new DownloadTask(getContext());
			downloadTask.execute(audio.getIcon(),"icon_"+audio.name.replace(" ","_"));
			ApiImg apiImg = new ApiImg(iconAudio);
			apiImg.execute(audio.icon);
		}

		audioButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(audio.getSonando()==0) {

					for (int i =0 ; i < parent.getChildCount();i++){
						getItem(i).setSonando(0);
						ImageView test = (ImageView) parent.getChildAt(i).findViewById(R.id.iconPlayAudio);
						test.setImageResource(R.mipmap.playb);
					}
					audio.setSonando(1);
					audioButton.setImageResource(R.mipmap.stopb);
				}else{
					audio.setSonando(0);
					audioButton.setImageResource(R.mipmap.playb);
				}
				SeekBar aux = seekBar;
				context.startPreview(audio,aux);
			}
		});
		//TextView tvHome = (TextView) convertView.findViewById(R.id.placeAddress);

		// Populate the data into the template view using the data object
		tvName.setText(audio.name);
		//tvHome.setText(audio.address);
		// Return the completed view to render on screen
		String audioPrice= audio.price.toString();
		if(audioPrice.equals("0.0")) {
			tvPrecio.setText("Gratis");
		}else{
			tvPrecio.setText("S./"+audioPrice);
		}
		return convertView;
	}
}
