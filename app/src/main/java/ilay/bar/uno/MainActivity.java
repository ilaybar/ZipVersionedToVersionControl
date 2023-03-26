package ilay.bar.uno;

import static ilay.bar.uno.Utils2.handleMainMenu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import ilay.bar.uno.View.GameActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
        , AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener{

    Intent intent;
    Button moveToGameActivity;
    TextView tvTip;
    Spinner spinnerPlayers1, spinnerPlayers2;
    ImageView cardImage;

    ArrayAdapter<Player> spinnerAdapter;

    ArrayList<Player> players;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private String[] unoTips = {
            "When playing a Wild card, choose the color that will benefit you the most.",
            "Be strategic when using Reverse cards to prevent opponents from going out before you do.",
            "Keep track of played cards to predict opponents' hands and make better decisions."
    };

    private void initData()
    {
        pref = getSharedPreferences(Globals.PrefName, 0); // 0 - for private mode
        editor = pref.edit();
        players = PrefsUtils.readPlayersList(pref, Globals.PlayersKey);
        if (players == null) // first time we run it
        {
            players = new ArrayList<Player>();
            players.add(new Player("Guest1"));
            players.add(new Player("Guest2"));
            PrefsUtils.writePlayersList(players, editor, Globals.PlayersKey);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sets the default language to english (left to right layout)
        Locale.setDefault(Locale.ENGLISH);
        Configuration config = new Configuration();
        config.locale = Locale.ENGLISH;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        moveToGameActivity = findViewById(R.id.btnMoveToGameActivity);

        String[] tips = {"When playing a Wild card, it's important to choose the color that will benefit you the most. Consider the cards in your hand and the cards on the table before making your decision.",
                "Be strategic when using Reverse cards. Playing a Reverse card can change the direction of play, but it can also prevent your opponents from going out before you do.",
                "Keep track of the cards that have been played to improve your chances of winning. This can help you predict which cards your opponents may have and make better decisions about which cards to play."};
        tvTip = findViewById(R.id.tvTip);
        tvTip.setText("Tip: " + getRandomTip());

        // Connects between this activity to the service (MediaPlayer service)
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, Globals.connection, Context.BIND_AUTO_CREATE);
    }

    private String getRandomTip() {
        // Generate a random number between 0 and the length of the unoTips array
        Random random = new Random();
        int randomIndex = random.nextInt(unoTips.length);

        // Return the tip at the random index
        return unoTips[randomIndex];
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
                players.add(new Player(edt.getText().toString()));
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

        initData();

        pref = getSharedPreferences(Globals.PrefName, 0); // 0 - for private mode
        editor = pref.edit();

        // In order to clear the players arraylist
        // editor.clear();
        // editor.commit();

        spinnerAdapter = new ArrayAdapter<Player>(this, R.layout.player_adapter2, R.id.tvFullName, players);

        load();
        //

        spinnerPlayers1 = dialog.findViewById(R.id.spinner1);
        spinnerPlayers2 = dialog.findViewById(R.id.spinner2);
        spinnerPlayers1.setAdapter(spinnerAdapter);
        spinnerPlayers1.setOnItemSelectedListener(this);
        spinnerPlayers2.setAdapter(spinnerAdapter);
        spinnerPlayers2.setOnItemSelectedListener(this);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
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
        int ind1 = spinnerPlayers1.getSelectedItemPosition();
        int ind2 = spinnerPlayers2.getSelectedItemPosition();
        intent.putExtra("Player1", ind1);
        intent.putExtra("Player2", ind2);
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
        PrefsUtils.writePlayersList(players, editor, Globals.PlayersKey);
    }

    public void load()
    {
        Toast.makeText(this, "Load", Toast.LENGTH_LONG).show();
        ArrayList<Player> personArrList = PrefsUtils.readPlayersList(pref, Globals.PlayersKey);
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