package com.paulocaldeira.movies.adapters;

import android.util.Log;

import com.paulocaldeira.movies.data.MovieFactory;
import com.paulocaldeira.movies.data.MovieModel;
import com.paulocaldeira.movies.providers.JsonRequestHandler;
import com.paulocaldeira.movies.providers.RequestHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Movies Request Handler Adapter
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 2/3/17
 */

public class MoviesRequestHandlerAdapter extends JsonRequestHandler {
    // Contants
    private static String TAG = "#" + MoviesRequestHandlerAdapter.class.getSimpleName();

    // Attributes
    private final RequestHandler<List<MovieModel>> mHandler;

    public MoviesRequestHandlerAdapter(RequestHandler<List<MovieModel>> handler) {
        mHandler = handler;
    }

    @Override
    public void beforeRequest() {
        mHandler.beforeRequest();
    }

    @Override
    public void onSuccess(JSONObject response) {
        List<MovieModel> movies = new ArrayList<>();

        try {
            JSONArray results = response.getJSONArray("results");

            if (results != null) {
                for (int i = 0; i < results.length(); i++) {
                    JSONObject movieObject = results.getJSONObject(i);

                    movies.add(MovieFactory.fromJsonObject(movieObject));
                }
            }

            mHandler.onSuccess(movies);

        } catch (Throwable e) {
            mHandler.onError(e);
        }

    }

    @Override
    public void onError(Throwable e) {
        mHandler.onError(e);
    }

    @Override
    public void onComplete() {
        mHandler.onComplete();
    }
}