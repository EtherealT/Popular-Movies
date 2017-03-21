package com.tobiadeyinka.popularmovies.activities;

import android.os.Bundle;
import android.content.Intent;
import android.widget.LinearLayout;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tobiadeyinka.popularmovies.R;
import com.tobiadeyinka.popularmovies.networking.*;

/**
 * @author Tobi Adeyinka
 */

public class MovieDetailsActivity extends AppCompatActivity {

    private int movieId;
    private LinearLayout trailerSection;
    private LinearLayout reviewsSection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_movie_details);

        trailerSection = (LinearLayout) findViewById(R.id.trailer_linear_layout);
        reviewsSection = (LinearLayout) findViewById(R.id.review_linear_layout);

        Intent intent = getIntent();
        movieId = intent.getIntExtra("id", 0);

        new MovieDetailsQueryTask(this).query(movieId);
        new TrailersQueryTask(trailerSection, this).query(movieId);
        new ReviewsQueryTask(reviewsSection, this).query(movieId);
    }

}
