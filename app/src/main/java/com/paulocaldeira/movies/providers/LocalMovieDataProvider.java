package com.paulocaldeira.movies.providers;

import android.content.Context;
import android.database.Cursor;

import com.paulocaldeira.movies.data.MovieFactory;
import com.paulocaldeira.movies.data.MovieModel;
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
    public void getTopRated(int page, final RequestHandler<List<MovieModel>> handler) {
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

    @Override
    public void getMostPopular(int page, final RequestHandler<List<MovieModel>> handler) {
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
}
