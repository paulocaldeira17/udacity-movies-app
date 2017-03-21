package com.paulocaldeira.movies;

import android.accounts.NetworkErrorException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.paulocaldeira.movies.adapters.MovieRVAdapter;
import com.paulocaldeira.movies.components.InfiniteRecyclerView;
import com.paulocaldeira.movies.data.Movie;
import com.paulocaldeira.movies.helpers.PreferencesHelper;
import com.paulocaldeira.movies.providers.FavoriteMovieDataProvider;
import com.paulocaldeira.movies.providers.MovieDataProvider;
import com.paulocaldeira.movies.providers.MovieRemoteDataProvider;
import com.paulocaldeira.movies.providers.RequestHandler;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        MovieRVAdapter.OnMovieItemClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        InfiniteRecyclerView.OnLoadMoreListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    // Constants
    private static final String TAG = "#" + MainActivity.class.getSimpleName();
    private static final int DEFAULT_SPAN = 2;
    private static final int FIRST_PAGE = 1;

    // List mode
    private static final String MODE_POPULAR = "popular";
    private static final String MODE_TOP_RATED = "top_rated";
    private static final String MODE_FAVORITES = "favorites";
    private static final String DEFAULT_MODE = MODE_POPULAR;

    // Layout
    @BindView(R.id.rv_movies_images) InfiniteRecyclerView mRecyclerView;
    @BindView(R.id.srl_loader) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.ll_no_internet) LinearLayout mNoInternetLayout;
    @BindView(R.id.ll_request_error) LinearLayout mRequestErrorLayout;
    @BindView(R.id.ll_no_results) LinearLayout mNoResultsLayout;

    private MovieRVAdapter mAdapter;
    private ActionBar mActionBar;

    // Providers
    private MovieDataProvider mProvider;
    private MovieRemoteDataProvider mRemoteDataProvider;
    private FavoriteMovieDataProvider mFavoriteDataProvider;

    // List mode
    private String mCurrentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize ButterKnife library
        ButterKnife.bind(this);

        mActionBar = getSupportActionBar();

        GridLayoutManager gridLayout = new GridLayoutManager(this, DEFAULT_SPAN);
        mRecyclerView.setLayoutManager(gridLayout);

        // Movies Remote Provider
        MovieRemoteDataProvider.Builder dataProviderBuilder = new MovieRemoteDataProvider.Builder(this);
        mRemoteDataProvider = dataProviderBuilder
                .setHostResource(R.string.movies_database_api_host)
                .setApiKeyResource(R.string.movies_database_api_key)
                .build();

        mFavoriteDataProvider = new FavoriteMovieDataProvider(this);

        // Movies Local Provider
        // mProvider = new LocalMovieDataProvider(this);

        // Swipe to refresh listener
        mSwipeRefreshLayout.setOnRefreshListener(this);

        // Creates adapter
        mAdapter = new MovieRVAdapter();
        mRecyclerView.setAdapter(mAdapter);

        // Add click listener
        mAdapter.setItemClickListener(this);

        // Infinite on scroll listener
        mRecyclerView.addLoadMoreListener(this);

        // Set first mode
        PreferencesHelper.registerUserSharedPreferencesListener(this, this);
        setCurrentMode(PreferencesHelper.getMoviesListMode(this, DEFAULT_MODE));
    }

    /**
     * Sets current mode
     * @param mode Mode
     */
    private void setCurrentMode(String mode) {
        if (mCurrentMode != mode) {
            // Set current mode
            mCurrentMode = mode;

            // Resets recycler view infinite scroll current state
            mRecyclerView.resetState();

            switch (mCurrentMode) {
                case MODE_POPULAR:
                    mActionBar.setTitle(R.string.most_popular);
                    mProvider = mRemoteDataProvider;
                    break;
                case MODE_TOP_RATED:
                    mActionBar.setTitle(R.string.top_rated);
                    mProvider = mRemoteDataProvider;
                    break;
                case MODE_FAVORITES:
                    mActionBar.setTitle(R.string.favorites);
                    mProvider = mFavoriteDataProvider;
                    break;
            }

            reloadMovies();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        // Inflates movies menu
        inflater.inflate(R.menu.menu_movies, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_most_popular:
                PreferencesHelper.setMoviesListMode(this, MODE_POPULAR);
                return true;
            case R.id.action_top_rated:
                PreferencesHelper.setMoviesListMode(this, MODE_TOP_RATED);
                return true;
            case R.id.action_favorite:
                PreferencesHelper.setMoviesListMode(this, MODE_FAVORITES);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Reloads current mode movies
     */
    private void reloadMovies() {
        // Clears adapter current items
        mAdapter.clear();

        loadMovies(FIRST_PAGE);
    }

    /**
     * Loads current mode movies by page
     * @param page Page number
     */
    private void loadMovies(int page) {
        switch (mCurrentMode) {
            case MODE_POPULAR:
                loadMostPopularMovies(page);
                break;
            case MODE_TOP_RATED:
                loadTopRatedMovies(page);
                break;
            case MODE_FAVORITES:
                loadFavoriteMovies(page);
                break;
        }
    }

    /**
     * Loads most rated movies
     * @param page Page number
     */
    private void loadTopRatedMovies(int page) {
        mProvider.getTopRated(page, new MoviesRequestHandler());
    }

    /**
     * Loads popular movies
     * @param page Page number
     */
    private void loadMostPopularMovies(int page) {
        mProvider.getMostPopular(page, new MoviesRequestHandler());
    }

    /**
     * Loads favorite movies
     * @param page Page number
     */
    private void loadFavoriteMovies(int page) {
        mProvider.getFavorites(page, new MoviesRequestHandler());
    }

    @Override
    public void onRefresh() {
        reloadMovies();
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        loadMovies(page);
    }

    /**
     * Show items
     */
    private void showItems() {
        mNoInternetLayout.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRequestErrorLayout.setVisibility(View.INVISIBLE);
        mNoResultsLayout.setVisibility(View.INVISIBLE);
    }

    private void showError() {
        mNoInternetLayout.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mRequestErrorLayout.setVisibility(View.VISIBLE);
        mNoResultsLayout.setVisibility(View.INVISIBLE);
    }

    /**
     * Shows no internet alert
     */
    private void showNoInternet() {
        mNoInternetLayout.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mRequestErrorLayout.setVisibility(View.INVISIBLE);
        mNoResultsLayout.setVisibility(View.INVISIBLE);
    }

    /**
     * Shows no internet alert
     */
    private void showNoResults() {
        mNoInternetLayout.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mRequestErrorLayout.setVisibility(View.INVISIBLE);
        mNoResultsLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMovieImageClicked(Movie movie) {
        Intent intent = new Intent(this, DetailsActivity.class);

        // Pass movie (parceable) through extra
        intent.putExtra(DetailsActivity.EXTRA_MOVIE, movie);

        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(PreferencesHelper.PREF_MOVIES_LIST_MODE)) {
            setCurrentMode(sharedPreferences.getString(s, DEFAULT_MODE));
        }
    }

    /**
     * Movies request Handler
     */
    private class MoviesRequestHandler implements RequestHandler<List<Movie>> {
        @Override
        public void beforeRequest() {
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        public void onSuccess(List<Movie> movies) {
            if (mAdapter.getItemCount() == 0 && null != movies && movies.isEmpty()) {
                showNoResults();
                return;
            }

            showItems();
            mAdapter.addItems(movies);
        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof NetworkErrorException) {
                showNoInternet();
            } else {
                showError();
            }
        }

        @Override
        public void onComplete() {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
