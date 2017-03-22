package com.tobiadeyinka.popularmovies.networking;

import android.widget.Toast;
import android.os.AsyncTask;
import android.net.NetworkInfo;
import android.content.Context;
import android.database.MatrixCursor;
import android.net.ConnectivityManager;
import android.support.v7.widget.RecyclerView;

import com.tobiadeyinka.popularmovies.entities.Movie;
import com.tobiadeyinka.popularmovies.entities.QueryType;
import com.tobiadeyinka.popularmovies.database.ConfigValues;
import com.tobiadeyinka.popularmovies.utilities.MovieAdapter;

import org.json.*;
import java.io.IOException;
import java.util.List;

public class MoviesQueryTask extends AsyncTask<QueryType, Void, String>{

    private Context context;
    private MovieAdapter movieAdapter;
    private RecyclerView recyclerView;
    private List<Movie> data;

    public MoviesQueryTask(Context context, MovieAdapter movieAdapter, RecyclerView recyclerView){
        this.context = context;
        this.movieAdapter = movieAdapter;
        this.recyclerView = recyclerView;
        this.data = movieAdapter.getData();
    }

    public void query(QueryType queryType){
        this.execute(queryType);
    }

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
            Toast.makeText(context, "Check your internet connection", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            JSONObject object = new JSONObject(s);
            JSONArray results = object.getJSONArray("results");
            int n = results.length();
            data.clear();
            JSONObject tmp;

            for(int i = 0; i < n; i++){
                tmp = results.getJSONObject(i);
                int id = tmp.getInt("id");
                String title = tmp.getString("title");
                String releaseDate = tmp.getString("release_date");
                String moviePoster = tmp.getString("poster_path");
                String voteAverage = tmp.getString("vote_average");
                String plotSynopsis = tmp.getString("overview");
                data.add(new Movie(id, title, releaseDate, moviePoster, voteAverage, plotSynopsis));
            }

            movieAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
