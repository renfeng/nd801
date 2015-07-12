package hu.dushu.developers.popularmovies.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import hu.dushu.developers.popularmovies.R;
import hu.dushu.developers.popularmovies.data.MovieContract;

/**
 * Created by renfeng on 7/11/15.
 */
public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String LOG_TAG = MovieSyncAdapter.class.getSimpleName();

    /*
     * Interval at which to sync with the weather, in seconds.
     * 60 seconds (1 minute) * 180 = 3 hours
     */
    /*
     * TODO change to 3 hours
     */
//    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_INTERVAL = 30;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL;

    public MovieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        /*
         * use google http client with url connection transport, and jackson json factory
         */
        GenericUrl url = new GenericUrl("http://api.themoviedb.org/3/discover/movie" +
                "?sort_by=popularity.desc" +
                "&api_key=" + getContext().getString(R.string.api_key));

        HttpTransport transport = new NetHttpTransport();
        HttpRequestFactory factory = transport.createRequestFactory(new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) throws IOException {
                request.setParser(new JsonObjectParser(new JacksonFactory()));
            }
        });
//        HttpRequestFactory factory = transport.createRequestFactory(new HttpRequestInitializer() {
//            @Override
//            public void initialize(HttpRequest request) throws IOException {
//                request.setParser(new JsonObjectParser.Builder(new JacksonFactory())
//                        .setWrapperKeys(Arrays.asList("results")).build());
//            }
//        });

        try {
            HttpRequest request = factory.buildGetRequest(url);
            HttpResponse response = request.execute();
            Movie movie = response.parseAs(Movie.class);

            ArrayList<ContentValues> list = new ArrayList<>();
            for (Result r : movie.getResults()) {
                ContentValues values = new ContentValues();
                values.put(MovieContract.MovieEntity.POSTER_COLUMN, r.getPosterPath());
                values.put(MovieContract.MovieEntity.TITLE_COLUMN, r.getTitle());
                values.put(MovieContract.MovieEntity.POPULARITY_COLUMN, r.getPopularity());
                values.put(MovieContract.MovieEntity.RELEASE_COLUMN, r.getReleaseDate());
                values.put(MovieContract.MovieEntity.ID_COLUMN, r.getId());
                list.add(values);
            }

            getContext().getContentResolver().bulkInsert(
                    MovieContract.MovieEntity.getContentUri(getContext()),
                    list.toArray(new ContentValues[list.size()]));

//            Uri uri = Uri.parse("http://api.themoviedb.org/3/discover/movie");
//            Uri.Builder ub = uri.buildUpon();
//            ub.appendQueryParameter("sort_by", "popularity.desc");
//            ub.appendQueryParameter("api_key", getContext().getString(R.string.api_key));
//            URL url = new URL(ub.build().toString());
//
//            // Create the request to OpenWeatherMap, and open the connection
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//            try {
//                // Read the input stream into a String
//                InputStream inputStream = urlConnection.getInputStream();
//                StringBuffer buffer = new StringBuffer();
//                if (inputStream != null) {
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//                    try {
//                        String line;
//                        while ((line = reader.readLine()) != null) {
//                            // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
//                            // But it does make debugging a *lot* easier if you print out the completed
//                            // buffer for debugging.
//                            buffer.append(line + "\n");
//                        }
//
//                        if (buffer.length() > 0) {
//                            String jsonStr = buffer.toString();
//                            Log.d(LOG_TAG, jsonStr);
//
//                            try {
//                                getMovieDataFromJson(jsonStr);
//                            } catch (JSONException e) {
//                                Log.e(LOG_TAG, e.getMessage(), e);
//                            }
//                        }
//                    } finally {
//                        reader.close();
//                    }
//                }
//            } finally {
//                urlConnection.disconnect();
//            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        }

        /*
         * TODO remove old movies
         */
    }


    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        MovieSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(
                newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            /*
             * http://forums.udacity.com/questions/100242288/error-unparceling-bundle#ud853
             */
            SyncRequest.Builder builder = new SyncRequest.Builder();
            Bundle extras = new Bundle();
            builder.setExtras(extras);

            // we can enable inexact timers in our periodic sync
            SyncRequest request = builder.
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }

        return newAccount;
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}
