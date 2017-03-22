package com.tobiadeyinka.popularmovies.networking;

import android.widget.*;
import android.view.View;
import android.app.Activity;
import android.os.AsyncTask;
import android.content.Context;

import com.squareup.picasso.Picasso;
import com.tobiadeyinka.popularmovies.R;
import com.tobiadeyinka.popularmovies.entities.Movie;
import com.tobiadeyinka.popularmovies.database.MoviesTable;
import com.tobiadeyinka.popularmovies.utilities.MoviesProvider;

import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;

public class MovieDetailsQueryTask extends AsyncTask<Integer, Void, String>{

    private int movieId;
    private Context context;

    public MovieDetailsQueryTask(Context context){
        this.context = context;
    }

    public void query(int movieId){
        this.execute(movieId);
        this.movieId = movieId;
    }

    @Override
    protected String doInBackground(Integer... integers) {
        int id = integers[0];
        try {
            return MovieQueries.getMovieDetails(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject object = new JSONObject(s);
            final String title = object.getString("title");
            final String releaseDate = object.getString("release_date");
            final String moviePoster = object.getString("poster_path");
            final String voteAverage = object.getString("vote_average");
            final String plotSynopsis = object.getString("overview");
            String runtime = object.getString("runtime");

            TextView titleTextView = (TextView)((Activity) context).findViewById(R.id.tv_movie_title);
            titleTextView.setText(title);

            ImageView posterImageView = (ImageView)((Activity) context).findViewById(R.id.poster_image_view);
            Picasso.with(context).load("https://image.tmdb.org/t/p/w500/" + moviePoster).into(posterImageView);

            TextView yearTextView = (TextView)((Activity) context).findViewById(R.id.year_text_view);
            yearTextView.setText(releaseDate.substring(0,4));

            TextView runtimeTextView = (TextView)((Activity) context).findViewById(R.id.runtime_text_view);
            runtimeTextView.setText(context.getString(R.string.runtime, runtime));

            TextView ratingTextView = (TextView)((Activity) context).findViewById(R.id.rating_text_view);
            ratingTextView.setText(context.getString(R.string.vote_average, voteAverage));

            TextView synopsisTextView = (TextView)((Activity) context).findViewById(R.id.synopsis_text_view);
            synopsisTextView.setText(plotSynopsis);

            final Button favoritesButton = (Button)((Activity) context).findViewById(R.id.add_favorite_btn);
            favoritesButton.setText(getFavoritesButtonText(movieId));
            favoritesButton.setVisibility(View.VISIBLE);
            favoritesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*
                     * if the text on the button indicates adding as favorite,
                     * save the movie as favorite.
                     */

                    if(favoritesButton.getText().equals(context.getString(R.string.add_favorite))){
                        Movie movie = new Movie(movieId, title, releaseDate, moviePoster, voteAverage, plotSynopsis);
                        context.getContentResolver().insert(MoviesProvider.getCONTENT_URI(), Movie.toContentValues(movie));
                        favoritesButton.setText(context.getString(R.string.remove_favorite));
                    }
                    else{
                        context.getContentResolver().delete(MoviesProvider.generateUri(movieId), null, null);
                        favoritesButton.setText(context.getString(R.string.add_favorite));
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getFavoritesButtonText(int movieId){
        MoviesTable moviesTable = new MoviesTable(context);
        if(moviesTable.contains(movieId)) return context.getString(R.string.remove_favorite);
        return context.getString(R.string.add_favorite);
    }

}
