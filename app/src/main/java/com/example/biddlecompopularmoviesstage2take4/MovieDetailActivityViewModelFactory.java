package com.example.biddlecompopularmoviesstage2take4;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.biddlecompopularmoviesstage2take4.Database.FavoriteMovieDatabase;

public class MovieDetailActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    //Member variables. One for the database and one for the mMovieId
    private final FavoriteMovieDatabase mDb;
    private final int mMovieId;

    //Initialize the member variables in the constructor with the parameters received
    public MovieDetailActivityViewModelFactory(FavoriteMovieDatabase mDb, int mMovieId) {
        this.mDb = mDb;
        this.mMovieId = mMovieId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MovieDetailActivityViewModel(mDb, mMovieId);
    }
}
