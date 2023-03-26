package ilay.bar.uno;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener
{

    MediaPlayer mp;
    private final IBinder mBinder = new MyBinder();

    public MusicService()
    {
        Log.d("MyService", "Constructor");
    }

    public class MyBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyService", "OnCreate");
        // add a notification here later

        mp = MediaPlayer.create(this, R.raw.background_music);
        mp.setOnCompletionListener(this);
        mp.setLooping(true); // Set the music to loop

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Set the volume to 50% of the maximum
        int volume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2;
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d("MyService", "onStartCommand");
        int retVal = super.onStartCommand(intent, flags, startId);
        Toast.makeText(this, "Service starting", Toast.LENGTH_SHORT).show();

        if (mp == null) {
            mp = MediaPlayer.create(this, R.raw.background_music);
            mp.setOnCompletionListener(this);
            mp.setLooping(true);
            mp.start();
        }

        return retVal;
    }

    @Override
    public void onDestroy()
    {
        Log.d("MyService", "onDestroy");
        super.onDestroy();

        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {
        Log.d("MyService", "MediaPlayer-OnCompletion");
        stopSelf(); //  the service will destroy itself once the media stops playing
    }

    public void playMusic() {
        if (mp == null) {
            mp = MediaPlayer.create(this, R.raw.background_music);
            mp.setOnCompletionListener(this);
            mp.setLooping(true);
        }
        mp.start();
    }

    public void pauseMusic() {
        if (mp != null) {
            mp.pause();
        }
    }

    public void stopMusic() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

}