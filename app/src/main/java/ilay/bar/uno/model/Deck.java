package ilay.bar.uno.Model;

import java.util.ArrayList;
import java.util.Collections;

public class Deck extends Hand{


    public Deck(){
        super();
        initData();
    }

    // Initialize Deck
    public void initData(){
        super.cardsArray = new ArrayList<Card>();
        for (int value = 0; value <=12; value++){
            for (Card.Colors color:Card.ColorValues){
                if(color != Card.Colors.black){
                    super.addCard(new Card(color, value, true));
                }
                if(color != Card.Colors.black && value > 0){
                    super.addCard(new Card(color, value, true));
                }
            }
        }
        for(int value = 13; value <=14; value++){
            for(int i = 0; i<=3; i++){
                super.addCard(new Card(Card.Colors.black, value, true));
            }
        }
        Collections.shuffle(super.cardsArray);
    }

    // Get 7 Cards Hand From Deck
    public ArrayList<Card> getUserCards(){
        ArrayList<Card> hand = new ArrayList<Card>();
        for(int i = 0; i<=6; i++){
            hand.add(super.cardsArray.remove(0));
        }
        return hand;
    }

    // Remove First Card From Deck
    public Card removeFirst(){
        return super.removeFirst();
    }

    public void addCard(Card card){
        super.addCard(card);
    }

    public ArrayList<Card> deckToArrayList(){
        return super.cardsArray;
    }
}
