package multiverse.androidapp.multiverse.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;

import multiverse.androidapp.multiverse.database.localDatabase.MultiverseDbHelper;
import multiverse.androidapp.multiverse.database.localDatabase.localDatabaseServices.NotificationLocalDbService;
import multiverse.androidapp.multiverse.database.webDatabase.WebServiceResponse;
import multiverse.androidapp.multiverse.database.webDatabase.webServices.NotificationWebService;
import multiverse.androidapp.multiverse.model.commonModel.notification.NotificationModel;
import multiverse.androidapp.multiverse.model.dbModel.NotificationDbModel;
import multiverse.androidapp.multiverse.model.webModel.notification.ConversationNotificationResponseWebModel;
import multiverse.androidapp.multiverse.model.webModel.notification.NotificationListResponseWebModel;
import multiverse.androidapp.multiverse.model.webModel.notification.UserNotificationResponseWebModel;
import multiverse.androidapp.multiverse.model.webModel.util.ListRequestWebModel;
import multiverse.androidapp.multiverse.repository.callback.NotificationListCallback;
import multiverse.androidapp.multiverse.repository.callback.WebError;

public class NotificationRepository {

    private final Executor executor;
    private final MultiverseDbHelper dbHelper;

    public NotificationRepository(Executor executor, MultiverseDbHelper dbHelper) {
        this.executor = executor;
        this.dbHelper = dbHelper;
    }

    public void getNotificationList(final int count, final int offset, final NotificationListCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // Fetch notification in db for now
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                int totalSize = NotificationLocalDbService.getSize(db);
                if (totalSize > 0) {
                    List<NotificationDbModel> dbList = NotificationLocalDbService.getNotifications(db, offset, count);
                    List<NotificationModel> notifList = new ArrayList<>();
                    for (NotificationDbModel dbNotif : dbList) {
                        notifList.add(NotificationModel.toModel(dbNotif));
                    }
                    callback.notificationListCallback(NotificationListCallback.NotificationListCallbackType.NOTIFICATION, notifList, notifList.size(), offset, totalSize);
                }

                // Fetch notification from the web
                ListRequestWebModel webRequest = new ListRequestWebModel();
                webRequest.offset = offset;
                webRequest.count = count;
                WebServiceResponse<NotificationListResponseWebModel> webReponse = NotificationWebService.getNotificationList(webRequest, context);
                if(webReponse.isResponseOK) {
                    List<NotificationModel> notifList = new ArrayList<>();
                    for (UserNotificationResponseWebModel webNotif : webReponse.data.userNotificationList) {
                        notifList.add(NotificationModel.toModel(webNotif));
                    }
                    for (ConversationNotificationResponseWebModel webNotif : webReponse.data.conversationNotificationList) {
                        notifList.add(NotificationModel.toModel(webNotif));
                    }
                    Collections.sort(notifList, new Comparator<NotificationModel>() {
                        @Override
                        public int compare(NotificationModel o1, NotificationModel o2) {
                            return o1.notificationDate.compareTo(o2.notificationDate);
                        }
                    });

                    // Send the notification to the callback
                    callback.notificationListCallback(NotificationListCallback.NotificationListCallbackType.NOTIFICATION, notifList, webReponse.data.count, webReponse.data.offset, webReponse.data.totalSize);

                    // Add the notification to the database
                    for(NotificationModel notif : notifList) {
                        NotificationLocalDbService.updateOrAddNotification(db, NotificationDbModel.toDbModel(notif));
                    }
                } else {
                    callback.notificationErrorCallback(NotificationListCallback.NotificationListCallbackType.NOTIFICATION, new WebError(webReponse));
                }
                db.close();
            }
        });
    }
}
