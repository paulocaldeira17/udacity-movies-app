package com.paulocaldeira.movies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 3/19/17
 */

public class MoviesContract {
    // Constants
    public static final String CONTENT_AUTHORITY = "com.paulocaldeira.movies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class FavoriteMovieEntry implements BaseColumns {
        // table name
        public static final String TABLE_NAME = "favorite_movies";
        // columns
        public static final String _ID = "_id";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_USER_RATING = "user_rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER_URL = "poster_img_url";
        public static final String COLUMN_BACKDROP_URL = "backdrop_img_url";
        public static final String COLUMN_FAVORITE = "is_favorite";

        // create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAME).build();

        // create content uri
        public static final Uri CONTENT_URI_PAGE = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAME).appendPath("page").build();

        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // for building URIs on insertion
        public static Uri buildMoviesUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        // for building URIs base on page
        public static Uri buildMoviesPageUri(long page){
            return ContentUris.withAppendedId(CONTENT_URI_PAGE, page);
        }
    }
}
