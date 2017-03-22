package com.tobiadeyinka.popularmovies.utilities;

import android.net.Uri;
import android.content.*;
import android.database.Cursor;

import com.tobiadeyinka.popularmovies.entities.Movie;
import com.tobiadeyinka.popularmovies.database.MoviesTable;

public class MoviesProvider extends ContentProvider {

    private MoviesTable moviesTable;

    private static final String AUTHORITY = "com.tobiadeyinka.popularmovies.utilities";
    private static final String MOVIE_BASE_PATH = "movie";

    private static final int MOVIE = 100;
    private static final int MOVIE_ID = 101;

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + MOVIE_BASE_PATH);

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, MOVIE_BASE_PATH , MOVIE);
        sURIMatcher.addURI(AUTHORITY, MOVIE_BASE_PATH + "/*" , MOVIE_ID);
    }

    @Override
    public boolean onCreate() {
        moviesTable = new MoviesTable(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return moviesTable.getAll();
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long id = moviesTable.save(Movie.fromContentValues(contentValues));
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        String id = uri.getPathSegments().get(1);
        return moviesTable.delete(Long.valueOf(id));
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    public static Uri getCONTENT_URI() {
        return CONTENT_URI;
    }

    public static Uri generateUri(long id){
        Uri uri = Uri.parse(CONTENT_URI.toString() + "/" + String.valueOf(id));
        return uri;
    }
}
