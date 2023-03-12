package com.me.jobsearch.presentation.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.me.jobsearch.data.common.Response;
import com.me.jobsearch.data.common.SchedulersFacade;
import com.me.jobsearch.data.remote.model.JobResponse;
import com.me.jobsearch.domain.usecase.GetJobListUseCase;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;

@HiltViewModel
public class HomeViewModel extends ViewModel {
    private final GetJobListUseCase getJobListUseCase;
    private final CompositeDisposable disposable;
    private final MutableLiveData<Boolean> visibleFilter = new MutableLiveData<>(false);
    private final MutableLiveData<String> searchText = new MutableLiveData<>(null);
    private final MutableLiveData<Boolean> isFulltime = new MutableLiveData<>(null);
    private final MutableLiveData<String> locationText = new MutableLiveData<>(null);

    public LiveData<Boolean> getVisibleFilter() {
        return visibleFilter;
    }

    public void setVisibleFilter(Boolean visibleFilter) {
        this.visibleFilter.setValue(visibleFilter);
    }

    public LiveData<String> getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText.setValue(searchText);
    }

    public LiveData<Boolean> getIsFulltime() {
        return isFulltime;
    }

    public void setIsFulltime(Boolean isFulltime) {
        this.isFulltime.setValue(isFulltime);
    }

    public LiveData<String> getLocationText() {
        return locationText;
    }

    public void setLocationText(String locationText) {
        this.locationText.setValue(locationText);
    }

    private final MutableLiveData<Response<List<JobResponse>>> response = new MutableLiveData<>();

    MutableLiveData<Response<List<JobResponse>>> response() {
        return response;
    }

    private final MutableLiveData<Boolean> isLoadingLoadMore = new MutableLiveData<>(false);

    public LiveData<Boolean> getIsLoadingLoadMore() {
        return isLoadingLoadMore;
    }

    private final MutableLiveData<Integer> currentPage = new MutableLiveData<>(1);

    public LiveData<Integer> getCurrentPage() {
        return currentPage;
    }

    private static final int PAGE_SIZE = 10;
    private boolean isLastPage = false;

    @Inject
    public HomeViewModel(
            GetJobListUseCase getJobListUseCase
    ) {
        this.getJobListUseCase = getJobListUseCase;
        disposable = new CompositeDisposable();
        init();
    }

    private void init() {
        loadJobs(null, null, null);
    }

    public void onSearch(String searchText) {
        setSearchText(!searchText.isEmpty() ? searchText : null);
        loadJobs(
                getSearchText().getValue(),
                getLocationText().getValue(),
                getIsFulltime().getValue());
    }

    public void onSearch(String searchText, Boolean isFulltime, String locationText) {
        setSearchText(!searchText.isEmpty() ? searchText : null);
        setIsFulltime(isFulltime);
        setLocationText(!locationText.isEmpty() ? locationText : null);
        loadJobs(
                getSearchText().getValue(),
                getLocationText().getValue(),
                getIsFulltime().getValue());
    }

    public void onLoadMore(String searchText, Boolean isFulltime, String locationText) {
        setSearchText(!searchText.isEmpty() ? searchText : null);
        setIsFulltime(isFulltime);
        setLocationText(!locationText.isEmpty() ? locationText : null);
        loadMoreJobs(
                getSearchText().getValue(),
                getLocationText().getValue(),
                getIsFulltime().getValue());
    }

    public void loadJobs(String description, String location, Boolean isFulltime) {
        currentPage.setValue(1);
        isLastPage = false;
        disposable.clear();
        doGetPositions(description, location, isFulltime, currentPage.getValue().intValue());
    }

    public void loadMoreJobs(String description, String location, Boolean isFulltime) {
        if (!isLoadingLoadMore.getValue().booleanValue() && !isLastPage) {
            currentPage.setValue(currentPage.getValue().intValue() + 1);
            isLoadingLoadMore.setValue(true);
            doGetPositions(description, location, isFulltime, currentPage.getValue().intValue());
        }
    }

    private void doGetPositions(
            @Nullable String description,
            @Nullable String location,
            @Nullable Boolean isFulltime,
            @Nullable Integer page) {
        disposable.add(getJobListUseCase.execute(description, location, isFulltime, page)
                .compose(SchedulersFacade.applySingleSchedulers())
                .doOnSubscribe(__ -> response.setValue(Response.loading()))
                .subscribeWith(new DisposableSingleObserver<List<JobResponse>>() {
                    @Override
                    public void onSuccess(List<JobResponse> data) {
                        Iterator<JobResponse> iterator = data.iterator();
                        while (iterator.hasNext()) {
                            Object obj = iterator.next();
                            if (obj == null) {
                                iterator.remove();
                            }
                        }
                        if (data.size() < PAGE_SIZE) {
                            isLastPage = true;
                        }
                        if (currentPage.getValue().intValue() != 1) {
                            isLoadingLoadMore.setValue(false);
                        }
                        response.setValue(Response.success(data));
                    }

                    @Override
                    public void onError(Throwable e) {
                        response.setValue(Response.error(e));
                    }
                }));
    }

    @Override
    protected void onCleared() {
        disposable.clear();
        super.onCleared();
    }
}
