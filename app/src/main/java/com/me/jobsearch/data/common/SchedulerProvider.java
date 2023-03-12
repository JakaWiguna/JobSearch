package com.me.jobsearch.data.common;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


interface SchedulerProvider {
    Scheduler ui();

    Scheduler computation();

    Scheduler io();
}

class DefaultSchedulerProvider implements SchedulerProvider{
    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }

    @Override
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @Override
    public Scheduler io() {
        return Schedulers.io();
    }
}
