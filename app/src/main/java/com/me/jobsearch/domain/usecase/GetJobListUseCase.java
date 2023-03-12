package com.me.jobsearch.domain.usecase;

import com.me.jobsearch.data.remote.model.JobResponse;
import com.me.jobsearch.data.repository.RepositoryImpl;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class GetJobListUseCase {

    private final RepositoryImpl repository;

    @Inject
    public GetJobListUseCase(
            RepositoryImpl repository) {
        this.repository = repository;
    }

    public Single<List<JobResponse>> execute(
            String description,
            String location,
            Boolean isFulltime,
            Integer page
    ) {
        return repository.getJobList(
                description,
                location,
                isFulltime,
                page);
    }
}
