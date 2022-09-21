package ilay.bar.uno;

public class GameManager {

    protected static String GameMode, Player1Name, Player2Name;

    public GameManager(){
        GameMode = "";
        Player1Name = "";
        Player2Name = "";
    }

    public static void setGameMode(String gameMode) {
        GameMode = gameMode;
    }

    public static void setPlayer1Name(String player1Name) {
        Player1Name = player1Name;
    }

    public static void setPlayer2Name(String player2Name) {
        Player2Name = player2Name;
    }
}
