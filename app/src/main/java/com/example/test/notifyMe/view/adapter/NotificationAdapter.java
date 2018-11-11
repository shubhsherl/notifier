package com.example.test.notifyMe.view.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.test.notifyMe.R;
import com.example.test.notifyMe.databinding.NotificationListItemBinding;
import com.example.test.notifyMe.service.model.Notification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    List<? extends Notification> notificationList;


    public NotificationAdapter() {
    }

    public void setNotificationList(final List<? extends Notification> projectList) {
        if (this.notificationList == null) {
            this.notificationList = projectList;
            notifyItemRangeInserted(0, projectList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return NotificationAdapter.this.notificationList.size();
                }

                @Override
                public int getNewListSize() {
                    return projectList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return NotificationAdapter.this.notificationList.get(oldItemPosition).time ==
                            projectList.get(newItemPosition).time;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Notification project = projectList.get(newItemPosition);
                    Notification old = projectList.get(oldItemPosition);
                    return project.time == old.time
                            && project.message.equals(old.message);
                }
            });
            this.notificationList = projectList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NotificationListItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.notification_list_item,
                        parent, false);

//        binding.setCallback(projectClickCallback);

        return new NotificationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        holder.binding.setNotification(notificationList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return notificationList == null ? 0 : notificationList.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {

        final NotificationListItemBinding binding;

        public NotificationViewHolder(NotificationListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
