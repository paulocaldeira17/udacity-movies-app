package com.paulocaldeira.movies.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.paulocaldeira.movies.data.MoviesContract;

/**
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 3/19/17
 */

public class MoviesDBHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = MoviesDBHelper.class.getSimpleName();

    // name & version
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create the database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MoviesContract.FavoriteMovieEntry.TABLE_NAME + "(" + MoviesContract.FavoriteMovieEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.FavoriteMovieEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                MoviesContract.FavoriteMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MoviesContract.FavoriteMovieEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, " +
                MoviesContract.FavoriteMovieEntry.COLUMN_BACKDROP_URL + " TEXT, " +
                MoviesContract.FavoriteMovieEntry.COLUMN_POSTER_URL + " TEXT, " +
                MoviesContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE + " NUMERIC, " +
                MoviesContract.FavoriteMovieEntry.COLUMN_USER_RATING + " NUMERIC NOT NULL, " +
                MoviesContract.FavoriteMovieEntry.COLUMN_FAVORITE + " NUMERIC DEFAULT 0);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    // Upgrade database when version is changed.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.FavoriteMovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                MoviesContract.FavoriteMovieEntry.TABLE_NAME + "'");

        // re-create database
        onCreate(sqLiteDatabase);
    }
}
