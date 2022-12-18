package ilay.bar.uno;

import android.content.Context;

public class Player
{
	private String name;
	private int wins, losses, played;
	// private Bitmap photo;
	private int drawableId;

	public Player(String _name, int _drawableId)
	{
		this.name = _name;
		this.played = 0;
		this.wins = 0;
		this.losses = 0;
		this.drawableId = _drawableId;
	}

	public Player(String _name)
	{
		this.played = 0;
		this.wins = 0;
		this.losses = 0;
		this.name = _name;
	}

	public String getName()
	{
		return name;
	}

	public int getDrawableId()
	{
		return drawableId;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getLosses() {
		return losses;
	}

	public void setLosses(int losses) {
		this.losses = losses;
	}

	public int getPlayed() {
		return played;
	}

	public void setPlayed(int played) {
		this.played = played;
	}

	public void addPlayed(){
		this.played++;
	}

	public void addWin(){
		this.wins++;
	}

	public void addLose(){
		this.losses++;
	}

	@Override
	public String toString()
	{
		return name;
	}
}
