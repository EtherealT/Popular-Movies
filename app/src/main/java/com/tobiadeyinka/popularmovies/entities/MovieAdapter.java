package com.tobiadeyinka.popularmovies.entities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tobiadeyinka.popularmovies.R;
import com.tobiadeyinka.popularmovies.activities.MovieDetailsActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Tobi Adeyinka
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    List<Movie> data = Collections.EMPTY_LIST;
    Context context;

    public MovieAdapter(ArrayList<Movie> data) {
       this.data = data;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        final LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        final MovieViewHolder viewHolder = new MovieViewHolder(view);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                Intent intent = new Intent(context, MovieDetailsActivity.class);

                Movie movie = data.get(position);
                intent.putExtra("id", movie.getId());
                intent.putExtra("title", movie.getTitle());
                intent.putExtra("releaseDate", movie.getReleaseDate());
                intent.putExtra("poster", movie.getMoviePoster());
                intent.putExtra("voteAverage", movie.getVoteAverage());
                intent.putExtra("plotSynopsis", movie.getPlotSynopsis());
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

    class MovieViewHolder extends RecyclerView.ViewHolder{

        ImageView posterImageView;

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
