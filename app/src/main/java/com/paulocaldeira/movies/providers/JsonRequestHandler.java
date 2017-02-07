package com.paulocaldeira.movies.providers;

import org.json.JSONObject;

/**
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 2/3/17
 */

public abstract class JsonRequestHandler implements RequestHandler<JSONObject> {
    @Override
    public void beforeRequest() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
