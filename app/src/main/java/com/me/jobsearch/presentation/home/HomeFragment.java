package com.me.jobsearch.presentation.home;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.me.jobsearch.R;
import com.me.jobsearch._core.BaseFragment;
import com.me.jobsearch.data.common.Response;
import com.me.jobsearch.data.remote.model.JobResponse;
import com.me.jobsearch.databinding.FragmentHomeBinding;
import com.me.jobsearch.presentation.common.EndlessRecyclerViewScrollListener;
import com.me.jobsearch.presentation.common.SpaceDecoration;
import com.me.jobsearch.presentation.detail_job.DetailJobActivity;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> {
    private static final String TAG = "DEBUG_APP";
    private final boolean isFilterShow = false;
    private JobListAdapter adapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected Class<HomeViewModel> getViewModelClass() {
        return HomeViewModel.class;
    }

    @Override
    protected void initViews() {
        binding.setLifecycleOwner(this);
        viewModel.response().observe(this, this::observeResponse);
        initFilter();
        initAdapter();
    }

    private void initFilter() {
        viewModel.getVisibleFilter().observe(this, isShow -> {
            if (isShow) {
                binding.ivFilter.setImageResource(R.drawable.ic_expand_less);
                binding.cvFilter.setVisibility(View.VISIBLE);
            } else {
                binding.ivFilter.setImageResource(R.drawable.ic_expand_more);
                binding.cvFilter.setVisibility(View.GONE);
            }
        });
        binding.ivFilter.setOnClickListener(v -> {
            handleFilterShow();
        });
        binding.etSearch.setText(viewModel.getSearchText().getValue() != null ? viewModel.getSearchText().getValue() : "");
        binding.fulltimeSwitch.setChecked(viewModel.getIsFulltime().getValue() != null ? viewModel.getIsFulltime().getValue() : false);
        binding.etLocation.setText(viewModel.getLocationText().getValue() != null ? viewModel.getLocationText().getValue() : "");

        binding.btnApplyFilter.setOnClickListener(v -> {
            viewModel.onSearch(
                    binding.etSearch.getText().toString(),
                    binding.fulltimeSwitch.isChecked(),
                    binding.etLocation.getText().toString()
            );
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.onSearch(s.toString());
            }
        });
    }

    private void initAdapter() {
        adapter = new JobListAdapter(item -> {
            Log.e(TAG, "initAdapter: " + item.getId());
            Intent intent = new Intent(getContext(), DetailJobActivity.class);
            intent.putExtra("id", item.getId());
            startActivity(intent);
        });

        binding.rvList.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.rvList.setLayoutManager(linearLayoutManager);
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                viewModel.onLoadMore(
                        binding.etSearch.getText().toString(),
                        binding.fulltimeSwitch.isChecked(),
                        binding.etLocation.getText().toString());
            }
        };
        binding.rvList.addOnScrollListener(scrollListener);
        SpaceDecoration spaceDecoration = new SpaceDecoration(
                getResources().getDimensionPixelSize(R.dimen.item_spacing)
        );
        binding.rvList.addItemDecoration(spaceDecoration);

        viewModel.getIsLoadingLoadMore().observe(this, isLoadMore -> {
            adapter.setLoading(isLoadMore.booleanValue());
        });
    }

    private void handleFilterShow() {
        viewModel.setVisibleFilter(!viewModel.getVisibleFilter().getValue());
    }

    private void observeResponse(Response<List<JobResponse>> response) {
        switch (response.status) {
            case LOADING:
                handleLoading();
                break;
            case SUCCESS:
                handleSuccess(response);
                break;
            case ERROR:
                handleError(response);
                break;
        }
    }

    private void handleLoading() {
        if (viewModel.getCurrentPage().getValue().intValue() == 1) {
            binding.rvList.setVisibility(View.GONE);
            binding.pbLoading.setVisibility(View.VISIBLE);
        }
    }

    private void handleSuccess(Response<List<JobResponse>> response) {
        if (viewModel.getCurrentPage().getValue().intValue() == 1) {
            binding.pbLoading.setVisibility(View.GONE);
            binding.rvList.setVisibility(View.VISIBLE);
            adapter.setJobList(response.data);
        } else {
            binding.pbLoading.setVisibility(View.GONE);
            adapter.addJobList(response.data);
        }
    }

    private void handleError(Response<List<JobResponse>> response) {
        Toast.makeText(getContext(), response.error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

}