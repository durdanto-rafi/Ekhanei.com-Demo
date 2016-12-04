package com.androidtime.mvp.interfaces;

import android.view.View;

/**
 * Created by RAFI on 8/10/2016.
 */
public interface RecyclerViewClickListener {
    //create interface for all recyclerView onClickItemLister
    void recyclerViewListClicked(View v, int position);
}
