package com.me.jobsearch.domain.repository;

import com.me.jobsearch.data.remote.model.JobDetailResponse;
import com.me.jobsearch.data.remote.model.JobResponse;

import java.util.List;

import io.reactivex.Single;

public interface Repository {
    Single<List<JobResponse>> getJobList(
            String description,
            String location,
            Boolean isFulltime,
            Integer page);

    Single<JobDetailResponse> getJobDetail(
            String id);
}
