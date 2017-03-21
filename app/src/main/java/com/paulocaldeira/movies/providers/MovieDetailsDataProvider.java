package com.paulocaldeira.movies.providers;

import com.paulocaldeira.movies.data.MovieReview;
import com.paulocaldeira.movies.data.MovieVideo;

import java.util.List;

/**
 * Movie Data Provider
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 1/31/17
 */

public interface MovieDetailsDataProvider {
    /**
     * Returns movie videos (trailers)
     * @param movieId Movie identification
     * @param handler Request handler
     * @return Movie videos
     */
    void getVideos(long movieId, RequestHandler<List<MovieVideo>> handler);

    /**
     * Returns movie user reviews
     * @param movieId Movie identification
     * @param page Page number
     * @param handler Request handler
     * @return User reviews
     */
    void getReviews(long movieId, int page, RequestHandler<List<MovieReview>> handler);
}
