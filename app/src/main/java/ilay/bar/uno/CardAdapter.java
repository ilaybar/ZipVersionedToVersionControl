package ilay.bar.uno;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ilay.bar.uno.Model.Card;

// Source: https://www.freshbytelabs.com/2018/12/android-recyclerview-with-cardview.html
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>
{
	// This array adapter handles *both* array & arraylist
	// This is done for demo purposes only!
	// Normally we will need just one of them
	private Context context;
	private ArrayList<Card> valuesList = new ArrayList<Card>();;


	// Constructor for an ArrayList
	public CardAdapter(Context _context, ArrayList<Card> _valuesList)
	{
		// super(_context, R.layout.person_adapter, _valuesList);
		this.context = _context;
		this.valuesList = _valuesList;
	}


	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_adapter, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position)
	{
		Card current = valuesList.get(position);
		final int DrawableId = current.calcFaceDrawableId(context);
		holder.imgPhoto.setImageResource(DrawableId);
	}

	@Override
	public int getItemCount()
	{
		return valuesList.size();
	}


	public class ViewHolder extends RecyclerView.ViewHolder{
		private ImageView imgPhoto;

		public ViewHolder(View rowView) {
			super(rowView);
			imgPhoto = (ImageView) rowView.findViewById(R.id.imgPhoto);
		}
	}
}
