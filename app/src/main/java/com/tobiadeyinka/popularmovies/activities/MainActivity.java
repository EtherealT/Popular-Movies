package com.tobiadeyinka.popularmovies.activities;

import android.database.Cursor;
import android.database.MatrixCursor;
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
import com.tobiadeyinka.popularmovies.database.ConfigValues;
import com.tobiadeyinka.popularmovies.database.MoviesTable;
import com.tobiadeyinka.popularmovies.entities.MainActivityStatus;
import com.tobiadeyinka.popularmovies.entities.QueryType;
import com.tobiadeyinka.popularmovies.networking.MovieQueries;
import com.tobiadeyinka.popularmovies.entities.MovieAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;

/**
 * @author Tobi Adeyinka
 */

public class MainActivity extends AppCompatActivity{

    private MovieAdapter movieAdapter;
    private RecyclerView recyclerView;
    private MainActivityStatus status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        new MovieQueryTask().execute(QueryType.POPULAR);

        recyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        movieAdapter = new MovieAdapter(getApplicationContext(), null);
        recyclerView.setAdapter(movieAdapter);

        status = MainActivityStatus.NORMAL;
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
            case R.id.popularity: return sortByPopularity();
            case R.id.rating: return sortByRating();
            case R.id.favorites: return displayFavorites();
            default: return false;
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

    /*
     * Display locally saved favorites
     */
    private boolean displayFavorites(){
        MoviesTable moviesTable = new MoviesTable(getApplicationContext());
        Cursor moviesCursor = moviesTable.getAll();

        movieAdapter = new MovieAdapter(getApplicationContext(), moviesCursor);
        recyclerView.setAdapter(movieAdapter);

        if (moviesCursor.getCount() == 0)
            Toast.makeText(getApplicationContext(), "No favorite movies saved", Toast.LENGTH_LONG).show();

        moviesTable.close();
        status = MainActivityStatus.FAVORITES;
        return true;
    }

    private class MovieQueryTask extends AsyncTask<QueryType, Void, String> {

        @Override
        protected String doInBackground(QueryType... queryTypes) {
            QueryType queryType = queryTypes[0];

            if(!isOnline()) return null;

            switch (queryType){
                case POPULAR:
                    try {
                        return MovieQueries.getPopularMovies();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                case TOP_RATED:
                    try {
                        return MovieQueries.getTopRatedMovies();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                default: return null;
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
                JSONObject tmp;

                MatrixCursor matrixCursor = new MatrixCursor(new String[] {
                        ConfigValues.ID,
                        ConfigValues.TITLE,
                        ConfigValues.RELEASE_DATE,
                        ConfigValues.POSTER_PATH,
                        ConfigValues.VOTE_AVERAGE,
                        ConfigValues.OVERVIEW
                });

                for(int i = 0; i < len; i++){
                    tmp = results.getJSONObject(i);
                    int id = tmp.getInt(ConfigValues.ID);
                    String title = tmp.getString(ConfigValues.TITLE);
                    String releaseDate = tmp.getString(ConfigValues.RELEASE_DATE);
                    String posterPath = tmp.getString(ConfigValues.POSTER_PATH);
                    String voteAverage = tmp.getString(ConfigValues.VOTE_AVERAGE);
                    String overview = tmp.getString(ConfigValues.OVERVIEW);
                    matrixCursor.addRow(new Object[]{id, title,releaseDate, posterPath, voteAverage, overview});
                }

                movieAdapter = new MovieAdapter(getApplicationContext(), matrixCursor);
                recyclerView.setAdapter(movieAdapter);

                status = MainActivityStatus.NORMAL;

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

    @Override
    protected void onResume() {
        if (status == MainActivityStatus.FAVORITES) displayFavorites();
        super.onResume();
    }
}
