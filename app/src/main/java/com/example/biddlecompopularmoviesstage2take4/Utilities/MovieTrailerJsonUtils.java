package com.example.biddlecompopularmoviesstage2take4.Utilities;

import com.example.biddlecompopularmoviesstage2take4.Model.MovieTrailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieTrailerJsonUtils {

    public static ArrayList<MovieTrailer> parsedMovieTrailerJson(String movieTrailerJsonString) {

        try {

            //Getting a trailer object that will be populated later
            MovieTrailer trailer;

            //Creating a new movieTrailerJsonObject
            JSONObject movieTrailerJsonObject = new JSONObject(movieTrailerJsonString);

            //Key value pairs for getting and parsing the JSON data
            JSONArray trailerArray = new JSONArray(movieTrailerJsonObject.getString("results"));

            //Creating a new ArrayList
            ArrayList<MovieTrailer> movieTrailerItems = new ArrayList<>();

            //For loop to cycle through the array grabbing all the string values we need to show in the app
            for (int i = 0; i < trailerArray.length(); i++) {
                String movieTrailerItem = trailerArray.getString(i);
                //Creating a new MovieTrailer object
                JSONObject movieTrailerJson = new JSONObject(movieTrailerItem);

                //Getting the movie trailer strings at the different "key" areas and storing them in our trailer
                trailer = new MovieTrailer(
                        movieTrailerJson.getString("name"),
                        movieTrailerJson.getString("key")
                );
                //Adding all of our strings into the movieTrailerItems ArrayList
                movieTrailerItems.add(trailer);
            }
            //Returning the movieTrailerItems ArrayList
            return movieTrailerItems;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
