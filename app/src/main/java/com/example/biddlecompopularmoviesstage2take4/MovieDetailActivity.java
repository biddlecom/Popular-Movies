package com.example.biddlecompopularmoviesstage2take4;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.biddlecompopularmoviesstage2take4.Adapters.MovieReviewAdapter;
import com.example.biddlecompopularmoviesstage2take4.Adapters.MovieTrailerAdapter;
import com.example.biddlecompopularmoviesstage2take4.Database.FavoriteMovieDatabase;
import com.example.biddlecompopularmoviesstage2take4.Database.FavoriteMovieEntry;
import com.example.biddlecompopularmoviesstage2take4.Model.MovieInfo;
import com.example.biddlecompopularmoviesstage2take4.Model.MovieReview;
import com.example.biddlecompopularmoviesstage2take4.Model.MovieTrailer;
import com.example.biddlecompopularmoviesstage2take4.Utilities.MovieReviewJsonUtils;
import com.example.biddlecompopularmoviesstage2take4.Utilities.MovieTrailerJsonUtils;
import com.example.biddlecompopularmoviesstage2take4.Utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MovieDetailActivity extends AppCompatActivity implements MovieTrailerAdapter.ListItemClickListener {

    // Extra for the Movie ID to be received in the intent
    public static final String EXTRA_MOVIE_ID = "movieItem";

    //String constants for the Reviews and Trailers API searches
    private final static String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private final static String MOVIE_DB_PARAM_VIDEO_URL = "/videos?api_key=";
    private final static String MOVIE_DB_PARAM_REVIEW_URL = "/reviews?api_key=";

    //String constants for the Poster Path API searches
    private final static String POSTER_PATH_BASE_URL = "https://image.tmdb.org/t/p/";
    private final static String POSTER_PATH_SIZE = "w500";

    //My API key for the Movie Data Base website
    private final static String BIDDLECOM_API_KEY = BuildConfig.BiddlecomApiKey;

    //Global variables for our text and image views, progress bar, recycler view, movie adapter, and movie array
    private ImageView mMovieFavoriteHeartIV;
    private TextView mMovieDetailErrorMessageTV;
    private TextView mMovieTitleTV;
    private ImageView mMoviePosterIV;
    private TextView mMovieReleaseDateNumbersTV;
    private TextView mMovieUserRatingNumbersTV;
    private TextView mMoviePlotSummaryTV;
    private ImageView mMovieTrailersPlayButtonIV;
    private TextView mMovieTrailerNameTV;
    private TextView mMovieDetailsNoTrailersTV;
    private TextView mMovieReviewAuthorTV;
    private TextView mMovieReviewContentTV;
    private TextView mMovieDetailNoReviewsTV;
    private MovieInfo movieItem;

    //Review ArrayList, Recycler View, Adapter and Layout
    private ArrayList<MovieReview> reviewArrayList;
    private RecyclerView mMovieReviewRV;
    private MovieReviewAdapter mMovieReviewAdapter;
    private RecyclerView.LayoutManager mReviewLayoutManager;

    //Trailer ArrayList, Recycler View, Adapter and Layout
    private ArrayList<MovieTrailer> trailerArrayList;
    private RecyclerView mMovieTrailerRV;
    private MovieTrailerAdapter mMovieTrailerAdapter;
    private RecyclerView.LayoutManager mTrailerLayoutManager;

    //Favorite Movies Database, Favorite "Heart" Button, Boolean to see if the movie is already a favorite
    private FavoriteMovieDatabase mFavoriteMovieDB;
    private ImageView mFavHeartButton;
    private Boolean isMovieFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //Getting the intent that started this activity
        Intent intent = getIntent();
        //Checking to see if it is null and if it contains the EXTRA_MOVIE_ID
        if (intent != null && intent.hasExtra(EXTRA_MOVIE_ID)) {

            //Getting the parcelable "movieItem" and setting up the MovieDetailActivityViewModelFactory
            movieItem = intent.getParcelableExtra("movieItem");
            if (movieItem == null) {
                int mMovieId = 0;
                MovieDetailActivityViewModelFactory factory = new MovieDetailActivityViewModelFactory(mFavoriteMovieDB, mMovieId);
                final MovieDetailActivityViewModel viewModel
                        = new ViewModelProvider(this, factory).get(MovieDetailActivityViewModel.class);
                viewModel.getMovies().observe(this, new Observer<FavoriteMovieEntry>() {
                    @Override
                    public void onChanged(@Nullable FavoriteMovieEntry favoriteMovieEntry) {
                        viewModel.getMovies().removeObserver(this);
                    }
                });
            }
        }

        //Finding the favorite heart button image view
        mFavHeartButton = findViewById(R.id.movie_detail_favorite_heart_button_IV);
        //Getting an instance of the Favorite Movie Database
        mFavoriteMovieDB = FavoriteMovieDatabase.getInstance(getApplicationContext());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final LiveData<FavoriteMovieEntry> favoriteMovie = mFavoriteMovieDB.movieDao()
                        .loadMovieById(Integer.parseInt(movieItem.getMovieId()));
                if (favoriteMovie != null) {
                    setFavorite(true);
                }
            }
        });

        //Initializing our views
        mMovieDetailErrorMessageTV = findViewById(R.id.detail_activity_error_message_TV);
        mMovieTitleTV = findViewById(R.id.movie_detail_activity_movie_title_TV);
        mMoviePosterIV = findViewById(R.id.movie_detail_movie_poster_IV);
        mFavHeartButton = findViewById(R.id.movie_detail_favorite_heart_button_IV);
        mMovieReleaseDateNumbersTV = findViewById(R.id.movie_detail_set_release_date_TV);
        mMovieUserRatingNumbersTV = findViewById(R.id.movie_detail_set_user_rating_TV);
        mMoviePlotSummaryTV = findViewById(R.id.movie_detail_set_plot_summary_TV);
        mMovieTrailersPlayButtonIV = findViewById(R.id.movie_detail_trailer_play_button_RV_Item);
        mMovieTrailerNameTV = findViewById(R.id.movie_detail_trailer_RV_item);
        mMovieDetailsNoTrailersTV = findViewById(R.id.movie_detail_activity_no_trailers_TV);
        mMovieReviewAuthorTV = findViewById(R.id.movie_detail_review_author_RV_Item);
        mMovieReviewContentTV = findViewById(R.id.movie_detail_review_content_RV_item);
        mMovieDetailNoReviewsTV = findViewById(R.id.movie_detail_activity_no_reviews_TV);

        //Getting the Trailers Linear Layout
        mMovieTrailerRV = findViewById(R.id.movie_detail_activity_trailer_RV);
        mTrailerLayoutManager = new LinearLayoutManager(this);
        mMovieTrailerRV.setLayoutManager(mTrailerLayoutManager);
        mMovieTrailerRV.setHasFixedSize(true);
        mMovieTrailerAdapter = new MovieTrailerAdapter(this, trailerArrayList, this);
        mMovieTrailerRV.setAdapter(mMovieTrailerAdapter);

        //Getting the Reviews Linear Layout
        mMovieReviewRV = findViewById(R.id.movie_detail_activity_review_RV);
        mReviewLayoutManager = new LinearLayoutManager(this);
        mMovieReviewRV.setLayoutManager(mReviewLayoutManager);
        mMovieReviewRV.setHasFixedSize(true);
        mMovieReviewAdapter = new MovieReviewAdapter(this, reviewArrayList);
        mMovieReviewRV.setAdapter(mMovieReviewAdapter);

        //Method that will call/start the two async tasks that will query the Movie Database Web API
        //for the REVIEWS and TRAILERS information
        doReviewAndTrailerAsyncTaskQueries();

        //Method that will populate the UI with the data from the parcelable extra in our intent
        populateUI();
    }

    //Setting the favorites icon to be full if it is a favorited movie and empty if it is not a favorite.
    private void setFavorite(Boolean favorite) {
        if (favorite) {
            isMovieFavorite = true;
            mFavHeartButton.setImageResource(R.drawable.ic_favorite_full_red_52dp);
        } else {
            isMovieFavorite = false;
            mFavHeartButton.setImageResource(R.drawable.ic_favorite_border_black_52dp);
        }
    }

    //Method that will query the movie database API, get the trailer results and the review results
    //and show them on the screen in their appropriate recycler views
    private void doReviewAndTrailerAsyncTaskQueries() {
        new TrailerAsyncTaskQuery().execute();
        new ReviewAsyncTaskQuery().execute();
    }

    //Intent that will show the movie trailer using the YouTube app or a web browser
    private void watchMovieTrailer(String id) {
        //Intent to use the YouTube app to watch the movie trailer
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        //Intent to use a web browser to watch the movie trailer
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/" + id));
        try {
            startActivity(appIntent);
        } catch (Exception e) {
            e.printStackTrace();
            startActivity(webIntent);
        }
    }

    //This will handle the onClick that will trigger the intent that will in turn launch the movie
    //trailer so the user can view it.
    @Override
    public void OnListItemClick(MovieTrailer trailerItem) {
        watchMovieTrailer(trailerItem.getKey());
    }

    //Setting the data from the "movieItem" parcelable intent on the appropriate views.
    private void populateUI() {

        ((TextView) findViewById(R.id.movie_detail_activity_movie_title_TV)).setText(movieItem.getMovieTitle());
        ((TextView) findViewById(R.id.movie_detail_set_release_date_TV)).setText(movieItem.getReleaseDate());
        ((TextView) findViewById(R.id.movie_detail_set_user_rating_TV)).setText(movieItem.getUserRating());
        ((TextView) findViewById(R.id.movie_detail_set_plot_summary_TV)).setText(movieItem.getMoviePlot());

        //Favorite "Heart" button and setting an OnClickListener on it.
        //When clicked it will save the movie ID, title, poster, release date, user rating and plot
        //into the FavoriteMovieDatabase
        mFavHeartButton.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                final FavoriteMovieEntry mov = new FavoriteMovieEntry(
                        Integer.parseInt(movieItem.getMovieId()),
                        movieItem.getMovieTitle(),
                        movieItem.getPosterImage(),
                        movieItem.getReleaseDate(),
                        movieItem.getUserRating(),
                        movieItem.getMoviePlot()
                );
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (isMovieFavorite) {
                            //If movie IS already a favorite then delete it from the database
                            mFavoriteMovieDB.movieDao().deleteMovie(mov);
                        } else {
                            //If movie is NOT already a favorite then insert it into the database
                            mFavoriteMovieDB.movieDao().insertMovie(mov);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Method call to change the Heart icon
                                setFavorite(!isMovieFavorite);
                            }
                        });
                    }
                });
            }
        });

        //String containing a complete API URL so it can be called by Picasso and put into the Movie Poster ImageView
        final String completePosterPath = (POSTER_PATH_BASE_URL + POSTER_PATH_SIZE + movieItem.getPosterImage());
        try {
            Picasso.get()
                    .load(completePosterPath)
                    .placeholder(R.drawable.movie_poster_loading_image_png_smaller)
                    .error(R.drawable.no_movie_poster_available_png_smaller)
                    .into((ImageView) this.findViewById(R.id.movie_detail_movie_poster_IV));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Async task that will query the network and return the movie trailers
    public class TrailerAsyncTaskQuery extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL trailerUrl = null;
            String trailerResults = null;
            try {
                //Creating the complete URL that will be used to eventually query the "trailers" Movie database API
                trailerUrl = new URL(MOVIE_DB_BASE_URL + movieItem.getMovieId() + MOVIE_DB_PARAM_VIDEO_URL + BIDDLECOM_API_KEY);
                trailerResults = NetworkUtils.getResponseFromHttpUrl(trailerUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return trailerResults;
        }

        @Override
        protected void onPostExecute(String trailerResults) {
            //If the search results are not null and are not empty
            if (trailerResults != null && !trailerResults.equals("")) {
                //Retrieve the information from the API
                trailerArrayList = MovieTrailerJsonUtils.parsedMovieTrailerJson(trailerResults);
                //Set the retrieved trailer data on our Adapter
                mMovieTrailerAdapter.setTrailerData(trailerArrayList);
            } else {
                //Show the "No Trailers" text view.
                mMovieDetailsNoTrailersTV.setVisibility(View.VISIBLE);
                mMovieDetailsNoTrailersTV.setText(R.string.detail_activity_no_trailers_error_message);
            }
        }
    }

    //Async task that will query the network and return the movie reviews
    public class ReviewAsyncTaskQuery extends AsyncTask<URL, Void, String> {


        @Override
        protected String doInBackground(URL... params) {
            URL reviewUrl = null;
            String reviewResults = null;
            try {
                //Creating the complete URL that will be used to eventually query the "reviews" Movie database API
                reviewUrl = new URL(MOVIE_DB_BASE_URL + movieItem.getMovieId() + MOVIE_DB_PARAM_REVIEW_URL + BIDDLECOM_API_KEY);
                reviewResults = NetworkUtils.getResponseFromHttpUrl(reviewUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return reviewResults;
        }

        @Override
        protected void onPostExecute(String reviewResults) {
            //If the search results are not null and are not empty
            if (reviewResults != null && !reviewResults.equals("")) {
                //Retrieve the information from the API
                reviewArrayList = MovieReviewJsonUtils.parsedMovieReviewsJson(reviewResults);
                //Set the retrieved review data on our Adapter
                mMovieReviewAdapter.setReviewData(reviewArrayList);
            } else {
                //Show the "No Reviews" text view.
                mMovieDetailNoReviewsTV.setVisibility(View.VISIBLE);
                mMovieDetailNoReviewsTV.setText(R.string.detail_activity_no_reviews_error_message);
            }
        }
    }
}
