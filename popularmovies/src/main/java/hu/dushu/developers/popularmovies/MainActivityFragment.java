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
            MovieContract.MovieEntity._ID,
            MovieContract.MovieEntity.POSTER_COLUMN,
            MovieContract.MovieEntity.TITLE_COLUMN,
    };

    // These indices are tied to MOVIE_COLUMNS.  If MOVIE_COLUMNS changes, these
    // must change.
    public static final int ID_COLUMN = 0;
    public static final int POSTER_COLUMN = 1;
    public static final int TITLE_COLUMN = 2;

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
//                Cursor cursor = getAdapter().getCursor();
//                if (cursor != null && cursor.moveToPosition(position)) {
//                    String word = cursor.getString(WORD_COLUMN);
//                    Callback callback = (Callback) getActivity();
//                    callback.onWordSelected(word);
//                }

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

                String sortOrder = MovieContract.MovieEntity.POPULARITY_COLUMN + " DESC, " +
                        MovieContract.MovieEntity._ID + " DESC";

                return new CursorLoader(
                        getActivity(),
                        Uri.parse("content://" + getActivity().getString(R.string.content_authority))
                                .buildUpon().appendPath(MovieContract.MOVIE_PATH).build(),
                        MOVIE_COLUMNS,
                        null,
                        null,
                        sortOrder);
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
}
