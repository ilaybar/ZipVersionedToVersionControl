package ilay.bar.uno.model;

import java.util.ArrayList;

public class Hands {

    protected ArrayList<Card> player1Hand, player2Hand;

    public Hands(){
        this.player1Hand = new ArrayList<Card>();
        this.player2Hand = new ArrayList<Card>();
    }

    public ArrayList<Card> getPlayer1Hand(){
        return player1Hand;
    }

    public ArrayList<Card> getPlayer2Hand(){
        return player2Hand;
    }

    public void setPlayer1Hand(ArrayList<Card> array){
        this.player1Hand = array;
    }

    public void setPlayer2Hand(ArrayList<Card> array) {
        this.player2Hand = array;
    }
}
