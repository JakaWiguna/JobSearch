package com.me.jobsearch.presentation.common;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    private static final int VISIBLE_THRESHOLD = 2;
    private int mPreviousTotalItemCount = 0;
    private boolean mLoading = true;
    private int mCurrentPage = 1;

    private final LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        this.mLinearLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
        int totalItemCount = mLinearLayoutManager.getItemCount();

        if (mLoading && totalItemCount > mPreviousTotalItemCount) {
            mLoading = false;
            mPreviousTotalItemCount = totalItemCount;
        }

        if (!mLoading && (lastVisibleItemPosition + VISIBLE_THRESHOLD) > totalItemCount) {
            mCurrentPage++;
            onLoadMore(mCurrentPage);
            mLoading = true;
        }
    }

    public abstract void onLoadMore(int currentPage);
}