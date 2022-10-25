package ilay.bar.uno;

import static ilay.bar.uno.Utils.handleMainMenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class EndActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
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