package com.example.biddlecompopularmoviesstage2take4.Utilities;

import android.net.Uri;

import com.example.biddlecompopularmoviesstage2take4.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    //Base URL for the Movie Database website.
    private final static String MOVIE_DATABASE_BASE_URL = "https://api.themoviedb.org/3/movie";

    //Search query parameter for displaying (sorting) the results by most popular movies.
    private final static String PARAM_QUERY_POPULAR = "popular";

    //Search query parameter for displaying (sorting) the results by top rated movies.
    private final static String PARAM_QUERY_TOP_RATED = "top_rated";

    //My API key for the Movie Database website
    private final static String BIDDLECOM_API_KEY = BuildConfig.BiddlecomApiKey;

    /**
     * Builds the URL used to query the Movie Database website.
     *
     * @return popularUrl - the complete URL used to query the Movie Database server that will return
     * the list of popular movies.
     */
    public static URL buildPopularUrl() {
        Uri builtUri = Uri.parse(MOVIE_DATABASE_BASE_URL)
                .buildUpon()
                .appendPath(PARAM_QUERY_POPULAR)
                .appendQueryParameter("api_key", BIDDLECOM_API_KEY)
                .build();

        URL popularUrl = null;
        try {
            popularUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return popularUrl;
    }

    /**
     * Builds the URL used to query the Movie Database website.
     *
     * @return topRatedUrl - the complete URL used to query the Movie Database server that will return
     * the list of top-rated movies.
     */
    public static URL buildTopRatedUrl() {
        Uri builtUri = Uri.parse(MOVIE_DATABASE_BASE_URL)
                .buildUpon()
                .appendPath(PARAM_QUERY_TOP_RATED)
                .appendQueryParameter("api_key", BIDDLECOM_API_KEY)
                .build();

        URL topRatedUrl = null;
        try {
            topRatedUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return topRatedUrl;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
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
