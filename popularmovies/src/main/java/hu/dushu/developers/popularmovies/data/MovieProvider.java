package hu.dushu.developers.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import hu.dushu.developers.popularmovies.R;

/**
 * Created by renfeng on 7/11/15.
 */
public class MovieProvider extends ContentProvider {

    private MovieDBHelper helper;

    // The URI Matcher used by this content provider.
    private UriMatcher uriMatcher;
    private static final int MOVIE = 100;
    private static final int MOVIE_ID = 101;

    private UriMatcher buildUriMatcher(Context context) {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = context.getString(R.string.content_authority);

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, MovieContract.MOVIE_PATH, MOVIE);
        matcher.addURI(authority, MovieContract.MOVIE_PATH + "/#", MOVIE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        setHelper(new MovieDBHelper(getContext()));
        setUriMatcher(buildUriMatcher(getContext()));
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (getUriMatcher().match(uri)) {
            // "movie/#"
            case MOVIE_ID: {
                long id = ContentUris.parseId(uri);
                retCursor = helper.getReadableDatabase().query(
                        MovieContract.MovieEntity.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntity._ID + " = ?",
                        new String[]{id + ""},
                        null,
                        null,
                        sortOrder);
                break;
            }
            // "movie"
            case MOVIE: {
                retCursor = getHelper().getReadableDatabase().query(
                        MovieContract.MovieEntity.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {

        String type;

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = getUriMatcher().match(uri);

        switch (match) {
            case MOVIE: {
                type = "vnd.android.cursor.dir/" +
                        getContext().getString(R.string.content_authority) + "/" +
                        MovieContract.MOVIE_PATH;
                break;
            }
            case MOVIE_ID: {
                type = "vnd.android.cursor.item/" +
                        getContext().getString(R.string.content_authority) + "/" +
                        MovieContract.MOVIE_PATH;
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        return type;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = getHelper().getWritableDatabase();
        final int match = getUriMatcher().match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE: {
                long _id = db.insert(MovieContract.MovieEntity.TABLE_NAME, null, values);
                if (_id > 0) {
                    String authority = getContext().getString(R.string.content_authority);
                    returnUri = ContentUris.withAppendedId(Uri.parse("content://" + authority)
                            .buildUpon().appendPath(MovieContract.MOVIE_PATH).build(), _id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = helper.getWritableDatabase();
        int rowsDeleted;
        switch (getUriMatcher().match(uri)) {
            case MOVIE:
                rowsDeleted = db.delete(
                        MovieContract.MovieEntity.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /*
         * TODO remove selection == null
         */
        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = helper.getWritableDatabase();
        int rowsUpdated;
        switch (getUriMatcher().match(uri)) {
            case MOVIE:
                rowsUpdated = db.update(
                        MovieContract.MovieEntity.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /*
         * TODO remove selection == null
         */
        // Because a null updates all rows
        if (selection == null || rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    public MovieDBHelper getHelper() {
        return helper;
    }

    public void setHelper(MovieDBHelper helper) {
        this.helper = helper;
    }

    public UriMatcher getUriMatcher() {
        return uriMatcher;
    }

    public void setUriMatcher(UriMatcher uriMatcher) {
        this.uriMatcher = uriMatcher;
    }
}
