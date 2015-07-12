package hu.dushu.developers.popularmovies.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by renfeng on 7/11/15.
 */
public class MovieSyncService extends Service {

    private static final Object lock = new Object();
    private static MovieSyncAdapter adapter;

    @Override
    public void onCreate() {

        synchronized (lock) {
            if (getAdapter() == null) {
                setAdapter(new MovieSyncAdapter(getApplicationContext(), true));
            }
        }

        return;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return getAdapter().getSyncAdapterBinder();
    }

    public static MovieSyncAdapter getAdapter() {
        return adapter;
    }

    public static void setAdapter(MovieSyncAdapter adapter) {
        MovieSyncService.adapter = adapter;
    }
}
