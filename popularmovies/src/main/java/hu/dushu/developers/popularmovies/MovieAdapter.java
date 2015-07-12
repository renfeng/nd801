package hu.dushu.developers.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by renfeng on 7/12/15.
 */
public class MovieAdapter extends CursorAdapter {


    public MovieAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        view.setTag(new ViewHolder(view));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        String poster = cursor.getString(MainActivityFragment.POSTER_COLUMN);
        String title = cursor.getString(MainActivityFragment.TITLE_COLUMN);

        Picasso.with(context).load("http://image.tmdb.org/t/p/" + "w185" + poster)
                .into(holder.posterImageView);

        holder.titleTextView.setText(title);
    }

    private static class ViewHolder {

        public final ImageView posterImageView;
        public final TextView titleTextView;

        public ViewHolder(View view) {
            posterImageView = (ImageView) view.findViewById(R.id.imageView);
            titleTextView = (TextView) view.findViewById(R.id.textView);
        }
    }
}
