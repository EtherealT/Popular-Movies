package com.tobiadeyinka.popularmovies.entities;

/**
 * @author Tobi Adeyinka
 */

public class Movie {

    private int id;
    private String title;
    private String releaseDate;
    private String moviePoster;
    private String voteAverage;
    private String plotSynopsis;

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
