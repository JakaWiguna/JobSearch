package com.me.jobsearch;

import android.app.Application;

import androidx.multidex.MultiDex;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class JobSearchApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);
    }
}
