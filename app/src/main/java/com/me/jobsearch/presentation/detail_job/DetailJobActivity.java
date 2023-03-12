package com.me.jobsearch.presentation.detail_job;

import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.core.text.HtmlCompat;

import com.me.jobsearch.R;
import com.me.jobsearch._core.BaseActivity;
import com.me.jobsearch.data.common.Response;
import com.me.jobsearch.data.remote.model.JobDetailResponse;
import com.me.jobsearch.databinding.ActivityDetailJobBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DetailJobActivity extends BaseActivity<ActivityDetailJobBinding, DetailJobViewModel> {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_detail_job;
    }

    @Override
    protected Class<DetailJobViewModel> getViewModelClass() {
        return DetailJobViewModel.class;
    }

    @Override
    protected void initViews() {
        binding.setLifecycleOwner(this);
        String id = getIntent().getStringExtra("id");
        binding.setLifecycleOwner(this);
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        binding.toolbarTitle.setText("Job Detail");

        viewModel.setId(id);
        viewModel.response().observe(this, this::observeResponse);
    }

    private void observeResponse(Response<JobDetailResponse> response) {
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
        binding.nsvJobDetail.setVisibility(View.GONE);
        binding.pbLoading.setVisibility(View.VISIBLE);
    }

    private void handleSuccess(Response<JobDetailResponse> response) {
        binding.pbLoading.setVisibility(View.GONE);
        binding.nsvJobDetail.setVisibility(View.VISIBLE);
        if (response.data != null) {
            binding.setItem(response.data);
            binding.tvUrlWeb.setText(HtmlCompat.fromHtml(getResources().getString(R.string.go_to_website), 0));
            binding.tvUrlWeb.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(response.data.getCompanyUrl()));
                startActivity(intent);
            });
            binding.tvDescription.setText(HtmlCompat.fromHtml(response.data.getDescription(), 0));
        }
    }

    private void handleError(Response<JobDetailResponse> response) {
        binding.nsvJobDetail.setVisibility(View.GONE);
        binding.pbLoading.setVisibility(View.GONE);
        Toast.makeText(this, response.error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
