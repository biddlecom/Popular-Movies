package com.example.biddlecompopularmoviesstage2take4.Utilities;

import com.example.biddlecompopularmoviesstage2take4.Model.MovieInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieInfoJsonUtils {

    public static ArrayList<MovieInfo> parsedMovieInfoJson(String movieInfoJsonString) {

        try {

            //Getting a movie object that will be populated later
            MovieInfo movie;

            //Creating a new movieInfoJsonObject
            JSONObject movieInfoJsonObject = new JSONObject(movieInfoJsonString);

            //Key value pairs for getting and parsing the JSON data
            JSONArray infoArray = new JSONArray(movieInfoJsonObject.getString("results"));

            //Creating a new ArrayList
            ArrayList<MovieInfo> movieInfoItems = new ArrayList<>();

            //For loop to cycle through the array grabbing all the string values we need to show in the app
            for (int i = 0; i < infoArray.length(); i++) {
                String movieInfoItem = infoArray.getString(i);
                //Creating a new MovieInfo object
                JSONObject movieInfoJson = new JSONObject(movieInfoItem);

                //Getting the movie info strings at the different "key" areas and storing them in our movie
                movie = new MovieInfo(
                        movieInfoJson.getString("id"),
                        movieInfoJson.getString("original_title"),
                        movieInfoJson.getString("poster_path"),
                        movieInfoJson.getString("release_date"),
                        movieInfoJson.getString("vote_average"),
                        movieInfoJson.getString("overview")
                );
                //Adding all of our strings into the movieInfoItems ArrayList
                movieInfoItems.add(movie);
            }
            //Returning the movieInfoItems ArrayList
            return movieInfoItems;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
