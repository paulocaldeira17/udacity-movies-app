package com.paulocaldeira.movies.helpers;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Grid layout helper
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 3/27/17
 */

public class GridLayoutHelper {
    /**
     * Caculate number of columns on a grid layout
     * @param context Context
     * @return Number of columns
     */
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        return noOfColumns;
    }
}
