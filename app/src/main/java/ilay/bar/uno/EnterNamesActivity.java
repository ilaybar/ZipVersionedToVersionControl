package ilay.bar.uno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EnterNamesActivity extends AppCompatActivity {

    Intent intent;
    String GameMode, Player1Name, Player2Name;
    EditText edt1, edt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_names_screen);
        intent = new Intent();
        GameMode = intent.getStringExtra("GameMode");
        edt1 = findViewById(R.id.edt1);
        edt2 = findViewById(R.id.edt2);
    }

    public void acceptAndContinue(View view){
        Player1Name = edt1.getText().toString();
        Player2Name = edt2.getText().toString();
        intent = new Intent(this, GameActivity.class);
        intent.putExtra("Player1Name", Player1Name);
        intent.putExtra("Player2Name", Player2Name);
        intent.putExtra("GameMode", GameMode);
        startActivity(intent);
    }
}