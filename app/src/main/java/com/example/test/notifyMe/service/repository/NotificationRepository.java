package com.example.test.notifyMe.service.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.test.notifyMe.service.model.Notifications;
import com.example.test.notifyMe.service.model.Notification;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class NotificationRepository {
    private NotificationService notificationService;

    @Inject
    public NotificationRepository(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public LiveData<List<Notification>> getNotificationList() {
        final MutableLiveData<List<Notification>> data = new MutableLiveData<>();

        notificationService.getNotificationList().enqueue(new Callback<Notifications>() {
            @Override
            public void onResponse(Call<Notifications> call, Response<Notifications> response) {
                data.setValue(response.body().data);

            }

            @Override
            public void onFailure(Call<Notifications> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<List<Notification>> getBeforeNotificationList(Double timestamp, List<Notification> previousdata) {
        final MutableLiveData<List<Notification>> data = new MutableLiveData<>();

        notificationService.getBeforeNotificationList(timestamp).enqueue(new Callback<Notifications>() {
            @Override
            public void onResponse(Call<Notifications> call, Response<Notifications> response) {
                if (previousdata !=null) {
                    previousdata.addAll(response.body().data);
                    data.setValue(previousdata);
                }else
                    data.setValue(response.body().data);
            }

            @Override
            public void onFailure(Call<Notifications> call, Throwable t) {
                data.setValue(previousdata);
            }
        });

        return data;
    }

    public LiveData<List<Notification>> getAfterNotificationList(Double timestamp, List<Notification> previousdata) {
        final MutableLiveData<List<Notification>> data = new MutableLiveData<>();

        notificationService.getAfterNotificationList(timestamp).enqueue(new Callback<Notifications>() {
            @Override
            public void onResponse(Call<Notifications> call, Response<Notifications> response) {
                if (previousdata !=null) {
                    List<Notification> temp = response.body().data;
                    for (int i = 0;i<temp.size();i++){
                        previousdata.add(0,temp.get(i));
                    }
                    data.setValue(previousdata);
                }else
                    data.setValue(response.body().data);
            }

            @Override
            public void onFailure(Call<Notifications> call, Throwable t) {
                data.setValue(previousdata);
            }
        });
        return data;
    }
}
