package ilay.bar.uno.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ilay.bar.uno.CardAdapter;
import ilay.bar.uno.Controller.GameManager;
import ilay.bar.uno.EndActivity;
import ilay.bar.uno.Model.Card;
import ilay.bar.uno.R;
import ilay.bar.uno.RecyclerItemClickListener;

public class GameActivity extends AppCompatActivity {

    GameManager gm;

    Intent intent;
    String gameMode, player1Name, player2Name;
    ImageView unoImage, pileImg;

    // RecyclerView
    TextView tvSelected, gameStatus;
    RecyclerView rclvMyCards, rclvOpponentCards;
    CardAdapter cardAdapterMyCards, cardAdapterOpponentCards;
    ArrayList<Card> deck, myCards, opponentCards, pile, temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        pileImg = findViewById(R.id.imgPile);
        gameStatus = findViewById(R.id.tvGameStatus);

        gm = new GameManager(this);
        deck = gm.getDeck().deckToArrayList();
        myCards = gm.getPlayer1Hand().getCardsArray();
        opponentCards = gm.getPlayer2Hand().getCardsArray();
        pile = gm.getPile().getPileArray();

        intent = new Intent();
        gameMode = intent.getStringExtra("GameMode");
        player1Name = intent.getStringExtra("Player1Name");
        player2Name = intent.getStringExtra("Player2Name");
        unoImage = findViewById(R.id.imgUno);
        unoImage.setEnabled(false);

        tvSelected = (TextView) findViewById(R.id.tvSelected);

        ItemClickListener itemClickListener = new ItemClickListener();

        cardAdapterMyCards = new CardAdapter(this, myCards);
        rclvMyCards = (RecyclerView) findViewById(R.id.rclvMyCards);
        rclvMyCards.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rclvMyCards.setAdapter(cardAdapterMyCards);

        cardAdapterOpponentCards = new CardAdapter(this, opponentCards);
        rclvOpponentCards = (RecyclerView) findViewById(R.id.rclvEnemyCards);
        rclvOpponentCards.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rclvOpponentCards.setAdapter(cardAdapterOpponentCards);

        RecyclerItemClickListener hListener1 = new RecyclerItemClickListener(this, rclvMyCards, itemClickListener);
        rclvMyCards.addOnItemTouchListener(hListener1);

        // rclvOpponentCards.addOnItemTouchListener(hListener2);

        gm.newGame();
    }

    public void switchRecyclerViewsMain(String turn){
        if(turn.equals("Player1")){
            myCards = gm.getPlayer1Hand().getCardsArray();
            opponentCards = gm.getPlayer2Hand().getCardsArray();
            rclvMyCards.setAdapter(cardAdapterMyCards);
            rclvOpponentCards.setAdapter(cardAdapterOpponentCards);
        }
        else{
            myCards = gm.getPlayer2Hand().getCardsArray();
            opponentCards = gm.getPlayer1Hand().getCardsArray();
            rclvMyCards.setAdapter(cardAdapterOpponentCards);
            rclvOpponentCards.setAdapter(cardAdapterMyCards);
        }
    }

    private class ItemClickListener implements RecyclerItemClickListener.OnItemClickListener
    {
        @Override
        public void onItemClick(View view, int position)
        {
            Toast.makeText(getApplicationContext(), "selected: " + myCards.get(position), Toast.LENGTH_LONG).show();
            // tvSelected.setText(myCards.get(position).toString());
        }

        @Override
        public void onLongItemClick(View view, int position)
        {
            // Toast.makeText(getApplicationContext(), "long click: " + deck.get(position) + " deck size: " + deck.size() , Toast.LENGTH_LONG).show();
        }
    }

    public void changePileImg(Card card){
        pileImg.setImageResource(card.calcFaceDrawableId(this));
    }

    public void setCardsNonClickable(){
        rclvMyCards.setEnabled(false);
    }

    public void setCardsClickable(){
        rclvMyCards.setEnabled(true);
    }

    public void setUnoButtonClickable(){
        unoImage.setEnabled(true);
    }

    public void setGameStatusText(String str){
        gameStatus.setText(str);
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
                showStartDialog("Player 1");
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
            else if(id == R.id.btnBlue){ // TODO: Remember that 'plus 4' card also changes the color, it means that I need to separate between 'change color' and 'plus 4'.
                reply = "blue";
                gm.changeColorOrPlusFour("blue", 14);
            }
            else if(id == R.id.btnRed){
                reply = "red";
                gm.changeColorOrPlusFour("red", 14);
            }
            else if(id == R.id.btnGreen){
                reply = "green";
                gm.changeColorOrPlusFour("green", 14);
            }
            else if(id == R.id.btnYellow){
                reply = "yellow";
                gm.changeColorOrPlusFour("yellow", 14);
            }
            else
                reply = "Empty";
            Toast.makeText(getApplicationContext(), reply, Toast.LENGTH_LONG).show();
            // tvResult.setText(reply);
            dialog.dismiss();
        }
    }

    public void showPlusFourDialog()
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

    public void showPlusTwoDialog(String str)
    {
        // Toast.makeText(this, "Custom", Toast.LENGTH_LONG).show();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.plus_two_dialog);

        // set the custom dialog components - text, image and button
        Button btnOk = dialog.findViewById(R.id.btnOk);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);

        tvTitle.setText(str + " Turn");

        CustomDialogClickListener dcl = new CustomDialogClickListener(dialog);
        btnOk.setOnClickListener(dcl);

        dialog.show();
    }

    public void showStartDialog(String str)
    {
        // Toast.makeText(this, "Custom", Toast.LENGTH_LONG).show();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.start_dialog);

        // set the custom dialog components - text, image and button
        Button btnYes = dialog.findViewById(R.id.btnAcceptTurns);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);

        tvTitle.setText(str + " Starts");

        CustomDialogClickListener dcl = new CustomDialogClickListener(dialog);
        btnYes.setOnClickListener(dcl);

        dialog.show();
    }

    public void showContinueDialog()
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

    public void showChangeColorDialog(String str)
    {
        // Toast.makeText(this, "Custom", Toast.LENGTH_LONG).show();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.change_color_dialog);

        // set the custom dialog components - text, image and button
        Button btnRed = dialog.findViewById(R.id.btnRed);
        Button btnBlue = dialog.findViewById(R.id.btnBlue);
        Button btnYellow = dialog.findViewById(R.id.btnYellow);
        Button btnGreen = dialog.findViewById(R.id.btnGreen);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);

        tvTitle.setText(str + " Turn");

        CustomDialogClickListener dcl = new CustomDialogClickListener(dialog);
        btnRed.setOnClickListener(dcl);
        btnBlue.setOnClickListener(dcl);
        btnYellow.setOnClickListener(dcl);
        btnGreen.setOnClickListener(dcl);

        dialog.show();
    }

}