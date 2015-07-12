package hu.dushu.developers.popularmovies.data;

import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.Date;

import hu.dushu.developers.popularmovies.R;

/**
 * Created by renfeng on 7/11/15.
 */
public class MovieContract {

    public static final String MOVIE_PATH = "movie";

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static String getDbDateString(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    public static class MovieEntity implements BaseColumns {

        public static final String TABLE_NAME = "movie";

        public static final String POSTER_COLUMN = "poster_path";
        public static final String TITLE_COLUMN = "title";
        public static final String POPULARITY_COLUMN = "popularity";
        public static final String RATE_COLUMN = "vote_average";
        public static final String RELEASE_COLUMN = "release_date";
        public static final String ID_COLUMN = "id";
        public static final String PLOT_COLUMN = "overview";

        public static Uri getContentUri(Context context) {
            return Uri.parse("content://" + context.getString(R.string.content_authority))
                    .buildUpon().appendPath(MOVIE_PATH).build();
        }
    }
}
