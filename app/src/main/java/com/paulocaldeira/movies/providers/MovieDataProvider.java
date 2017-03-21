package com.paulocaldeira.movies.providers;

import com.paulocaldeira.movies.data.Movie;

import java.util.List;

/**
 * Movie Data Provider
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 1/31/17
 */

public interface MovieDataProvider {
    /**
     * Returns top rated movies
     * @param page Page number
     * @param handler Request handler
     * @return Top rated movies
     */
    void getTopRated(int page, RequestHandler<List<Movie>> handler);

    /**
     * Returns most popular movies
     * @param page Page number
     * @param handler Request handler
     * @return Most popular movies
     */
    void getMostPopular(int page, RequestHandler<List<Movie>> handler);

    /**
     * Returns favorite movies
     * @param page Page number
     * @param handler Request handler
     * @return Favorite movies
     */
    void getFavorites(int page, RequestHandler<List<Movie>> handler);
}
