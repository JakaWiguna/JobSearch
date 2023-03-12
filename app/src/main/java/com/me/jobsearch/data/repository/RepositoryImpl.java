package com.me.jobsearch.data.repository;

import com.me.jobsearch.data.common.SchedulersFacade;
import com.me.jobsearch.data.remote.api.NetworkApi;
import com.me.jobsearch.data.remote.model.JobDetailResponse;
import com.me.jobsearch.data.remote.model.JobResponse;
import com.me.jobsearch.domain.repository.Repository;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import retrofit2.HttpException;

public class RepositoryImpl implements Repository {

    private final NetworkApi api;

    @Inject
    public RepositoryImpl(
            NetworkApi api) {
        this.api = api;
    }

    @Override
    public Single<List<JobResponse>> getJobList(String description, String location, Boolean isFulltime, Integer page) {
        return api.getJobList(description, location, isFulltime, page)
                .onErrorResumeNext(error -> {
                    String errorMessage = "An unexpected error occurred";
                    if (error instanceof HttpException) {
                        HttpException httpException = (HttpException) error;
                        if (httpException.code() == 404) {
                            errorMessage = "User not found";
                        } else if (httpException.code() == 401) {
                            errorMessage = "Authentication failed";
                        } else if (httpException.code() == 500) {
                            errorMessage = "Internal server error";
                        }
                    } else if (error instanceof IOException) {
                        errorMessage = "Network error";
                    }
                    return Single.error(new Throwable(errorMessage));
                })
                .map(response -> response.body())
                .compose(SchedulersFacade.applySingleSchedulers());
    }

    @Override
    public Single<JobDetailResponse> getJobDetail(String id) {
        return api.getJobDetailById(id)
                .onErrorResumeNext(error -> {
                    String errorMessage = "An unexpected error occurred";
                    if (error instanceof HttpException) {
                        HttpException httpException = (HttpException) error;
                        if (httpException.code() == 404) {
                            errorMessage = "User not found";
                        } else if (httpException.code() == 401) {
                            errorMessage = "Authentication failed";
                        } else if (httpException.code() == 500) {
                            errorMessage = "Internal server error";
                        }
                    } else if (error instanceof IOException) {
                        errorMessage = "Network error";
                    }
                    return Single.error(new Throwable(errorMessage));
                })
                .map(response -> response.body())
                .compose(SchedulersFacade.applySingleSchedulers());
    }
}
