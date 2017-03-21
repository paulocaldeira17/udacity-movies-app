package com.paulocaldeira.movies.data;

import android.database.Cursor;

import com.paulocaldeira.movies.helpers.FormatHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Movie Model Factory Class
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 1/31/17
 */
public final class MovieFactory {
    // Constants
    public static String TAG = "#" + MovieFactory.class.getSimpleName();

    /**
     * Creates a movie from a json object
     * @param jsonObject Json object
     * @return Movie
     * @throws JSONException Json malformed
     * @throws ParseException Date malformed
     */
    public static MovieModel fromJsonObject(JSONObject jsonObject) throws
            JSONException, ParseException {

        long id = jsonObject.getLong("id");
        String title = jsonObject.getString("original_title");
        String synopsis = jsonObject.getString("overview");
        double rate = jsonObject.getDouble("vote_average");
        String posterUrl = jsonObject.getString("poster_path");
        String backdropUrl = jsonObject.getString("backdrop_path");
        String dateString = jsonObject.getString("release_date");
        Date releaseDate = FormatHelper.parseDate(dateString);

        MovieModel.Builder movieBuilder = new MovieModel.Builder();
        return movieBuilder
                .setId(id)
                .setTitle(title)
                .setSynopsis(synopsis)
                .setReleaseDate(releaseDate)
                .setRate(rate)
                .setPosterUrl(posterUrl)
                .setBackdropUrl(backdropUrl)
                .build();
    }

    /**
     * Creates a movie from a json object
     * @param jsonArray Json array
     * @return Movie
     * @throws JSONException Json malformed
     * @throws ParseException Date malformed
     */
    public static List<MovieModel> fromJsonArray(JSONArray jsonArray) throws
            JSONException, ParseException {

        List<MovieModel> movies = new ArrayList<>();

        if (null != jsonArray) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject movieObject = jsonArray.getJSONObject(i);

                movies.add(MovieFactory.fromJsonObject(movieObject));
            }
        }

        return movies;
    }

    /**
     * Creates a movie from a database cursor
     * @param cursor Cursor
     * @return Movie
     */
    public static MovieModel fromCursor(Cursor cursor) {
        if (null == cursor) {
            return null;
        }

        int idCol = cursor.getColumnIndex(MoviesContract.FavoriteMovieEntry.COLUMN_ID);
        int titleCol = cursor.getColumnIndex(MoviesContract.FavoriteMovieEntry.COLUMN_TITLE);
        int synopsisCol = cursor.getColumnIndex(MoviesContract.FavoriteMovieEntry.COLUMN_SYNOPSIS);
        int userRatingCol = cursor.getColumnIndex(MoviesContract.FavoriteMovieEntry.COLUMN_USER_RATING);
        int releaseDateCol = cursor.getColumnIndex(MoviesContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE);
        int posterImgUrlCol = cursor.getColumnIndex(MoviesContract.FavoriteMovieEntry.COLUMN_POSTER_URL);
        int backdropImgUrlCol = cursor.getColumnIndex(MoviesContract.FavoriteMovieEntry.COLUMN_BACKDROP_URL);
        int isFavoriteCol = cursor.getColumnIndex(MoviesContract.FavoriteMovieEntry.COLUMN_FAVORITE);

        long id = cursor.getLong(idCol);
        String title = cursor.getString(titleCol);
        String synopsis = cursor.getString(synopsisCol);
        double rate = cursor.getDouble(userRatingCol);
        String posterUrl = cursor.getString(posterImgUrlCol);
        String backdropUrl = cursor.getString(backdropImgUrlCol);
        boolean isFavorite = cursor.getInt(isFavoriteCol) != 0;

        String dateString = cursor.getString(releaseDateCol);

        Date releaseDate = FormatHelper.parseDate(dateString);

        MovieModel.Builder movieBuilder = new MovieModel.Builder();
        return movieBuilder
                .setId(id)
                .setTitle(title)
                .setSynopsis(synopsis)
                .setReleaseDate(releaseDate)
                .setRate(rate)
                .setPosterUrl(posterUrl)
                .setBackdropUrl(backdropUrl)
                .setFavorite(isFavorite)
                .build();
    }

    /**
     * Creates a movie from a database cursor
     * @param cursor Cursor
     * @return Movie
     */
    public static List<MovieModel> fromCursorMultiple(Cursor cursor) {
        List<MovieModel> movies = new ArrayList<>();

        if (null != cursor) {
            while (cursor.moveToNext()) {
                MovieModel model = MovieFactory.fromCursor(cursor);
                if (null != model) {
                    movies.add(model);
                }
            }
            cursor.close();
        }

        return movies;
    }
}
