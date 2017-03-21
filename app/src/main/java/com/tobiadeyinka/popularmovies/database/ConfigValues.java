package com.tobiadeyinka.popularmovies.database;

public abstract class ConfigValues {

    public static final String DATABASE = "com.tobiadeyinka.popularmovies";
    public static final int DATABASE_VERSION = 1;

    public static final String MOVIES_TABLE = "movies";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String POSTER_PATH = "poster_path";
    public static final String RELEASE_DATE = "release_date";
    public static final String VOTE_AVERAGE = "vote_average";
    public static final String OVERVIEW = "overview";
    public static final String[] MOVIES_TABLE_COLUMNS = new String[]{ID, TITLE, POSTER_PATH, RELEASE_DATE, VOTE_AVERAGE, OVERVIEW};

}
