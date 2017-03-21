package com.paulocaldeira.movies.providers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.paulocaldeira.movies.data.MovieFactory;
import com.paulocaldeira.movies.data.MovieModel;
import com.paulocaldeira.movies.data.MoviesContract;
import com.paulocaldeira.movies.helpers.FormatHelper;

import java.util.List;

/**
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 1/31/17
 */

public final class FavoriteMovieDataProvider implements MovieDataProvider {
    // Contants
    private static String TAG = "#" + FavoriteMovieDataProvider.class.getSimpleName();

    // Attributes
    private final Context mContext;

    public FavoriteMovieDataProvider(Context context) {
        mContext = context;
    }

    @Override
    public void getTopRated(int page, final RequestHandler<List<MovieModel>> handler) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void getMostPopular(int page, final RequestHandler<List<MovieModel>> handler) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void getFavorites(int page, final RequestHandler<List<MovieModel>> handler) {
        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.FavoriteMovieEntry.buildMoviesPageUri(page),
                null,
                null,
                null,
                null
        );

        // Load from local
        List<MovieModel> movies = MovieFactory.fromCursorMultiple(cursor);

        handler.beforeRequest();
        handler.onSuccess(movies);
        handler.onComplete();
    }

    /**
     * Saves a favorite movie
     * @param movie Movie
     */
    public void save(MovieModel movie) {
        if (null == movie) {
            return;
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put(MoviesContract.FavoriteMovieEntry.COLUMN_ID, movie.getId());
        contentValues.put(MoviesContract.FavoriteMovieEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(MoviesContract.FavoriteMovieEntry.COLUMN_SYNOPSIS, movie.getSynopsis());
        contentValues.put(MoviesContract.FavoriteMovieEntry.COLUMN_USER_RATING, movie.getRate());
        contentValues.put(MoviesContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE, FormatHelper.formatDate(movie.getReleaseDate()));
        contentValues.put(MoviesContract.FavoriteMovieEntry.COLUMN_POSTER_URL, movie.getPosterUrl());
        contentValues.put(MoviesContract.FavoriteMovieEntry.COLUMN_BACKDROP_URL, movie.getBackdropUrl());
        contentValues.put(MoviesContract.FavoriteMovieEntry.COLUMN_FAVORITE, movie.isFavorite() ? 1 : 0);

        mContext.getContentResolver().insert(
                MoviesContract.FavoriteMovieEntry.CONTENT_URI,
                contentValues
        );
    }

    /**
     * Removes a favorite movie
     * @param movie Movie
     */
    public void remove(MovieModel movie) {
        if (null == movie) {
            return;
        }

        mContext.getContentResolver().delete(
                MoviesContract.FavoriteMovieEntry.CONTENT_URI,
                "id = ?",
                new String[]{String.valueOf(movie.getId())}
        );
    }

    /**
     * Removes a favorite movie
     * @param movie Movie
     */
    public boolean isFavorite(MovieModel movie) {
        if (null == movie) {
             return false;
        }

        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.FavoriteMovieEntry.CONTENT_URI,
                null,
                "id = ?",
                new String[]{String.valueOf(movie.getId())},
                null
        );

        return cursor.moveToNext();
    }
}
