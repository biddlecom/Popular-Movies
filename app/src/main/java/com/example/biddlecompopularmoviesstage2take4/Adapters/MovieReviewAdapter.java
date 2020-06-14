package com.example.biddlecompopularmoviesstage2take4.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biddlecompopularmoviesstage2take4.Model.MovieReview;
import com.example.biddlecompopularmoviesstage2take4.R;

import java.util.ArrayList;
import java.util.List;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewAdapterViewHolder> {

    private final Context mContext;
    //Global variables for our Movie Review array
    private List<MovieReview> mReviewList;

    //Constructor to initialize our array
    public MovieReviewAdapter(Context mContext, ArrayList<MovieReview> reviewItems) {
        this.mContext = mContext;
        this.mReviewList = reviewItems;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If the RecyclerView has more than one type of item you can use this viewType
     *                  integer to provide a different layout.
     * @return A new MovieInfoAdapterViewHolder that holds the View for each list item
     */
    @NonNull
    @Override
    public MovieReviewAdapter.MovieReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_detail_activity_review_rv;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new MovieReviewAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, it updates the contents of the ViewHolder to display the review
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the
     *                 contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MovieReviewAdapterViewHolder holder, int position) {
        holder.bind(position);
    }

    /**
     * This method returns the number of items to display.
     *
     * @return The number of items available in our review list
     */
    @Override
    public int getItemCount() {
        if (null == mReviewList) {
            return 0;
        }
        return mReviewList.size();
    }

    /**
     * When data changes, this method updates the list of "reviewItemList" and notifies the adapter
     * to use the new values on it.
     */
    public void setReviewData(List<MovieReview> reviewItemList) {
        mReviewList = reviewItemList;
        notifyDataSetChanged();
    }

    //Cache of the children views for a MovieReview list item
    public class MovieReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        //Variables for the text views
        final TextView mListItemAuthorReviewTV;
        final TextView mListItemContentReviewTV;

        //Cache of the children views for a Review list item
        public MovieReviewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            //Getting the text views
            mListItemAuthorReviewTV = itemView.findViewById(R.id.movie_detail_review_author_RV_Item);
            mListItemContentReviewTV = itemView.findViewById(R.id.movie_detail_review_content_RV_item);
        }

        //Bind method that will bind the movie info to the specific text and image views.
        public void bind(int position) {
            mListItemAuthorReviewTV.setText(mReviewList.get(position).getAuthor());
            mListItemContentReviewTV.setText(mReviewList.get(position).getContent());
        }
    }
}
