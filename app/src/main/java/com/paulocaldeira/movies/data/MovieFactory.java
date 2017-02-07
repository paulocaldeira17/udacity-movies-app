package com.paulocaldeira.movies.data;

import android.util.Log;

import com.paulocaldeira.movies.helpers.FormatHelper;

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

        String title = jsonObject.getString("original_title");
        String synopsis = jsonObject.getString("overview");
        double rate = jsonObject.getDouble("vote_average");
        String posterUrl = jsonObject.getString("poster_path");
        String backdropUrl = jsonObject.getString("backdrop_path");

        String dateString = jsonObject.getString("release_date");

        Date releaseDate = FormatHelper.parseDate(dateString);

        MovieModel.Builder movieBuilder = new MovieModel.Builder();
        return movieBuilder
                .setTitle(title)
                .setSynopsis(synopsis)
                .setReleaseDate(releaseDate)
                .setRate(rate)
                .setPosterUrl(posterUrl)
                .setBackdropUrl(backdropUrl)
                .build();
    }

    /**
     * Generates a new movie
     * @return Movie
     */
    public static MovieModel generate() {
        MovieModel.Builder movieBuilder = new MovieModel.Builder();

        return movieBuilder
                .setTitle(generateTitle())
                .setSynopsis(generateDescription())
                .setReleaseDate(genereateYear())
                .setRate(generateRate())
                .build();
    }

    /**
     * Generates rate
     * @return Rate
     */
    private static double generateRate() {
        List<Double> rates = new ArrayList<>();
        rates.add(8.1d);
        rates.add(7.4d);
        rates.add(9.2d);
        rates.add(6.3d);

        return randDouble(rates);
    }

    /**
     * Generates year
     * @return Year
     */
    private static Date genereateYear() {
        List<Date> years = new ArrayList<>();
        years.add(new Date(2017, 1, 12));
        years.add(new Date(2016, 3, 23));
        years.add(new Date(2015, 8, 25));
        years.add(new Date(1991, 10, 01));

        return randDate(years);
    }

    /**
     * Generates description
     * @return Description
     */
    private static String generateDescription() {
        return "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod\n" +
                "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,\n" +
                "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo\n" +
                "consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse\n" +
                "cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non\n" +
                "proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    }

    /**
     * Generates title
     * @return Title
     */
    private static String generateTitle() {
        List<String> titles = new ArrayList<>();
        titles.add("Titanic");
        titles.add("Mirrors");
        titles.add("Mirrors 2");
        titles.add("American Pie - Reunion");
        titles.add("Lord of the Rings");

        return randString(titles);
    }

    /**
     * Random double
     * @param items Items
     * @return Random item
     */
    private static Double randDouble(List<Double> items) {
        Random r = new Random();
        int pos = r.nextInt(items.size());
        return items.get(pos);
    }

    /**
     * Random string
     * @param items Items
     * @return Random item
     */
    private static String randString(List<String> items) {
        Random r = new Random();
        int pos = r.nextInt(items.size());
        return items.get(pos);
    }

    /**
     * Random date
     * @param items Items
     * @return Random item
     */
    private static Date randDate(List<Date> items) {
        Random r = new Random();
        int pos = r.nextInt(items.size());
        return items.get(pos);
    }
}
