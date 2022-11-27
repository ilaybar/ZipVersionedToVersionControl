package ilay.bar.uno.Model;

import java.util.ArrayList;

public class Hand {

    protected ArrayList<Card> cardsArray;

    public Hand(){
        this.cardsArray = new ArrayList<Card>();
    }

    public void addCard(Card card){
        cardsArray.add(card);
    }

    public int handSize(){
        return cardsArray.size();
    }

    public ArrayList<Card> getCardsArray(){
        return cardsArray;
    }

    public void setCardsArray(ArrayList<Card> array){
        this.cardsArray = array;
    }

    public Card removeFirst(){
        return cardsArray.remove(handSize() - 1);
    }

    @Override
    public String toString() {
        return "Hand{" +
                "cardsArray=" + cardsArray +
                '}';
    }
}
