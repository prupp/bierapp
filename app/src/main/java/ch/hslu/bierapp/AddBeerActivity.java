package ch.hslu.bierapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Date;
import java.util.List;

import ch.hslu.bierapp.common.Beer;
import ch.hslu.bierapp.db.DBAdapter;
import ch.hslu.bierapp.webservice.AsyncHTTPRequest;
import ch.hslu.bierapp.webservice.IAsyncHTTPRequest;
import ch.hslu.bierapp.webservice.JsonParser;


public class AddBeerActivity extends ActionBarActivity implements IAsyncHTTPRequest {
    private DBAdapter dbAdapter;

    public void addBeerToDB(View v) {
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

        this.showEnterNameDialog();

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

    private void callWebservice(String resource) {

        if(this.isConnectedToInternet()) {
            Log.d("DEBUG::connectionState ", "connected");
            AsyncHTTPRequest httpRequest = new AsyncHTTPRequest(this);
            httpRequest.execute(resource);
        } else {
            Log.d("DEBUG::connectionState ", "disconnected");
            showNoInternetConnectionDialog();
        }
    }

    private void showNoInternetConnectionDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Keine Internetverbindung verfügbar");
        alert.setMessage("Internetverbindung herstellen und erneut versuchen.");

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", null);

        alert.show();
    }

    private void showEnterNameDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Online Informationen laden");
        alert.setMessage("Durch Eingabe eines Biernamens werden Informationen aus dem Internet geladen.");

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                callWebservice(input.getText().toString());
            }
        });

        alert.setNegativeButton("Abbrechen", null);

        alert.show();
    }

    private void fillAddBeerTextFields(Beer beer) {
        EditText name = (EditText) findViewById(R.id.addBeer_editText_title);
        EditText brewery = (EditText) findViewById(R.id.addBeer_editText_brewery);
        EditText origin = (EditText) findViewById(R.id.addBeer_editText_origin);
        EditText alcoholContent = (EditText) findViewById(R.id.addBeer_editText_alcoholContent);
        EditText calories = (EditText) findViewById(R.id.addBeer_editText_calories);
        EditText description = (EditText) findViewById(R.id.addBeer_editText_text);

        name.setText(beer.getTitle());
        brewery.setText(beer.getBrewery());
        origin.setText(beer.getOrigin());
        alcoholContent.setText(Double.toString(beer.getAlcoholContent()));
        calories.setText(Integer.toString(beer.getCalories()));
        description.setText(beer.getText());
    }

    private void addOriginTextFields(String origin) {
        EditText originField = (EditText) findViewById(R.id.addBeer_editText_origin);
        originField.setText(origin);
    }

    private void showSelectBeerDialog(final List<Beer> beerList) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final CharSequence[] beerNames = new String[beerList.size()];

        for(int i = 0; i < beerList.size(); i++) {
            beerNames[i] = beerList.get(i).getTitle();
        }

        alert.setTitle("Bier wählen");
        alert.setItems(beerNames, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                callWebservice(beerList.get(item).getBeerRestUrl());
            }
        });

        alert.setPositiveButton("Neue Suche", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                showEnterNameDialog();
            }
        });

        alert.setNegativeButton("Abbrechen", null);

        alert.show();
    }

    private void showEmptyDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Kein Bier gefunden");
        alert.setMessage("Tippfehler?");

        alert.setPositiveButton("Neue Suche", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                showEnterNameDialog();
            }
        });

        alert.setNegativeButton("Abbrechen", null);

        alert.show();
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void webserviceResponse(String response) {
        Log.d("DEBUG::HTTPResponse ", response);

        if(JsonParser.isBrewery(response)) {
            this.addOriginTextFields(JsonParser.getOrigin(response));
        }
        else if(JsonParser.isSingleBeer(response)) {
            Beer beer = JsonParser.getBeerFromJson(response);
            this.fillAddBeerTextFields(beer);
            callWebservice(beer.getBreweryRestUrl());
        }
        else {
            List<Beer> beerList = JsonParser.getAllBeersFromJson(response);
            if(beerList.isEmpty()) {
                showEmptyDialog();
            }
            else  if (beerList.size() == 1) {
                callWebservice(beerList.get(0).getBeerRestUrl());
            }
            else {
                showSelectBeerDialog(beerList);
            }
        }
    }
}
