package ilay.bar.uno.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class Hand {

    protected ArrayList<Card> cardsArray;

    public Hand(){
        this.cardsArray = new ArrayList<Card>();
    }

    public void cardsFaceDown(){
        for (Card card:cardsArray) {
            card.setFaceUp(false);
        }
    }

    public void cardsFaceUp(){
        for (Card card:cardsArray) {
            card.setFaceUp(true);
        }
    }

    public void addCard(Card card){
        cardsArray.add(card);
    }

    public int arraySize(){
        return cardsArray.size();
    }

    public ArrayList<Card> getCardsArray(){
        return cardsArray;
    }

    public void setCardsArray(ArrayList<Card> array){
        this.cardsArray = array;
    }

    public Card removeFirst(){
        return cardsArray.remove(arraySize() - 1);
    }

    public Card removeSpecific(int pos){
        Log.d("Game", "removeSpecificInPos: " + pos);
        return cardsArray.remove(pos);
    }

    public void shuffleHand(){
        Collections.shuffle(cardsArray);
    }

    @Override
    public String toString() {
        return "" + cardsArray + "";
    }
}
