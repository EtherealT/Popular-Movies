package com.tobiadeyinka.popularmovies.networking;

import com.tobiadeyinka.popularmovies.config.Values;

import java.net.URL;
import java.util.Scanner;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

/**
 * @author Tobi Adeyinka
 */

public class MovieQueries {

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String POPULAR_MOVIES_QUERY = "popular?api_key=" + Values.MOVIE_DB_API_KEY + "&language=en-US&page=1";
    private static final String TOP_RATED_MOVIES_QUERY = "top_rated?api_key=" + Values.MOVIE_DB_API_KEY + "&language=en-US&page=1";

    public static String getPopularMovies() throws IOException {
        try {
            URL url = new URL(BASE_URL + POPULAR_MOVIES_QUERY);
            return query(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getTopRatedMovies() throws IOException {
        try {
            URL url = new URL(BASE_URL + TOP_RATED_MOVIES_QUERY);
            return query(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getMovieDetails(int movieId) throws IOException {
        URL url;
        try {
            url = new URL(BASE_URL + movieId + "?api_key=" + Values.MOVIE_DB_API_KEY + "&language=en-US");
            return query(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getMovieVideos(int movieId) throws IOException{
        URL url;
        try{
            url = new URL(BASE_URL + movieId + "/videos" + "?api_key=" + Values.MOVIE_DB_API_KEY + "&language=en-US");
            return query(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String query(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
