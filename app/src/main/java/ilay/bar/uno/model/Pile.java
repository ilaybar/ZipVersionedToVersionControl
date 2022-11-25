package ilay.bar.uno.Model;

import java.util.ArrayList;

public class Pile {

    protected ArrayList<Card> pile;
    // protected Card pileTop = getFirst();
    // TODO: Think if i can use a pileTop variable that is always equal to the top

    public Pile(){
        this.pile = new ArrayList<Card>();
    }

    public ArrayList<Card> getPileArray(){
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

    public Card removeFirst(){
        return pile.remove(pileSize() - 1);
    }


}
