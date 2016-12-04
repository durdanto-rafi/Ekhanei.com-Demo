package com.androidtime.mvp.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidtime.mvp.DetailsActivity;
import com.androidtime.mvp.R;
import com.androidtime.mvp.interfaces.RecyclerViewClickListener;
import com.androidtime.mvp.model.RecipeDetail;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sab99r
 */
public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int NORMAL = 0;
    public final int LOADING = 1;

    static Context context;
    List<RecipeDetail> recipeDetails;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    private static RecyclerViewClickListener itemListener;

    /*
    * isLoading - to set the remote loading and complete status to fix back to back load more call
    * isMoreDataAvailable - to set whether more data from server available or not.
    * It will prevent useless load more request even after all the server data loaded
    * */


    public Adapter(Context context, List<RecipeDetail> recipeDetails,RecyclerViewClickListener itemListener) {
        this.context = context;
        this.recipeDetails = recipeDetails;
        this.itemListener = itemListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == NORMAL) {
            return new MovieHolder(inflater.inflate(R.layout.gridview_row, parent, false));
        } else {
            return new LoadHolder(inflater.inflate(R.layout.row_load, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        if (getItemViewType(position) == NORMAL) {
            ((MovieHolder) holder).bindData(recipeDetails.get(position));
        }
        //No else part needed as load holder doesn't bind any data
    }

    @Override
    public int getItemViewType(int position) {
        if (recipeDetails.get(position).getTitle().length() > 0) {
            return NORMAL;
        } else {
            return LOADING;
        }
    }

    @Override
    public int getItemCount() {
        return recipeDetails.size();
    }

    /* VIEW HOLDERS */

    static class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvTitle;
        ImageView ivThumbnail;

        public MovieHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivThumbnail = (ImageView) itemView.findViewById(R.id.ivThumbnail);

        }

        void bindData(RecipeDetail recipeDetail) {
            tvTitle.setText(recipeDetail.getTitle());
            if (recipeDetail.getThumbnail().length() > 0)
                Picasso.with(context).load(recipeDetail.getThumbnail()).resize(80, 80).into(ivThumbnail);
            else
                Picasso.with(context).load("https://media.licdn.com/mpr/mpr/shrinknp_100_100/AAEAAQAAAAAAAAk7AAAAJDVkYTRmMWJjLWExMDYtNDg0OC1hNDE2LTI0NzE4Mzk0ZTgwYw.png").resize(80, 80).into(ivThumbnail);

            ivThumbnail.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemListener != null) {
                itemListener.recyclerViewListClicked(v, this.getAdapterPosition());
            }
        }



    }

    static class LoadHolder extends RecyclerView.ViewHolder {
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    /* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
    public void notifyDataChanged() {
        notifyDataSetChanged();
        isLoading = false;
    }


    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }
}
