package com.example.biddlecompopularmoviesstage2take4.Utilities;

import com.example.biddlecompopularmoviesstage2take4.Model.MovieReview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieReviewJsonUtils {

    public static ArrayList<MovieReview> parsedMovieReviewsJson(String movieReviewJsonString) {

        try {

            //Getting a review object that will be populated later
            MovieReview review;

            //Creating a new movieReviewJsonObject
            JSONObject movieReviewJsonObject = new JSONObject(movieReviewJsonString);

            //Key value pairs for getting and parsing the JSON data
            JSONArray reviewArray = new JSONArray(movieReviewJsonObject.getString("results"));

            //Creating a new ArrayList
            ArrayList<MovieReview> movieReviewItems = new ArrayList<>();

            //For loop to cycle through the array grabbing all the string values we need to show in the app
            for (int i = 0; i < reviewArray.length(); i++) {
                String movieReviewItem = reviewArray.getString(i);
                //Creating a new MovieReview object
                JSONObject movieReviewJson = new JSONObject(movieReviewItem);

                //Getting the movie review strings at the different "key" areas and storing them in our review
                review = new MovieReview(
                        movieReviewJson.getString("author"),
                        movieReviewJson.getString("content")
                );
                //Adding all of our strings into the movieReviewItems ArrayList
                movieReviewItems.add(review);
            }
            //Returning the movieReviewItems ArrayList
            return movieReviewItems;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
