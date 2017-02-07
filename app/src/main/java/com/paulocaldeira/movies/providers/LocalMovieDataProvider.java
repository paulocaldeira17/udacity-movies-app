package com.paulocaldeira.movies.providers;

import android.content.Context;
import android.util.Log;

import com.paulocaldeira.movies.R;
import com.paulocaldeira.movies.adapters.MoviesRequestHandlerAdapter;
import com.paulocaldeira.movies.data.MovieModel;

import org.json.JSONException;
import org.json.JSONObject;

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
    public void getTopRated(int page, RequestHandler<List<MovieModel>> handler) {
        String moviesJson = mContext.getString(R.string.movies_json);

        // Request Handler Adapter
        MoviesRequestHandlerAdapter moviesRequestAdapter = new MoviesRequestHandlerAdapter(handler);

        JSONObject moviesObject = null;
        try {
            moviesObject = new JSONObject(moviesJson);
        } catch (JSONException e) {
            moviesRequestAdapter.onError(e);
        }

        moviesRequestAdapter.beforeRequest();
        moviesRequestAdapter.onSuccess(moviesObject);
        moviesRequestAdapter.onComplete();
    }

    @Override
    public void getMostPopular(int page, RequestHandler<List<MovieModel>> handler) {
        getTopRated(page, handler);
    }
}
