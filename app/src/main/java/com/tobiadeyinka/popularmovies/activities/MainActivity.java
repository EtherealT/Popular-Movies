package com.tobiadeyinka.popularmovies.activities;

import android.os.Bundle;
import android.widget.Toast;
import android.view.MenuItem;
import android.database.Cursor;
import android.net.NetworkInfo;
import android.content.Context;
import android.view.MenuInflater;
import android.net.ConnectivityManager;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;

import com.tobiadeyinka.popularmovies.R;
import com.tobiadeyinka.popularmovies.entities.*;
import com.tobiadeyinka.popularmovies.database.MoviesTable;
import com.tobiadeyinka.popularmovies.networking.MoviesQueryTask;

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

        recyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        movieAdapter = new MovieAdapter(getApplicationContext(), null);
        recyclerView.setAdapter(movieAdapter);
        sortByPopularity();
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

    @Override
    protected void onResume() {
        if (status == MainActivityStatus.FAVORITES) displayFavorites();
        super.onResume();
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /*
     * Sort movies by popularity
     */
    private boolean sortByPopularity(){
        MoviesQueryTask m = new MoviesQueryTask(getApplicationContext(), movieAdapter, recyclerView);
        m.query(QueryType.POPULAR);
        status = MainActivityStatus.NORMAL;
        return true;
    }

    /*
     * Sort movies by rating
     */
    private boolean sortByRating(){
        MoviesQueryTask m = new MoviesQueryTask(getApplicationContext(), movieAdapter, recyclerView);
        m.query(QueryType.TOP_RATED);
        status = MainActivityStatus.NORMAL;
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

}
