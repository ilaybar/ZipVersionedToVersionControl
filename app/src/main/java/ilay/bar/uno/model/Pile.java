package ilay.bar.uno.Model;

import java.util.ArrayList;

public class Pile extends Hand{

    public Pile(){
        super();
    }

    public Card getFirst(){
        return super.cardsArray.get(super.arraySize() - 1);
    }

}
