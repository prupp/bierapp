package ch.hslu.bierapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.InputStream;

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

        if(beer != null) {
            String imgUrl = beer.getImageLink();
            if(imgUrl != null && !imgUrl.trim().isEmpty()) {
                new DownloadImageTask(img).execute(beer.getImageLink());
            } else {
                img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_beer));
            }
            title.setText(beer.getTitle());
            brewery.setText(beer.getBrewery());
            origin.setText(beer.getOrigin());
            alcoholContent.setText(beer.getAlcoholContent() + " %");
            calories.setText(beer.getCalories() + " kcal");
            dateAdded.setText(beer.getDateAdded().toString());
            text.setText(beer.getText());
            rating.setRating((float) beer.getRating());
        } else {
            Log.w("BierApp:BeerDetail", "Beer was null");
        }
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
        switch (item.getItemId()) {
            case R.id.action_edit_beer:
                Intent intentEdit = new Intent(this, EditBeerActivity.class);
                intentEdit.putExtra(BeerListActivity.KEY_EXTRA_BEER_ID, beer.getId());
                startActivity(intentEdit);
                return true;
            case R.id.action_delete_beer:
                showDeleteConfirmDialog(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDeleteConfirmDialog(final Context context) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Löschen bestätigen");
        alert.setMessage("Sind Sie sicher, dass sie " + beer.getTitle() + " löschen möchten?");

        alert.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dbAdapter.deleteBeer(beer.getId());
                Intent intentList = new Intent(context, BeerListActivity.class);
                startActivity(intentList);
            }
        });

        alert.setNegativeButton("Nein", null);

        alert.show();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if(result != null) {
                bmImage.setImageBitmap(resize(result, 150, 150));
            }
        }

        private Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
            if (maxHeight > 0 && maxWidth > 0) {
                int width = image.getWidth();
                int height = image.getHeight();
                float ratioBitmap = (float) width / (float) height;
                float ratioMax = (float) maxWidth / (float) maxHeight;

                int finalWidth = maxWidth;
                int finalHeight = maxHeight;
                if (ratioMax > 1) {
                    finalWidth = (int) ((float)maxHeight * ratioBitmap);
                } else {
                    finalHeight = (int) ((float)maxWidth / ratioBitmap);
                }
                image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
                return image;
            } else {
                return image;
            }
        }
    }
}
