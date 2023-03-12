package com.me.jobsearch.presentation.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.me.jobsearch.R;
import com.me.jobsearch.data.remote.model.JobResponse;
import com.me.jobsearch.databinding.ItemJobListBinding;
import com.me.jobsearch.databinding.ItemLoadingBinding;

import java.util.ArrayList;
import java.util.List;

public class JobListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private List<JobResponse> jobList = new ArrayList<>();
    private boolean isLoading = false;
    private OnJobClickListener onJobClickListener;

    public JobListAdapter(OnJobClickListener onJobClickListener) {
        this.onJobClickListener = onJobClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_ITEM) {
            ItemJobListBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_job_list, parent, false);
            return new JobViewHolder(binding, onJobClickListener);
        } else {
            ItemLoadingBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_loading, parent, false);
            return new LoadingViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof JobViewHolder) {
            ((JobViewHolder) holder).bind(jobList.get(position));
        } else {
            ((LoadingViewHolder) holder).bind();
        }
    }

    @Override
    public int getItemCount() {
        return isLoading ? jobList.size() + 1 : jobList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return isLoading && position == jobList.size() ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setJobList(List<JobResponse> jobList) {
        this.jobList = jobList;
        notifyDataSetChanged();
    }

    public void addJobList(List<JobResponse> jobList) {
        int lastPosition = this.jobList.size();
        this.jobList.addAll(jobList);
        notifyItemRangeInserted(lastPosition, jobList.size());
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
        notifyItemChanged(jobList.size());
    }

    public void setOnJobClickListener(OnJobClickListener onJobClickListener) {
        this.onJobClickListener = onJobClickListener;
    }

    static class JobViewHolder extends RecyclerView.ViewHolder {

        private final ItemJobListBinding binding;
        private final OnJobClickListener onJobClickListener;

        public JobViewHolder(@NonNull ItemJobListBinding binding, OnJobClickListener onJobClickListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.onJobClickListener = onJobClickListener;
        }

        public void bind(JobResponse job) {
            binding.setItem(job);
            itemView.setOnClickListener(v -> {
                onJobClickListener.onJobClick(job);
            });
            binding.executePendingBindings();
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {

        private final ItemLoadingBinding binding;

        public LoadingViewHolder(@NonNull ItemLoadingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind() {
            binding.setIsLoading(true);
        }
    }

    interface OnJobClickListener {
        void onJobClick(JobResponse job);
    }
}
