package com.example.biddlecompopularmoviesstage2take4;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.biddlecompopularmoviesstage2take4.Adapters.MovieInfoAdapter;
import com.example.biddlecompopularmoviesstage2take4.Database.FavoriteMovieEntry;
import com.example.biddlecompopularmoviesstage2take4.Model.MovieInfo;
import com.example.biddlecompopularmoviesstage2take4.Utilities.MovieInfoJsonUtils;
import com.example.biddlecompopularmoviesstage2take4.Utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieInfoAdapter.ListItemClickListener {

    //Sort Ids for the menu Sort By options
    private static final String SORT_ID_POPULAR = "popular";
    private static final String SORT_ID_TOP_RATED = "top_rated";
    private static final String SORT_ID_FAVORITE = "favorite";
    private static String currentSortId = SORT_ID_POPULAR;

    //Global variables for our text views, progress bar, recycler view, movie adapter, and movie array
    ImageView mMoviePosterIV;
    TextView mMovieTitleTV;
    TextView mErrorMessageTV;
    ProgressBar mIndeterminateProgressBar;
    private ArrayList<MovieInfo> movieList;
    private RecyclerView mMovieRecyclerView;
    private MovieInfoAdapter mMovieInfoAdapter;

    //Variables detailing how many columns to use based on the screen size.
    private int XXXXLARGE_SCREEN = 5;
    private int XXXLARGE_SCREEN = 3;
    private int XXLARGE_SCREEN = 3;
    private int XLARGE_SCREEN = 2;
    private int LARGE_SCREEN = 2;
    private int MEDIUM_SCREEN = 2;
    private int SMALL_SCREEN = 2;
    private int XSMALL_SCREEN = 1;
    private int XXSMALL_SCREEN = 1;
    private int currentScreen = LARGE_SCREEN;

    //Getting a reference to the FavoriteMovieEntry List
    private List<FavoriteMovieEntry> favMovieEntryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Calling the method that will calculate the width and height of the device screen at start up.
        calculatingDisplaySize();

        //Getting our recycler view
        mMovieRecyclerView = (RecyclerView) findViewById(R.id.movie_info_recycler_view);
        //Getting our grid layout and specifying the span count (columns) equal to the "currentScreen"
        //value which will be set programmatically.
        GridLayoutManager layoutManager = new GridLayoutManager(this, currentScreen);
        //Setting the grid layout on the recycler view
        mMovieRecyclerView.setLayoutManager(layoutManager);
        //Specifying that the recycler view has a fixed size
        mMovieRecyclerView.setHasFixedSize(true);
        //Creating a new MovieInfoAdapter with out ArrayList, listener and context
        mMovieInfoAdapter = new MovieInfoAdapter(movieList, this, this);
        //Setting the adapter on the recycler view
        mMovieRecyclerView.setAdapter(mMovieInfoAdapter);

        //Getting the movie poster image view
        mMoviePosterIV = (ImageView) findViewById(R.id.main_activity_movie_poster_IV);

        //Getting the movie title text view
        mMovieTitleTV = (TextView) findViewById(R.id.main_activity_movie_title_TV);

        //Getting the error message text view
        mErrorMessageTV = (TextView) findViewById(R.id.error_message_TV);

        //Getting the indeterminate progress bar
        mIndeterminateProgressBar = (ProgressBar) findViewById(R.id.indeterminate_progress_bar);

        //Creating a new FavoriteMovieEntry List
        favMovieEntryList = new ArrayList<>();

        //Calling the method to setup the MainActivityViewModel
        setupViewModel();

        //Reference to the method that will query the movie database API, get the results and then
        //show them on the screen in the recycler view
        loadMovieData();
    }

    //This will get the width and height of the device screen at startup.  We are using this to calculate
    //how many columns to populate on the screen based on the smallest size (width) in portrait mode.
    public void calculatingDisplaySize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels; //Not used for this project
        int width = displayMetrics.widthPixels;

        if (width <= 319) {
            currentScreen = XXSMALL_SCREEN;//1
        }
        if (width >= 320 && width <= 479) {
            currentScreen = XSMALL_SCREEN;//1
        }
        if (width >= 480 && width <= 719) {
            currentScreen = SMALL_SCREEN;//2
        }
        if (width >= 720 && width <= 1079) {
            currentScreen = MEDIUM_SCREEN;//2
        }
        if (width >= 1080 && width <= 1439) {
            currentScreen = LARGE_SCREEN;//2
        }
        if (width >= 1440 && width <= 1799) {
            currentScreen = XLARGE_SCREEN;//2
        }
        if (width >= 1800 && width <= 2159) {
            currentScreen = XXLARGE_SCREEN;//3
        }
        if (width >= 2160 && width <= 2559) {
            currentScreen = XXXLARGE_SCREEN;//3
        }
        if (width >= 2560) {
            currentScreen = XXXXLARGE_SCREEN;//5
        }
    }

    //Setting up the MainActivityViewModel
    private void setupViewModel() {
        MainActivityViewModel viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<FavoriteMovieEntry>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteMovieEntry> favoriteMovies) {
                if (favoriteMovies != null && favoriteMovies.size() > 0) {
                    favMovieEntryList.clear();
                    favMovieEntryList = favoriteMovies;
                    currentSort();
                } else {
                    favMovieEntryList.clear();
                    showInfoMessageNoFavoriteMovies();
                }
            }
        });
    }

    //Calling the appropriate methods based on which SORT ID is currently selected
    public void currentSort() {
        if (currentSortId.equals(SORT_ID_POPULAR)) {
            makeMovieSearchPopularMoviesQuery();
        }
        if (currentSortId.equals(SORT_ID_TOP_RATED)) {
            makeMovieSearchTopRatedMoviesQuery();
        }
        if (currentSortId.equals(SORT_ID_FAVORITE)) {
            favoriteMoviesQueryTask();
        }
    }

    //Method that will query the movie database API, get the results and then show them on the screen
    //in the recycler view.  By default this is what will be shown to the user when the app first loads up.
    private void loadMovieData() {
        makeMovieSearchPopularMoviesQuery();
    }

    //Method that will hide the error message and the progress bar and show the recycler view
    public void showMovieInfoDataView() {
        // Hide the error message and progress bar
        mIndeterminateProgressBar.setVisibility(View.INVISIBLE);
        mErrorMessageTV.setVisibility(View.INVISIBLE);
        // Then show the RecyclerView
        mMovieRecyclerView.setVisibility(View.VISIBLE);
    }

    //Method that will hide the recycler view and show an error message when no movies can be shown
    //in the app.  This will happen when the app has no internet connection while trying to retrieve
    //popular or top-rated movies. It can also happen if there is an error retrieving movies from the API
    //(The favorites database will not be affects by these problems.)
    private void showErrorMessageNoMovies() {
        // Hide the recycler view
        mMovieRecyclerView.setVisibility(View.INVISIBLE);
        // Then show the error message
        mErrorMessageTV.setVisibility(View.VISIBLE);
        mErrorMessageTV.setText(R.string.error_message_no_movies);
    }

    //Method that will hide the recycler view and show an information message letting the user know
    //that they do not have any movies stored in the favorites yet.
    private void showInfoMessageNoFavoriteMovies() {
        // Hide the recycler view
        mMovieRecyclerView.setVisibility(View.INVISIBLE);
        // Then show the information message
        mErrorMessageTV.setVisibility(View.VISIBLE);
        mErrorMessageTV.setText(R.string.error_message_no_favorite_movies);
    }

    //Execute the popular movies Async task to search and retrieve data from the Movie Database API
    private void makeMovieSearchPopularMoviesQuery() {
        new MovieDatabaseQueryTaskPopularMovies().execute();
    }

    //Execute the top-rated movies Async task to search and retrieve data from the Movie Database API
    private void makeMovieSearchTopRatedMoviesQuery() {
        new MovieDatabaseQueryTaskTopRatedMovies().execute();
    }

    //Menu inflater to get and show the SORT BY menu.  It uses the SORT icon.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater sortMenuInflater = getMenuInflater();
        //inflating the sort_by menu
        sortMenuInflater.inflate(R.menu.sort_by, menu);
        //returning true so it will show in the app bar
        return true;
    }

    //This will handle the clicks on the SORT BY menu items.
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        //Get the menu item ID
        int menuID = menuItem.getItemId();
        //If the user clicks on the sort by MOST POPULAR menu item then sort the movies by the most
        //popular user ratings.
        if (menuID == R.id.action_sort_by_most_popular) {
            //Current ID of this menu
            currentSortId = SORT_ID_POPULAR;
            //Method to clear the screen
            clearCurrentScreen();
            //Async task that will query the network and return the movies in the "Popular Movies" category
            makeMovieSearchPopularMoviesQuery();
        }
        //If the user clicks on the sort by TOP RATED menu item then sort the movies by the top rated
        //user ratings
        if (menuID == R.id.action_sort_by_top_rated) {
            //Current ID of this menu
            currentSortId = SORT_ID_TOP_RATED;
            //Method to clear the screen
            clearCurrentScreen();
            //Async task that will query the network and return the movies in the "Top-Rated Movies" category
            makeMovieSearchTopRatedMoviesQuery();
        }
        //If the user clicks on the sort by MY FAVORITES menu item then sort the movies by the favorites
        //stored in the ROOM Database
        if (menuID == R.id.action_sort_by_favorites) {
            //Current ID of this menu
            currentSortId = SORT_ID_FAVORITE;
            //Method to clear the screen
            clearCurrentScreen();
            //Method call to query the ROOM database and retrieve the movies stored in there (if any)
            favoriteMoviesQueryTask();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    //Method to query the ROOM database and retrieve the movies stored in there (if any)
    private void favoriteMoviesQueryTask() {
        //Method to clear the screen
        clearCurrentScreen();
        //For loop to cycle through the database and retrieve the movies info stored in there.
        for (int i = 0; i < favMovieEntryList.size(); i++) {
            //Creating a new movieInfo object to store the movie info retrieved from the database
            MovieInfo movieInfo = new MovieInfo(
                    String.valueOf(favMovieEntryList.get(i).getId()),
                    favMovieEntryList.get(i).getMovieTitle(),
                    favMovieEntryList.get(i).getMoviePosterPath(),
                    favMovieEntryList.get(i).getMovieReleaseDate(),
                    favMovieEntryList.get(i).getMovieUserRating(),
                    favMovieEntryList.get(i).getMoviePlotSummary()
            );
            //Adding all of our strings into the movieList ArrayList
            movieList.add(movieInfo);
        }

        //If the movieList is NOT empty then show the movies stored in the database.
        //If the movieList IS empty then show the info message stating that there are no movies stored
        //in the "My Favorites" section.
        if (!movieList.isEmpty()) {
            showMovieInfoDataView();
            mMovieInfoAdapter.setMovieData(movieList);
        } else {
            showInfoMessageNoFavoriteMovies();
        }
    }

    //Method to clear the current screen of any views (movieLists) that are on it
    private void clearCurrentScreen() {
        if (movieList != null) {
            movieList.clear();
        } else {
            movieList = new ArrayList<MovieInfo>();
        }
    }

    //This will handle the clicks on the movie posters and the movie titles and send the user to the
    //MovieDetailActivity where they can see the details of the currently selected movie.
    @Override
    public void OnListItemClick(MovieInfo movieItem) {
        Intent intentToDetailActivity = new Intent(MainActivity.this, MovieDetailActivity.class);
        intentToDetailActivity.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, movieItem);
        startActivity(intentToDetailActivity);
    }

    //Async task that will query the network and return the movies in the "Popular Movies" category
    public class MovieDatabaseQueryTaskPopularMovies extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            //clear the recycler view
            clearCurrentScreen();
            //show the progress bar while the query is made
            mIndeterminateProgressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... params) {
            URL popularMoviesUrl = null;
            String popularMoviesResults = null;
            try {
                //getting the popular movies URL from the Network Utils class
                popularMoviesUrl = NetworkUtils.buildPopularUrl();
                popularMoviesResults = NetworkUtils.getResponseFromHttpUrl(popularMoviesUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return popularMoviesResults;
        }

        @Override
        protected void onPostExecute(String popularMoviesResults) {
            //If the search results are not null and are not empty
            if (popularMoviesResults != null && !popularMoviesResults.equals("")) {
                //Retrieve the information from the API
                movieList = MovieInfoJsonUtils.parsedMovieInfoJson(popularMoviesResults);
                //Hide the progress bar (and any error message) and show the Movie RecyclerView
                showMovieInfoDataView();
                //Set the retrieved movie data on our Adapter
                mMovieInfoAdapter.setMovieData(movieList);
            } else {
                //If the search results ARE NULL then invoke this error method.
                showErrorMessageNoMovies();
            }
        }
    }

    //Async task that will query the network and return the movies in the "Top-Rated Movies" category
    public class MovieDatabaseQueryTaskTopRatedMovies extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            //clear the recycler view
            clearCurrentScreen();
            //show the progress bar while the query is made
            mIndeterminateProgressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... params) {
            URL topRatedMoviesUrl = null;
            String topRatedMoviesResults = null;
            try {
                //getting the top-rated movies URL from the Network Utils class
                topRatedMoviesUrl = NetworkUtils.buildTopRatedUrl();
                topRatedMoviesResults = NetworkUtils.getResponseFromHttpUrl(topRatedMoviesUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return topRatedMoviesResults;
        }

        @Override
        protected void onPostExecute(String topRatedMoviesResults) {
            //If the search results are not null and are not empty
            if (topRatedMoviesResults != null && !topRatedMoviesResults.equals("")) {
                //Retrieve the information from the API
                movieList = MovieInfoJsonUtils.parsedMovieInfoJson(topRatedMoviesResults);
                //Hide the progress bar (and any error message) and show the Movie RecyclerView
                showMovieInfoDataView();
                //Set the retrieved movie data on our Adapter
                mMovieInfoAdapter.setMovieData(movieList);
            } else {
                //If the results ARE NULL then invoke this error method.
                showErrorMessageNoMovies();
            }
        }
    }
}
