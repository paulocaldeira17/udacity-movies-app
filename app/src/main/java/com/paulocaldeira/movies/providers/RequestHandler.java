package com.paulocaldeira.movies.providers;

/**
 * Request Subscriber
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 2/2/17
 */
public interface RequestHandler<T> {
    /**
     * Before request happens
     */
    void beforeRequest();

    /**
     * On success response
     * @param response Response
     */
    void onSuccess(T response);

    /**
     * On error response
     * @param e Exception
     */
    void onError(Throwable e);

    /**
     * On complete request
     */
    void onComplete();
}
