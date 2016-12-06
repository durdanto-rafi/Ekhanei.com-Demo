package com.ekhanei.mvp.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekhanei.mvp.R;
import com.ekhanei.mvp.interfaces.OnRecyclerViewClickListener;
import com.ekhanei.mvp.model.RecipeDetail;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.ekhanei.mvp.ConstantValues.ALTERNATIVE_IMAGE;

/**
 * Created by RAFI
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int NORMAL = 0;
    public final int LOADING = 1;

    static Context context;
    List<RecipeDetail> recipeDetails;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    private static OnRecyclerViewClickListener itemListener;

    /*
    * isLoading - to set the remote loading and complete status to fix back to back load more call
    * isMoreDataAvailable - to set whether more data from server available or not.
    * It will prevent useless load more request even after all the server data loaded
    * */


    public RecipeAdapter(Context context, List<RecipeDetail> recipeDetails, OnRecyclerViewClickListener itemListener) {
        this.context = context;
        this.recipeDetails = recipeDetails;
        this.itemListener = itemListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == NORMAL) {
            return new MovieHolder(inflater.inflate(R.layout.row_grid_item, parent, false));
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
                Picasso.with(context).load(recipeDetail.getThumbnail()).resize(100, 100).into(ivThumbnail);
            else
                Picasso.with(context).load(ALTERNATIVE_IMAGE).resize(100, 100).into(ivThumbnail);

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
