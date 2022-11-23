package ilay.bar.uno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ilay.bar.uno.Controller.GameManager;

public class InstructionsActivity extends AppCompatActivity {

    Intent intent;
    GameManager gm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
    }

    public void homeButton(View view){
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}