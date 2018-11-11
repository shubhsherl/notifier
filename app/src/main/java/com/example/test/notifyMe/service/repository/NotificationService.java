package com.example.test.notifyMe.service.repository;

import com.example.test.notifyMe.service.model.Notifications;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface NotificationService {
    String HTTPS_API_URL = "http://18.222.17.234:8000/";

    @POST("getNotifications")
    Call<Notifications> getNotificationList();

    @POST("getNotifications")
    @FormUrlEncoded
    Call<Notifications> getBeforeNotificationList(@Field("before_time") Double time);

    @POST("getNotifications")
    @FormUrlEncoded
    Call<Notifications> getAfterNotificationList(@Field("after_time") Double time);
}
