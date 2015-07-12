package hu.dushu.developers.popularmovies.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by renfeng on 7/11/15.
 */
public class MovieAuthenticatorService extends Service {

    private MovieAuthenticator authenticator;

    @Override
    public void onCreate() {
        setAuthenticator(new MovieAuthenticator(this));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return getAuthenticator().getIBinder();
    }

    public MovieAuthenticator getAuthenticator() {
        return authenticator;
    }

    public void setAuthenticator(MovieAuthenticator authenticator) {
        this.authenticator = authenticator;
    }
}
