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

    public GameActivity unoUI; // a reference to the activity
    private Deck deck;
    private Pile pile;
    private Hand player1Hand, player2Hand; // both players hands
    private Card pileTop;
    private boolean canUseCards;
    private String player1Name, player2Name;

    private enum GameStatus {Player1, Player2}
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
        /*
        player1Hand.addCard(new Card(Card.Colors.green, 12, true));
        player1Hand.addCard(new Card(Card.Colors.yellow, 1, true));
        player1Hand.addCard(new Card(Card.Colors.green, 6, true));
        player1Hand.addCard(new Card(Card.Colors.blue, 9, true));
        player1Hand.addCard(new Card(Card.Colors.green, 10, true));
        player1Hand.addCard(new Card(Card.Colors.green, 2, true));
        player1Hand.addCard(new Card(Card.Colors.yellow, 2, true));
         */

        player2Hand = new Hand();
        player2Hand.setCardsArray(deck.getUserCards());
        /*
        player2Hand.addCard(new Card(Card.Colors.red, 2, true));
        player2Hand.addCard(new Card(Card.Colors.blue, 5, true));
        player2Hand.addCard(new Card(Card.Colors.yellow, 1, true));
        player2Hand.addCard(new Card(Card.Colors.black, 14, true));
        player2Hand.addCard(new Card(Card.Colors.green, 7, true));
        player2Hand.addCard(new Card(Card.Colors.blue, 8, true));
        player2Hand.addCard(new Card(Card.Colors.blue, 2, true));
         */
    }

    public void newGame(){
        pile.addCard(deck.removeFirst()); // Random card
        // pile.addCard(new Card(Card.Colors.red, 10, true)); // If you want to start with a specific card
        pileTop = pile.getFirst();
        unoUI.changePileImg(pileTop);
        gameStatus = GameStatus.Player1;

        hideBothHands();

        checkStartingCase();

        startGame();

    }

    public void startGame(){
        updateGameStatus();
        logDFunction();
    }

    public void checkWin(){
        if(player1Hand.arraySize() == 0){
            unoUI.showWinDialog("Player1");
        }
        if(player2Hand.arraySize() == 0){
            unoUI.showWinDialog("Player2");
        }
    }

    public void useCard(int pos){
        String gameStatusSave = "";
        if(gameStatus.equals(GameStatus.Player1)){
            pile.addCard(player1Hand.removeSpecific(pos));
            unoUI.setMyCards(player1Hand.getCardsArray());
            gameStatusSave = GameStatus.Player2.toString();
        }
        if(gameStatus.equals(GameStatus.Player2)){
            pile.addCard(player2Hand.removeSpecific(pos));
            unoUI.setOpponentCards(player2Hand.getCardsArray());
            gameStatusSave = GameStatus.Player1.toString();
        }
        pileTop = pile.getFirst();
        unoUI.changePileImg(pileTop);
        String card = pileTop.getValueName();
        switch (card){
            case "ChangeColor": // Works fine I think
                unoUI.showChangeColorDialog(gameStatus.toString()); // show dialog
                updateRecyclerViews();
                setGameStatus(gameStatusSave);
                updateGameStatus();
                break;
            case "Plus2": // Works fine I think
                setGameStatus(gameStatusSave);
                hideBothHands();
                updateRecyclerViews();
                updateGameStatus();
                unoUI.showPlusTwoDialog(gameStatus.toString()); // show dialog
                topIsPlus2();
                break;
            case "Plus4":
                setGameStatus(gameStatusSave);
                unoUI.showPlusFourDialog(gameStatus.toString()); // show dialog
                break;
            case "Skip":
            case "Reverse": // Works fine I think
                hideBothHands();
                unoUI.showContinueDialog(gameStatus.toString()); // show dialog
                handNoMove();
                break;
            default: // Works fine I think
                setGameStatus(gameStatusSave);
                hideBothHands(); // These three repeat themselves i think i can merge into a function
                updateRecyclerViews(); // These three repeat themselves i think i can merge into a function
                // unoUI.updateRecyclerViews();
                updateGameStatus(); // These three repeat themselves i think i can merge into a function
                unoUI.showContinueDialog(gameStatusSave); // show dialog
                handNoMove();
        }
        checkWin();
        logDFunction();
    }

    public void takeCardFromDeck(Hand hand, int mode){ // (mode = 1 take 1 card), (mode = 2 take 2 cards) for cases like +2 and +4 / +6 (challenge)
        while (mode > 0){
            hand.addCard(deck.removeFirst());
            mode--;
        }
    }

    public void logDFunction(){
        Log.d("Game", "Player Turn: " + gameStatus.toString());
        Log.d("Game", "Player1Hand: " + player1Hand.toString());
        Log.d("Game", "Player2Hand: " + player2Hand.toString());
        Log.d("Game", "");
        Log.d("Game", "myCards: " + unoUI.getMyCards());
        Log.d("Game", "OpponentCards: " + unoUI.getOpponentCards());
        Log.d("Game", "");
        Log.d("Game", "Pile: " + pile.toString());
        Log.d("Game", "-----------------------------------");
    }

    public void setGameStatus(String str){
        if(str.equals("Player1")){
            gameStatus = GameStatus.Player1;
        }
        else{
            gameStatus = GameStatus.Player2;
        }
    }

    public boolean isCardUsable(Card card){
        if(card.getValueName().equals("Plus4") || card.getValueName().equals("ChangeColor")){
            return true;
        }
        return card.getValueName().equals(getPileTop().getValueName()) || card.getColor().equals(getPileTop().getColor());
    }

    public void updateGameStatus(){
        unoUI.updateGamesStatusText(gameStatus.toString());
    }

    // Checks all starting cases
    public void checkStartingCase(){
        canUseCards = true;
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
                    updateRecyclerViews();
                    unoUI.showStartDialog(gameStatus.toString());
                    break;
                case "Reverse":
                    unoUI.showStartDialog(gameStatus.toString());
                    break;
                case "ChangeColor":
                    unoUI.showChangeColorDialog(gameStatus.toString());
                    unoUI.showStartDialog(gameStatus.toString());
                    pileTop = pile.getFirst();
                    break;
                case "Plus2":
                    canUseCards = false;
                    unoUI.showPlusTwoDialog(gameStatus.toString());
                    topIsPlus2();
                    break;
            }
        }
        else {
            unoUI.showStartDialog(gameStatus.toString());
        }
    }

    // TODO: run and check the log, when I change the color, the only thing that changes is the change color card itself. I'm supposed to add a new one, when I reshuffle the pile I'll just get the weird colorChange and Plus4 out and then shuffle.
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
        unoUI.changePileImg(pileTop);
    }

    public void hideBothHands(){
        for(int i = 0; i<player1Hand.getCardsArray().size(); i++){
            player1Hand.getCardsArray().get(i).setFaceUp(false);
        }
        for(int j = 0; j<player2Hand.getCardsArray().size(); j++){
            player2Hand.getCardsArray().get(j).setFaceUp(false);
        }
    }

    public void showPlayingHand(){
        if(gameStatus.equals(GameStatus.Player1)){
            player1Hand.cardsFaceUp();
        }
        else{
            player2Hand.cardsFaceUp();
        }
    }

    public void updateRecyclerViews(){
        unoUI.updateRecyclerViews();
    }

    public void topIsPlus2(){
        if(pile.getFirst().getValueName().equals("Plus2")){
            canUseCards = false;
            unoUI.takeTwoCards();
        }
        else{
            canUseCards = true;
        }
    }

    public void handNoMove(){
        if(gameStatus.equals(GameStatus.Player1)){
            if(hasMove(player1Hand.getCardsArray())){
                unoUI.noCardsToPlay();
            }
        }
        else{
            if(hasMove(player2Hand.getCardsArray())){
                unoUI.noCardsToPlay();
            }
        }
    }

    // Checks If A Hand Has A Card To Play
    public boolean hasMove(ArrayList<Card> array){
        boolean flag = false;
        for (int i = 0; i < array.size(); i++){
            if(array.get(i).getColor().equals(pileTop.getColor()) || array.get(i).getValueName().equals(pileTop.getValueName()) || array.get(i).getValueName().equals("ChangeColor") || array.get(i).getValueName().equals("Plus4")){
                flag = true;
            }
        }
        return !flag;
    }

    public Card shuffleIfPlusFour(Card card){
        while(card.getValueName().equals("Plus4")){
            deck.addCard(card);
            Collections.shuffle(deck.getCardsArray());
            card = deck.removeFirst();
        }
        return card;
    }

    public boolean topCardSpecial(){
        Card card = pileTop;
        return card.getValueName().equals("Plus2") || card.getValueName().equals("ChangeColor") || card.getValueName().equals("Reverse") || pileTop.getValueName().equals("Skip");
    }

    public Deck getDeck() {
        return deck;
    }

    public Pile getPile() {
        return pile;
    }

    public Card getPileTop(){
        return pile.getFirst();
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

    public int getDeckSize(){
        return deck.arraySize();
    }

    public boolean canUseCards() {
        return canUseCards;
    }

    public void setCanUseCards(boolean bool){
        canUseCards = bool;
    }

    public void shufflePile(){
        pile.shuffleHand();
        deck.setCardsArray(pile.getCardsArray());
        while (pile.arraySize() > 0){
            pile.removeFirst();
        }
    }

    public void addPileTop(Card card){
        pile.addCard(card);
    }

    public Card removePileTop(){
        return pile.removeFirst();
    }
}
