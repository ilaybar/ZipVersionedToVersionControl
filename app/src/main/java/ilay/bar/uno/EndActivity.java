package ilay.bar.uno;

import static ilay.bar.uno.Utils2.handleMainMenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import ilay.bar.uno.View.GameActivity;

public class EndActivity extends AppCompatActivity {

    Intent intent;

    private MyServiceConnection connection; // MediaPlayer Service

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        // Connects between this activity to the service (MediaPlayer service)
        Intent intent = new Intent(this, MusicService.class);
        connection = new MyServiceConnection();
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (handleMainMenu(item, this))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public void playAgainButton(View view){
        intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void homeButton(View view){
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void recordsButton(View view){
        intent = new Intent(this, RecordsActivity.class);
        startActivity(intent);
    }
}