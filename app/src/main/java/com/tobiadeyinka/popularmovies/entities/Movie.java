package com.tobiadeyinka.popularmovies.entities;

import android.content.ContentValues;
import android.database.Cursor;

import com.tobiadeyinka.popularmovies.database.ConfigValues;

/**
 * @author Tobi Adeyinka
 */

public class Movie {

    private final int id;
    private final String title;
    private final String releaseDate;
    private final String moviePoster;
    private final String voteAverage;
    private final String plotSynopsis;

    public Movie(int id, String title, String releaseDate, String moviePoster, String voteAverage, String plotSynopsis) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.moviePoster = moviePoster;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;
    }

    public static Movie fromCursor(Cursor cursor){
        int idColumnIndex = cursor.getColumnIndex(ConfigValues.ID);
        int titleColumnIndex = cursor.getColumnIndex(ConfigValues.TITLE);
        int posterColumnIndex = cursor.getColumnIndex(ConfigValues.POSTER_PATH);
        int releaseDateColumnIndex = cursor.getColumnIndex(ConfigValues.RELEASE_DATE);
        int synopsisColumnIndex = cursor.getColumnIndex(ConfigValues.OVERVIEW);
        int voteAverageColumnIndex = cursor.getColumnIndex(ConfigValues.VOTE_AVERAGE);

        int id = cursor.getInt(idColumnIndex);
        String title = cursor.getString(titleColumnIndex);
        String releaseDate = cursor.getString(releaseDateColumnIndex);
        String moviePoster = cursor.getString(posterColumnIndex);
        String voteAverage = cursor.getString(voteAverageColumnIndex);
        String plotSynopsis = cursor.getString(synopsisColumnIndex);

        return new Movie(id, title, releaseDate, moviePoster, voteAverage, plotSynopsis);
    }

    public static Movie fromContentValues(ContentValues cv){
        int id = cv.getAsInteger(ConfigValues.ID);
        String title = cv.getAsString(ConfigValues.TITLE);
        String releaseDate = cv.getAsString(ConfigValues.RELEASE_DATE);
        String moviePoster = cv.getAsString(ConfigValues.POSTER_PATH);
        String voteAverage = cv.getAsString(ConfigValues.VOTE_AVERAGE);
        String plotSynopsis = cv.getAsString(ConfigValues.OVERVIEW);

        return new Movie(id, title, releaseDate, moviePoster, voteAverage, plotSynopsis);
    }

    public static ContentValues toContentValues(Movie movie){
        ContentValues cv = new ContentValues();
        cv.put(ConfigValues.ID, movie.id);
        cv.put(ConfigValues.TITLE, movie.title);
        cv.put(ConfigValues.RELEASE_DATE, movie.releaseDate);
        cv.put(ConfigValues.POSTER_PATH, movie.moviePoster);
        cv.put(ConfigValues.VOTE_AVERAGE, movie.voteAverage);
        cv.put(ConfigValues.OVERVIEW, movie.plotSynopsis);
        return cv;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }
}
