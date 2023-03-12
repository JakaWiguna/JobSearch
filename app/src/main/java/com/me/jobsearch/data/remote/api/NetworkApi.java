package com.me.jobsearch.data.remote.api;

import com.me.jobsearch.data.remote.model.JobDetailResponse;
import com.me.jobsearch.data.remote.model.JobResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkApi {
    @GET("api/recruitment/positions.json")
    Single<Response<List<JobResponse>>> getJobList(
            @Query(value = "description") String description,
            @Query(value = "location") String location,
            @Query(value = "full_time") Boolean isFulltime,
            @Query(value = "page") Integer page);

    @GET("api/recruitment/positions/{id}")
    Single<Response<JobDetailResponse>> getJobDetailById(@Path("id") String id);
}
