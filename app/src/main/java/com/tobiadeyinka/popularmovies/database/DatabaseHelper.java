package com.tobiadeyinka.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context){
        super(context, ConfigValues.DATABASE, null, ConfigValues.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE " + ConfigValues.MOVIES_TABLE + " (" +
                ConfigValues.ID + " TEXT PRIMARY KEY NOT NULL, " +
                ConfigValues.TITLE + " TEXT NOT NULL, " +
                ConfigValues.RELEASE_DATE + " TEXT NOT NULL, " +
                ConfigValues.VOTE_AVERAGE + " TEXT NOT NULL, " +
                ConfigValues.OVERVIEW + " TEXT NOT NULL, " +
                ConfigValues.POSTER_PATH + " TEXT NOT NULL) "
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
