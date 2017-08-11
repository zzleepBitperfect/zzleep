package pe.geekadvice.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.SeekBar;

import java.io.IOException;

import pe.geekadvice.zzleep.AudioActivity;

/**
 * Created by YaguarRuna on 14/07/2017.
 */

public class SeekBarTask2 extends AsyncTask<String,String,String> {
    private Context ctx;
    public SeekBarTask2(AudioActivity mp){
        this.ctx=mp;
    }
    @Override
    protected String doInBackground(String... params) {
        try {
            if (((AudioActivity) ctx).getMp() != null)
                while (((AudioActivity) ctx).getMpPlaying()) {
                    Double pos;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pos = (double) ((AudioActivity) ctx).mpPosition();
                    float duration = ((AudioActivity) ctx).mpDuration();
                    pos = pos / duration;
                    pos *= 100;
                    ((AudioActivity) ctx).setValueSB(pos.intValue());
                }
            return null;
        }catch (Exception e){
            return null;
        }
    }
}
