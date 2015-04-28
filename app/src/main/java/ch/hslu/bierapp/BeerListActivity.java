package ch.hslu.bierapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import ch.hslu.bierapp.common.Beer;
import ch.hslu.bierapp.db.DBAdapter;
import ch.hslu.bierapp.listadapter.BeerListAdapter;


public class BeerListActivity extends ActionBarActivity {
    public static final String KEY_EXTRA_BEER_ID = "beerId";
    private DBAdapter dbAdapter;

    private void showBeerDetailView(Beer beer) {
        Intent beerDetail = new Intent(this, BeerDetailActivity.class);
        beerDetail.putExtra(KEY_EXTRA_BEER_ID, beer.getId());
        startActivity(beerDetail);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_list);

        dbAdapter = new DBAdapter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbAdapter.open();

        List<Beer> beerList = dbAdapter.getBeers();

        ListView listView = (ListView) findViewById(R.id.beer_list);
        BeerListAdapter customAdapter = new BeerListAdapter(this, R.layout.beer_list_row, beerList);

        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Beer beer = (Beer) parent.getItemAtPosition(position);
                showBeerDetailView(beer);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbAdapter.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_beer_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_beer:
                Intent intent = new Intent(this, AddBeerActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
