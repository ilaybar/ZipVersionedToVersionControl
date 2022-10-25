package ilay.bar.uno;

import static ilay.bar.uno.Utils.handleMainMenu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Intent intent;
    GameManager gm;
    Button moveToGameActivity;

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
            if (id == R.id.btnSignUp){
                reply = "Signing Up...";
                EditText edt = dialog.findViewById(R.id.edtName);
                TextView createANew = dialog.findViewById(R.id.txtCreateANew);
                btnCreateUser.setVisibility(View.VISIBLE);
                edt.setVisibility(View.VISIBLE);
                createANew.setVisibility(View.VISIBLE);
                Button btnSignUp = dialog.findViewById(R.id.btnSignUp);
                btnSignUp.setEnabled(false);
            }
            else if (id == R.id.btnCreateUser){
                reply = "User Created !";
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

        CustomDialogClickListener dcl = new CustomDialogClickListener(dialog);
        btnCreateUser.setOnClickListener(dcl);
        btnSignUp.setOnClickListener(dcl);
        btnConfirm.setOnClickListener(dcl);
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

    public void instructionsButton(View view){
        intent = new Intent(this, InstructionsActivity.class);
        startActivity(intent);
    }

    public void moveToGameActivity(View view){
        intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}