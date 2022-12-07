package ilay.bar.uno;

import static ilay.bar.uno.Utils2.handleMainMenu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import ilay.bar.uno.View.GameActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
        , AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener{

    Intent intent;
    Button moveToGameActivity;
    Spinner spinnerPlayers1, spinnerPlayers2;

    ArrayAdapter<Player> spinnerAdapter;

    ArrayList<Player> players;

    private final String PlayersKey = "artistsStr";
    private final String PrefName = "MyPrefs";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private void initData()
    {
        players = new ArrayList<Player>();
        players.add(new Player("Paul", this));
        players.add(new Player("John", this));
        players.add(new Player("George", this));
        players.add(new Player("Ringo", this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                save();
                btnCreateUser.setEnabled(true);
            }
            else if (id == R.id.btnConfirm) {
                reply = "Confirmed !";
                moveToGameActivity.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
            else{
                reply = "Empty";
                dialog.dismiss();
            }
            Toast.makeText(getApplicationContext(), reply, Toast.LENGTH_SHORT).show();
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

        //
        initData();

        pref = getSharedPreferences(PrefName, 0); // 0 - for private mode
        editor = pref.edit();

        spinnerAdapter = new ArrayAdapter<Player>(this, R.layout.person_adapter2, R.id.tvFullName, players);

        load();
        //

        spinnerPlayers1 = dialog.findViewById(R.id.spinner1);
        spinnerPlayers2 = dialog.findViewById(R.id.spinner2);
        spinnerPlayers1.setAdapter(spinnerAdapter);
        spinnerPlayers1.setOnItemSelectedListener(this);
        spinnerPlayers2.setAdapter(spinnerAdapter);
        spinnerPlayers2.setOnItemSelectedListener(this);

        dialog.show();
    }

    // Handle click on an item (displays it in the TextView)
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id)
    {
        Toast.makeText(getApplicationContext(), "select: " + players.get(position), Toast.LENGTH_LONG).show();
        // tvSelected.setText(artists.get(position).toString());

    }

    // Handle a long click on an item (deletes it)
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id)
    {
        Toast.makeText(getApplicationContext(),
                "del: " + players.get(position),
                Toast.LENGTH_LONG).show();
        players.remove(position);
        spinnerAdapter.notifyDataSetChanged(); // Update the ListView

        return true;  // i.e. all ended well
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
        // Toast.makeText(getApplicationContext(), players.get(position).getName(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void save()
    {
        Toast.makeText(this, "Save", Toast.LENGTH_LONG).show();
        PrefsUtils.writePlayersList(players, editor, PlayersKey);
    }

    public void load()
    {
        Toast.makeText(this, "Load", Toast.LENGTH_LONG).show();
        ArrayList<Player> personArrList = PrefsUtils.readPlayersList(pref, PlayersKey);
        // copy new list to artists
        // since the adapter is tied to the artists ArrayList
        if (personArrList != null)
        {
            players.clear();
            players.addAll(personArrList);
            spinnerAdapter.notifyDataSetChanged();
        }
    }

}