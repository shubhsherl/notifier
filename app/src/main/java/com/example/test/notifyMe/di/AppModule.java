package com.example.test.notifyMe.di;

import com.example.test.notifyMe.service.repository.NotificationService;
import com.example.test.notifyMe.viewmodel.NotificationViewModelFactory;

import javax.inject.Singleton;

import androidx.lifecycle.ViewModelProvider;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(subcomponents = ViewModelSubComponent.class)
class AppModule {
    @Singleton @Provides
    NotificationService provideGithubService() {
        return new Retrofit.Builder()
                .baseUrl(NotificationService.HTTPS_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NotificationService.class);
    }

    @Singleton
    @Provides
    ViewModelProvider.Factory provideViewModelFactory(
            ViewModelSubComponent.Builder viewModelSubComponent) {

        return new NotificationViewModelFactory(viewModelSubComponent.build());
    }
}
