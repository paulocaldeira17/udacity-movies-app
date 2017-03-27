package com.paulocaldeira.movies;

import android.accounts.NetworkErrorException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.paulocaldeira.movies.helpers.GridLayoutHelper;
import com.paulocaldeira.movies.helpers.PreferencesHelper;
import com.paulocaldeira.movies.providers.FavoriteMovieDataProvider;
import com.paulocaldeira.movies.providers.MovieDataProvider;
import com.paulocaldeira.movies.providers.MovieRemoteDataProvider;
import com.paulocaldeira.movies.providers.RequestHandler;

import java.util.ArrayList;
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
    private static final int FIRST_PAGE = 1;

    // State
    private static final String STATE_RV_POSITION = "state_rv_scroll_position";
    private static final String STATE_MOVIES_LIST = "state_movies_list";
    private static final String STATE_CURRENT_PAGE_NUMBER = "state_current_page_number";
    private static final String STATE_CURRENT_MODE = "state_mode";

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
    private GridLayoutManager mLayoutManager;
    private Parcelable mLayoutManagerSavedState;

    // Providers
    private MovieDataProvider mProvider;
    private MovieRemoteDataProvider mRemoteDataProvider;
    private FavoriteMovieDataProvider mFavoriteDataProvider;

    // Attributes
    private String mCurrentMode;
    private int mPageNumber = FIRST_PAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize ButterKnife library
        ButterKnife.bind(this);

        mActionBar = getSupportActionBar();

        mLayoutManager = new GridLayoutManager(this, GridLayoutHelper.calculateNoOfColumns(this));
        mRecyclerView.setLayoutManager(mLayoutManager);

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

        // Initializes user shared preferences listener
        PreferencesHelper.registerUserSharedPreferencesListener(this, this);

        // Set current mode
        setCurrentMode(PreferencesHelper.getMoviesListMode(this, DEFAULT_MODE), null == savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Stores recycler view current position
        outState.putParcelable(STATE_RV_POSITION, mLayoutManager.onSaveInstanceState());

        // Stores movies list
        outState.putParcelableArrayList(STATE_MOVIES_LIST, (ArrayList) mAdapter.getItems());

        // Stores current page
        outState.putInt(STATE_CURRENT_PAGE_NUMBER, mPageNumber);

        // Stores current mode
        outState.putString(STATE_CURRENT_MODE, mCurrentMode);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Check for recycler view current position
        if (null != savedInstanceState) {
            // Recycler view last position
            if (savedInstanceState.containsKey(STATE_RV_POSITION)) {
                mLayoutManagerSavedState = savedInstanceState.getParcelable(STATE_RV_POSITION);
            }

            // Movies list
            if (savedInstanceState.containsKey(STATE_MOVIES_LIST)) {
                mAdapter.addItems((List) savedInstanceState.getParcelableArrayList(STATE_MOVIES_LIST));
            }

            // Current page number
            if (savedInstanceState.containsKey(STATE_CURRENT_PAGE_NUMBER)) {
                mPageNumber = savedInstanceState.getInt(STATE_CURRENT_PAGE_NUMBER);
            }

            // Current mode
            if (savedInstanceState.containsKey(STATE_CURRENT_MODE)) {
                mCurrentMode = savedInstanceState.getString(STATE_CURRENT_MODE);
            }
        }
    }

    /**
     * Sets current mode
     * @param mode Mode
     * @param forceLoad Forces load
     */
    private void setCurrentMode(String mode, boolean forceLoad) {
        if (mCurrentMode != mode) {
            // Set current mode
            mCurrentMode = mode;

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

            if (forceLoad) {
                reloadMovies();
            }
        }
    }

    /**
     * Sets current mode
     * @param mode Mode
     */
    private void setCurrentMode(String mode) {
        setCurrentMode(mode, false);
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
        // Restart page number
        mPageNumber = FIRST_PAGE;
        // Loads first page
        loadMovies(mPageNumber);
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
        loadMovies(++mPageNumber);
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
            setCurrentMode(sharedPreferences.getString(s, DEFAULT_MODE), true);
        }
    }

    /**
     * Movies request Handler
     */
    private class MoviesRequestHandler implements RequestHandler<List<Movie>> {
        @Override
        public void beforeRequest() {
            mLayoutManagerSavedState = null;
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

            if (mLayoutManagerSavedState != null) {
                mLayoutManager.onRestoreInstanceState(mLayoutManagerSavedState);
            }
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
