package hu.dushu.developers.popularmovies;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import hu.dushu.developers.popularmovies.data.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MOVIE_LOADER = 0;

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntity.ID_COLUMN,
            MovieContract.MovieEntity.POSTER_COLUMN,
            MovieContract.MovieEntity.TITLE_COLUMN,
            /*
             * TODO comment out the following two columns if they are not used
             */
            MovieContract.MovieEntity.POPULARITY_COLUMN,
            MovieContract.MovieEntity.RATE_COLUMN,

            MovieContract.MovieEntity._ID
    };

    // These indices are tied to MOVIE_COLUMNS.  If MOVIE_COLUMNS changes, these
    // must change.
    public static final int ID_COLUMN = 0;
    public static final int POSTER_COLUMN = 1;
    public static final int TITLE_COLUMN = 2;
    public static final int POPULARITY_COLUMN = 3;
    public static final int RATE_COLUMN = 4;

    private static final String POSITION_KEY = "position";

    private MovieAdapter adapter;
    private int position;
    private GridView gridView;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        MovieAdapter adapter = new MovieAdapter(getActivity(), null, 0);
        setAdapter(adapter);

        GridView grid = (GridView) view.findViewById(R.id.movie_grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*
                 * must move cursor for two-panel layout
                 */
                Cursor cursor = getAdapter().getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    int movieId = cursor.getInt(ID_COLUMN);
                    Callback callback = (Callback) getActivity();
                    callback.onMovieSelected(movieId);
                }

                setPosition(position);
            }
        });
        setGridView(gridView);

        if (savedInstanceState != null && savedInstanceState.containsKey(POSITION_KEY)) {
            int position = savedInstanceState.getInt(POSITION_KEY);
            setPosition(position);
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        getLoaderManager().initLoader(MOVIE_LOADER, null, this);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {
            case MOVIE_LOADER: {

                return new CursorLoader(
                        getActivity(),
                        Uri.parse("content://" + getString(R.string.content_authority))
                                .buildUpon().appendPath(MovieContract.MOVIE_PATH).build(),
                        MOVIE_COLUMNS,
                        null,
                        null,
                        null);
            }
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (getGridView() != null) {
            getGridView().smoothScrollToPosition(getPosition());
        }

        getAdapter().swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        getAdapter().swapCursor(null);
    }

    public MovieAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(MovieAdapter adapter) {
        this.adapter = adapter;
    }

    public GridView getGridView() {
        return gridView;
    }

    public void setGridView(GridView gridView) {
        this.gridView = gridView;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * Callback for when an item has been selected.
         */
        void onMovieSelected(int movieId);
    }
}
