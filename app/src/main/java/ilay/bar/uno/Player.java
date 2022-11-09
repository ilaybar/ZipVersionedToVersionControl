package ilay.bar.uno;

import android.content.Context;

public class Player
{
	private String firstName, lastName;
	// private Bitmap photo;
	private int drawableId;

	public Player(String _firstName, String _lastName, int _drawableId)
	{
		this.firstName = _firstName;
		this.lastName = _lastName;
		this.drawableId = _drawableId;
	}

	public Player(String _firstName, String _lastName, Context context)
	{
		this.firstName = _firstName;
		this.lastName = _lastName;
		this.drawableId = Utils.string2drawbleId(context, firstName.toLowerCase());
	}

	public String getFirstName()
	{
		return firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public int getDrawableId()
	{
		return drawableId;
	}


	@Override
	public String toString()
	{
		return firstName + ' ' + lastName;
	}
}
