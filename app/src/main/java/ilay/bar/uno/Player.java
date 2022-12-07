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
		this.drawableId = _drawableId;
	}

	public Player(String _name, Context context)
	{
		this.name = _name;
		// this.drawableId = Utils.string2drawbleId(context, name.toLowerCase());
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

	@Override
	public String toString()
	{
		return name;
	}
}
