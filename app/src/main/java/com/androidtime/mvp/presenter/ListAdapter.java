package com.androidtime.mvp.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.androidtime.mvp.R;
import com.androidtime.mvp.interfaces.RecyclerViewClickListener;
import com.androidtime.mvp.model.RecipeDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RAFI on 7/19/2016.
 */


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<RecipeDetail> recipeDetails;
    Context context;
    private RecyclerViewClickListener itemListener;

    /**
     * created by Rk-reaz for feedBack dialog
     */
    public ListAdapter(Context context, ArrayList<RecipeDetail> recipeDetails, RecyclerViewClickListener itemListener) {
        this.context = context;
        this.itemListener = itemListener;
        this.recipeDetails = recipeDetails;

    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridview_row, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position) {
        //imageProcessing.setImageWith_loaderFullPath(holder.ivGridItem, imageList.get(holder.getAdapterPosition()));
        Picasso.with(context).load(recipeDetails.get(holder.getAdapterPosition()).getThumbnail()).resize(80, 80).into(holder.ivGridItem);
    }

    @Override
    public int getItemCount() {
        return recipeDetails.size();
    }

    public void deleteItem(int position) {
        recipeDetails.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView ivGridItem;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            ivGridItem = (ImageView) itemLayoutView.findViewById(R.id.ivThumbnail);
            ivGridItem.setOnClickListener(this);
        }

        /**
         * implement Click Listener for ItemClick
         */
        @Override
        public void onClick(View view) {
            if (itemListener != null) {
                itemListener.recyclerViewListClicked(view, this.getAdapterPosition());
            }
        }
    }


}




