package hu.dushu.developers.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by renfeng on 7/11/15.
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";

    /*
     * If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 2;

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + MovieContract.MovieEntity.TABLE_NAME + " (" +
                /*
                 * TODO what will _ID be without AUTOINCREMENT?
                 */
//                MovieContract.MovieEntity._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieEntity._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.MovieEntity.ID_COLUMN + " INTEGER UNIQUE NOT NULL, " +
                MovieContract.MovieEntity.POSTER_COLUMN + " TEXT NOT NULL, " +
                MovieContract.MovieEntity.TITLE_COLUMN + " TEXT NOT NULL, " +
                MovieContract.MovieEntity.POPULARITY_COLUMN + " REAL NOT NULL, " +
                MovieContract.MovieEntity.RATE_COLUMN + " REAL NOT NULL, " +
                MovieContract.MovieEntity.RELEASE_COLUMN + " TEXT NOT NULL, " +
                MovieContract.MovieEntity.PLOT_COLUMN + " TEXT NOT NULL, " +
                "UNIQUE (" + MovieContract.MovieEntity.ID_COLUMN + ") ON CONFLICT REPLACE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntity.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
