package com.tobiadeyinka.popularmovies.entities;

import android.database.Cursor;
import android.view.View;
import android.widget.Toast;
import android.view.ViewGroup;
import android.content.Intent;
import android.content.Context;
import android.net.NetworkInfo;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.net.ConnectivityManager;
import android.support.v7.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tobiadeyinka.popularmovies.R;
import com.tobiadeyinka.popularmovies.activities.MovieDetailsActivity;

/**
 * @author Tobi Adeyinka
 */

public class MovieAdapter extends CursorRecyclerViewAdapter<MovieAdapter.MovieViewHolder>{

    private Context context;
    private Cursor cursor;

    public MovieAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.cursor = cursor;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        final LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        final MovieViewHolder viewHolder = new MovieViewHolder(view);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isOnline()) {
                    Toast.makeText(context, "Check your internet connection", Toast.LENGTH_LONG).show();
                    return;
                }

                int position = viewHolder.getAdapterPosition();
                Intent intent = new Intent(context, MovieDetailsActivity.class);

                cursor.moveToPosition(position);
                Movie movie = Movie.fromCursor(cursor);
                intent.putExtra("id", movie.getId());
                context.startActivity(intent);
            }
        });

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder viewHolder, Cursor cursor) {
        viewHolder.bind(cursor);
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder{
        final ImageView posterImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            posterImageView = (ImageView)itemView.findViewById(R.id.movie_item_image);
        }

        void bind(Cursor cursor) {
            Movie movie = Movie.fromCursor(cursor);
            Picasso.with(context).load("https://image.tmdb.org/t/p/w500/" + movie.getMoviePoster()).into(posterImageView);
        }
    }



}
