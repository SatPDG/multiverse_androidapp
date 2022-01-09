package multiverse.androidapp.multiverse.repository.callback;

import java.util.List;

import multiverse.androidapp.multiverse.model.commonModel.notification.NotificationModel;

public interface NotificationListCallback {

    void notificationListCallback(NotificationListCallback.NotificationListCallbackType type, List<NotificationModel> notifications, int count, int offset, int totalSize);

    void notificationErrorCallback(NotificationListCallbackType type, WebError webError);

    enum NotificationListCallbackType {
        NOTIFICATION
    }
}
