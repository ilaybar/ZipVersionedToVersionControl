package ilay.bar.uno.Controller;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;

import ilay.bar.uno.Globals;
import ilay.bar.uno.Model.Card;
import ilay.bar.uno.Model.Deck;
import ilay.bar.uno.Model.Hand;
import ilay.bar.uno.Model.Pile;
import ilay.bar.uno.Player;
import ilay.bar.uno.PrefsUtils;
import ilay.bar.uno.View.GameActivity;
import android.content.SharedPreferences;

public class GameManager {

    public GameActivity unoUI; // a reference to the activity
    private Deck deck;
    private Pile pile;
    private Card pileTop;
    private boolean canUseCards;

    private Hand player1Hand, player2Hand; // both players hands
    private String player1Name, player2Name;
    private int player1Index, player2Index;

    private enum GameStatus {Player1, Player2}
    private GameStatus gameStatus;
    private enum PlusStatus {Plus2, Plus4, Plus6};
    private PlusStatus plusStatus;

    ArrayList<Player> players;

    public GameManager(GameActivity _unoUI){
        unoUI = _unoUI;
        init();
    }

    public void init(){
        deck = new Deck();
        pile = new Pile();

        player1Hand = new Hand();
        // player1Hand.setCardsArray(deck.getUserCards());
        // /*
        player1Hand.addCard(new Card(Card.Colors.green, 12, true));
        player1Hand.addCard(new Card(Card.Colors.red, 3, true));
        player1Hand.addCard(new Card(Card.Colors.red, 4, true));
        player1Hand.addCard(new Card(Card.Colors.black, 13, true));
        player1Hand.addCard(new Card(Card.Colors.black, 13, true));
        player1Hand.addCard(new Card(Card.Colors.blue, 7, true));
        player1Hand.addCard(new Card(Card.Colors.yellow, 2, true));
        // */

        player2Hand = new Hand();
        player2Hand.setCardsArray(deck.getUserCards());
        /*
        player2Hand.addCard(new Card(Card.Colors.red, 2, true));
        player2Hand.addCard(new Card(Card.Colors.blue, 5, true));
        player2Hand.addCard(new Card(Card.Colors.yellow, 1, true));
        player2Hand.addCard(new Card(Card.Colors.black, 14, true));
        player2Hand.addCard(new Card(Card.Colors.black, 13, true));
        player2Hand.addCard(new Card(Card.Colors.black, 13, true));
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
        updateGameStatus();
    }

    public void checkWin(){
        players.get(player1Index).addPlayed();
        players.get(player2Index).addPlayed();
        if(player1Hand.arraySize() == 0){
            players.get(player1Index).addWin();
            players.get(player2Index).addLose();
            unoUI.showWinDialog("Player1");
        }
        if(player2Hand.arraySize() == 0){
            players.get(player2Index).addWin();
            players.get(player1Index).addLose();
            unoUI.showWinDialog("Player2");
        }
    }

    public void useCard(int pos){
        String gameStatusSave = "", gameStatusOther = "";
        if(gameStatus.equals(GameStatus.Player1)){
            pile.addCard(player1Hand.removeSpecific(pos));
            unoUI.setMyCards(player1Hand.getCardsArray());
            gameStatusSave = GameStatus.Player2.toString();
            gameStatusOther = GameStatus.Player1.toString();
        }
        if(gameStatus.equals(GameStatus.Player2)){
            pile.addCard(player2Hand.removeSpecific(pos));
            unoUI.setMyCards(player2Hand.getCardsArray());
            gameStatusSave = GameStatus.Player1.toString();
            gameStatusOther = GameStatus.Player2.toString();
        }
        pileTop = pile.getFirst();
        unoUI.changePileImg(pileTop);
        String card = pileTop.getValueName();
        switch (card){
            case "ChangeColor":
                unoUI.showChangeColorDialog(gameStatus.toString()); // show dialog
                updateRecyclerViews();
                setGameStatus(gameStatusSave);
                updateGameStatus();
                break;
            case "Plus2":
                setGameStatus(gameStatusSave);
                hideBothHands();
                updateRecyclerViews();
                updateGameStatus();
                unoUI.showPlusTwoDialog(gameStatus.toString());
                topIsPlus2();
                break;
            case "Plus4":
                setGameStatus(gameStatusSave);
                unoUI.showPlusFourDialog(gameStatus.toString());
                setGameStatus(gameStatusOther);
                unoUI.showChangeColorDialog(gameStatus.toString());
                setGameStatus(gameStatusSave);
                topIsPlus4();
                break;
            case "Skip":
            case "Reverse":
                hideBothHands();
                unoUI.showContinueDialog(gameStatus.toString());
                handNoMove();
                break;
            default:
                setGameStatus(gameStatusSave);
                hideBothHands();
                updateRecyclerViews();
                updateGameStatus();
                unoUI.showContinueDialog(gameStatusSave);
                handNoMove();
        }
        checkWin();
    }

    public void takeCardFromDeck(Hand hand, int amount){
        while (amount > 0){
            hand.addCard(deck.removeFirst());
            amount--;
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
        if(gameStatus.equals(GameStatus.Player1)){
            unoUI.updateGamesStatusText(player1Name);
        }
        else{
            unoUI.updateGamesStatusText(player2Name);
        }
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

    public void removeSpecialsInPile(){ // Removes Colorful ChangeColor and Plus4
        ListIterator<Card> li = pile.getCardsArray().listIterator();
        while (li.hasNext())
        {
            Card c = li.next();
            if ((c.getValueName().equals("ChangeColor") || c.getValueName().equals("Plus4")) && !c.getColor().equals("Black")){
                li.remove();
            }
        }
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
        plusStatus = PlusStatus.Plus2;
        if(pile.getFirst().getValueName().equals("Plus2")){
            canUseCards = false;
            unoUI.takeTwoCards();
        }
        else{
            canUseCards = true;
        }
    }

    public void topIsPlus4(){
        plusStatus = PlusStatus.Plus4;
        if(pile.getFirst().getValueName().equals("Plus4")){
            canUseCards = false;
        }
        else{
            canUseCards = true;
        }
    }

    public void handNoMove(){
        if(gameStatus.equals(GameStatus.Player1)){
            if(!hasMove(player1Hand.getCardsArray())){
                unoUI.noCardsToPlay();
            }
        }
        else{
            if(!hasMove(player2Hand.getCardsArray())){
                unoUI.noCardsToPlay();
            }
        }
    }

    // Checks If A Hand Has A Card To Play
    public boolean hasMove(ArrayList<Card> array){
        boolean has = false;
        for (int i = 0; i < array.size(); i++){
            if(array.get(i).getColor().equals(pileTop.getColor()) || array.get(i).getValueName().equals(pileTop.getValueName()) || array.get(i).getValueName().equals("ChangeColor") || array.get(i).getValueName().equals("Plus4")){
                has = true;
            }
        }
        return has;
    }

    public boolean hadMove(ArrayList<Card> array){
        boolean has = false;
        int plus4Counter = 0;
        Card card = pile.getCardsArray().get(pile.arraySize() - 3);
        for (int i = 0; i < array.size(); i++){
            if(array.get(i).getColor().equals(card.getColor()) || array.get(i).getValueName().equals(card.getValueName()) || array.get(i).getValueName().equals("ChangeColor")){
                has = true;
            }
            else if(array.get(i).getValueName().equals("Plus4")){
                plus4Counter++;
            }
        }
        if(plus4Counter > 0){
            Log.d("Plus4Counter", "counter: " + plus4Counter);
            return false;
        }
        return has;
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

    public void setPlayer1Name(String _player1Name) {
        this.player1Name = _player1Name;
    }

    public void setPlayer2Name(String _player2Name) {
        this.player2Name = _player2Name;
    }

    public void setPlayer1Index(int player1Index) {
        this.player1Index = player1Index;
    }

    public void setPlayer2Index(int player2Index) {
        this.player2Index = player2Index;
    }

    public boolean canUseCards() {
        return canUseCards;
    }

    public void setCanUseCards(boolean bool){
        canUseCards = bool;
    }

    public String getPlayer1Name(){
        return player1Name;
    }

    public String getPlayer2Name(){
        return player2Name;
    }

    public void setPlayers(ArrayList<Player> _players){
        this.players = _players;
    }

    public String getPlusStatus() {
        return plusStatus.toString();
    }

    public void setPlusStatus(String str) {
        if(str.equals("Plus2")){
            plusStatus = PlusStatus.Plus2;
        }
        else if(str.equals("Plus4")){
            plusStatus = PlusStatus.Plus4;
        }
        else{
            plusStatus = PlusStatus.Plus6;
        }
    }

    public void shufflePile(){
        pile.shuffleHand();
    }

    public Card getAndRemovePileBottom(){ // Removes The Bottom
        return pile.getAndRemoveLast();
    }

    public void addCardToDeck(Card card){
        deck.addCard(card);
    }

    public void addPileTop(Card card){
        pile.addCard(card);
    }

    public Card removePileTop(){
        return pile.removeFirst();
    }

    public void playerChallenge(boolean playerChallenged, boolean hadMove){
        if(playerChallenged){
            if(hadMove){
                // Log.d("TAG", "playerChallenge: " + "INSIDE");
                hideBothHands();
                updateRecyclerViews();
                if(gameStatus.equals(GameStatus.Player1)){
                    gameStatus = GameStatus.Player2;
                }
                else{
                    gameStatus = GameStatus.Player1;
                }
                unoUI.showContinueDialog(gameStatus.toString());
                setPlusStatus("Plus4");
            }
            else{
                setPlusStatus("Plus6");
            }
        }
        else{
            setPlusStatus("Plus4");
        }
        unoUI.takeFourOrSixCards();
    }
}
