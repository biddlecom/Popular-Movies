package com.example.biddlecompopularmoviesstage2take4.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FavoriteMovieEntry.class}, version = 1, exportSchema = false)
public abstract class FavoriteMovieDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favoriteMovieDatabase";
    private static FavoriteMovieDatabase sInstance;

    public static FavoriteMovieDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        FavoriteMovieDatabase.class, FavoriteMovieDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }
    public abstract FavoriteMovieDao movieDao();
}
