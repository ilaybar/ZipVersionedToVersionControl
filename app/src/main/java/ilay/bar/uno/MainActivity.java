package ilay.bar.uno;

import static ilay.bar.uno.Utils2.handleMainMenu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Intent intent;
    GameManager gm;
    Button moveToGameActivity;
    Spinner spinnerPlayers1, spinnerPlayers2;

    ArrayAdapter<Player> spinnerAdapter;

    ArrayList<Player> players;

    private void initData()
    {
        players = new ArrayList<Player>();
        players.add(new Player("Paul", this));
        players.add(new Player("John", this));
        players.add(new Player("George", this));
        players.add(new Player("Ringo", this));

		/*
		Bitmap photo = Utils.stringDrawableBitmap(this, "paul");
		items2.add(new Person("Paul", "McCartney", photo));
		 */
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gm = new GameManager();
        moveToGameActivity = findViewById(R.id.btnMoveToGameActivity);
    }

    private class CustomDialogClickListener implements View.OnClickListener
    {
        Dialog dialog;

        public CustomDialogClickListener(Dialog _dialog)
        {
            this.dialog = _dialog;
        }

        @Override
        public void onClick(View v)
        {
            int id = v.getId();
            String reply;
            Button btnCreateUser = dialog.findViewById(R.id.btnCreateUser);
            EditText edt = dialog.findViewById(R.id.edtName);
            if (id == R.id.btnSignUp){
                reply = "Signing Up...";

                TextView createANew = dialog.findViewById(R.id.txtCreateANew);
                btnCreateUser.setVisibility(View.VISIBLE);
                edt.setVisibility(View.VISIBLE);
                createANew.setVisibility(View.VISIBLE);
                Button btnSignUp = dialog.findViewById(R.id.btnSignUp);
                btnSignUp.setEnabled(false);
            }
            else if (id == R.id.btnCreateUser){
                reply = "User Created !";
                players.add(new Player(edt.getText().toString(), MainActivity.this));
                btnCreateUser.setEnabled(false);
            }
            else if (id == R.id.btnConfirm) {
                reply = "Confirmed !";
                Toast.makeText(getApplicationContext(), reply, Toast.LENGTH_LONG).show();
                moveToGameActivity.setVisibility(View.VISIBLE);
                dialog.dismiss();

            }
            else{
                reply = "Empty";
                Toast.makeText(getApplicationContext(), reply, Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
            Toast.makeText(getApplicationContext(), reply, Toast.LENGTH_LONG).show();
            // tvResult.setText(reply);
        }
    }

    private void showUserDialog()
    {
        // Toast.makeText(this, "Custom", Toast.LENGTH_LONG).show();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.user_dialog);

        // set the custom dialog components - text, image and button
        Button btnCreateUser = dialog.findViewById(R.id.btnCreateUser);
        Button btnSignUp = dialog.findViewById(R.id.btnSignUp);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);
        // TextView tvFullName1 = dialog.findViewById(R.id.tvFullName1);

        CustomDialogClickListener dcl = new CustomDialogClickListener(dialog);
        btnCreateUser.setOnClickListener(dcl);
        btnSignUp.setOnClickListener(dcl);
        btnConfirm.setOnClickListener(dcl);

        initData();

        spinnerPlayers1 = dialog.findViewById(R.id.spinner1);
        spinnerPlayers2 = dialog.findViewById(R.id.spinner2);
        spinnerAdapter = new ArrayAdapter<Player>(this, R.layout.person_adapter2, R.id.tvFullName, players);
        spinnerPlayers1.setAdapter(spinnerAdapter);
        spinnerPlayers1.setOnItemSelectedListener(this);
        spinnerPlayers2.setAdapter(spinnerAdapter);
        spinnerPlayers2.setOnItemSelectedListener(this);

        dialog.show();
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

    public void gameModeChose(View view){
        showUserDialog();
    }

    public void moveToGameActivity(View view){
        intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        Toast.makeText(getApplicationContext(), players.get(position).getName(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}