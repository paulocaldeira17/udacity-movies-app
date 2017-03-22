package com.paulocaldeira.movies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paulocaldeira.movies.R;
import com.paulocaldeira.movies.data.MovieVideo;

/**
 * MovieVideo Recycler View Adapter
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 1/31/17
 */

public class MovieVideoRVAdapter extends InfiniteRVAdapter<MovieVideo, MovieVideoRVAdapter.MovieVideoViewHolder> {

    // Attributes
    protected OnMovieVideoItemClickListener mItemClickListener;

    @Override
    public MovieVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Layout inflater
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflated view
        View view = layoutInflater.inflate(R.layout.item_movie_video, parent, false);

        return new MovieVideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieVideoViewHolder holder, int position) {
        MovieVideo video = mItems.get(position);

        String fullName = String.format("[%s] %s", video.getType(), video.getName());
        holder.mName.setText(fullName);
    }

    /**
     * Sets item click listener
     * @param listener Listener
     */
    public void setItemClickListener(OnMovieVideoItemClickListener listener) {
        mItemClickListener = listener;
    }

    /**
     * MovieVideo Click Listener Interface
     */
    public interface OnMovieVideoItemClickListener {
        /**
         * On MovieVideo Click
         * @param video MovieVideo
         */
        void onMovieVideoClicked(MovieVideo video);
    }

    /**
     * Movie View Holder
     */
    public final class MovieVideoViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private TextView mName;

        public MovieVideoViewHolder(View itemView) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.tv_video_name);

            // Set click handler
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onMovieVideoClicked(mItems.get(getAdapterPosition()));
            }
        }
    }
}
