package com.paulocaldeira.movies;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paulocaldeira.movies.data.Movie;
import com.paulocaldeira.movies.helpers.FormatHelper;
import com.paulocaldeira.movies.providers.FavoriteMovieDataProvider;
import com.paulocaldeira.movies.providers.MovieDetailsDataProvider;
import com.paulocaldeira.movies.providers.MovieRemoteDataProvider;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {
    // Constants
    private static final String TAG = "#" + DetailsActivity.class.getSimpleName();
    private static final int ANIMATION_DURATION = 1000; // 1 second

    // Extra
    public static String EXTRA_MOVIE = "extraMovie";

    // Layout
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout mCollapsingLayout;
    @BindView(R.id.iv_backdrop) ImageView mBackdropImageView;
    @BindView(R.id.iv_small_poster) ImageView mPosterImageView;
    @BindView(R.id.tv_synopsis) TextView mSynopsisTextView;
    @BindView(R.id.tv_original_title) TextView mTitleTextView;
    @BindView(R.id.tv_year) TextView mYearTextView;
    @BindView(R.id.tv_rate) TextView mRateTextView;
    @BindView(R.id.ll_rate_bar) LinearLayout mRateBarLayout;

    // Attributes
    private Movie mMovie;
    private FavoriteMovieDataProvider mFavoriteProvider;
    private MovieDetailsDataProvider mProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Initialize ButterKnife library
        ButterKnife.bind(this);

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Favorites provider
        mFavoriteProvider = new FavoriteMovieDataProvider(this);

        // Movies Remote Provider
        MovieRemoteDataProvider.Builder dataProviderBuilder = new MovieRemoteDataProvider.Builder(this);
        mProvider = dataProviderBuilder
                .setHostResource(R.string.movies_database_api_host)
                .setApiKeyResource(R.string.movies_database_api_key)
                .build();

        // Movie passed through extra
        mMovie = getExtraMovie();
        mMovie.setFavorite(mFavoriteProvider.isFavorite(mMovie));

        fillLayout(mMovie);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);

        MenuItem favoriteItem = menu.findItem(R.id.action_favorite);
        if (null != favoriteItem) {
            favoriteItem.setIcon(mMovie.isFavorite() ?
                    R.drawable.ic_favorite_white_24dp :
                    R.drawable.ic_favorite_border_white_24dp);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_favorite:
                mMovie.setFavorite(!mMovie.isFavorite());

                item.setIcon(mMovie.isFavorite() ?
                        R.drawable.ic_favorite_white_24dp :
                        R.drawable.ic_favorite_border_white_24dp);

                if (mMovie.isFavorite()) {
                    mFavoriteProvider.save(mMovie);
                } else {
                    mFavoriteProvider.remove(mMovie);
                }

                Toast.makeText(this, mMovie.isFavorite() ?
                        getString(R.string.added_to_favorites) :
                        getString(R.string.removed_from_favorites), Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Fills layout components
     * @param movie Movie Model
     */
    private void fillLayout(Movie movie) {
        String releaseDate = FormatHelper.formatYear(movie.getReleaseDate());
        String title = String.format("%s (%s)", movie.getTitle(), releaseDate);

        String imgPath = getString(R.string.movies_database_img_path);

        // Load backdrop image
        Picasso.with(this)
                .load(imgPath + movie.getBackdropUrl())
                .into(mBackdropImageView);

        // Load poster image
        Picasso.with(this)
                .load(imgPath + movie.getPosterUrl())
                .placeholder(R.drawable.ic_image_grey_400_48dp)
                .error(R.drawable.ic_broken_image_grey_400_48dp)
                .into(mPosterImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        mPosterImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }

                    @Override
                    public void onError() {
                        mPosterImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    }
                });

        mCollapsingLayout.setTitle(title);
        mTitleTextView.setText(movie.getTitle());
        mSynopsisTextView.setText(movie.getSynopsis());
        mYearTextView.setText(releaseDate);

        // Creates an animation
        animateWithRate(movie.getRate());
    }

    /**
     * Returns extra movie
     * @return Movie model
     */
    private Movie getExtraMovie() {
        Intent intent = getIntent();
        if (intent != null) {

            if (intent.hasExtra(EXTRA_MOVIE)) {
                return intent.getParcelableExtra(EXTRA_MOVIE);
            }
        }

        return null;
    }

    /**
     * Increases rate bar
     * @param rate Rate
     */
    private void animateWithRate(double rate){
        ValueAnimator va = ValueAnimator.ofFloat(0f, (float) rate);
        va.setDuration(ANIMATION_DURATION);
        va.setInterpolator(new DecelerateInterpolator());

        // Animate bar
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) mRateBarLayout.getLayoutParams();
                p.weight = (Float) animation.getAnimatedValue();
                mRateBarLayout.getParent().requestLayout();
            }
        });

        // Animate rate text
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                String rateString = String.format("%.1f", animation.getAnimatedValue());
                mRateTextView.setText("" + rateString);
            }
        });

        va.start();
    }
}
