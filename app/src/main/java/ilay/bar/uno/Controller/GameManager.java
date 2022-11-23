package ilay.bar.uno.Controller;

import java.util.ArrayList;

import ilay.bar.uno.model.Card;
import ilay.bar.uno.model.Deck;
import ilay.bar.uno.model.Hands;
import ilay.bar.uno.model.Pile;

public class GameManager {

    protected static String GameMode, Player1Name, Player2Name;
    private Deck deck;
    private Pile pile;
    private Hands hands;
    private Card pileTop;

    private enum GameStatus {player1, player2, win, loss, plus2, plus4, skip, reverse, start}
    private GameStatus gameStatus;

    public GameManager(){
        GameMode = "";
        Player1Name = "";
        Player2Name = "";
        newGame();
    }

    public void newGame(){
        gameStatus = GameStatus.start;
        deck = new Deck();
        pile = new Pile();
        hands = new Hands();
        hands.setPlayer1Hand(deck.getUserCards());
        hands.setPlayer2Hand(deck.getUserCards());
        pile.addCard(deck.removeFirst());
        pileTop = pile.getFirst();
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
