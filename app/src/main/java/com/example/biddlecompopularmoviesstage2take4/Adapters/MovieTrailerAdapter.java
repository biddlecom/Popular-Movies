package com.example.biddlecompopularmoviesstage2take4.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biddlecompopularmoviesstage2take4.Model.MovieTrailer;
import com.example.biddlecompopularmoviesstage2take4.R;

import java.util.ArrayList;
import java.util.List;

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.TrailerViewHolder> {

    private final Context mContext;
    //Global variables for our Movie array and our on click handler
    final private ListItemClickListener mOnClickListener;
    private List<MovieTrailer> mTrailerList;

    //Creates a movie trailer adapter constructor
    public MovieTrailerAdapter(Context mContext, ArrayList<MovieTrailer> trailerItems, ListItemClickListener listener) {
        this.mContext = mContext;
        mTrailerList = trailerItems;
        mOnClickListener = listener;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If the RecyclerView has more than one type of item you can use this viewType
     *                  integer to provide a different layout.
     * @return A new MovieTrailerAdapterViewHolder that holds the View for each list item
     */
    @NonNull
    @Override
    public MovieTrailerAdapter.TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_detail_activity_trailer_rv;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new TrailerViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, it updates the contents of the ViewHolder to display the trailer
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder    The ViewHolder which should be updated to represent the contents of the item at
     *                  the given position in the data set.
     * @param position  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MovieTrailerAdapter.TrailerViewHolder holder, int position) {
        holder.bind(position);
    }

    /**
     * This method returns the number of items to display.
     *
     * @return The number of items available in our trailer list
     */
    @Override
    public int getItemCount() {
        if (mTrailerList == null) {
            return 0;
        }
        return mTrailerList.size();
    }

    /**
     * When data changes, this method updates the list of "trailerItemList" and notifies the adapter
     * to use the new values on it.
     */
    public void setTrailerData(List<MovieTrailer> trailerItemList) {
        mTrailerList = trailerItemList;
        notifyDataSetChanged();
    }

    //Interface for the ListItemClickListener
    public interface ListItemClickListener {
        void OnListItemClick(MovieTrailer trailerItem);
    }

    //Cache of the children views for a MovieTrailer list item
    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Variables for the text and image views
        TextView listTrailerItemView;
        ImageView trailerPlayButtonIV;

        public TrailerViewHolder(View itemView) {
            super(itemView);

            //Getting the text view and setting an onClickListener on it.
            listTrailerItemView = itemView.findViewById(R.id.movie_detail_trailer_RV_item);
            itemView.setOnClickListener(this);

            //Getting the image view and setting an onClickListener on it.
            trailerPlayButtonIV = itemView.findViewById(R.id.movie_detail_trailer_play_button_RV_Item);
            itemView.setOnClickListener(this);
        }

        /**
         * This gets called by a child view during a click.
         *
         * @param view is the view that was clicked.
         */
        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.OnListItemClick(mTrailerList.get(clickedPosition));
        }

        //Bind method that will bind the movie trailer to the specific text view.
        public void bind(int position) {
            listTrailerItemView.setText(mTrailerList.get(position).getName());
        }
    }
}
