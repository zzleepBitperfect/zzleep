package pe.geekadvice.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.Switch;

import java.io.IOException;

import pe.geekadvice.zzleep.AudioActivity;
import pe.geekadvice.zzleep.MainActivity;

/**
 * Created by yaguar on 13/07/2017.
 */

public class MusicTask extends AsyncTask<String,String,String> {
    public MediaPlayer mediaPlayer;
    public Context ctx;
    public int option;
    public MusicTask(MainActivity mainActivity,MediaPlayer auxMedia){
        this.mediaPlayer=auxMedia;
        this.ctx=mainActivity;
        option=0;
    }
    public MusicTask(AudioActivity mainActivity, MediaPlayer auxMedia){
        this.mediaPlayer=auxMedia;
        this.ctx=mainActivity;
        option=1;
    }
    @Override
    protected String doInBackground(String... params) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(params[0]);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
            switch (option) {
                case 0:
                    ((MainActivity) ctx).setMusicTask(mediaPlayer);
                    break;
                case 1:
                    ((AudioActivity) ctx).setCurrentAudio(mediaPlayer);
                    break;

            }
        } catch (IOException e) {
            return e.toString();
        }

        return null;
    }
}
