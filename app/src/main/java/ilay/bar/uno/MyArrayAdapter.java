package ilay.bar.uno;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

// Based on code from Vogella tutorial
// + http://theopentutorials.com/tutorials/android/listview/android-custom-listview-with-image-and-text-using-arrayadapter/
public class MyArrayAdapter extends ArrayAdapter<Player>
{
	// This array adapter handles *both* array & arraylist
	// This is done for demo purposes only!
	// Normally we will need just one of them
	private final Context context;
	private final Player[] valuesArr;
	private final ArrayList<Player> valuesList;

	TextView tvFirstName, tvPlayed, tvWon, tvLost;
	ImageView imgPhoto;

	// Constructor for an array
	/**
	 * Constructor for an array
	 * 
	 * @param _context - the context
	 * @param _values - the array
	 */
	public MyArrayAdapter(Context _context, Player[] _values)
	{
		super(_context, R.layout.player_adapter, _values);
		this.context = _context;
		this.valuesArr = _values;
		this.valuesList = null;
	}

	// Constructor for an ArrayList
	public MyArrayAdapter(Context _context, ArrayList<Player> _valuesList)
	{
		super(_context, R.layout.player_adapter, _valuesList);
		this.context = _context;
		this.valuesList = _valuesList;
		this.valuesArr = null;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.player_adapter, parent, false);

		tvFirstName = (TextView) rowView.findViewById(R.id.tvFirstName);
		tvPlayed = (TextView) rowView.findViewById(R.id.tvPlayed);
		tvWon = (TextView) rowView.findViewById(R.id.tvWon);
		tvLost = (TextView) rowView.findViewById(R.id.tvLost);

		ImageView imgPhoto = (ImageView) rowView.findViewById(R.id.imgPhoto);

		if (valuesList == null)
			handleArray(position);
		else
			handleArrayList(position);

		return rowView;
	}


	private void handleArray(int position)
	{
		tvFirstName.setText(valuesArr[position].getName());
		tvPlayed.setText("Played: " + valuesArr[position].getPlayed());
		tvWon.setText("Won: " + valuesArr[position].getWins());
		tvLost.setText("Lost: " + valuesArr[position].getLosses());
		// imgPhoto.setImageResource(valuesArr[position].getDrawableId());
	}
	
	private void handleArrayList(int position)
	{
		tvFirstName.setText(valuesList.get(position).getName());
		tvPlayed.setText("Played: " + valuesList.get(position).getPlayed());
		tvWon.setText("Won: " + valuesList.get(position).getWins());
		tvLost.setText("Lost: " + valuesList.get(position).getLosses());
		// imgPhoto.setImageResource(players.get(position).getDrawableId());
	}

}
