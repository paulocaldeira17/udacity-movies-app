package com.paulocaldeira.movies.providers;

import android.content.Context;
import android.database.Cursor;

import com.paulocaldeira.movies.data.Movie;
import com.paulocaldeira.movies.factories.MovieFactory;
import com.paulocaldeira.movies.data.MoviesContract;

import java.util.List;

/**
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 1/31/17
 */

public final class LocalMovieDataProvider implements MovieDataProvider {
    // Contants
    private static String TAG = "#" + LocalMovieDataProvider.class.getSimpleName();

    // Attributes
    private final Context mContext;

    public LocalMovieDataProvider(Context context) {
        mContext = context;
    }

    @Override
    public void getTopRated(int page, final RequestHandler<List<Movie>> handler) {
        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.FavoriteMovieEntry.buildMoviesPageUri(page),
                null,
                null,
                null,
                null
        );

        // Load from local
        List<Movie> movies = MovieFactory.fromCursorMultiple(cursor);
        handler.beforeRequest();
        handler.onSuccess(movies);
        handler.onComplete();
    }

    @Override
    public void getMostPopular(int page, final RequestHandler<List<Movie>> handler) {
        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.FavoriteMovieEntry.buildMoviesPageUri(page),
                null,
                null,
                null,
                null
        );

        // Load from local
        List<Movie> movies = MovieFactory.fromCursorMultiple(cursor);
        handler.beforeRequest();
        handler.onSuccess(movies);
        handler.onComplete();
    }

    @Override
    public void getFavorites(int page, final RequestHandler<List<Movie>> handler) {
        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.FavoriteMovieEntry.buildMoviesPageUri(page),
                null,
                null,
                null,
                null
        );

        // Load from local
        List<Movie> movies = MovieFactory.fromCursorMultiple(cursor);
        handler.beforeRequest();
        handler.onSuccess(movies);
        handler.onComplete();
    }
}
