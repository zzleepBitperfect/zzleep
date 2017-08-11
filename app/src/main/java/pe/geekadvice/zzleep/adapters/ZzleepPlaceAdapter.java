package pe.geekadvice.zzleep.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import pe.geekadvice.zzleep.R;

/**
 * Created by gerson on 18/12/16.
 */

public class ZzleepPlaceAdapter extends ArrayAdapter<ZzleepPlace>
{


	public ZzleepPlaceAdapter(Context context, int resource, List<ZzleepPlace> objects)
	{
		super(context, resource, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		ZzleepPlace place = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.place_item, parent, false);
		}
		// Lookup view for data population
		TextView tvName = (TextView) convertView.findViewById(R.id.placeName);
		TextView tvHome = (TextView) convertView.findViewById(R.id.placeAddress);

		// Populate the data into the template view using the data object
		tvName.setText(place.getName());
		tvHome.setText(place.getAddress());
		// Return the completed view to render on screen


		return convertView;
	}
}
