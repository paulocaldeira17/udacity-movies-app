package com.paulocaldeira.movies.factories;

import com.paulocaldeira.movies.data.MovieVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Movie Video Factory Class
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 1/31/17
 */
public final class MovieVideoFactory {
    // Constants
    public static String TAG = "#" + MovieVideoFactory.class.getSimpleName();

    /**
     * Creates a movie from a json object
     * @param jsonObject Json object
     * @return Movie
     * @throws JSONException Json malformed
     * @throws ParseException Date malformed
     */
    public static MovieVideo fromJsonObject(JSONObject jsonObject) throws
            JSONException, ParseException {

        String name = jsonObject.getString("name");
        String type = jsonObject.getString("type");
        String key = jsonObject.getString("key");

        return new MovieVideo(name, type, key);
    }

    /**
     * Creates a movie from a json object
     * @param jsonArray Json array
     * @return Movie
     * @throws JSONException Json malformed
     * @throws ParseException Date malformed
     */
    public static List<MovieVideo> fromJsonArray(JSONArray jsonArray) throws
            JSONException, ParseException {

        List<MovieVideo> videos = new ArrayList<>();

        if (null != jsonArray) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject movieObject = jsonArray.getJSONObject(i);

                videos.add(MovieVideoFactory.fromJsonObject(movieObject));
            }
        }

        return videos;
    }
}
