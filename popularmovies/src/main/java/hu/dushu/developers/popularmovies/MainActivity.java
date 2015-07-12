package hu.dushu.developers.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import hu.dushu.developers.popularmovies.sync.MovieSyncAdapter;

public class MainActivity extends ActionBarActivity implements MainActivityFragment.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {

            Intent intent = getIntent();
            int movieId = intent.getIntExtra(Intent.EXTRA_TEXT, -1);
            if (movieId != -1) {
                onMovieSelected(movieId);
            }
        }

        MovieSyncAdapter.initializeSyncAdapter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieSelected(int movieId) {
        if (getResources().getBoolean(R.bool.two_pane_layout)) {
            DetailsActivityFragment detailFragment = new DetailsActivityFragment();

            Bundle args = new Bundle();
            args.putInt(DetailsActivity.MOVIE_KEY, movieId);
            detailFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.details_container, detailFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(DetailsActivity.MOVIE_KEY, movieId);
            startActivity(intent);
        }
    }
}
