package com.example.biddlecompopularmoviesstage2take4;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.biddlecompopularmoviesstage2take4.Database.FavoriteMovieDatabase;
import com.example.biddlecompopularmoviesstage2take4.Database.FavoriteMovieEntry;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private LiveData<List<FavoriteMovieEntry>> movies;

    public MainActivityViewModel(Application application) {
        super(application);
        FavoriteMovieDatabase database = FavoriteMovieDatabase.getInstance(this.getApplication());
        movies = database.movieDao().loadAllMovies();
    }

    public LiveData<List<FavoriteMovieEntry>> getMovies() {
        return movies;
    }
}
