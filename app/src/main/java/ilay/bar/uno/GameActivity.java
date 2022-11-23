package ilay.bar.uno;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import ilay.bar.uno.Controller.GameManager;
import ilay.bar.uno.model.Card;

public class GameActivity extends AppCompatActivity {

    GameManager gm;

    Intent intent;
    String gameMode, player1Name, player2Name;
    ImageView unoImage;

    // RecyclerView
    TextView tvSelected;
    RecyclerView rclvPlayer1Cards, rclvPlayer2Cards;
    CardAdapter cardAdapterPlayer1Cards, cardAdapterPlayer2Cards;
    ArrayList<Card> deck, player1Cards, player2Cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gm = new GameManager();
        deck = gm.getDeck().deckToArrayList();
        player1Cards = gm.getHands().getPlayer1Hand();
        player2Cards = gm.getHands().getPlayer2Hand();

        intent = new Intent();
        gameMode = intent.getStringExtra("GameMode");
        player1Name = intent.getStringExtra("Player1Name");
        player2Name = intent.getStringExtra("Player2Name");
        unoImage = findViewById(R.id.imgUno);
        unoImage.setEnabled(false);

        tvSelected = (TextView) findViewById(R.id.tvSelected);

        ItemClickListener itemClickListener = new ItemClickListener();

        cardAdapterPlayer1Cards = new CardAdapter(this, player1Cards);
        rclvPlayer1Cards = (RecyclerView) findViewById(R.id.rclvMyCards);
        rclvPlayer1Cards.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rclvPlayer1Cards.setAdapter(cardAdapterPlayer1Cards);

        cardAdapterPlayer2Cards = new CardAdapter(this, player2Cards);
        rclvPlayer2Cards = (RecyclerView) findViewById(R.id.rclvEnemyCards);
        rclvPlayer2Cards.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rclvPlayer2Cards.setAdapter(cardAdapterPlayer2Cards);

        RecyclerItemClickListener hListener1 = new RecyclerItemClickListener(this, rclvPlayer1Cards, itemClickListener);
        rclvPlayer1Cards.addOnItemTouchListener(hListener1);
        RecyclerItemClickListener hListener2 = new RecyclerItemClickListener(this, rclvPlayer1Cards, itemClickListener);
        rclvPlayer2Cards.addOnItemTouchListener(hListener2);
    }

    private class ItemClickListener implements RecyclerItemClickListener.OnItemClickListener
    {
        @Override
        public void onItemClick(View view, int position)
        {
            Toast.makeText(getApplicationContext(), "selected: " + deck.get(position), Toast.LENGTH_LONG).show();
            tvSelected.setText(deck.get(position).toString());
        }

        @Override
        public void onLongItemClick(View view, int position)
        {
            Toast.makeText(getApplicationContext(), "long click: " + deck.get(position) + " deck size: " + deck.size() , Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem Item) {
        Intent myIntent = new Intent(this, EndActivity.class);
        myIntent.putExtra("key", ""); //Optional parameters
        int resID = Item.getItemId();
        switch(resID){
            case R.id.item1:
                startActivity(myIntent);
                break;
            case R.id.item2:
                showStartDialog();
                break;
            case R.id.item3:
                showContinueDialog();
                break;
            case R.id.item4:
                showPlusFourDialog();
                break;
            case R.id.item5:
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.game_menu, menu);
        return true;
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
            if (id == R.id.btnYes)
                reply = "Yes";
            else if(id == R.id.btnNo)
                reply = "No";
            else if(id == R.id.btnAcceptTurns)
                reply = "Accept Turns";
            else if(id == R.id.btnContinueGame)
                reply = "Continue Game";
            else
                reply = "Empty";
            Toast.makeText(getApplicationContext(), reply, Toast.LENGTH_LONG).show();
            // tvResult.setText(reply);
            dialog.dismiss();
        }
    }

    private void showPlusFourDialog()
    {
        // Toast.makeText(this, "Custom", Toast.LENGTH_LONG).show();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.plus_four_dialog);

        // set the custom dialog components - text, image and button
        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);

        tvTitle.setText("Player 1 Turn");

        CustomDialogClickListener dcl = new CustomDialogClickListener(dialog);
        btnYes.setOnClickListener(dcl);
        btnNo.setOnClickListener(dcl);

        dialog.show();
    }

    private void showStartDialog()
    {
        // Toast.makeText(this, "Custom", Toast.LENGTH_LONG).show();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.start_dialog);

        // set the custom dialog components - text, image and button
        Button btnYes = dialog.findViewById(R.id.btnAcceptTurns);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);

        tvTitle.setText("Player 1 Starts");

        CustomDialogClickListener dcl = new CustomDialogClickListener(dialog);
        btnYes.setOnClickListener(dcl);

        dialog.show();
    }

    private void showContinueDialog()
    {
        // Toast.makeText(this, "Custom", Toast.LENGTH_LONG).show();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.continue_dialog);

        // set the custom dialog components - text, image and button
        Button btnYes = dialog.findViewById(R.id.btnContinueGame);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);

        tvTitle.setText("Player 1 Turn");

        CustomDialogClickListener dcl = new CustomDialogClickListener(dialog);
        btnYes.setOnClickListener(dcl);

        dialog.show();
    }




}