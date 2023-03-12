package com.me.jobsearch.presentation.detail_job;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.me.jobsearch.data.common.Response;
import com.me.jobsearch.data.common.SchedulersFacade;
import com.me.jobsearch.data.remote.model.JobDetailResponse;
import com.me.jobsearch.domain.usecase.GetJobDetailUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.disposables.CompositeDisposable;

@HiltViewModel
public class DetailJobViewModel extends ViewModel {

    private final GetJobDetailUseCase getJobDetailUseCase;
    private final CompositeDisposable disposable;

    private final MutableLiveData<Response<JobDetailResponse>> response = new MutableLiveData<>();

    MutableLiveData<Response<JobDetailResponse>> response() {
        return response;
    }

    private String id;

    @Inject
    public DetailJobViewModel(GetJobDetailUseCase getJobDetailUseCase,
                              SavedStateHandle savedStateHandle) {
        this.getJobDetailUseCase = getJobDetailUseCase;
        this.id = savedStateHandle.get("id");
        disposable = new CompositeDisposable();
    }

    public void setId(String id) {
        this.id = id;
        init();
    }

    private void init() {
        doGetJobDetailById(id);
    }

    private void doGetJobDetailById(String id) {
        disposable.add(getJobDetailUseCase.execute(id)
                .compose(SchedulersFacade.applySingleSchedulers())
                .doOnSubscribe(__ -> response.setValue(Response.loading()))
                .subscribe(data -> response.setValue(Response.success(data)),
                        throwable -> response.setValue(Response.error(throwable))));
    }

    @Override
    protected void onCleared() {
        disposable.clear();
        super.onCleared();
    }
}
