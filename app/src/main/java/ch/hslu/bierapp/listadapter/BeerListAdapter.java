package ch.hslu.bierapp.listadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import ch.hslu.bierapp.R;
import ch.hslu.bierapp.common.Beer;

public class BeerListAdapter extends ArrayAdapter<Beer> {

    public BeerListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public BeerListAdapter(Context context, int resource, List<Beer> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.beer_list_row, null);

        }

        Beer beer = getItem(position);

        if (beer != null) {

            TextView beerName = (TextView) v.findViewById(R.id.beer_name);
            RatingBar ratingBar = (RatingBar) v.findViewById(R.id.beer_rating);

            beerName.setText(beer.getTitle());
            ratingBar.setRating((float) beer.getRating());
        }

        return v;

    }
}
