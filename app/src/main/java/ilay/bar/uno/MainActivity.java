package ilay.bar.uno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    Intent intent;
    GameManager gm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gm = new GameManager();
    }

    public void gameModeChoose(View view){
        intent = new Intent(this, EnterNamesActivity.class);
        if(view.getId() == R.id.btnSame){
            intent.putExtra("GameMode", "same");
        }
        else{
            intent.putExtra("GameMode", "online");
        }
        startActivity(intent);
    }
}