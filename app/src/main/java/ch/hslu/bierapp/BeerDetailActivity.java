package ch.hslu.bierapp;

import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import ch.hslu.bierapp.common.Beer;
import ch.hslu.bierapp.db.DBAdapter;


public class BeerDetailActivity extends ActionBarActivity {
    private DBAdapter dbAdapter;
    private Beer beer;

    private void updateUI() {
        ImageView img = (ImageView)findViewById(R.id.imageView_detail_img);
        TextView title = (TextView)findViewById(R.id.textView_detail_title);
        TextView brewery = (TextView)findViewById(R.id.textView_detail_brewery);
        TextView origin = (TextView)findViewById(R.id.textView_detail_origin);
        TextView alcoholContent = (TextView)findViewById(R.id.textView_detail_alcoholContent);
        TextView calories = (TextView)findViewById(R.id.textView_detail_calories);
        TextView dateAdded = (TextView)findViewById(R.id.textView_detail_dateAdded);
        TextView text = (TextView)findViewById(R.id.textView_detail_text);
        RatingBar rating = (RatingBar)findViewById(R.id.ratingBar_detail);

        img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_beer));
        title.setText(beer.getTitle());
        brewery.setText(beer.getBrewery());
        origin.setText(beer.getOrigin());
        alcoholContent.setText(beer.getAlcoholContent() + " %");
        calories.setText(beer.getCalories() + " kcal");
        dateAdded.setText(beer.getDateAdded().toString());
        text.setText(beer.getText());
        rating.setRating((float)beer.getRating());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_detail);

        dbAdapter = new DBAdapter(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        dbAdapter.open();
        long beerId = getIntent().getExtras().getLong(BeerListActivity.KEY_EXTRA_BEER_ID);
        beer = dbAdapter.getBeer(beerId);
        updateUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbAdapter.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_beer_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
