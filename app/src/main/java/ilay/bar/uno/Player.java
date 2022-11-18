package ilay.bar.uno;

import android.content.Context;

public class Player
{
	private String name;
	// private Bitmap photo;
	private int drawableId;

	public Player(String _firstName, int _drawableId)
	{
		this.name = _firstName;
		this.drawableId = _drawableId;
	}

	public Player(String _name, Context context)
	{
		this.name = _name;
		this.drawableId = Utils.string2drawbleId(context, name.toLowerCase());
	}

	public String getName()
	{
		return name;
	}


	public int getDrawableId()
	{
		return drawableId;
	}


	@Override
	public String toString()
	{
		return name;
	}
}
