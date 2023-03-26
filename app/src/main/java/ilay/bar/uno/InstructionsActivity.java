package ilay.bar.uno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class InstructionsActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        // Connects between this activity to the service (MediaPlayer service)
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, Globals.connection, Context.BIND_AUTO_CREATE);
    }

    public void homeButton(View view){
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}