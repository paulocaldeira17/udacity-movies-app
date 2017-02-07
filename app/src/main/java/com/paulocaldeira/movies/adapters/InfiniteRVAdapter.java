package com.paulocaldeira.movies.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Infinite Items Recycler View Adapter
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 1/31/17
 */

public abstract class InfiniteRVAdapter<T, VH extends RecyclerView.ViewHolder> extends
        RecyclerView.Adapter<VH> {

    // Attributes
    protected List<T> mItems;

    public InfiniteRVAdapter() {
        mItems = new ArrayList<>();
    }

    /**
     * Creates an adapter filling items
     * @param items Items
     */
    public InfiniteRVAdapter(List<T> items) {
        mItems = items;
    }

    @Override
    public int getItemCount() {
        if (mItems == null) {
            return 0;
        }

        return mItems.size();
    }

    public void clear() {
        if (mItems != null) {
            mItems.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * Adds an item
     * @param item Item
     */
    public void addItem(T item) {
        if (mItems != null && item != null) {
            mItems.add(item);
            notifyDataSetChanged();
        }
    }

    /**
     * Adds multiple items
     * @param items Items
     */
    public void addItems(List<T> items) {
        if (mItems != null && items != null) {
            mItems.addAll(items);
            notifyDataSetChanged();
        }
    }
}
