package com.tobiadeyinka.popularmovies.entities;

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

import java.util.List;
import java.util.ArrayList;

/**
 * @author Tobi Adeyinka
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    private final List<Movie> data;
    private Context context;

    public MovieAdapter(ArrayList<Movie> data) {
       this.data = data;
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

                Movie movie = data.get(position);
                intent.putExtra("id", movie.getId());
                context.startActivity(intent);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
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

        void bind(int listIndex) {
            Movie movie = data.get(listIndex);
            Picasso.with(context).load("https://image.tmdb.org/t/p/w500/" + movie.getMoviePoster()).into(posterImageView);
        }
    }

}
