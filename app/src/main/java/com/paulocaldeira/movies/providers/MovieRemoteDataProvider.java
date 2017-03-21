package com.paulocaldeira.movies.providers;

import android.content.Context;
import android.net.Uri;

import com.paulocaldeira.movies.R;
import com.paulocaldeira.movies.data.MovieFactory;
import com.paulocaldeira.movies.data.MovieModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Host Movie Data Provider
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 1/31/17
 */
public final class MovieRemoteDataProvider extends RemoteDataProvider implements MovieDataProvider {

    // Constants
    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_PAGE = "page";

    // Paths
    private static final String PATH_TOP_RATED = "/3/movie/top_rated";
    private static final String PATH_POPULAR = "/3/movie/popular";

    public static int HOST_RESOURCE_ID = R.string.movies_database_api_host;
    public static int API_KEY_RESOURCE_ID = R.string.movies_database_api_key;

    // Configs
    protected String mApiKey;

    /**
     * Initializes Movie remote data provider
     * @param context Context
     */
    public MovieRemoteDataProvider(Context context) {
        super(context);

        mHost = mContext.getString(HOST_RESOURCE_ID);
        mApiKey = mContext.getString(API_KEY_RESOURCE_ID);
    }

    /**
     * Sets Movies Database Api key resource string
     * @param apiKeyResource Api key resource string
     */
    public void setApiKey(int apiKeyResource) {
        mApiKey = mContext.getString(apiKeyResource);
    }

    public void setApiKey(String apiKey) {
        mApiKey = apiKey;
    }

    @Override
    protected Uri.Builder baseUri() {
        Uri.Builder uriBuilder = super.baseUri();
        uriBuilder.appendQueryParameter(PARAM_API_KEY, mApiKey);
        return uriBuilder;
    }

    @Override
    public void getTopRated(int page, RequestHandler<List<MovieModel>> handler) {
        Uri.Builder uriBuilder = baseUri();
        uriBuilder.path(PATH_TOP_RATED)
                .appendQueryParameter(PARAM_PAGE, "" + page);

        this.request(uriBuilder, new MoviesRequestHandlerAdapter(handler));
    }

    @Override
    public void getMostPopular(int page, RequestHandler<List<MovieModel>> handler) {
        Uri.Builder uriBuilder = baseUri();
        uriBuilder.path(PATH_POPULAR)
                .appendQueryParameter(PARAM_PAGE, "" + page);

        this.request(uriBuilder, new MoviesRequestHandlerAdapter(handler));
    }

    @Override
    public void getFavorites(int page, RequestHandler<List<MovieModel>> handler) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    /**
     * Movie Resource
     */
    public static class Builder {
        // Attributes
        private final MovieRemoteDataProvider mDataProvider;

        public Builder(Context context) {
            mDataProvider = new MovieRemoteDataProvider(context);
        }

        /**
         * Sets Movies Database Host resource string
         * @param hostResource Host resource string
         */
        public Builder setHostResource(int hostResource) {
            mDataProvider.setHost(hostResource);
            return this;
        }

        /**
         * Sets Movies Database Api key resource string
         * @param apiKeyResource Api key resource string
         */
        public Builder setApiKeyResource(int apiKeyResource) {
            mDataProvider.setApiKey(apiKeyResource);
            return this;
        }

        public Builder setHost(String host) {
            mDataProvider.setHost(host);
            return this;
        }

        public Builder setApiKey(String apiKey) {
            mDataProvider.setApiKey(apiKey);
            return this;
        }

        /**
         * Builds
         * @return
         */
        public MovieRemoteDataProvider build() {
            return mDataProvider;
        }
    }

    public class MoviesRequestHandlerAdapter extends JsonRequestHandler {
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
            try {
                JSONArray results = response.getJSONArray("results");

                List<MovieModel> movies = MovieFactory.fromJsonArray(results);

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
}
