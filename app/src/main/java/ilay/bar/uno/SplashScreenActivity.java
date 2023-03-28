package ilay.bar.uno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class SplashScreenActivity extends AppCompatActivity {
    private RainingCardsThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Start the raining cards animation thread
        View view = findViewById(R.id.raining_cards_animation);
        thread = new RainingCardsThread(view);
        thread.start();

        // Stop the thread after a set amount of time (e.g., 5 seconds)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                thread.stopRunning();
                finish();
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            }
        }, 5000);
    }
}
