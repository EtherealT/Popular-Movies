package com.tobiadeyinka.popularmovies.activities;

import android.os.Bundle;
import android.os.AsyncTask;
import android.widget.Toast;
import android.view.MenuItem;
import android.net.NetworkInfo;
import android.content.Context;
import android.view.MenuInflater;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;

import com.tobiadeyinka.popularmovies.R;
import com.tobiadeyinka.popularmovies.entities.Movie;
import com.tobiadeyinka.popularmovies.entities.QueryType;
import com.tobiadeyinka.popularmovies.utilities.MovieQuery;
import com.tobiadeyinka.popularmovies.entities.MovieAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Tobi Adeyinka
 */

public class MainActivity extends AppCompatActivity{

    private ArrayList<Movie> data;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        data = new ArrayList<>();
        new MovieQueryTask().execute(QueryType.POPULAR);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        movieAdapter = new MovieAdapter(data);
        recyclerView.setAdapter(movieAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.popularity:
                return sortByPopularity();
            case R.id.rating:
                return sortByRating();
            default:
                return false;
        }
    }

    /*
     * Sort movies by popularity
     */
    private boolean sortByPopularity(){
        new MovieQueryTask().execute(QueryType.POPULAR);
        return true;
    }

    /*
     * Sort movies by rating
     */
    private boolean sortByRating(){
        new MovieQueryTask().execute(QueryType.TOP_RATED);
        return true;
    }

    private class MovieQueryTask extends AsyncTask<QueryType, Void, String> {

        @Override
        protected String doInBackground(QueryType... queryTypes) {
            QueryType queryType = queryTypes[0];

            if(!isOnline())
                return null;

            switch (queryType){
                case POPULAR:
                    try {
                        return MovieQuery.getPopularMovies();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                case TOP_RATED:
                    try {
                        return MovieQuery.getTopRatedMovies();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                default:
                    return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == null) {
                Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                JSONObject object = new JSONObject(s);
                JSONArray results = object.getJSONArray("results");
                int len = results.length();
                data.clear();
                JSONObject tmp;

                for(int i = 0; i < len; i++){
                    tmp = results.getJSONObject(i);
                    int id = tmp.getInt("id");
                    String title = tmp.getString("title");
                    String releaseDate = tmp.getString("release_date");
                    String moviePoster = tmp.getString("poster_path");
                    String voteAverage = tmp.getString("vote_average");
                    String plotSynopsis = tmp.getString("overview");
                    data.add(new Movie(id, title, releaseDate, moviePoster, voteAverage, plotSynopsis));
                    movieAdapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
