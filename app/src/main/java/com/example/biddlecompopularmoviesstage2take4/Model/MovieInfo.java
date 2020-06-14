package com.example.biddlecompopularmoviesstage2take4.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieInfo implements Parcelable {

    //Mandatory Parcel creator method
    public static final Creator<MovieInfo> CREATOR = new Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };

    private String movieId;
    private String movieTitle;
    private String posterImage;
    private String releaseDate;
    private String userRating;
    private String moviePlot;

    // No argument constructor for serialization
    public MovieInfo() {
    }

    //Constructor for serialization
    public MovieInfo(String specificMovieId, String originalTitle, String posterPathImage, String movieReleaseDate,
                     String movieUserRating, String moviePlotSummary) {

        this.movieId = specificMovieId;
        this.movieTitle = originalTitle;
        this.posterImage = posterPathImage;
        this.releaseDate = movieReleaseDate;
        this.userRating = movieUserRating;
        this.moviePlot = moviePlotSummary;
    }

    //Parcel in method
    protected MovieInfo(Parcel in) {
        movieId = in.readString();
        movieTitle = in.readString();
        posterImage = in.readString();
        releaseDate = in.readString();
        userRating = in.readString();
        moviePlot = in.readString();
    }

    //Getters and Setters
    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(String posterImage) {
        this.posterImage = posterImage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getMoviePlot() {
        return moviePlot;
    }

    public void setMoviePlot(String moviePlot) {
        this.moviePlot = moviePlot;
    }

    // Not used for this project
    @Override
    public int describeContents() {
        return 0;
    }

    //Write to parcel method
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(movieId);
        parcel.writeString(movieTitle);
        parcel.writeString(posterImage);
        parcel.writeString(releaseDate);
        parcel.writeString(userRating);
        parcel.writeString(moviePlot);
    }
}
