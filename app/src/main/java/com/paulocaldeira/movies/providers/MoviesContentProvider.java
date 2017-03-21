package com.paulocaldeira.movies.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.paulocaldeira.movies.R;
import com.paulocaldeira.movies.data.MoviesContract;
import com.paulocaldeira.movies.helpers.MoviesDBHelper;

/**
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 3/19/17
 */

public class MoviesContentProvider extends ContentProvider {

    private static final String LOG_TAG = MoviesContentProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDBHelper mDBHelper;

    // Codes for the UriMatcher //////
    private static final int MOVIE = 100;
    private static final int MOVIE_WITH_ID = 101;
    private static final int MOVIE_BY_PAGE = 102;
    ////////

    private static UriMatcher buildUriMatcher() {
        // Build a UriMatcher by adding a specific code to return based on a match
        // It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        // add a code for each type of URI you want
        matcher.addURI(authority, MoviesContract.FavoriteMovieEntry.TABLE_NAME, MOVIE);
        matcher.addURI(authority, MoviesContract.FavoriteMovieEntry.TABLE_NAME + "/#", MOVIE_WITH_ID);
        matcher.addURI(authority, MoviesContract.FavoriteMovieEntry.TABLE_NAME + "/page/#", MOVIE_BY_PAGE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDBHelper = new MoviesDBHelper(getContext());

        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE:
                return MoviesContract.FavoriteMovieEntry.CONTENT_DIR_TYPE;

            case MOVIE_BY_PAGE:
                return MoviesContract.FavoriteMovieEntry.CONTENT_DIR_TYPE;

            case MOVIE_WITH_ID:
                return MoviesContract.FavoriteMovieEntry.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch(sUriMatcher.match(uri)) {
            // All Movies selected
            case MOVIE:
                retCursor = mDBHelper.getReadableDatabase().query(
                        MoviesContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                return retCursor;

            // All Movies paginated
            case MOVIE_BY_PAGE:
                int page   = Integer.parseInt(uri.getLastPathSegment());
                int limit  = getContext().getResources().getInteger(R.integer.items_per_page);
                int offset = limit * (page - 1);

                String limitString = offset + "," + limit;

                retCursor = mDBHelper.getReadableDatabase().query(
                        MoviesContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder,
                        limitString);

                return retCursor;

            // Individual flavor based on Id selected
            case MOVIE_WITH_ID:
                retCursor = mDBHelper.getReadableDatabase().query(
                        MoviesContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        MoviesContract.FavoriteMovieEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                return retCursor;

            default:
                // By default, we assume a bad URI
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                long _id = db.insert(MoviesContract.FavoriteMovieEntry.TABLE_NAME, null, values);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    returnUri = MoviesContract.FavoriteMovieEntry.buildMoviesUri(_id);
                } else {
                    throw new SQLException("Failed to insert row into: " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numDeleted;

        switch(match) {
            case MOVIE:
                numDeleted = db.delete(
                        MoviesContract.FavoriteMovieEntry.TABLE_NAME, selection, selectionArgs);
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MoviesContract.FavoriteMovieEntry.TABLE_NAME + "'");
                break;
            case MOVIE_WITH_ID:
                numDeleted = db.delete(MoviesContract.FavoriteMovieEntry.TABLE_NAME,
                        MoviesContract.FavoriteMovieEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MoviesContract.FavoriteMovieEntry.TABLE_NAME + "'");

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return numDeleted;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch(match) {
            case MOVIE:
                // allows for multiple transactions
                db.beginTransaction();

                // keep track of successful inserts
                int numInserted = 0;
                try {
                    for (ContentValues value : values) {
                        if (value == null) {
                            throw new IllegalArgumentException("Cannot have null content values");
                        }
                        long _id = -1;
                        try {
                            _id = db.insertOrThrow(MoviesContract.FavoriteMovieEntry.TABLE_NAME,
                                    null, value);
                        } catch(SQLiteConstraintException e) {
                            Log.w(LOG_TAG, "Attempting to insert " +
                                    value.getAsString(
                                            MoviesContract.FavoriteMovieEntry.COLUMN_TITLE)
                                    + " but value is already in database.");
                        }
                        if (_id != -1) {
                            numInserted++;
                        }
                    }
                    if(numInserted > 0) {
                        // If no errors, declare a successful transaction.
                        // database will not populate if this is not called
                        db.setTransactionSuccessful();
                    }
                } finally {
                    // all transactions occur at once
                    db.endTransaction();
                }
                if (numInserted > 0) {
                    // if there was successful insertion, notify the content resolver that there
                    // was a change
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return numInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int numUpdated = 0;

        if (contentValues == null) {
            throw new IllegalArgumentException("Cannot have null content values");
        }

        switch(sUriMatcher.match(uri)) {
            case MOVIE:
                numUpdated = db.update(MoviesContract.FavoriteMovieEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            case MOVIE_WITH_ID:
                numUpdated = db.update(MoviesContract.FavoriteMovieEntry.TABLE_NAME,
                        contentValues,
                        MoviesContract.FavoriteMovieEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }
}
