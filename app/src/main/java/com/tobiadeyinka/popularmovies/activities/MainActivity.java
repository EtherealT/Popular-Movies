package com.tobiadeyinka.popularmovies.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;
import android.view.MenuItem;
import android.database.Cursor;
import android.view.MenuInflater;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;

import com.tobiadeyinka.popularmovies.R;
import com.tobiadeyinka.popularmovies.entities.*;
import com.tobiadeyinka.popularmovies.utilities.MovieAdapter;
import com.tobiadeyinka.popularmovies.utilities.MoviesProvider;
import com.tobiadeyinka.popularmovies.networking.MoviesQueryTask;

import java.util.ArrayList;

/**
 * @author Tobi Adeyinka
 */

public class MainActivity extends AppCompatActivity{

    private final String KEY_RECYCLER_STATE = "recycler_state";
    private  Bundle bundleRecyclerViewState;

    private ArrayList<Movie> data;
    private MovieAdapter movieAdapter;
    private RecyclerView recyclerView;
    private MainActivityStatus status;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        data = new ArrayList<>();
        movieAdapter = new MovieAdapter(data);
        recyclerView.setAdapter(movieAdapter);
        sortMovies(QueryType.POPULAR);
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
            case R.id.popularity: return sortMovies(QueryType.POPULAR);
            case R.id.rating: return sortMovies(QueryType.TOP_RATED);
            case R.id.favorites: return displayFavorites();
            default: return false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        bundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        bundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);

    }

    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        if (bundleRecyclerViewState != null) {
            Parcelable listState = bundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (status == MainActivityStatus.FAVORITES) displayFavorites();
    }

    private boolean sortMovies(QueryType type){
        MoviesQueryTask m = new MoviesQueryTask(getApplicationContext(), movieAdapter, recyclerView);
        m.query(type);
        status = ( type == QueryType.POPULAR) ? MainActivityStatus.POPULAR : MainActivityStatus.RATING;
        return true;
    }

    /*
     * Display locally saved favorites
     */
    private boolean displayFavorites(){
        Cursor moviesCursor = getApplicationContext().getContentResolver().query(MoviesProvider.getCONTENT_URI(), null, null, null, null);
        getDataFromCursor(moviesCursor);
        movieAdapter.notifyDataSetChanged();

        if (moviesCursor.getCount() == 0)
            Toast.makeText(getApplicationContext(), "No favorite data saved", Toast.LENGTH_LONG).show();

        status = MainActivityStatus.FAVORITES;
        return true;
    }

    private void getDataFromCursor(Cursor c){
        data.clear();

        try {
            while (c.moveToNext())
                data.add(Movie.fromCursor(c));
        }finally {
            c.close();
        }
    }

}
