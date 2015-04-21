package ch.hslu.bierapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DBAdapter {
    public static final String DB_NAME = "bierapp";
    public static final int DB_VERSION = 1;

    public static final String TABLE_BEERS = "tbl_beers";
    public static final String FIELD_ID = "id";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_BREWERY = "brewery";
    public static final String FIELD_ORIGIN = "origin";
    public static final String FIELD_TEXT = "text";
    public static final String FIELD_IMAGE_LINK = "image_link";
    public static final String FIELD_DATE_ADDED = "added";
    public static final String FIELD_ALCOHOL_CONTENT = "alcohol_content";
    public static final String FIELD_RATING = "rating";
    public static final String FIELD_CALORIES = "calories";
    /**
     * Array containing all fields (FIELD_*)
     */
    public static final String[] FIELDS_ALL = {FIELD_ID, FIELD_TITLE, FIELD_BREWERY, FIELD_ORIGIN, FIELD_TEXT, FIELD_IMAGE_LINK, FIELD_DATE_ADDED, FIELD_ALCOHOL_CONTENT, FIELD_RATING, FIELD_CALORIES};

    private final DBHelper dbHelper;
    private SQLiteDatabase db;

    public DBAdapter(final Context context) {
        dbHelper = new DBHelper(context);
    }

    /**
     * Open a writable database instance
     */
    public void open() {
        Log.i("BierApp:DBAdapter", "Database open()");
        if(db == null || !db.isOpen()) {
            db = dbHelper.getWritableDatabase();
        }
    }

    /**
     * Close the databse
     */
    public void close() {
        Log.i("BierApp:DBAdapter", "Database close()");
        dbHelper.close();
    }

    /**
     * Insert a new beer in the database
     * @param beer The {@link ch.hslu.bierapp.db.Beer} to insert
     * @return True on success, false otherwise
     */
    public boolean insertBeer(final Beer beer) {
        try {
            final ContentValues values = new ContentValues();
            values.put(FIELD_TITLE, beer.getTitle());
            values.put(FIELD_BREWERY, beer.getBrewery());
            values.put(FIELD_ORIGIN, beer.getOrigin());
            values.put(FIELD_TEXT, beer.getText());
            values.put(FIELD_IMAGE_LINK, beer.getImageLink());
            values.put(FIELD_DATE_ADDED, beer.getDateAdded().getTime());
            values.put(FIELD_ALCOHOL_CONTENT, beer.getAlcoholContent());
            values.put(FIELD_RATING, beer.getRating());
            values.put(FIELD_CALORIES, beer.getCalories());
            final long id = db.insert(TABLE_BEERS, null, values);
            beer.setId(id);
            return true;
        } catch(Exception e) {
            Log.w("BierApp:DBAdapter", "Failed to insert beer " + beer.getTitle() + " into db.");
            return false;
        }
    }

    /**
     * Get a cursor of all beers in the database
     * @return List of {@link ch.hslu.bierapp.db.Beer}s
     */
    public Cursor getBeersCursor() {
        final Cursor result = db.query(TABLE_BEERS, FIELDS_ALL, null, null, null, null, null);
        return result;
    }

    /**
     * Get a list of all beers in the database
     * @return List of {@link ch.hslu.bierapp.db.Beer}s
     */
    public List<Beer> getBeers() {
        final List<Beer> allBeers = new ArrayList<>();
        final Cursor result = getBeersCursor();
        while(result.moveToNext()) {
            allBeers.add(getBeerFromCursor(result));
        }
        result.close();
        return allBeers;
    }

    /**
     * Get a beer with a specific id.
     * @param id The id of the beer
     * @return The {@link ch.hslu.bierapp.db.Beer} with the given id. Null if not found.
     */
    public Beer getBeer(final long id) {
        Beer beer = null;
        final Cursor result = db.query(TABLE_BEERS, FIELDS_ALL, FIELD_ID + " = " + id, null, null, null, null);
        if(result.moveToFirst()) {
            beer = getBeerFromCursor(result);
        }
        result.close();
        return beer;
    }

    /**
     * Update a beer in the database
     * @param beer The {@link ch.hslu.bierapp.db.Beer} to update
     * @return True on success, false otherwise
     */
    public boolean updateBeer(final Beer beer) {
        final ContentValues values = new ContentValues();
        values.put(FIELD_TITLE, beer.getTitle());
        values.put(FIELD_BREWERY, beer.getBrewery());
        values.put(FIELD_ORIGIN, beer.getOrigin());
        values.put(FIELD_TEXT, beer.getText());
        values.put(FIELD_IMAGE_LINK, beer.getImageLink());
        values.put(FIELD_DATE_ADDED, beer.getDateAdded().getTime());
        values.put(FIELD_ALCOHOL_CONTENT, beer.getAlcoholContent());
        values.put(FIELD_RATING, beer.getRating());
        values.put(FIELD_CALORIES, beer.getCalories());
        return db.update(TABLE_BEERS, values, FIELD_ID + "=" + beer.getId(), null) > 0;
    }

    /**
     * Delete a beer from the database
     * @param id The id of the {@link ch.hslu.bierapp.db.Beer} to delete
     * @return True on success, false otherwiese
     */
    public boolean deleteBeer(final long id) {
        return db.delete(TABLE_BEERS, FIELD_ID + "=" + id, null) > 0;
    }

    /**
     * Extract the beer from the cursor.
     * @param cursor The cursor, which will not be modified or moved.
     *               Order of the columns: FIELD_ID, FIELD_TITLE, FIELD_BREWERY, FIELD_ORIGIN, FIELD_TEXT, FIELD_IMAGE_LINK, FIELD_DATE_ADDED, FIELD_ALCOHOL_CONTENT, FIELD_RATING, FIELD_CALORIES
     * @return The beer at the current cursors position. Null on error.
     */
    private Beer getBeerFromCursor(final Cursor cursor) {
        try {
            final Beer beer = new Beer();
            beer.setId(cursor.getLong(0));
            beer.setTitle(cursor.getString(1));
            beer.setBrewery(cursor.getString(2));
            beer.setOrigin(cursor.getString(3));
            beer.setText(cursor.getString(4));
            beer.setImageLink(cursor.getString(5));
            beer.setDateAdded(new Date(cursor.getLong(6)));
            beer.setAlcoholContent(cursor.getDouble(7));
            beer.setRating(cursor.getDouble(8));
            beer.setCalories(cursor.getInt(9));
            return beer;
        } catch(Exception e) {
            Log.w("BierApp:DBAdapter", "Failed to extract beer from cursor. Returning null");
            return null;
        }
    }
}
