package com.example.test.notifyMe.viewmodel;

import android.app.Application;
import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.test.notifyMe.service.model.Notification;
import com.example.test.notifyMe.service.repository.NotificationRepository;

import java.util.List;

import javax.inject.Inject;

public class NotificationListViewModel extends AndroidViewModel {
    private LiveData<List<Notification>> notificationListObservable;
    private static final String TAG = NotificationListViewModel.class.getName();
    private boolean initialization;
    private boolean before;
    private MutableLiveData<Pair<Double,Double>> time; //<after, before>

    @Inject
    public NotificationListViewModel(@NonNull NotificationRepository notificationRepository, @NonNull Application application) {
        super(application);
        this.time = new MutableLiveData<>();
        before = false;
        initialization = true;
       notificationListObservable = Transformations.switchMap(time, input -> {
            if (initialization) {
                Log.i(TAG, "getNotification");
                return notificationRepository.getNotificationList();
            }
            if (before){
                Log.i(TAG, "getNotification Before" + time.getValue().second.toString());
                return notificationRepository.getBeforeNotificationList(time.getValue().second, notificationListObservable.getValue());
            }
            else {
                Log.i(TAG, "getNotification After" + time.getValue().first.toString());
                return notificationRepository.getAfterNotificationList(time.getValue().first, notificationListObservable.getValue());
            }
        });
        Object unixTime =  System.currentTimeMillis() / 1000L;
        this.time.setValue(Pair.create(((Long) unixTime).doubleValue(), ((Long) unixTime).doubleValue()));
    }

    public LiveData<List<Notification>> getNotificationListObservable() {
        return notificationListObservable;
    }

    public void setTime(Double afterTime, Double beforeTime, boolean before){
        this.time.setValue(Pair.create(afterTime,beforeTime));
        this.before =  before;
        initialization = false;
    }

}
