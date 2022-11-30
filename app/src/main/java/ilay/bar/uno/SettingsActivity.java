package ilay.bar.uno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    Intent intent;

    private MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mp = MediaPlayer.create(this, R.raw.background_music);
    }

    public void moveToHome(View view){
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void play(View v){
        if(mp == null){
            mp = MediaPlayer.create(this, R.raw.background_music);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    stopPlayer();
                }
            });
        }
        mp.start();
    }

    public void pause(View v){
        if(mp != null){
            mp.pause();
        }
    }

    public void stop(View v){
        stopPlayer();
    }

    public void stopPlayer(){
        if(mp != null){
            mp.release();
            mp = null;
            Toast.makeText(this, "MediaPlayer released", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }
}