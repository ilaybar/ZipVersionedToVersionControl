package ilay.bar.uno.Model;

import android.content.Context;
import android.util.Log;

import java.util.Random;

import ilay.bar.uno.Utils;

public class Card
{

    public enum Colors {blue, green, red, yellow, black};

    // properties
    private Colors color; //Blue, Green, Red, Yellow, Black (Special cards)

    private int value;  // 0-13 (10 - Plus2 | 11 - Reverse | 12 - Skip | 13 - Plus4 | 14 - ChangeColor)
    private boolean isFaceUp;
    private static final Random rnd = new Random();


    // public static final String[] SuitNames = {"Spade", "Heart", "Diamond", "Club"};

    public static final Colors[] ColorValues = Colors.values();
    // used to generate cards randomly
    // trick... https://stackoverflow.com/questions/1972392/pick-a-random-value-from-an-enum

    private static final String[] ValueNames = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "Plus2",
                                            "Reverse", "Skip", "Plus4", "ChangeColor"};

    private static final String[] ValueCodes = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                                            "11", "12", "13", "14"};
    private static final String Back = "uno_back_of_card"; // card's back image name

    // Constructor
    public Card(Colors _color, int _value)
    {
        this.color = _color;
        this.value = _value;
        this.isFaceUp = false;
    }

    public Card(Colors _color, int _value, boolean _isFaceUp)
    {
        this.color = _color;
        this.value = _value;
        this.isFaceUp = _isFaceUp;
    }

    public Card()
    {
        this.value = rnd.nextInt(13) + 2; // values 2..14
        int suitIndex = rnd.nextInt(ColorValues.length);
        this.color = ColorValues[suitIndex];
        this.isFaceUp = false;
        Log.d("Cards - Constructor", "value = " + value + " suit = " + color);
    }

    public int calcFaceDrawableId(Context _context)
    {
        String fileName = getCardImageName();
        Log.d("Cards - calcFaceDrawableId", " code = " + fileName);
        int id = Utils.string2drawbleId(_context, fileName);
        Log.d("Cards - calcFaceDrawableId", "faceDrawableId = " + id);
        return id;
    }

    // Getters (note that setters are not necessary here)
    public Colors getColor()
    {
        return color;
    }

    public int getValue()
    {
        return value;
    }

    public boolean getIsFaceUp()
    {
        return isFaceUp;
    }

    public void setFaceUp(boolean faceUp) {
        isFaceUp = faceUp;
    }

    // Calculate and return the card's image name e.g. h5, da etc.
    public String getCardImageName()
    {
        String result;
        if (isFaceUp)
            result = color.toString().toLowerCase() + "_" + ValueCodes[value];
        else
            result = Back;
        return result;
    }

    public void turn()
    {
        isFaceUp = ! isFaceUp;
    }

    public String getValueName()
    {
        return ValueNames[this.value];
    }

    @Override
    public String toString()
    {
        return "Card{ " +
                color +
                ", " + ValueNames[value] +
                ", " + (isFaceUp?"Face Up":"Face Down") +
                " }";
    }
}
