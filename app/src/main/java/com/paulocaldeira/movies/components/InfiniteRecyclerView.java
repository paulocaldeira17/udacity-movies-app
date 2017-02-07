package com.paulocaldeira.movies.components;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import com.paulocaldeira.movies.listeners.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Infinite Recycler View
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 2/6/17
 */

public class InfiniteRecyclerView extends RecyclerView {

    // Attributes
    private EndlessRecyclerViewScrollListener mScrollListener;
    private List<OnLoadMoreListener> mLoadMoreListeners;

    /**
     * Infinite Recycler view
     * @param context Context
     * @param attrs Attributes Set
     */
    public InfiniteRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mLoadMoreListeners = new ArrayList<>();
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        // Removes previous listener
        if (mScrollListener != null) {
            removeOnScrollListener(mScrollListener);
            mScrollListener = null;
        }

        if (layout instanceof LinearLayoutManager) {
            mScrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) layout) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    notifyOnLoadMoreListeners(page, totalItemsCount);
                }
            };
        } else if (layout instanceof GridLayoutManager) {
            mScrollListener = new EndlessRecyclerViewScrollListener((GridLayoutManager) layout) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    notifyOnLoadMoreListeners(page, totalItemsCount);
                }
            };
        } else if (layout instanceof StaggeredGridLayoutManager) {
            mScrollListener = new EndlessRecyclerViewScrollListener((StaggeredGridLayoutManager) layout) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    notifyOnLoadMoreListeners(page, totalItemsCount);
                }
            };
        }

        // Adds the scroll listener to RecyclerView
        if (mScrollListener != null) {
            addOnScrollListener(mScrollListener);
        }

        super.setLayoutManager(layout);
    }

    /**
     * Add a load more listener
     * @param listener Listener
     */
    public void addLoadMoreListener(OnLoadMoreListener listener) {
        if (mLoadMoreListeners == null) {
            return;
        }

        mLoadMoreListeners.add(listener);
    }

    /**
     * Remove a load more listener
     * @param listener Listener
     */
    public void removeOnLoadMoreListener(OnLoadMoreListener listener) {
        if (mLoadMoreListeners == null) {
            return;
        }

        mLoadMoreListeners.remove(listener);
    }

    /**
     * Notifies load more listeners
     * @param page Page number
     * @param totalItemsCount Total items count
     */
    public void notifyOnLoadMoreListeners(int page, int totalItemsCount) {
        if (mLoadMoreListeners == null || mLoadMoreListeners.isEmpty()) {
            return;
        }

        for (OnLoadMoreListener listener : mLoadMoreListeners) {
            if (listener != null) {
                listener.onLoadMore(page, totalItemsCount);
            }
        }
    }

    /**
     * Reset current scroll listener state
     */
    public void resetState() {
        if (mScrollListener != null) {
            mScrollListener.resetState();
        }
    }

    /**
     * On load more listener
     */
    public interface OnLoadMoreListener {
        /**
         * On load more
         * @param page Page number
         * @param totalItemsCount Total items count
         */
        void onLoadMore(int page, int totalItemsCount);
    }
}
