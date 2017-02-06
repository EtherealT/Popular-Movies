package com.tobiadeyinka.popularmovies.entities;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tobiadeyinka.popularmovies.R;

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
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MovieViewHolder viewHolder = new MovieViewHolder(view);
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
            Picasso.with(context).load("https://image.tmdb.org/t/p/w185/" + movie.getMoviePoster()).into(posterImageView);
        }

    }

}
