package ilay.bar.uno.Model;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    protected ArrayList<Card> deck;

    public Deck(){
        initData();
    }

    // Initialize Deck
    public void initData(){
        deck = new ArrayList<Card>();
        for (int value = 0; value <=12; value++){
            for (Card.Colors color:Card.ColorValues){
                if(color != Card.Colors.black){
                    deck.add(new Card(color, value, true));
                }
                if(color != Card.Colors.black && value > 0){
                    deck.add(new Card(color, value, true));
                }
            }
        }
        for(int value = 13; value <=14; value++){
            for(int i = 0; i<=3; i++){
                deck.add(new Card(Card.Colors.black, value, true));
            }
        }
        Collections.shuffle(deck);
    }

    // Get 7 Cards Hand From Deck
    public ArrayList<Card> getUserCards(){
        ArrayList<Card> hand = new ArrayList<Card>();
        for(int i = 0; i<=6; i++){
            hand.add(deck.remove(0));
        }
        return hand;
    }


    // Remove First Card From Deck
    public Card removeFirst(){
        return deck.remove(0);
    }

    public void addCard(Card card){
        deck.add(card);
    }

    public ArrayList<Card> deckToArrayList(){
        return deck;
    }
}
