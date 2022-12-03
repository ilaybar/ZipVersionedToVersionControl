package ilay.bar.uno.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
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
    ImageView unoImage, pileImg, deckImg;

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
        deckImg = findViewById(R.id.imgDeck);
        deckImg.setOnClickListener(this::deckClick);

        gm = new GameManager(this);
        deck = gm.getDeck().getCardsArray();
        myCards = gm.getPlayer1Hand().getCardsArray();
        opponentCards = gm.getPlayer2Hand().getCardsArray();
        pile = gm.getPile().getCardsArray();

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

    public ArrayList<Card> getMyCards() {
        return myCards;
    }

    public ArrayList<Card> getOpponentCards() {
        return opponentCards;
    }

    public void setMyCards(ArrayList<Card> arrayList){
        myCards = arrayList;
        cardAdapterMyCards = new CardAdapter(this, myCards);
        rclvMyCards.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rclvMyCards.setAdapter(cardAdapterMyCards);
    }

    public void setOpponentCards(ArrayList<Card> arrayList){
        opponentCards = arrayList;
        cardAdapterOpponentCards = new CardAdapter(this, opponentCards);
        rclvOpponentCards.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rclvOpponentCards.setAdapter(cardAdapterOpponentCards);
    }

    public void updateGamesStatusText(String str){
        gameStatus.setText(str + "'s Turn");
    }

    // Switches the recyclerViews according to the turn, if player1 turn: my cards = player1Hand, else: my cards = player2Hand.
    public void switchRecyclerViewsMain(String turn){
        if(turn.equals("Player1")){
            myCards = gm.getPlayer1Hand().getCardsArray();
            opponentCards = gm.getPlayer2Hand().getCardsArray();
        }
        if(turn.equals("Player2")){
            myCards = gm.getPlayer2Hand().getCardsArray();
            opponentCards = gm.getPlayer1Hand().getCardsArray();
        }
        cardAdapterMyCards = new CardAdapter(this, myCards);
        cardAdapterOpponentCards = new CardAdapter(this, opponentCards);
        rclvMyCards.setAdapter(cardAdapterMyCards);
        rclvOpponentCards.setAdapter(cardAdapterOpponentCards);

    }

    public void updateRecyclerViews(){
        if(gm.getGameStatus().equals("Player1")){
            setMyCards(gm.getPlayer1Hand().getCardsArray());
            setOpponentCards(gm.getPlayer2Hand().getCardsArray());
        }
        else{
            setMyCards(gm.getPlayer2Hand().getCardsArray());
            setOpponentCards(gm.getPlayer1Hand().getCardsArray());
        }
    }

    private class ItemClickListener implements RecyclerItemClickListener.OnItemClickListener
    {
        @Override
        public void onItemClick(View view, int position)
        {
            if(gm.isCardUsable(myCards.get(position)) && gm.canUseCards()){
                Toast.makeText(getApplicationContext(), "Can Be Used: " + myCards.get(position), Toast.LENGTH_LONG / 2).show();
                gm.useCard(position);
            }
            else{
                Toast.makeText(getApplicationContext(), "Can't Be Used: " + myCards.get(position), Toast.LENGTH_LONG / 2).show();
            }
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

    public void setUnoButtonClickable(){
        unoImage.setEnabled(true);
    }

    public void noCardsToPlay(){
        gameStatus.setText(gm.getGameStatus() + ": Take A Card !");
    }

    public void takeTwoCards(){
        gameStatus.setText(gm.getGameStatus() + ": Take 2 Cards !");
    }

    public void deckClick(View view){
        Boolean flag = false;
        Card card = gm.getPileTop();
        String str = card.getValueName();
        String nextPlayer = "";
        switch (str){
            case "Plus2":
                if(gm.getGameStatus().equals("Player1") && gm.getDeckSize() > 0){
                    if(!gm.canUseCards()){
                        gm.takeCardFromDeck(gm.getPlayer1Hand(), 2);
                    }
                    else{
                        gm.takeCardFromDeck(gm.getPlayer1Hand(), 1);
                        Card card1 = gm.getPlayer1Hand().getCardsArray().get(gm.getPlayer1Hand().arraySize() - 1);
                        if(gm.isCardUsable(card1)){
                            flag = true;
                        }
                    }
                    nextPlayer = "Player2";
                }
                else{
                    if(!gm.canUseCards()){
                        gm.takeCardFromDeck(gm.getPlayer2Hand(), 2);
                    }
                    else{
                        gm.takeCardFromDeck(gm.getPlayer2Hand(), 1);
                        Card card1 = gm.getPlayer2Hand().getCardsArray().get(gm.getPlayer2Hand().arraySize() - 1);
                        if(gm.isCardUsable(card1)){
                            flag = true;
                        }
                    }
                    nextPlayer = "Player1";
                }
                gm.setCanUseCards(true);
                break;
            case "Plus4":
                break;
            default:
                if(gm.getGameStatus().equals("Player1") && gm.getDeckSize() > 0){
                    gm.takeCardFromDeck(gm.getPlayer1Hand(), 1);
                    Card card1 = gm.getPlayer1Hand().getCardsArray().get(gm.getPlayer1Hand().arraySize() - 1);
                    if(gm.isCardUsable(card1)){
                        flag = true;
                    }
                    nextPlayer = "Player2";
                }
                else if(gm.getGameStatus().equals("Player2") && gm.getDeckSize() > 0){
                    gm.takeCardFromDeck(gm.getPlayer2Hand(), 1);
                    Card card1 = gm.getPlayer2Hand().getCardsArray().get(gm.getPlayer2Hand().arraySize() - 1);
                    if(gm.isCardUsable(card1)){
                        flag = true;
                    }
                    nextPlayer = "Player1";
                }
                break;
        }
        // TODO: this doesn't work for some reason, it's important to fix it.
        /*
        if(gm.getDeckSize() == 0) {
            Card card = gm.removePileTop();
            gm.shufflePile();
            gm.addPileTop(card);
        }*/
        updateRecyclerViews();
        gm.setGameStatus(nextPlayer);
        gm.hideBothHands();
        gm.switchRecyclerViewsAndHands();
        gm.updateGameStatus();
        showContinueDialog(gm.getGameStatus());
        /*
        if(flag && gm.getGameStatus().equals("Player2")){
            showCardTakeDialog(gm.getPlayer2Hand().getCardsArray().get(gm.getPlayer2Hand().arraySize() - 1));
        }
        else if(flag && gm.getGameStatus().equals("Player1")){
            showCardTakeDialog(gm.getPlayer1Hand().getCardsArray().get(gm.getPlayer1Hand().arraySize() - 1));
        }*/
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
                showStartDialog("Player1");
                break;
            case R.id.item3:
                showContinueDialog("Player1");
                break;
            case R.id.item4:
                showPlusFourDialog("Player1");
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
            else if(id == R.id.btnAcceptTurns){
                reply = "Accept Turns";
                updateRecyclerViews();
                gm.showPlayingHand();
                gm.handNoMove();
            }
            else if(id == R.id.btnContinueGame){
                reply = "Continue Game";
                updateRecyclerViews();
                gm.showPlayingHand();
                gm.logDFunction();
                gm.handNoMove();
            }
            else if(id == R.id.btnEndScreenFromWin){
                reply = "A Player Won";
                moveToEndScreen();
            }
            else if(id == R.id.btnOk){
                reply = "Plus 2";
                updateRecyclerViews();
                gm.showPlayingHand();
                gm.logDFunction();
                gm.topIsPlus2();
            }
            else if(id == R.id.btnBlue){ // TODO: Remember that 'plus 4' card also changes the color, it means that I need to separate between 'change color' and 'plus 4'.
                reply = "blue";
                gm.updateGameStatus();
                updateRecyclerViews();
                gm.showPlayingHand();
                gm.changeColorOrPlusFour("blue", 14);
                gm.hideBothHands();
                showContinueDialog(gm.getGameStatus());
            }
            else if(id == R.id.btnRed){
                reply = "red";
                gm.updateGameStatus();
                updateRecyclerViews();
                gm.showPlayingHand();
                gm.changeColorOrPlusFour("red", 14);
                gm.hideBothHands();
                showContinueDialog(gm.getGameStatus());
            }
            else if(id == R.id.btnGreen){
                reply = "green";
                gm.updateGameStatus();
                updateRecyclerViews();
                gm.showPlayingHand();
                gm.changeColorOrPlusFour("green", 14);
                gm.hideBothHands();
                showContinueDialog(gm.getGameStatus());
            }
            else if(id == R.id.btnYellow){
                reply = "yellow";
                gm.updateGameStatus();
                updateRecyclerViews();
                gm.showPlayingHand();
                gm.changeColorOrPlusFour("yellow", 14);
                gm.hideBothHands();
                showContinueDialog(gm.getGameStatus());
            }
            else
                reply = "Empty";
            Toast.makeText(getApplicationContext(), reply, Toast.LENGTH_LONG).show();
            // tvResult.setText(reply);
            dialog.dismiss();
        }
    }

    public void showPlusFourDialog(String str)
    {
        // Toast.makeText(this, "Custom", Toast.LENGTH_LONG).show();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.plus_four_dialog);

        // set the custom dialog components - text, image and button
        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);

        tvTitle.setText(str + "'s Turn");

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

        tvTitle.setText(str + "'s Turn");

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

    public void showContinueDialog(String str)
    {
        // Toast.makeText(this, "Custom", Toast.LENGTH_LONG).show();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.continue_dialog);

        // set the custom dialog components - text, image and button
        Button btnContinue = dialog.findViewById(R.id.btnContinueGame);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);

        tvTitle.setText(str + "'s Turn");

        CustomDialogClickListener dcl = new CustomDialogClickListener(dialog);
        btnContinue.setOnClickListener(dcl);

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

        tvTitle.setText(str + "'s Turn");

        CustomDialogClickListener dcl = new CustomDialogClickListener(dialog);
        btnRed.setOnClickListener(dcl);
        btnBlue.setOnClickListener(dcl);
        btnYellow.setOnClickListener(dcl);
        btnGreen.setOnClickListener(dcl);

        dialog.show();
    }

    public void showCardTakeDialog(Card card)
    {
        // Toast.makeText(this, "Custom", Toast.LENGTH_LONG).show();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.card_take_dialog);

        // set the custom dialog components - text, image and button
        Button btnPlayIt = dialog.findViewById(R.id.btnPlayIt);
        Button btnSaveIt = dialog.findViewById(R.id.btnSaveIt);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        ImageView imgCard = dialog.findViewById(R.id.imgCard);

        if(gm.getGameStatus().equals("Player1")){
            tvTitle.setText("Player2" + "'s Turn");
        }
        else{
            tvTitle.setText("Player1" + "'s Turn");
        }
        imgCard.setImageResource(card.calcFaceDrawableId(this));

        CustomDialogClickListener dcl = new CustomDialogClickListener(dialog);
        btnPlayIt.setOnClickListener(dcl);
        btnSaveIt.setOnClickListener(dcl);

        dialog.show();
    }

    public void showWinDialog(String str)
    {
        // Toast.makeText(this, "Custom", Toast.LENGTH_LONG).show();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.win_dialog);

        // set the custom dialog components - text, image and button
        Button btnEndScreen = dialog.findViewById(R.id.btnEndScreenFromWin);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);

        tvTitle.setText(str + " Won !");

        CustomDialogClickListener dcl = new CustomDialogClickListener(dialog);
        btnEndScreen.setOnClickListener(dcl);

        dialog.show();
    }

    public void moveToEndScreen(){
        Intent intent = new Intent(this, EndActivity.class);
        startActivity(intent);
    }

}