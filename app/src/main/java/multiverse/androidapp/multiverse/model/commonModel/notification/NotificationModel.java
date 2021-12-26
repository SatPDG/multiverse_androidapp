package multiverse.androidapp.multiverse.model.commonModel.notification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import multiverse.androidapp.multiverse.model.dbModel.NotificationDbModel;
import multiverse.androidapp.multiverse.model.webModel.notification.ConversationNotificationResponseWebModel;
import multiverse.androidapp.multiverse.model.webModel.notification.UserNotificationResponseWebModel;

public class NotificationModel {

    public static final byte SYSTEM_NOTIF = 0;
    public static final byte NEW_FOLLOWER_REQ = 1;
    public static final byte NEW_FOLLOWED = 2;
    public static final byte NEW_CONVERSATION = 3;
    public static final byte NEW_MESSAGE = 4;
    public static final byte ADDED_IN_CONVERSATION = 5;

    public int notificationID;
    public Date notificationDate;
    public byte notificationType;

    public boolean isAnUserNotification() {
        return notificationType == NEW_FOLLOWED || notificationType == NEW_FOLLOWER_REQ;
    }

    public boolean isAConversationNotification() {
        return notificationType == NEW_CONVERSATION || notificationType == NEW_MESSAGE || notificationType == ADDED_IN_CONVERSATION;
    }

    public static NotificationModel toModel(UserNotificationResponseWebModel webModel) {
        UserNotificationModel model = new UserNotificationModel();
        model.notificationID = webModel.notificationID;
        try {
            model.notificationDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(webModel.notificationDate);
        } catch (ParseException e) {
            model.notificationDate = Calendar.getInstance().getTime();
        }
        model.notificationType = webModel.notificationType;

        model.userID = webModel.userID;
        model.userFirstname = webModel.userFirstname;
        model.userLastname = webModel.userLastname;
        return model;
    }

    public static NotificationModel toModel(ConversationNotificationResponseWebModel webModel) {
        ConversationNotificationModel model = new ConversationNotificationModel();
        model.notificationID = webModel.notificationID;
        try {
            model.notificationDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(webModel.notificationDate);
        } catch (ParseException e) {
            model.notificationDate = Calendar.getInstance().getTime();
        }
        model.notificationType = webModel.notificationType;

        model.conversationID = webModel.conversationID;
        model.conversationName = webModel.conversationName;
        return model;
    }

    public static NotificationModel toModel(NotificationDbModel dbModel) {
        NotificationModel model = null;
        if(dbModel.notificationType == NEW_FOLLOWER_REQ || dbModel.notificationType == NEW_FOLLOWED) {
            model = new UserNotificationModel();
            model.notificationID = dbModel.notificationID;
            model.notificationDate = new Date(dbModel.date);

            model.notificationType = (byte) dbModel.notificationType;

            ((UserNotificationModel)model).userID = dbModel.userID;
            ((UserNotificationModel)model).userFirstname = dbModel.userFirstname;
            ((UserNotificationModel)model).userLastname = dbModel.userLastname;
        } else if(dbModel.notificationType == NEW_CONVERSATION || dbModel.notificationType == NEW_MESSAGE || dbModel.notificationType == ADDED_IN_CONVERSATION) {
            model = new ConversationNotificationModel();
            model.notificationID = dbModel.notificationID;
            model.notificationDate = new Date(dbModel.date);

            model.notificationType = (byte)dbModel.notificationType;

            ((ConversationNotificationModel)model).conversationID = dbModel.conversationID;
            ((ConversationNotificationModel)model).conversationName = dbModel.conversationName;
        }
        return model;
    }
}
