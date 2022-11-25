package ilay.bar.uno.Controller;

import android.app.Activity;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;

import ilay.bar.uno.Model.Card;
import ilay.bar.uno.Model.Deck;
import ilay.bar.uno.Model.Hands;
import ilay.bar.uno.Model.Pile;
import ilay.bar.uno.R;
import ilay.bar.uno.View.GameActivity;

public class GameManager {


    protected static String GameMode, Player1Name, Player2Name;
    public GameActivity unoUI; // a reference to the activity
    private Deck deck;
    private Pile pile;
    private Hands hands; // both players hands
    private ArrayList<Card> player1Cards, player2Cards;
    private Card pileTop;


    private enum GameStatus {player1, player2, win, plus2, plus4, skip, reverse, start}
    private GameStatus gameStatus;

    public GameManager(GameActivity _unoUI){
        unoUI = _unoUI;
        newGame();
    }

    public void newGame(){
        deck = new Deck();
        pile = new Pile();
        hands = new Hands();
        hands.setPlayer1Hand(deck.getUserCards());
        hands.setPlayer2Hand(deck.getUserCards());
        player1Cards = hands.getPlayer1Hand();
        player2Cards = hands.getPlayer2Hand();
        pile.addCard(deck.removeFirst());
        // pile.addCard(new Card(Card.Colors.red, 10, true));
        pileTop = pile.getFirst();
        unoUI.changePileImg(pileTop);
        gameStatus = GameStatus.player1;

        if(pileTop.getValueName().equals("Plus4")){
            Card card = shuffleIfPlusFour(pile.removeFirst());
            pile.addCard(card);
            unoUI.changePileImg(pileTop);
        }

        if(topCardSpecial()){
            if(pileTop.getValueName().equals("ChangeColor")){
                // TODO: Add a change color dialog
                unoUI.showContinueDialog();
                gameStatus = GameStatus.player2;
                startGame();
            }
            if(pileTop.getValueName().equals("Plus2")){
                unoUI.showPlusTwoDialog();
                gameStatus = GameStatus.player2;
                startGame();
            }
            if(pileTop.getValueName().equals("Reverse")){
                unoUI.showStartDialog();
                gameStatus = GameStatus.player1;
                startGame();
            }
        }
        else {
            unoUI.showStartDialog();
            gameStatus = GameStatus.player2;
            startGame();
        }

    }

    public void startGame(){
        // TODO: manage the turns and everything. I think this function is supposed to repeat itself until someone wins.
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
        if(card.getValueName().equals("Plus2") || card.getValueName().equals("ChangeColor") || card.getValueName().equals("Reverse")){
            return true;
        }
        return false;
    }

    public static void setGameMode(String gameMode) {
        GameMode = gameMode;
    }

    public Deck getDeck() {
        return deck;
    }

    public Pile getPile() {
        return pile;
    }

    public Hands getHands() {
        return hands;
    }

}
