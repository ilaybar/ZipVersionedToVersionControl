package ilay.bar.uno.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ilay.bar.uno.CardAdapter;
import ilay.bar.uno.Controller.GameManager;
import ilay.bar.uno.EndActivity;
import ilay.bar.uno.Globals;
import ilay.bar.uno.Model.Card;
import ilay.bar.uno.Model.Hand;
import ilay.bar.uno.Player;
import ilay.bar.uno.PrefsUtils;
import ilay.bar.uno.R;
import ilay.bar.uno.RecyclerItemClickListener;

public class GameActivity extends AppCompatActivity {

    GameManager gm;

    ConstraintLayout gameLayout;

    private BroadcastReceiver mReceiver;

    ImageView unoImage, pileImg, deckImg, movingCard;

    private	MyHandler handler; // Handler for animation thread
    private AnimationThread movingCardThread; // Thread that moves cards (To pile / From deck)
    int movingCardX, movingCardY, pileImgX, pileImgY;
    int pos; // Pos in deck of card used in the animation

    // RecyclerView
    TextView gameStatus;
    RecyclerView rclvMyCards, rclvOpponentCards;
    CardAdapter cardAdapterMyCards, cardAdapterOpponentCards;
    ArrayList<Card> myCards, opponentCards;

    // Shared Pref, editor and players arraylist
    ArrayList<Player> players;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    // TODO: go over the marks and problems and fix them

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // findViewByIds
        gameStatus = findViewById(R.id.tvGameStatus);
        deckImg = findViewById(R.id.imgDeck);
        pileImg = findViewById(R.id.imgPile);
        movingCard = findViewById(R.id.movingCard);
        unoImage = findViewById(R.id.imgUno);
        rclvMyCards = findViewById(R.id.rclvMyCards);
        rclvOpponentCards = findViewById(R.id.rclvEnemyCards);
        gameLayout = findViewById(R.id.gameLayout);

        // Set cords of pileImg and movingCard after the whole layout build is done
        gameLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setCords();
            }
        });

        // Get players indexes from mainActivity's spinners
        Intent mainActivityIntent = getIntent();
        int player1Index = mainActivityIntent.getIntExtra("Player1", 0);
        int player2Index = mainActivityIntent.getIntExtra("Player2", 0);

        // Hands Initialization
        gm = new GameManager(this);
        gm.setPlayer1Index(player1Index);
        gm.setPlayer2Index(player2Index);
        myCards = gm.getPlayer1Hand().getCardsArray();
        opponentCards = gm.getPlayer2Hand().getCardsArray();

        // Traits Initialization
        unoImage.setEnabled(false);
        deckImg.setOnClickListener(this::deckClick);
        movingCard.setVisibility(View.INVISIBLE);

        // Recycler views and card adapters
        ItemClickListener itemClickListener = new ItemClickListener();
        cardAdapterMyCards = new CardAdapter(this, myCards);
        rclvMyCards.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rclvMyCards.setAdapter(cardAdapterMyCards);
        cardAdapterOpponentCards = new CardAdapter(this, opponentCards);
        rclvOpponentCards.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rclvOpponentCards.setAdapter(cardAdapterOpponentCards);
        // Recycler on click listener
        RecyclerItemClickListener hListener1 = new RecyclerItemClickListener(this, rclvMyCards, itemClickListener);
        rclvMyCards.addOnItemTouchListener(hListener1);

        // Gets the players list from shared pref
        players = new ArrayList<>();
        pref = getSharedPreferences(Globals.PrefName, 0); // 0 - for private mode
        editor = pref.edit();
        players = PrefsUtils.readPlayersList(pref, Globals.PlayersKey);

        // Sets players names in gm
        gm.setPlayer1Index(player1Index);
        gm.setPlayer1Name(players.get(player1Index).getName());
        gm.setPlayer2Index(player2Index);
        gm.setPlayer2Name(players.get(player2Index).getName());
        gm.setPlayers(players);
        gm.newGame(); // Starts the game

        handler = new MyHandler(); // Handler

        mReceiver = new MultiActionBroadcastReceiver(); // Multi Action Broadcast Receiver
    }

    // Initialize cords of movingCard and pileImg for animation
    private void setCords() {
        int[] pileImg_coordinates = new int[2];
        pileImg.getLocationInWindow(pileImg_coordinates); // pileImg.getLocationOnScreen(pileImg_coordinates);
        pileImgX = pileImg_coordinates[0];
        pileImgY = pileImg_coordinates[1] - pileImg.getWidth();

        int[] movingCard_coordinates = new int[2];
        movingCard.getLocationInWindow(movingCard_coordinates); // movingCard.getLocationOnScreen(movingCard_coordinates);
        movingCardX =  105; // movingCard_coordinates[0];
        movingCardY = 1315; // movingCard_coordinates[1];
    }

    // Update players statistics as the game ends
    public void playersListUpdate(ArrayList<Player> _players){
        players = _players;
        save();
    }

    // Save players arraylist in shared pref
    public void save()
    {
        Toast.makeText(this, "Save", Toast.LENGTH_LONG).show();
        PrefsUtils.writePlayersList(players, editor, Globals.PlayersKey);
    }

    // Game status text update
    public void updateGamesStatusText(String str){
        gameStatus.setText(str + "'s Turn");
    }

    // Updates the top card image
    public void changePileImg(Card card){
        pileImg.setImageResource(card.calcFaceDrawableId(this));
    }

    // Can or Cannot use the uno button
    public void checkUnoImageClickable(Hand hand1, Hand hand2){
        if(hand1.arraySize() <= 2 || hand2.arraySize() <= 2){
            unoImage.setClickable(true);
            Toast.makeText(getApplicationContext(), "Clickable, Can Use Uno", Toast.LENGTH_LONG).show();
        }
        else{
            unoImage.setClickable(false);
            Toast.makeText(getApplicationContext(), "Not Clickable, Can't Use Uno", Toast.LENGTH_LONG).show();
        }
    }

    // Provides the player's name from a string
    public String getNameFromStatus(String str){
        if(str.equals("Player1")){
            return gm.getPlayer1Name();
        }
        return gm.getPlayer2Name();
    }

    // No cards text
    public void noCardsToPlay(){
        String name = getNameFromStatus(gm.getGameStatus());
        gameStatus.setText(name + ": Take A Card !");
    }

    // Plus Cards Text Update
    public void takePlusCards(){
        String name = gm.getPlusStatus().toString();
        if(gm.getPlusStatus().equals("Plus2")){
            gameStatus.setText(name + ": Take 2 Cards !");
        }
        else if(gm.getPlusStatus().equals("Plus4")){
            gameStatus.setText(name + ": Take 4 Cards !");
        }
        else{
            gameStatus.setText(name + ": Take 6 Cards !");
        }
    }

    // Sets the arraylist<Card> for bottom RecyclerView (Playing Hand)
    public void setMyCards(ArrayList<Card> arrayList){
        myCards = arrayList;
        cardAdapterMyCards = new CardAdapter(this, myCards);
        rclvMyCards.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rclvMyCards.setAdapter(cardAdapterMyCards);
    }

    // Sets the arraylist<Card> for top RecyclerView (Non Playing Hand)
    public void setOpponentCards(ArrayList<Card> arrayList){
        opponentCards = arrayList;
        cardAdapterOpponentCards = new CardAdapter(this, opponentCards);
        rclvOpponentCards.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rclvOpponentCards.setAdapter(cardAdapterOpponentCards);
    }

    // Switches between the hands presented in each RecyclerView
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

    // Player Chose A Card (Clicked the playing hand RecyclerView)
    private class ItemClickListener implements RecyclerItemClickListener.OnItemClickListener
    {
        @Override
        public void onItemClick(View view, int position)
        {
            if(gm.isCardUsable(myCards.get(position)) && gm.canUseCards()){
                deckImg.setClickable(false);
                rclvMyCards.setClickable(false);
                //Toast.makeText(getApplicationContext(), "Can Be Used: " + myCards.get(position), Toast.LENGTH_LONG / 2).show();
                Card card = myCards.get(position);

                movingCard.setX(movingCardX);
                movingCard.setY(movingCardY);

                movingCard.setImageResource(card.calcFaceDrawableId(GameActivity.this));
                movingCard.setVisibility(View.VISIBLE);
                movingCardThread = new AnimationThread(1);
                movingCardThread.start();
                pos = position;
            }
            else{
                //Toast.makeText(getApplicationContext(), "Can't Be Used: " + myCards.get(position), Toast.LENGTH_LONG / 2).show();
            }
            // tvSelected.setText(myCards.get(position).toString());
        }

        @Override
        public void onLongItemClick(View view, int position)
        {
            // Toast.makeText(getApplicationContext(), "long click: " + deck.get(position) + " deck size: " + deck.size() , Toast.LENGTH_LONG).show();
        }
    }

    // Player took a card/s
    public void deckClick(View view){
        boolean flag = false;
        Card card = gm.getPileTop();
        String str = card.getValueName();
        String nextPlayer = "";
        int mode = 1;
        if(str.equals("Plus2") || str.equals("Plus4") || str.equals("Plus6")){
            str = "Plus";
            mode = Integer.parseInt(String.valueOf(gm.getPlusStatus().toString().charAt(4)));
        }
        if(gm.getDeckSize() <= mode){
            Card topCard = gm.removePileTop();
            gm.removeSpecialsInPile();
            gm.shufflePile();
            while(gm.getPile().arraySize() > 0){
                gm.addCardToDeck(gm.getAndRemovePileBottom());
            }
            gm.addPileTop(topCard);
        }
        switch (str){
            case "Plus":
                if(gm.getGameStatus().equals("Player1") && gm.getDeckSize() > 0){
                    if(!gm.canUseCards()){
                        gm.takeCardFromDeck(gm.getPlayer1Hand(), mode);
                        flag = false;
                    }
                    else{
                        gm.takeCardFromDeck(gm.getPlayer1Hand(), 1); //1
                        Card card1 = gm.getPlayer1Hand().getCardsArray().get(gm.getPlayer1Hand().arraySize() - 1);
                        if(gm.isCardUsable(card1)){
                            flag = true;
                        }
                    }
                    nextPlayer = "Player2";
                }
                else{
                    if(!gm.canUseCards()){
                        gm.takeCardFromDeck(gm.getPlayer2Hand(), mode);
                        flag = false;
                    }
                    else{
                        gm.takeCardFromDeck(gm.getPlayer2Hand(), 1); //1
                        Card card1 = gm.getPlayer2Hand().getCardsArray().get(gm.getPlayer2Hand().arraySize() - 1);
                        if(gm.isCardUsable(card1)){
                            flag = true;
                        }
                    }
                    nextPlayer = "Player1";
                }
                gm.setCanUseCards(true);
                break;
            default:
                Log.d("NoCards", "Deck Size (New): " + gm.getDeckSize());
                if(gm.getGameStatus().equals("Player1") && gm.getDeckSize() > 0){
                    gm.takeCardFromDeck(gm.getPlayer1Hand(), 1); //1
                    Card card1 = gm.getPlayer1Hand().getCardsArray().get(gm.getPlayer1Hand().arraySize() - 1);
                    if(gm.isCardUsable(card1)){
                        flag = true;
                    }
                    nextPlayer = "Player2";
                }
                else if(gm.getGameStatus().equals("Player2") && gm.getDeckSize() > 0){
                    gm.takeCardFromDeck(gm.getPlayer2Hand(), 1); //1
                    Card card1 = gm.getPlayer2Hand().getCardsArray().get(gm.getPlayer2Hand().arraySize() - 1);
                    if(gm.isCardUsable(card1)){
                        flag = true;
                    }
                    nextPlayer = "Player1";
                }
                break;
        }
        // Take card from deck animation
        movingCard.setX(deckImg.getX());
        movingCard.setY(deckImg.getY());
        Card cardBack = new Card(Card.Colors.black, 7, false);
        movingCard.setImageResource(cardBack.calcFaceDrawableId(GameActivity.this));
        movingCard.setVisibility(View.VISIBLE);
        movingCardThread = new AnimationThread(2);
        movingCardThread.start();

        // Move to next turn
        gm.setGameStatus(nextPlayer);
        gm.updateGameStatusText();

        // Can play the player's card he got from the deck
        Card cardToBePlayed;
        if(flag && gm.getGameStatus().equals("Player1")){
            Card cardFromHand = gm.getPlayer2Hand().getCardsArray().get(gm.getPlayer2Hand().arraySize() - 1);
            cardToBePlayed = new Card(cardFromHand.getColor(), cardFromHand.getValue(), true);
            showCardTakeDialog(cardToBePlayed);
        }
        else if(flag && gm.getGameStatus().equals("Player2")){
            Card cardFromHand = gm.getPlayer1Hand().getCardsArray().get(gm.getPlayer1Hand().arraySize() - 1);
            cardToBePlayed = new Card(cardFromHand.getColor(), cardFromHand.getValue(), true);
            showCardTakeDialog(cardToBePlayed);
        }
        else{
            showContinueDialog(gm.getGameStatus());
        }

        // Move to next turn
        gm.hideBothHands();
        gm.updateRecyclerViews();
    }

    // Game Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem Item) {
        Intent myIntent = new Intent(this, EndActivity.class);
        myIntent.putExtra("key", ""); //Optional parameters
        int resID = Item.getItemId();
        switch(resID){
            case R.id.item1:
                startActivity(myIntent);
                break;
        }
        return true;
    }

    // Create Game Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.game_menu, menu);
        return true;
    }

    // Checks what button was pressed in the custom dialog, each button has a different goal
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
            if (id == R.id.btnYes){
                if(gm.getGameStatus().equals("Player2")){
                    gm.playerChallenge(true, gm.hadMove(gm.getPlayer1Hand().getCardsArray()));
                }
                else{
                    gm.playerChallenge(true, gm.hadMove(gm.getPlayer2Hand().getCardsArray()));
                }
            }
            else if(id == R.id.btnNo){
                gm.playerChallenge(false, false);
            }
            else if(id == R.id.btnAcceptTurns){
                updateRecyclerViews();
                gm.showPlayingHand();
                gm.handNoMove();
            }
            else if(id == R.id.btnContinueGame){
                updateRecyclerViews();
                gm.showPlayingHand();
                gm.handNoMove();
            }
            else if(id == R.id.btnEndScreenFromWin){
                moveToEndScreen();
            }
            else if(id == R.id.btnPlayIt){
                int lastIndex;
                if(gm.getGameStatus().equals("Player2")){
                    lastIndex = gm.getPlayer1Hand().getCardsArray().size() - 1;
                    Log.d("game", "Index1, Size1: " + lastIndex + ", " + gm.getPlayer1Hand().arraySize());
                    gm.setGameStatus("Player1");
                }
                else{
                    lastIndex = gm.getPlayer2Hand().getCardsArray().size() - 1;
                    Log.d("game", "Index2, Size2: " + lastIndex + ", " + gm.getPlayer2Hand().arraySize());
                    gm.setGameStatus("Player2");
                }
                gm.updateGameStatusText();
                gm.useCard(lastIndex);
                gm.getPile().getFirst().setFaceUp(true);
                changePileImg(gm.getPileTop());
            }
            else if(id == R.id.btnSaveIt){
                showContinueDialog(gm.getGameStatus());
            }
            else if(id == R.id.btnOk){
                updateRecyclerViews();
                gm.showPlayingHand();
                gm.topIsPlus();
            }
            else if(id == R.id.btnBlue || id == R.id.btnRed || id == R.id.btnGreen || id == R.id.btnYellow){
                switch (id){
                    case R.id.btnBlue:
                        gm.changeColorOrPlusFour("blue", gm.getPileTop().getValue());
                        break;
                    case R.id.btnRed:
                        gm.changeColorOrPlusFour("red", gm.getPileTop().getValue());
                        break;
                    case R.id.btnGreen:
                        gm.changeColorOrPlusFour("green", gm.getPileTop().getValue());
                        break;
                    case R.id.btnYellow:
                        gm.changeColorOrPlusFour("yellow", gm.getPileTop().getValue());
                        break;
                }
                if(gm.getPileTop().getValueName().equals("Plus4")){
                    gm.updateGameStatusText();
                }
                updateRecyclerViews();
                gm.showPlayingHand();
                gm.hideBothHands();
                showContinueDialog(gm.getGameStatus());
            }
            dialog.dismiss();
        }
    }

    public void showPlusFourDialog(String status)
    {
        // Toast.makeText(this, "Custom", Toast.LENGTH_LONG).show();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.plus_four_dialog);

        // set the custom dialog components - text, image and button
        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);

        String name = getNameFromStatus(status);
        tvTitle.setText(name + "'s Turn");

        CustomDialogClickListener dcl = new CustomDialogClickListener(dialog);
        btnYes.setOnClickListener(dcl);
        btnNo.setOnClickListener(dcl);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void showPlusTwoDialog(String status)
    {
        // Toast.makeText(this, "Custom", Toast.LENGTH_LONG).show();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.plus_two_dialog);

        // set the custom dialog components - text, image and button
        Button btnOk = dialog.findViewById(R.id.btnOk);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);

        String name = getNameFromStatus(status);
        tvTitle.setText(name + "'s Turn");

        CustomDialogClickListener dcl = new CustomDialogClickListener(dialog);
        btnOk.setOnClickListener(dcl);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void showStartDialog(String status)
    {
        // Toast.makeText(this, "Custom", Toast.LENGTH_LONG).show();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.start_dialog);

        // set the custom dialog components - text, image and button
        Button btnYes = dialog.findViewById(R.id.btnAcceptTurns);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);

        String name = getNameFromStatus(status);
        tvTitle.setText(name + " Starts");

        CustomDialogClickListener dcl = new CustomDialogClickListener(dialog);
        btnYes.setOnClickListener(dcl);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void showContinueDialog(String status)
    {
        // Toast.makeText(this, "Custom", Toast.LENGTH_LONG).show();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.continue_dialog);

        // set the custom dialog components - text, image and button
        Button btnContinue = dialog.findViewById(R.id.btnContinueGame);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);

        String name = getNameFromStatus(status);
        tvTitle.setText(name + "'s Turn");

        CustomDialogClickListener dcl = new CustomDialogClickListener(dialog);
        btnContinue.setOnClickListener(dcl);

        checkUnoImageClickable(gm.getPlayer1Hand(), gm.getPlayer2Hand());

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void showChangeColorDialog(String status)
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

        String name = getNameFromStatus(status);
        tvTitle.setText(name + "'s Turn");

        CustomDialogClickListener dcl = new CustomDialogClickListener(dialog);
        btnRed.setOnClickListener(dcl);
        btnBlue.setOnClickListener(dcl);
        btnYellow.setOnClickListener(dcl);
        btnGreen.setOnClickListener(dcl);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
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
            String name = getNameFromStatus("Player2");
            tvTitle.setText(name + "'s Turn");
        }
        else{
            String name = getNameFromStatus("Player1");
            tvTitle.setText(name + "'s Turn");
        }
        imgCard.setImageResource(card.calcFaceDrawableId(this));

        CustomDialogClickListener dcl = new CustomDialogClickListener(dialog);
        btnPlayIt.setOnClickListener(dcl);
        btnSaveIt.setOnClickListener(dcl);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void showWinDialog(String status)
    {
        // Toast.makeText(this, "Custom", Toast.LENGTH_LONG).show();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.win_dialog);

        // set the custom dialog components - text, image and button
        Button btnEndScreen = dialog.findViewById(R.id.btnEndScreenFromWin);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);

        String name = getNameFromStatus(status);

        tvTitle.setText(name + " Won !");

        CustomDialogClickListener dcl = new CustomDialogClickListener(dialog);
        btnEndScreen.setOnClickListener(dcl);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void moveToEndScreen(){
        Intent intent = new Intent(this, EndActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        IntentFilter filterBattery = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        IntentFilter filterWifi = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(mReceiver, filterBattery);
        registerReceiver(mReceiver, filterWifi);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(mReceiver);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private class MultiActionBroadcastReceiver extends BroadcastReceiver {

        private boolean showedToast = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("android.intent.action.BATTERY_CHANGED")){
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                if(level % 10 != 0 && level <= 70){
                    showedToast = false;
                }
                if(level % 10 == 0 && level <= 70 && !showedToast){
                    Toast.makeText(context, "Battery getting low", Toast.LENGTH_SHORT).show();
                    showedToast = true;
                }
            }
            else{
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (info != null && info.isConnected()) {
                    Toast.makeText(context, "Connected to Wi-Fi", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Disconnected from Wi-Fi", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Handling the animation thread
    private class MyHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            Bundle data = msg.getData();
            int counter = data.getInt("counter");
            int[] arr = data.getIntArray("arr");
            int mode = data.getInt("mode");
            movingCard.setX(arr[0]);
            movingCard.setY(arr[1]);
            if (counter == 30)
            {
                movingCard.setVisibility(View.INVISIBLE);
                setCords();
                if(mode == 1){
                    gm.useCard(pos);
                }
                deckImg.setClickable(true);
                rclvMyCards.setClickable(true);
            }
        } // handleMessage(...)

    } // class MyHandler

    public class AnimationThread extends Thread
    {
        private int x, y;
        private int interval = 32; // "sleep" interval in milisec
        private int mode = 1; // 1 - To Pile | 2 - To Hand
        public AnimationThread(int mode)
        {
            if(mode == 1){
                this.x = movingCardX;
                this.y = movingCardY;
            }
            else{
                this.x = (int) deckImg.getX();
                this.y = (int) deckImg.getY();
            }
            this.mode = mode;
        }

        public void run()
        {
            int counter = 0, xPos = x, yPos = y, dx, dy;
            if(mode == 1){
                dx = (pileImgX - x) / 30;
                dy = (pileImgY - y) / 30;
            }
            else{
                dx = (x + 25) / 30;
                dy = (y - movingCardY) / 30;
            }
            while (counter <= 30)
            {
                try
                {
                    if(mode == 1){
                        xPos += dx;
                        yPos += dy;
                    }
                    else{
                        xPos -= dx;
                        yPos -= dy;
                    }
                    Thread.sleep(interval);
                } catch (InterruptedException ex)
                {
                    ex.printStackTrace();
                }  // catch
                counter++;
                sendCounter2Activity(xPos, yPos, counter, mode);
            } // while
        } // run()

        private void sendCounter2Activity(int xPos, int yPos, int counter, int mode)
        {
            Message msg = handler.obtainMessage();
            Bundle data = msg.getData();
            data.putInt("counter", counter);
            int[] arr = {xPos, yPos};
            data.putIntArray("arr", arr);
            data.putInt("mode", mode);
            handler.sendMessage(msg);
        }
    } // Class PrintCharThread

}