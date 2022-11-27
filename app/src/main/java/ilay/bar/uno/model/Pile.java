package ilay.bar.uno.Model;

import java.util.ArrayList;

public class Pile extends Hand{

    public Pile(){
        super();
    }

    public ArrayList<Card> getPileArray(){
        return super.cardsArray;
    }

    public Card getFirst(){
        return super.cardsArray.get(pileSize() - 1);
    }

    public void addCard(Card card){
        super.addCard(card);
    }

    public int pileSize(){
        return super.handSize();
    }

    public Card removeFirst(){
        return super.removeFirst();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
