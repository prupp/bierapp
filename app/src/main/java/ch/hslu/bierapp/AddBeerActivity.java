package ch.hslu.bierapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Date;

import ch.hslu.bierapp.db.Beer;
import ch.hslu.bierapp.db.DBAdapter;


public class AddBeerActivity extends ActionBarActivity {
    private DBAdapter dbAdapter;

    public void addBeer(View v) {
        EditText inputName = (EditText) findViewById(R.id.addBeer_editText_title);
        String beerName = inputName.getText().toString().trim();
        if(beerName.isEmpty()) {
            Toast.makeText(this, "Bitte Name eingeben!", Toast.LENGTH_LONG).show();
            return;
        }
        Beer beer = new Beer();
        beer.setTitle(beerName);
        beer.setDateAdded(new Date(System.currentTimeMillis()));
        if(dbAdapter.insertBeer(beer)) {
            Toast.makeText(this, beer.getTitle() + " wurde gespeichert", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Fehler beim Speichern!", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_beer);

        dbAdapter = new DBAdapter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbAdapter.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbAdapter.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_beer, menu);
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
