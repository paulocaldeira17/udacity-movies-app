package com.paulocaldeira.movies.providers;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.util.MalformedJsonException;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Remote Data Provider
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 2/2/17
 */
public class RemoteDataProvider {
    private static final String TAG = "#" + RemoteDataProvider.class.getSimpleName();

    // Attributes
    protected final Context mContext;

    // Configs
    protected String mHost;

    /**
     * Initializes Movie remote data provider
     * @param context Context
     */
    public RemoteDataProvider(Context context) {
        mContext = context;
    }

    /**
     * Sets Movies Database Host resource string
     * @param hostResource Host resource string
     */
    public void setHost(int hostResource) {
        mHost = mContext.getString(hostResource);
    }


    public void setHost(String host) {
        mHost = host;
    }

    /**
     * Makes a request to an endpoint
     * @param uriBuilder Endpoint uri
     */
    public void request(final Uri.Builder uriBuilder, final JsonRequestHandler requestHandler) {
        if (!isOnline()) {
            requestHandler.onError(new NetworkErrorException());
            return;
        }

        NetworkTask requestTask = new NetworkTask() {
            @Override
            protected void onPreExecute() {
                requestHandler.beforeRequest();
            }

            @Override
            protected JSONObject doInBackground(URL... params) {
                URL searchUrl = params[0];
                JSONObject jsonObject = null;

                try {
                    Log.i(TAG, "Requesting " + uriBuilder.toString());

                    String results = this.getResponseFromHttpUrl(searchUrl);
                    jsonObject = new JSONObject(results);

                } catch (Throwable e) {
                    Log.e(TAG, "Error: " + e.getMessage());
                }

                return jsonObject;
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);

                if (jsonObject == null) {
                    requestHandler.onError(new MalformedJsonException("Invalid json response"));
                    return;
                } else {
                    Log.i(TAG, "Success: " + jsonObject.toString());
                    requestHandler.onSuccess(jsonObject);
                }

                requestHandler.onComplete();
            }
        };

        // Prepares uri
        URL url = null;
        try {
            url = this.prepareUrl(uriBuilder);
        } catch (MalformedURLException e) {
            requestHandler.onError(e);
        }

        requestTask.execute(url);
    }

    /**
     * Returns true if it is online
     * @return Is online
     */
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Returns base uri
     * @return Base uri
     */
    protected Uri.Builder baseUri() {
        return Uri.parse(mHost).buildUpon();
    }

    /**
     * Prepares url before execution
     * @param uriBuilder Uri Builder
     * @return Prepared url
     * @throws MalformedURLException Related to Malformed url
     */
    protected URL prepareUrl(Uri.Builder uriBuilder) throws MalformedURLException {
        Uri uri = uriBuilder.build();
        return new URL(uri.toString());
    }

    /**
     * Network Task
     * Responsible to retrieve data from a endpoint
     */
    public abstract class NetworkTask extends AsyncTask<URL, Void, JSONObject> {
        /**
         * This method returns the entire result from the HTTP response.
         *
         * @param url The URL to fetch the HTTP response from.
         * @return The contents of the HTTP response.
         * @throws IOException Related to network and stream reading
         */
        public String getResponseFromHttpUrl(URL url) throws IOException {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = urlConnection.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            } finally {
                urlConnection.disconnect();
            }
        }
    }
}
