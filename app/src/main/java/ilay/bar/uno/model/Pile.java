package ilay.bar.uno.model;

import java.util.ArrayList;

public class Pile {

    protected ArrayList<Card> pile;

    public Pile(){
        this.pile = new ArrayList<Card>();
    }

    public ArrayList<Card> getPile(){
        return pile;
    }

    public Card getFirst(){
        return pile.get(pileSize() - 1);
    }

    public void addCard(Card card){
        pile.add(card);
    }

    public int pileSize(){
        return pile.size();
    }

}
