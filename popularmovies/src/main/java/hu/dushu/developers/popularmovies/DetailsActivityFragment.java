package hu.dushu.developers.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import hu.dushu.developers.popularmovies.data.MovieContract;
import hu.dushu.developers.popularmovies.sync.Review;
import hu.dushu.developers.popularmovies.sync.ReviewsResponse;
import hu.dushu.developers.popularmovies.sync.Video;
import hu.dushu.developers.popularmovies.sync.VideosResponse;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment
		implements LoaderManager.LoaderCallbacks<Cursor> {

	private static final String LOG_TAG = DetailsActivityFragment.class.getSimpleName();

	private static final int MOVIE_LOADER = 0;
	public static final String MOVIE_KEY = "movieID";

	private static final String[] MOVIE_COLUMNS = {
			MovieContract.MovieEntity.ID_COLUMN,
			MovieContract.MovieEntity.BACKDROP_COLUMN,
			MovieContract.MovieEntity.POSTER_COLUMN,
			MovieContract.MovieEntity.TITLE_COLUMN,
			MovieContract.MovieEntity.RELEASE_COLUMN,
			MovieContract.MovieEntity.RATE_COLUMN,
			MovieContract.MovieEntity.PLOT_COLUMN,
			MovieContract.MovieEntity.TRAILERS_COLUMN,
			MovieContract.MovieEntity.REVIEWS_COLUMN,

			MovieContract.MovieEntity._ID
	};

	// These indices are tied to MOVIE_COLUMNS.  If MOVIE_COLUMNS changes, these
	// must change.
	public static final int ID_COLUMN = 0;
	public static final int BACKDROP_COLUMN = 1;
	public static final int POSTER_COLUMN = 2;
	public static final int TITLE_COLUMN = 3;
	public static final int RELEASE_COLUMN = 4;
	public static final int RATE_COLUMN = 5;
	public static final int PLOT_COLUMN = 6;
	public static final int TRAILERS_COLUMN = 7;
	public static final int REVIEWS_COLUMN = 8;

	private int movieId;

	private ImageView backdropImageView;
	private ImageView posterImageView;
	private TextView titleTextView;
	private TextView releaseDateTextView;
	private TextView voteAverageTextView;
	private TextView plotTextView;
	private ListView trailersListView;
	private ListView reviewssListView;

	public DetailsActivityFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		if (savedInstanceState != null) {
			setMovieId(savedInstanceState.getInt(MOVIE_KEY));
		}

		View view = inflater.inflate(R.layout.fragment_details, container, false);

		setBackdropImageView((ImageView) view.findViewById(R.id.backdropImageView));
		setPosterImageView((ImageView) view.findViewById(R.id.posterImageView));
		setTitleTextView((TextView) view.findViewById(R.id.titleTextView));
		setReleaseDateTextView((TextView) view.findViewById(R.id.releaseDateTextView));
		setVoteAverageTextView((TextView) view.findViewById(R.id.voteAverageTextView));
		setPlotTextView((TextView) view.findViewById(R.id.plotTextView));
		setTrailersListView((ListView) view.findViewById(R.id.trailersListView));
		setReviewssListView((ListView) view.findViewById(R.id.reviewsListView));

		return view;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
		switch (i) {
			case MOVIE_LOADER: {
				Uri uri = Uri.parse("content://" + getString(R.string.content_authority))
						.buildUpon()
						.appendPath(MovieContract.MOVIE_PATH)
						.appendPath(getMovieId() + "")
						.build();
				Log.v(LOG_TAG, uri.toString());

				// Now create and return a CursorLoader that will take care of
				// creating a Cursor for the data being displayed.
				return new CursorLoader(
						getActivity(),
						uri,
						MOVIE_COLUMNS,
						null,
						null,
						null
				);
			}
		}

		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		if (cursor != null && cursor.moveToFirst()) {

			String backdrop = cursor.getString(
					cursor.getColumnIndex(MovieContract.MovieEntity.BACKDROP_COLUMN));
			String poster = cursor.getString(
					cursor.getColumnIndex(MovieContract.MovieEntity.POSTER_COLUMN));
			String title = cursor.getString(
					cursor.getColumnIndex(MovieContract.MovieEntity.TITLE_COLUMN));
			String release = cursor.getString(
					cursor.getColumnIndex(MovieContract.MovieEntity.RELEASE_COLUMN));
			String voteAverage = cursor.getString(
					cursor.getColumnIndex(MovieContract.MovieEntity.RATE_COLUMN));
			String plot = cursor.getString(
					cursor.getColumnIndex(MovieContract.MovieEntity.PLOT_COLUMN));
			String trailers = cursor.getString(
					cursor.getColumnIndex(MovieContract.MovieEntity.TRAILERS_COLUMN));
			String reviews = cursor.getString(
					cursor.getColumnIndex(MovieContract.MovieEntity.REVIEWS_COLUMN));

			Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/" + "w185" + backdrop)
					.into(getBackdropImageView());
			Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/" + "w185" + poster)
					.into(getPosterImageView());
			getTitleTextView().setText(title);
			getReleaseDateTextView().setText(release);
			getVoteAverageTextView().setText(voteAverage);

			if (trailers != null) {
				List<String> trailerList = new ArrayList<>();
				final List<String> youtubeKeys = new ArrayList<>();

				JsonObjectParser parser = new JsonObjectParser(new JacksonFactory());
				try {
					VideosResponse response = parser.parseAndClose(
							new StringReader(trailers), VideosResponse.class);
					for (Video v : response.getResults()) {
						if (!"YouTube".equals(v.getSite())) {
							continue;
						}
						trailerList.add(v.getName());
						youtubeKeys.add(v.getKey());
					}
				} catch (IOException e) {
					Log.e(LOG_TAG, "Error ", e);
				}

				ListView trailersListView = getTrailersListView();
				trailersListView.setAdapter(new ArrayAdapter<>(getActivity(),
						R.layout.simple_list, R.id.text_view, trailerList));
				trailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(
							AdapterView<?> parent, View view, int position, long id) {
						String key = youtubeKeys.get(position);
						startActivity(new Intent(Intent.ACTION_VIEW,
								Uri.parse("http://www.youtube.com/watch?v=" + key)));
					}
				});
			}
			if (reviews != null) {
				List<String> reviewList = new ArrayList<>();

				JsonObjectParser parser = new JsonObjectParser(new JacksonFactory());
				try {
					ReviewsResponse response = parser.parseAndClose(
							new StringReader(reviews), ReviewsResponse.class);
					for (Review r : response.getResults()) {
						reviewList.add(r.getContent() + " - " + r.getAuthor());
					}
				} catch (IOException e) {
					Log.e(LOG_TAG, "Error ", e);
				}

				ListView reviewsListView = getReviewssListView();
				reviewsListView.setAdapter(new ArrayAdapter<>(
						getActivity(), R.layout.simple_list, R.id.text_view, reviewList));
			}

			if (plot.length() == 0) {
				plot = "(plot unavailable)";
			}
			getPlotTextView().setText(plot);

		} else {
			String msg = "Movie not found";
			Toast toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}

	@Override
	public void onResume() {
		super.onResume();
		Bundle arguments = getArguments();
		if (arguments != null && arguments.containsKey(MOVIE_KEY)) {
			getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Bundle arguments = getArguments();
		if (arguments != null && arguments.containsKey(MOVIE_KEY)) {

            /*
			 * it passes the word to next event handler of loader
             */
			setMovieId(arguments.getInt(MOVIE_KEY));

			getLoaderManager().initLoader(MOVIE_LOADER, null, this);
		}
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(MOVIE_KEY, getMovieId());
		super.onSaveInstanceState(outState);
	}

	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public TextView getPlotTextView() {
		return plotTextView;
	}

	public void setPlotTextView(TextView plotTextView) {
		this.plotTextView = plotTextView;
	}

	public TextView getVoteAverageTextView() {
		return voteAverageTextView;
	}

	public void setVoteAverageTextView(TextView voteAverageTextView) {
		this.voteAverageTextView = voteAverageTextView;
	}

	public TextView getReleaseDateTextView() {
		return releaseDateTextView;
	}

	public void setReleaseDateTextView(TextView releaseDateTextView) {
		this.releaseDateTextView = releaseDateTextView;
	}

	public TextView getTitleTextView() {
		return titleTextView;
	}

	public void setTitleTextView(TextView titleTextView) {
		this.titleTextView = titleTextView;
	}

	public ImageView getPosterImageView() {
		return posterImageView;
	}

	public void setPosterImageView(ImageView posterImageView) {
		this.posterImageView = posterImageView;
	}

	public ImageView getBackdropImageView() {
		return backdropImageView;
	}

	public void setBackdropImageView(ImageView backdropImageView) {
		this.backdropImageView = backdropImageView;
	}

	public ListView getReviewssListView() {
		return reviewssListView;
	}

	public void setReviewssListView(ListView reviewssListView) {
		this.reviewssListView = reviewssListView;
	}

	public ListView getTrailersListView() {
		return trailersListView;
	}

	public void setTrailersListView(ListView trailersListView) {
		this.trailersListView = trailersListView;
	}
}
