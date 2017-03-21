package com.paulocaldeira.movies.factories;

import com.paulocaldeira.movies.data.Movie;
import com.paulocaldeira.movies.data.MovieReview;
import com.paulocaldeira.movies.helpers.FormatHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Movie Review Factory Class
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 1/31/17
 */
public final class MovieReviewFactory {
    // Constants
    public static String TAG = "#" + MovieReviewFactory.class.getSimpleName();

    /**
     * Creates a movie review from a json object
     * @param jsonObject Json object
     * @return Movie
     * @throws JSONException Json malformed
     * @throws ParseException Date malformed
     */
    public static MovieReview fromJsonObject(JSONObject jsonObject) throws
            JSONException, ParseException {

        String author = jsonObject.getString("author");
        String content = jsonObject.getString("content");

        return new MovieReview(author, content);
    }

    /**
     * Creates a movie review from a json object
     * @param jsonArray Json array
     * @return Movie
     * @throws JSONException Json malformed
     * @throws ParseException Date malformed
     */
    public static List<MovieReview> fromJsonArray(JSONArray jsonArray) throws
            JSONException, ParseException {

        List<MovieReview> reviews = new ArrayList<>();

        if (null != jsonArray) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject movieObject = jsonArray.getJSONObject(i);

                reviews.add(MovieReviewFactory.fromJsonObject(movieObject));
            }
        }

        return reviews;
    }
}
