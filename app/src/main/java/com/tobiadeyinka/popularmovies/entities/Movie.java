package com.tobiadeyinka.popularmovies.entities;

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

    public Movie(int id, String title, String moviePoster){
        this.id = id;
        this.title = title;
        this.moviePoster = moviePoster;
        releaseDate = voteAverage = plotSynopsis = "";
    }

    public Movie(int id, String title, String releaseDate, String moviePoster, String voteAverage, String plotSynopsis) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.moviePoster = moviePoster;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;
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
