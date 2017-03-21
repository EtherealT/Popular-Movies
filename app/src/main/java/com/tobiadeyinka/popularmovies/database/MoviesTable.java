package com.tobiadeyinka.popularmovies.database;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tobiadeyinka.popularmovies.entities.Movie;

import java.util.ArrayList;
import java.util.List;

public class MoviesTable {

    private Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public MoviesTable(Context context){
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
    }

    public long save(Movie movie){
        ContentValues cv = new ContentValues();
        cv.put(ConfigValues.ID, movie.getId());
        cv.put(ConfigValues.TITLE, movie.getTitle());
        cv.put(ConfigValues.POSTER_PATH, movie.getMoviePoster());
        cv.put(ConfigValues.RELEASE_DATE, movie.getReleaseDate());
        cv.put(ConfigValues.VOTE_AVERAGE, movie.getVoteAverage());
        cv.put(ConfigValues.OVERVIEW, movie.getPlotSynopsis());
        return database.insert(ConfigValues.MOVIES_TABLE, null, cv);
    }

    public void delete(long movieId){
        database.delete(ConfigValues.MOVIES_TABLE, ConfigValues.ID + " = " + movieId, null);
    }

    public boolean contains(long movieId){
        Cursor cursor = database.rawQuery("SELECT * FROM " + ConfigValues.MOVIES_TABLE + " WHERE " + ConfigValues.ID + " = " + movieId, null);
        return cursor.getCount() != 0;
    }

    public Cursor getAll(){
        Cursor cursor = database.query(ConfigValues.MOVIES_TABLE, ConfigValues.MOVIES_TABLE_COLUMNS, null, null, null, null, null);
        return cursor;
    }

    public void close(){
        databaseHelper.close();
    }

}
