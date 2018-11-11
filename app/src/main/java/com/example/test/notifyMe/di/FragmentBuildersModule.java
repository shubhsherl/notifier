package com.example.test.notifyMe.di;

import com.example.test.notifyMe.view.ui.NotificationListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract NotificationListFragment contributeNotificationListFragment();
}
