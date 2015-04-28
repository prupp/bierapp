package ch.hslu.bierapp;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;

import ch.hslu.bierapp.common.Beer;
import ch.hslu.bierapp.db.DBAdapter;


public class EditBeerActivity extends ActionBarActivity {
    private DBAdapter dbAdapter;
    private Beer beer;

    private void updateUI() {
        EditText title = (EditText)findViewById(R.id.editBeer_editText_title);
        EditText brewery = (EditText)findViewById(R.id.editBeer_editText_brewery);
        EditText origin = (EditText)findViewById(R.id.editBeer_editText_origin);
        EditText alcoholContent = (EditText)findViewById(R.id.editBeer_editText_alcoholContent);
        EditText calories = (EditText)findViewById(R.id.editBeer_editText_calories);
        EditText text = (EditText)findViewById(R.id.editBeer_editText_text);
        RatingBar rating = (RatingBar)findViewById(R.id.ratingBar_edit);


        if(beer != null) {
            title.setText(beer.getTitle());
            brewery.setText(beer.getBrewery());
            origin.setText(beer.getOrigin());
            alcoholContent.setText(String.valueOf(beer.getAlcoholContent()));
            calories.setText(String.valueOf(beer.getCalories()));
            text.setText(beer.getText());
            rating.setRating((float) beer.getRating());
        } else {
            Log.w("BierApp:EditBeer", "Beer was null");
        }
    }

    public void updateBeerInDB(View v) {
        EditText inputName = (EditText)findViewById(R.id.editBeer_editText_title);
        RatingBar rating = (RatingBar)findViewById(R.id.ratingBar_edit);
        EditText inputCalories = (EditText)findViewById(R.id.editBeer_editText_calories);
        EditText inputAlcohol = (EditText)findViewById(R.id.editBeer_editText_alcoholContent);
        EditText inputOrigin = (EditText)findViewById(R.id.editBeer_editText_origin);
        EditText inputBrewery = (EditText)findViewById(R.id.editBeer_editText_brewery);
        EditText inputDescription = (EditText)findViewById(R.id.editBeer_editText_text);
        String beerName = inputName.getText().toString().trim();
        if(beerName.isEmpty()) {
            Toast.makeText(this, "Bitte Name eingeben!", Toast.LENGTH_LONG).show();
            return;
        }
        beer.setTitle(beerName);
        beer.setDateAdded(new Date(System.currentTimeMillis()));
        beer.setRating(rating.getRating());
        beer.setCalories(Integer.parseInt(inputCalories.getText().toString().trim()));
        beer.setOrigin(inputOrigin.getText().toString().trim());
        beer.setBrewery(inputBrewery.getText().toString().trim());
        beer.setAlcoholContent(Double.parseDouble(inputAlcohol.getText().toString().trim()));
        beer.setText(inputDescription.getText().toString().trim());
        if(dbAdapter.updateBeer(beer)) {
            Toast.makeText(this, beer.getTitle() + " wurde aktualisiert", Toast.LENGTH_LONG).show();
            Intent detailIntent = new Intent(this, BeerDetailActivity.class);
            detailIntent.putExtra(BeerListActivity.KEY_EXTRA_BEER_ID, beer.getId());
            startActivity(detailIntent);
        } else {
            Toast.makeText(this, "Fehler beim Aktualisieren!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_beer);
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
        getMenuInflater().inflate(R.menu.menu_edit_beer, menu);
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
