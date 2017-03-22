package com.paulocaldeira.movies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paulocaldeira.movies.R;
import com.paulocaldeira.movies.data.MovieReview;

/**
 * MovieReview Recycler View Adapter
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 1/31/17
 */

public class MovieReviewRVAdapter extends InfiniteRVAdapter<MovieReview, MovieReviewRVAdapter.MovieReviewViewHolder> {
    @Override
    public MovieReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Layout inflater
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflated view
        View view = layoutInflater.inflate(R.layout.item_movie_review, parent, false);

        return new MovieReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieReviewViewHolder holder, int position) {
        MovieReview review = mItems.get(position);

        holder.mAuthor.setText(review.getAuthor());
        holder.mContent.setText(review.getContent());
    }

    /**
     * Movie View Holder
     */
    public final class MovieReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView mAuthor;
        private TextView mContent;

        public MovieReviewViewHolder(View itemView) {
            super(itemView);

            mAuthor = (TextView) itemView.findViewById(R.id.tv_review_author);
            mContent = (TextView) itemView.findViewById(R.id.tv_review_content);
        }
    }
}
