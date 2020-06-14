package com.example.biddlecompopularmoviesstage2take4.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biddlecompopularmoviesstage2take4.Model.MovieInfo;
import com.example.biddlecompopularmoviesstage2take4.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieInfoAdapter extends RecyclerView.Adapter<MovieInfoAdapter.MovieViewHolder> {

    //Global variables for the movie poster
    private static String POSTER_PATH_BASE_URL = "https://image.tmdb.org/t/p/";
    private static String POSTER_PATH_SIZE = "w300";
    private final Context mContext;
    final private ListItemClickListener mOnClickListener;
    //Global variables for the MovieInfo list, Context and ListItemClickListener
    private List<MovieInfo> mMovieItemList;

    //Creates a movie info adapter constructor
    public MovieInfoAdapter(List<MovieInfo> movieItemList, ListItemClickListener listener, Context context) {
        mMovieItemList = movieItemList;
        mOnClickListener = listener;
        mContext = context;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent   The parent that these ViewHolders are contained within.
     * @param viewType If the RecyclerView has more than one type of item you can use this viewType
     *                 integer to provide a different layout.
     * @return A new MovieInfoAdapterViewHolder that holds the View for each list item
     */
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.main_activity_poster_title_rv_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MovieViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, it updates the contents of the ViewHolder to display the movie
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    /**
     * This method returns the number of items to display.
     *
     * @return The number of items available in our movie list
     */
    @Override
    public int getItemCount() {
        if (mMovieItemList == null) {
            return 0;
        }
        return mMovieItemList.size();
    }

    public List<MovieInfo> getMovies() {
        return mMovieItemList;
    }

    /**
     * When data changes, this method updates the list of "movieItemList" and notifies the adapter
     * to use the new values on it.
     */
    public void setMovieData(List<MovieInfo> movieItemList) {
        mMovieItemList = movieItemList;
        notifyDataSetChanged();
    }

    //Interface for the ListItemClickListener
    public interface ListItemClickListener {
        void OnListItemClick(MovieInfo movieItem);
    }

    //Cache of the children views for a Movie list item
    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Variables for the image and text views
        ImageView mMoviePosterIV;
        TextView mMovieTitleTV;

        public MovieViewHolder(View itemView) {
            super(itemView);

            //Getting the image view and setting an onClickListener on it.
            mMoviePosterIV = itemView.findViewById(R.id.main_activity_movie_poster_IV);
            itemView.setOnClickListener(this);

            //Getting the text view and setting an onClickListener on it.
            mMovieTitleTV = itemView.findViewById(R.id.main_activity_movie_title_TV);
            itemView.setOnClickListener(this);
        }

        //Bind method that will bind the movie info to the specific text and image views.
        void bind(int listIndex) {
            MovieInfo movieItem = mMovieItemList.get(listIndex);
            String movieTitle = mMovieItemList.get(listIndex).getMovieTitle();
            mMovieTitleTV.setText(movieTitle);
            mMoviePosterIV = itemView.findViewById(R.id.main_activity_movie_poster_IV);
            //Creating the full poster path URL so that Picasso can load it and set it into the
            //movie poster image view.
            String completePosterPath = (POSTER_PATH_BASE_URL + POSTER_PATH_SIZE + movieItem.getPosterImage());
            try {
                Picasso.get()
                        .load(completePosterPath)
                        .placeholder(R.drawable.movie_poster_loading_image_png_smaller)
                        .error(R.drawable.no_movie_poster_available_png_smaller)
                        .into(mMoviePosterIV);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * This gets called by a child view during a click.
         *
         * @param view is the view that was clicked.
         */
        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.OnListItemClick(mMovieItemList.get(clickedPosition));
        }
    }
}
