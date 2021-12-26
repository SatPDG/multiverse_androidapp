package multiverse.androidapp.multiverse.repository.callback;

import java.util.List;

import multiverse.androidapp.multiverse.model.commonModel.notification.NotificationModel;

public interface NotificationListCallback extends WebErrorCallback<NotificationListCallback.NotificationListCallbackType> {

    void notificationListCallback(NotificationListCallback.NotificationListCallbackType type, List<NotificationModel> notifications, int count, int offset, int totalSize);

    enum NotificationListCallbackType {
        NOTIFICATION
    }
}
