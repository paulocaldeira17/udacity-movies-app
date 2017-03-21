package com.paulocaldeira.movies.providers;

import com.paulocaldeira.movies.data.MovieModel;

import java.util.List;

/**
 * Movie Data Provider
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 1/31/17
 */

public interface MovieDataProvider {
    /**
     * Returns top rated movies
     * @return Top rated movies
     */
    void getTopRated(int page, RequestHandler<List<MovieModel>> handler);

    /**
     * Returns most popular movies
     * @return Most popular movies
     */
    void getMostPopular(int page, RequestHandler<List<MovieModel>> handler);

    /**
     * Returns favorite movies
     * @return Favorite movies
     */
    void getFavorites(int page, RequestHandler<List<MovieModel>> handler);
}
