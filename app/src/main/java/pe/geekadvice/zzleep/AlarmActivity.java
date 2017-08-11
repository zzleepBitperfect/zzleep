package pe.geekadvice.zzleep;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener {
	private MediaPlayer mp = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);

		//init();
	}

	private void init()
	{
		findViewById(R.id.btnStop).setOnClickListener(this);
//		mp = MediaPlayer.create(this, R.raw.alarma_saico);
//		if (!mp.isPlaying()) {
//			Log.v("GA", "Reproduciendo... " + mp.getDuration());
//			mp.start();
//		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
			case R.id.btnStop:
				mp.stop();
				mp.release();
				Intent backScreen = new Intent(this, MainActivity.class);
				startActivity(backScreen);
				break;
		}
	}
}
