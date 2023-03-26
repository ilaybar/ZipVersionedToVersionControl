package ilay.bar.uno;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    Intent intent;

    private MusicService myService; // Instance of MusicService
    private boolean isServiceBound = false; // Flag to check if the service is bound

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Connects between this activity to the service (MediaPlayer service)
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, Globals.connection, Context.BIND_AUTO_CREATE);

        // Initialize the play, pause, and stop buttons
        Button playButton = findViewById(R.id.btnPlay);
        Button pauseButton = findViewById(R.id.btnPause);
        Button stopButton = findViewById(R.id.btnStop);

        // Set the click listeners for the buttons:
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myService = Globals.connection.getService();
                isServiceBound = Globals.connection.isServiceBound();
                Toast.makeText(SettingsActivity.this, "isServiceBound: " + isServiceBound + " myService: " + myService, Toast.LENGTH_LONG).show();
                if (isServiceBound && myService != null) {
                    myService.playMusic();
                }
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myService = Globals.connection.getService();
                isServiceBound = Globals.connection.isServiceBound();
                Toast.makeText(SettingsActivity.this, "isServiceBound: " + isServiceBound + " myService: " + myService, Toast.LENGTH_LONG).show();
                if (isServiceBound && myService != null) {
                    myService.pauseMusic();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myService = Globals.connection.getService();
                isServiceBound = Globals.connection.isServiceBound();
                Toast.makeText(SettingsActivity.this, "isServiceBound: " + isServiceBound + " myService: " + myService, Toast.LENGTH_LONG).show();
                if (isServiceBound && myService != null) {
                    myService.stopMusic();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to the Service;
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, Globals.connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        unbindService(Globals.connection);
    }

    public void moveToHome(View view){
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}