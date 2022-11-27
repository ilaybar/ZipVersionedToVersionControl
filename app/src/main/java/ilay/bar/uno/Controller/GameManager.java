package ilay.bar.uno.Controller;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import ilay.bar.uno.Model.Card;
import ilay.bar.uno.Model.Deck;
import ilay.bar.uno.Model.Hand;
import ilay.bar.uno.Model.Pile;
import ilay.bar.uno.View.GameActivity;

public class GameManager {

    protected static String gameMode, Player1Name, Player2Name;
    public GameActivity unoUI; // a reference to the activity
    private Deck deck;
    private Pile pile;
    private Hand player1Hand, player2Hand; // both players hands
    private Card pileTop;

    private enum GameStatus {Player1, Player2, win, plus2, plus4, skip, reverse, start}
    private GameStatus gameStatus;

    public GameManager(GameActivity _unoUI){
        unoUI = _unoUI;
        init();
    }

    public void init(){
        deck = new Deck();
        pile = new Pile();

        player1Hand = new Hand();
        player1Hand.setCardsArray(deck.getUserCards());

        player2Hand = new Hand();
        player2Hand.setCardsArray(deck.getUserCards());
    }

    public void newGame(){
        pile.addCard(deck.removeFirst()); // Random card
        // pile.addCard(new Card(Card.Colors.black, 14, true)); // If you want to start with a specific card
        pileTop = pile.getFirst();
        unoUI.changePileImg(pileTop);
        gameStatus = GameStatus.Player1;

        hideBothHands();

        Log.d("GameStart", "Player1Hand: " + player1Hand.toString());
        Log.d("GameStart", "Player2Hand: " + player2Hand.toString());
        Log.d("GameStart", "Pile: " + pile.toString());

        checkStartingCase();

        startGame();
    }

    public void startGame(){
        // TODO: manage the turns and everything. I think this function is supposed to repeat itself until someone wins.
        showPlayingHand();
        // Log.d("GameEnd", "Player1Hand: " + player1Hand.toString());
        // Log.d("GameEnd", "Player2Hand: " + player2Hand.toString());
        // Log.d("GameEnd", "Pile: " + pile.toString());
    }

    // Checks all starting cases
    public void checkStartingCase(){
        if(pileTop.getValueName().equals("Plus4")){
            Card card = shuffleIfPlusFour(pile.removeFirst());
            pile.addCard(card);
            pileTop = pile.getFirst();
            unoUI.changePileImg(pileTop);
        }
        if(topCardSpecial()){
            if(!pileTop.getValueName().equals("Skip")){
                gameStatus = GameStatus.Player1;
            }
            else{
                gameStatus = GameStatus.Player2;
            }
            String str = pileTop.getValueName();
            switch (str){
                case "Skip":
                    switchRecyclerViewsAndHands(gameStatus.toString());
                    unoUI.showStartDialog(gameStatus.toString());
                    break;
                case "Reverse":
                    unoUI.showStartDialog(gameStatus.toString());
                    break;
                case "ChangeColor":
                    unoUI.showChangeColorDialog(gameStatus.toString());
                    pileTop = pile.getFirst();
                    break;
                case "Plus2":
                    unoUI.showPlusTwoDialog(gameStatus.toString());
                    break;
            }
        }
        else {
            unoUI.showStartDialog(gameStatus.toString());
        }
    }

    // TODO: run and check the log, when I change the color, the only thing that changes is the change color card itself. I'm not supposed to add a new one.
    // TODO: I need to create a new function for cases where the top is change color, I just need to change the color of the black one and not add a new one.
    public void changeColorOrPlusFour(String str, int i){
        switch(str){
            case "blue":
                pile.addCard(new Card(Card.Colors.blue, i, true));
                break;
            case "red":
                pile.addCard(new Card(Card.Colors.red, i, true));
                break;
            case "green":
                pile.addCard(new Card(Card.Colors.green, i, true));
                break;
            case "yellow":
                pile.addCard(new Card(Card.Colors.yellow, i, true));
                break;
        }
        pileTop = pile.getFirst();
        Log.d("GameEnd", "Player1Hand: " + player1Hand.toString());
        Log.d("GameEnd", "Player2Hand: " + player2Hand.toString());
        Log.d("GameEnd", "Pile: " + pile.toString());
        unoUI.changePileImg(pileTop);
    }

    public ArrayList<Card> copyArrayList(ArrayList<Card> arrayList){
        ArrayList<Card> dupArrayList = new ArrayList<Card>();
        for(int i=0; i<arrayList.size(); i++){
            dupArrayList.add(arrayList.get(i));
        }
        return dupArrayList;
    }

    public void hideBothHands(){
        for(int i = 0; i< player1Hand.getCardsArray().size(); i++){
            player1Hand.getCardsArray().get(i).setFaceUp(false);
        }
        for(int j = 0; j< player1Hand.getCardsArray().size(); j++){
            player2Hand.getCardsArray().get(j).setFaceUp(false);
        }
    }

    public void showBothHands(){
        for(int i = 0; i< player1Hand.getCardsArray().size(); i++){
            player1Hand.getCardsArray().get(i).setFaceUp(true);
        }
        for(int j = 0; j< player1Hand.getCardsArray().size(); j++){
            player2Hand.getCardsArray().get(j).setFaceUp(true);
        }
    }

    public void showPlayingHand(){
        if(gameStatus.equals(GameStatus.Player1)){
            for(int i = 0; i< player1Hand.handSize(); i++){
                player1Hand.getCardsArray().get(i).setFaceUp(true);
            }
            for(int j = 0; j< player2Hand.getCardsArray().size(); j++){
                player2Hand.getCardsArray().get(j).setFaceUp(false);
            }
        }
        else{
            for(int i = 0; i< player1Hand.handSize(); i++){
                player1Hand.getCardsArray().get(i).setFaceUp(false);
            }
            for(int j = 0; j< player2Hand.getCardsArray().size(); j++){
                player2Hand.getCardsArray().get(j).setFaceUp(true);
            }
        }
    }

    public void switchRecyclerViewsAndHands(String turn){
        unoUI.switchRecyclerViewsMain(turn);
    }

    // Checks If A Hand Has A Card To Play
    public boolean hasMove(ArrayList<Card> hand){
        boolean flag = false;
        for (int i = 0; i < hand.size(); i++){
            if(hand.get(i).getColor().equals(pileTop.getColor()) || hand.get(i).getValueName().equals(pileTop.getValueName())){
                flag = true;
            }
        }
        return flag;
    }

    public Card shuffleIfPlusFour(Card card){
        while(card.getValueName().equals("Plus4")){
            deck.addCard(card);
            Collections.shuffle(deck.deckToArrayList());
            card = deck.removeFirst();
        }
        return card;
    }

    public boolean topCardSpecial(){
        Card card = pileTop;
        if(card.getValueName().equals("Plus2") || card.getValueName().equals("ChangeColor") || card.getValueName().equals("Reverse") || pileTop.getValueName().equals("Skip")){
            return true;
        }
        return false;
    }

    public Deck getDeck() {
        return deck;
    }

    public Pile getPile() {
        return pile;
    }

    public Hand getPlayer1Hand() {
        return player1Hand;
    }

    public Hand getPlayer2Hand() {
        return player2Hand;
    }

    public String getGameStatus(){
        return gameStatus.toString();
    }

}
