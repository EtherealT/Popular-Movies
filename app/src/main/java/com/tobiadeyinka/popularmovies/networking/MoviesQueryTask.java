package com.tobiadeyinka.popularmovies.networking;

import android.widget.Toast;
import android.os.AsyncTask;
import android.net.NetworkInfo;
import android.content.Context;
import android.database.MatrixCursor;
import android.net.ConnectivityManager;
import android.support.v7.widget.RecyclerView;

import com.tobiadeyinka.popularmovies.entities.QueryType;
import com.tobiadeyinka.popularmovies.database.ConfigValues;
import com.tobiadeyinka.popularmovies.entities.MovieAdapter;

import org.json.*;
import java.io.IOException;

public class MoviesQueryTask extends AsyncTask<QueryType, Void, String>{

    private Context context;
    private MovieAdapter movieAdapter;
    private RecyclerView recyclerView;

    public MoviesQueryTask(Context context, MovieAdapter movieAdapter, RecyclerView recyclerView){
        this.context = context;
        this.movieAdapter = movieAdapter;
        this.recyclerView = recyclerView;
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
            JSONObject o;

            MatrixCursor matrixCursor = new MatrixCursor(new String[] {
                    ConfigValues.ID,
                    ConfigValues.TITLE,
                    ConfigValues.RELEASE_DATE,
                    ConfigValues.POSTER_PATH,
                    ConfigValues.VOTE_AVERAGE,
                    ConfigValues.OVERVIEW
            });

            for(int i = 0; i < n; i++){
                o = results.getJSONObject(i);
                int id = o.getInt(ConfigValues.ID);
                String title = o.getString(ConfigValues.TITLE);
                String releaseDate = o.getString(ConfigValues.RELEASE_DATE);
                String posterPath = o.getString(ConfigValues.POSTER_PATH);
                String voteAverage = o.getString(ConfigValues.VOTE_AVERAGE);
                String overview = o.getString(ConfigValues.OVERVIEW);
                matrixCursor.addRow(new Object[]{id, title,releaseDate, posterPath, voteAverage, overview});
            }

            movieAdapter = new MovieAdapter(context, matrixCursor);
            recyclerView.setAdapter(movieAdapter);

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
