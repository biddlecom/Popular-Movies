package com.example.biddlecompopularmoviesstage2take4.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FavoriteMovieDao {

    @Query("SELECT * FROM FavoriteMovieTable ORDER BY id")
    LiveData<List<FavoriteMovieEntry>> loadAllMovies();

    @Insert
    void insertMovie(FavoriteMovieEntry favMovie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(FavoriteMovieEntry favMovie);

    @Delete
    void deleteMovie(FavoriteMovieEntry favMovie);

    @Query("SELECT * FROM FavoriteMovieTable WHERE id = :id")
    LiveData<FavoriteMovieEntry> loadMovieById(int id);
}
