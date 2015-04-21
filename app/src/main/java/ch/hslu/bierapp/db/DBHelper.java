package ch.hslu.bierapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DBHelper to mange database instance.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(final Context context) {
        super(context, DBAdapter.DB_NAME, null, DBAdapter.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE tbl_beers (" +
                "id INTEGER PRIMARY KEY, " +
                "title TEXT not null, " +
                "brewery TEXT, " +
                "origin TEXT, " +
                "text TEXT, " +
                "image_link TEXT, " +
                "alcohol_content REAL, " +
                "rating REAL, " +
                "calories INTEGER, " +
                "added DATE not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //nothing to do here yet
    }
}
