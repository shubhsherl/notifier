package com.example.test.notifyMe.view.ui;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.test.notifyMe.R;
import com.example.test.notifyMe.databinding.FragmentNotificationListBinding;
import com.example.test.notifyMe.di.Injectable;
import com.example.test.notifyMe.service.model.Notification;
import com.example.test.notifyMe.utils.EndlessRecyclerOnScrollListener;
import com.example.test.notifyMe.view.adapter.NotificationAdapter;
import com.example.test.notifyMe.viewmodel.NotificationListViewModel;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

public class NotificationListFragment extends Fragment implements Injectable {
    public static final String TAG = "NotifyListFragment";
    private NotificationAdapter notificationAdapter;
    private NotificationListViewModel viewModel;

    private Double lastTimestamp;
    private Double latestTimestamp;
    private FragmentNotificationListBinding binding;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification_list, container, false);

        notificationAdapter = new NotificationAdapter();
        binding.notificationList.setAdapter(notificationAdapter);
        binding.setIsLoading(true);
        binding.refresh.setOnClickListener(v -> binding.notificationList.scrollToPosition(0));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.notificationList.setLayoutManager(linearLayoutManager);

        return (View) binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(NotificationListViewModel.class);
        RefreshAfterNotification();
        binding.notificationList.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                    viewModel.setTime(latestTimestamp, lastTimestamp, true);
            }
        });
        observeViewModel(viewModel);
    }

    private void observeViewModel(NotificationListViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getNotificationListObservable().observe(this, new Observer<List<Notification>>() {
            @Override
            public void onChanged(@Nullable List<Notification> notification) {
                if (notification != null) {
                    binding.setIsLoading(false);
                    latestTimestamp = notification.get(0).time;
                    lastTimestamp = notification.get(notification.size()-1).time;
                    notificationAdapter.setNotificationList(notification);
                    notificationAdapter.notifyDataSetChanged();

                }
            }
        });
    }

    private void RefreshAfterNotification() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {
                        try {
                            Log.d(TAG, "Updated After Notification: "+ latestTimestamp);
                            viewModel.setTime(latestTimestamp,lastTimestamp, false);
                        } catch (Exception e) {
                            Log.d(TAG, "Fail to Update After Notification");
                        }
                    }
                });
            }
        };
/*
*       refresh Notification after every 5 sec.
*/
        timer.schedule(doAsynchronousTask, 5000, 5000);
    }
}
