package com.example.biddlecompopularmoviesstage2take4;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.biddlecompopularmoviesstage2take4.Database.FavoriteMovieDatabase;
import com.example.biddlecompopularmoviesstage2take4.Database.FavoriteMovieEntry;

public class MovieDetailActivityViewModel extends ViewModel {

    //FavMovies member variable for the FavoriteMovieEntry object wrapped in a LiveData
    private LiveData<FavoriteMovieEntry> FavMovies;

    //Create a constructor where we call loadMovieById of the FavoriteMovieDao to initialize the
    //FavMovies variable.
    //Note: The constructor should receive the database and the movieId
    public MovieDetailActivityViewModel(FavoriteMovieDatabase favoriteMovieDatabase, int movieId) {
        FavMovies = favoriteMovieDatabase.movieDao().loadMovieById(movieId);
    }

    //Getter for the FavMovies variable
    public LiveData<FavoriteMovieEntry> getMovies() {
        return FavMovies;
    }
}
