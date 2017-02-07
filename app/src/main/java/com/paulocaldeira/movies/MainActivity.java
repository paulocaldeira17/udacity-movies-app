package com.paulocaldeira.movies;

import android.accounts.NetworkErrorException;
import android.content.Intent;
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
import com.paulocaldeira.movies.data.MovieModel;
import com.paulocaldeira.movies.providers.MovieDataProvider;
import com.paulocaldeira.movies.providers.MovieRemoteDataProvider;
import com.paulocaldeira.movies.providers.RequestHandler;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        MovieRVAdapter.OnMovieItemClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        InfiniteRecyclerView.OnLoadMoreListener {

    // Constants
    private static final String TAG = "#" + MainActivity.class.getSimpleName();
    private static final int DEFAULT_SPAN = 2;
    private static final int FIRST_PAGE = 1;

    // Layout
    private InfiniteRecyclerView mRecyclerView;
    private MovieRVAdapter mAdapter;
    private MovieDataProvider mProvider;
    private ActionBar mActionBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout mNoInternetLayout;
    private LinearLayout mRequestErrorLayout;

    // Attributes
    private enum Mode {
        TOP_RATED,
        MOST_POPULAR
    }

    private Mode mCurrentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActionBar = getSupportActionBar();

        mRecyclerView = (InfiniteRecyclerView) findViewById(R.id.rv_movies_images);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_loader);
        mNoInternetLayout = (LinearLayout) findViewById(R.id.ll_no_internet);
        mRequestErrorLayout = (LinearLayout) findViewById(R.id.ll_request_error);

        GridLayoutManager gridLayout = new GridLayoutManager(this, DEFAULT_SPAN);
        mRecyclerView.setLayoutManager(gridLayout);

        // Movies Provider
        MovieRemoteDataProvider.Builder dataProviderBuilder = new MovieRemoteDataProvider.Builder(this);
        mProvider = dataProviderBuilder
                .setHostResource(R.string.movies_database_api_host)
                .setApiKeyResource(R.string.movies_database_api_key)
                .build();

        // mProvider = new LocalMovieDataProvider(this);

        // Swipe to refresh listener
        mSwipeRefreshLayout.setOnRefreshListener(this);

        // Creates adapter
        mAdapter = new MovieRVAdapter();
        mRecyclerView.setAdapter(mAdapter);

        // Add click listener
        mAdapter.setItemClickListener(this);

        // Infinite on scroll listener
        mRecyclerView.addLoadMoreListener(MainActivity.this);

        // Set first mode
        setCurrentMode(Mode.MOST_POPULAR);
    }

    /**
     * Sets current mode
     * @param mode Mode
     */
    private void setCurrentMode(Mode mode) {
        if (mCurrentMode != mode) {
            // Set current mode
            mCurrentMode = mode;

            // Clears adapter current items
            mAdapter.clear();

            // Resets recycler view infinite scroll current state
            mRecyclerView.resetState();

            switch (mCurrentMode) {
                case MOST_POPULAR:
                    mActionBar.setTitle(R.string.most_popular);
                    break;
                case TOP_RATED:
                    mActionBar.setTitle(R.string.top_rated);
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
                setCurrentMode(Mode.MOST_POPULAR);
                return true;
            case R.id.action_top_rated:
                setCurrentMode(Mode.TOP_RATED);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Reloads current mode movies
     */
    private void reloadMovies() {
        loadMovies(FIRST_PAGE);
    }

    /**
     * Loads current mode movies by page
     * @param page Page number
     */
    private void loadMovies(int page) {
        switch (mCurrentMode) {
            case MOST_POPULAR:
                loadMostPopularMovies(page);
                break;
            case TOP_RATED:
                loadTopRatedMovies(page);
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
    }

    private void showError() {
        mNoInternetLayout.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mRequestErrorLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Shows no internet alert
     */
    private void showNoInternet() {
        mNoInternetLayout.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mRequestErrorLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onMovieImageClicked(MovieModel movie) {
        Intent intent = new Intent(this, DetailsActivity.class);

        // Pass movie (parceable) through extra
        intent.putExtra(DetailsActivity.EXTRA_MOVIE, movie);

        startActivity(intent);
    }

    /**
     * Movies request Handler
     */
    private class MoviesRequestHandler implements RequestHandler<List<MovieModel>> {
        @Override
        public void beforeRequest() {
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        public void onSuccess(List<MovieModel> movies) {
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
