package com.paulocaldeira.movies.providers;

import android.content.Context;
import android.net.Uri;

import com.paulocaldeira.movies.R;
import com.paulocaldeira.movies.factories.MovieFactory;
import com.paulocaldeira.movies.data.Movie;
import com.paulocaldeira.movies.data.MovieReview;
import com.paulocaldeira.movies.data.MovieVideo;
import com.paulocaldeira.movies.factories.MovieReviewFactory;
import com.paulocaldeira.movies.factories.MovieVideoFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Host Movie Data Provider
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 1/31/17
 */
public final class MovieRemoteDataProvider extends RemoteDataProvider implements
        MovieDataProvider,
        MovieDetailsDataProvider {

    // Constants
    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_PAGE = "page";

    // Paths
    private static final String PATH_VERSION = "/3";
    private static final String PATH_MOVIE = PATH_VERSION + "/movie";
    private static final String PATH_POPULAR = PATH_MOVIE + "/popular";
    private static final String PATH_TOP_RATED = PATH_MOVIE + "/top_rated";
    private static final String PARTIAL_PATH_VIDEOS = "videos";
    private static final String PARTIAL_PATH_REVIEWS = "reviews";

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
    public void getTopRated(int page, final RequestHandler<List<Movie>> handler) {
        Uri.Builder uriBuilder = baseUri();
        uriBuilder.path(PATH_TOP_RATED)
                .appendQueryParameter(PARAM_PAGE, "" + page);

        this.request(uriBuilder, new MovieJsonRequestHandlerAdapter(handler));
    }

    @Override
    public void getMostPopular(int page, final RequestHandler<List<Movie>> handler) {
        Uri.Builder uriBuilder = baseUri();
        uriBuilder.path(PATH_POPULAR)
                .appendQueryParameter(PARAM_PAGE, "" + page);

        this.request(uriBuilder, new MovieJsonRequestHandlerAdapter(handler));
    }

    @Override
    public void getFavorites(int page, RequestHandler<List<Movie>> handler) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public void getVideos(long movieId, RequestHandler<List<MovieVideo>> handler) {
        Uri.Builder uriBuilder = baseUri();
        uriBuilder.path(PATH_MOVIE)
                .appendEncodedPath("" + movieId)
                .appendEncodedPath(PARTIAL_PATH_VIDEOS);

        this.request(uriBuilder, new MovieVideoJsonRequestHandlerAdapter(handler));
    }

    @Override
    public void getReviews(long movieId, int page, RequestHandler<List<MovieReview>> handler) {
        Uri.Builder uriBuilder = baseUri();
        uriBuilder.path(PATH_MOVIE)
                .appendEncodedPath("" + movieId)
                .appendEncodedPath(PARTIAL_PATH_REVIEWS)
                .appendQueryParameter(PARAM_PAGE, "" + page);

        this.request(uriBuilder, new MovieReviewJsonRequestHandlerAdapter(handler));
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

    public abstract class BaseJsonRequestHandlerAdapter<T> extends JsonRequestHandler {
        // Attributes
        protected final RequestHandler<T> mHandler;

        public BaseJsonRequestHandlerAdapter(RequestHandler<T> handler) {
            mHandler = handler;
        }

        @Override
        public void beforeRequest() {
            mHandler.beforeRequest();
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

    public class MovieJsonRequestHandlerAdapter<T> extends BaseJsonRequestHandlerAdapter<List<Movie>> {
        public MovieJsonRequestHandlerAdapter(RequestHandler<List<Movie>> handler) {
            super(handler);
        }

        @Override
        public void onSuccess(JSONObject response) {
            try {
                JSONArray results = response.getJSONArray("results");

                List<Movie> movies = MovieFactory.fromJsonArray(results);

                mHandler.onSuccess(movies);
            } catch (Throwable e) {
                e.printStackTrace();
                mHandler.onError(e);
            }
        }
    }

    public class MovieReviewJsonRequestHandlerAdapter<T> extends BaseJsonRequestHandlerAdapter<List<MovieReview>> {
        public MovieReviewJsonRequestHandlerAdapter(RequestHandler<List<MovieReview>> handler) {
            super(handler);
        }

        @Override
        public void onSuccess(JSONObject response) {
            try {
                JSONArray results = response.getJSONArray("results");

                List<MovieReview> reviews = MovieReviewFactory.fromJsonArray(results);

                mHandler.onSuccess(reviews);
            } catch (Throwable e) {
                e.printStackTrace();
                mHandler.onError(e);
            }
        }
    }

    public class MovieVideoJsonRequestHandlerAdapter<T> extends BaseJsonRequestHandlerAdapter<List<MovieVideo>> {
        public MovieVideoJsonRequestHandlerAdapter(RequestHandler<List<MovieVideo>> handler) {
            super(handler);
        }

        @Override
        public void onSuccess(JSONObject response) {
            try {
                JSONArray results = response.getJSONArray("results");

                List<MovieVideo> videos = MovieVideoFactory.fromJsonArray(results);

                mHandler.onSuccess(videos);
            } catch (Throwable e) {
                e.printStackTrace();
                mHandler.onError(e);
            }
        }
    }
}
