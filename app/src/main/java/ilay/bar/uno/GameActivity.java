package ilay.bar.uno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class GameActivity extends AppCompatActivity {

    Intent intent;
    String GameMode, Player1Name, Player2Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        intent = new Intent();
        GameMode = intent.getStringExtra("GameMode");
        Player1Name = intent.getStringExtra("Player1Name");
        Player2Name = intent.getStringExtra("Player2Name");
    }




}