package com.tobiadeyinka.popularmovies.entities;

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
