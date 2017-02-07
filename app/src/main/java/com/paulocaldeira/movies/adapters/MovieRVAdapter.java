package com.paulocaldeira.movies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.paulocaldeira.movies.R;
import com.paulocaldeira.movies.data.MovieModel;
import com.squareup.picasso.Picasso;

/**
 * Movie Recycler View Adapter
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 1/31/17
 */

public class MovieRVAdapter extends InfiniteRVAdapter<MovieModel, MovieRVAdapter.MovieViewHolder> {

    // Attributes
    protected OnMovieItemClickListener mItemClickListener;

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Layout inflater
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflated view
        View view = layoutInflater.inflate(R.layout.item_movie, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        MovieModel movie = mItems.get(position);

        Picasso.with(context).load(context.getString(R.string.movies_database_img_path) + movie.getPosterUrl()).into(holder.mPoster);
    }

    /**
     * Sets item click listener
     * @param listener Listener
     */
    public void setItemClickListener(OnMovieItemClickListener listener) {
        mItemClickListener = listener;
    }

    /**
     * MovieModel Click Listener Interface
     */
    public interface OnMovieItemClickListener {
        /**
         * On MovieModel Click
         * @param movie MovieModel
         */
        void onMovieImageClicked(MovieModel movie);
    }

    /**
     * MovieModel View Holder
     */
    public final class MovieViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private ImageView mPoster;

        public MovieViewHolder(View itemView) {
            super(itemView);

            mPoster = (ImageView) itemView.findViewById(R.id.iv_poster);

            // Set click handler
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onMovieImageClicked(mItems.get(getAdapterPosition()));
            }
        }
    }
}
