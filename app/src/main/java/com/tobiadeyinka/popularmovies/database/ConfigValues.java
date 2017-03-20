package com.tobiadeyinka.popularmovies.database;

abstract class ConfigValues {

    public static final String DATABASE = "com.tobiadeyinka.popularmovies";
    public static final int DATABASE_VERSION = 1;

    public static final String MOVIES_TABLE = "movies";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String POSTER = "poster";
    public static final String[] MOVIES_TABLE_COLUMNS = new String[]{ID, TITLE, POSTER};

}
