package com.example.biddlecompopularmoviesstage2take4.Database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "FavoriteMovieTable")
public class FavoriteMovieEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String movieTitle;
    private String moviePosterPath;
    private String movieReleaseDate;
    private String movieUserRating;
    private String moviePlotSummary;

    @Ignore
    public FavoriteMovieEntry(String movieTitle, String moviePosterPath, String movieReleaseDate,
                              String movieUserRating, String moviePlotSummary) {
        this.movieTitle = movieTitle;
        this.moviePosterPath = moviePosterPath;
        this.movieReleaseDate = movieReleaseDate;
        this.movieUserRating = movieUserRating;
        this.moviePlotSummary = moviePlotSummary;
    }

    //Constructor
    public FavoriteMovieEntry(int id, String movieTitle, String moviePosterPath, String movieReleaseDate,
                              String movieUserRating, String moviePlotSummary) {
        this.id = id;
        this.movieTitle = movieTitle;
        this.moviePosterPath = moviePosterPath;
        this.movieReleaseDate = movieReleaseDate;
        this.movieUserRating = movieUserRating;
        this.moviePlotSummary = moviePlotSummary;
    }

    //Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMoviePosterPath() {
        return moviePosterPath;
    }

    public void setMoviePosterPath(String moviePosterPath) {
        this.moviePosterPath = moviePosterPath;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public String getMovieUserRating() {
        return movieUserRating;
    }

    public void setMovieUserRating(String movieUserRating) {
        this.movieUserRating = movieUserRating;
    }

    public String getMoviePlotSummary() {
        return moviePlotSummary;
    }

    public void setMoviePlotSummary(String moviePlotSummary) {
        this.moviePlotSummary = moviePlotSummary;
    }
}
