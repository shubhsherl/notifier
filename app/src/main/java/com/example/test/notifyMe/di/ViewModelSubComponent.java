package com.example.test.notifyMe.di;

import com.example.test.notifyMe.viewmodel.NotificationListViewModel;
import com.example.test.notifyMe.viewmodel.NotificationViewModelFactory;

import dagger.Subcomponent;

/**
 * A sub component to create ViewModels. It is called by the
 * {@link NotificationViewModelFactory}.
 */
@Subcomponent
public interface ViewModelSubComponent {
    @Subcomponent.Builder
    interface Builder {
        ViewModelSubComponent build();
    }

    NotificationListViewModel notificationListViewModel();
}
