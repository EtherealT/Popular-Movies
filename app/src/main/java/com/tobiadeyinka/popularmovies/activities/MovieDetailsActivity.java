package com.tobiadeyinka.popularmovies.activities;

import android.os.Bundle;
import android.os.AsyncTask;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.tobiadeyinka.popularmovies.R;
import com.tobiadeyinka.popularmovies.utilities.MovieQuery;

import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;

/**
 * @author Tobi Adeyinka
 */

public class MovieDetailsActivity extends AppCompatActivity {

    int movieId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_movie_details);

        Intent intent = getIntent();
        movieId = intent.getIntExtra("id", 0);
        new QueryTask().execute(movieId);
    }

    private class QueryTask extends AsyncTask<Integer, Void, String>{

        @Override
        protected String doInBackground(Integer... integers) {
            int id = integers[0];
            try {
                return MovieQuery.getMovieDetails(id);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject object = new JSONObject(s);
                String title = object.getString("title");
                String releaseDate = object.getString("release_date");
                String moviePoster = object.getString("poster_path");
                String voteAverage = object.getString("vote_average");
                String plotSynopsis = object.getString("overview");
                String runtime = object.getString("runtime");

                TextView titleTextView = (TextView)findViewById(R.id.tv_movie_title);
                titleTextView.setText(title);

                ImageView posterImageView = (ImageView)findViewById(R.id.poster_image_view);
                Picasso.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w500/" + moviePoster).into(posterImageView);

                TextView yearTextView = (TextView)findViewById(R.id.year_text_view);
                yearTextView.setText(releaseDate.substring(0,4));

                TextView runtimeTextView = (TextView)findViewById(R.id.runtime_text_view);
                runtimeTextView.setText(getString(R.string.runtime, runtime));

                TextView ratingTextView = (TextView)findViewById(R.id.rating_text_view);
                ratingTextView.setText(getString(R.string.vote_average, voteAverage));

                TextView synopsisTextView = (TextView)findViewById(R.id.synopsis_text_view);
                synopsisTextView.setText(plotSynopsis);

                Button addFavoritesButton = (Button)findViewById(R.id.add_favorite_btn);
                addFavoritesButton.setText(getFavoritesButtonText());
                addFavoritesButton.setVisibility(View.VISIBLE);
                addFavoritesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String getFavoritesButtonText(){
        return getString(R.string.add_favorite);
    }

}
