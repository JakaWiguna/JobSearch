package com.me.jobsearch.domain.usecase;

import com.me.jobsearch.data.remote.model.JobDetailResponse;
import com.me.jobsearch.data.repository.RepositoryImpl;

import javax.inject.Inject;

import io.reactivex.Single;

public class GetJobDetailUseCase {

    private final RepositoryImpl repository;

    @Inject
    public GetJobDetailUseCase(
            RepositoryImpl repository) {
        this.repository = repository;
    }

    public Single<JobDetailResponse> execute(
            String id
    ) {
        return repository.getJobDetail(
                id);
    }
}
