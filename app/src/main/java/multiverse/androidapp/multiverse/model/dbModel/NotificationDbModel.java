package multiverse.androidapp.multiverse.model.dbModel;

import android.app.Notification;
import android.content.ContentValues;
import android.database.Cursor;

import multiverse.androidapp.multiverse.model.commonModel.notification.ConversationNotificationModel;
import multiverse.androidapp.multiverse.model.commonModel.notification.NotificationModel;
import multiverse.androidapp.multiverse.model.commonModel.notification.UserNotificationModel;

public class NotificationDbModel {

    public static final String TABLE_NAME = "notification";
    public static final String NOTIFICATION_ID_COLUMN_NAME = "notificationID";
    public static final String DATE_COLUMN_NAME = "date";
    public static final String NOTIFICATION_TYPE_COLUMN_NAME = "notificationType";
    public static final String USER_ID_COlUMN_NAME = "userID";
    public static final String USER_FIRSTNAME_COlUMN_NAME = "userFirstname";
    public static final String USER_LASTNAME_COlUMN_NAME = "userLastname";
    public static final String CONVERSATION_ID_COlUMN_NAME = "conversationID";
    public static final String CONVERSATION_NAME_COlUMN_NAME = "conversationName";
    public static final String LAST_DATA_UPDATE_COLUMN_NAME = "lastDataUpdate";

    public static final String SQL_CREATE_NOTIFICATION_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    NOTIFICATION_ID_COLUMN_NAME + " INTEGER," +
                    DATE_COLUMN_NAME + " NUMERIC," +
                    NOTIFICATION_TYPE_COLUMN_NAME + " INTEGER," +
                    USER_ID_COlUMN_NAME + " INTEGER," +
                    USER_FIRSTNAME_COlUMN_NAME + " TEXT," +
                    USER_LASTNAME_COlUMN_NAME + " TEXT," +
                    CONVERSATION_ID_COlUMN_NAME + " INTEGER," +
                    CONVERSATION_NAME_COlUMN_NAME + " TEXT," +
                    LAST_DATA_UPDATE_COLUMN_NAME + " NUMERIC," +
                    "PRIMARY KEY(" + NOTIFICATION_ID_COLUMN_NAME + "))";

    public static final String SQL_DELETE_NOTIFICATION_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public int notificationID;
    public long date;
    public int notificationType;
    public int userID;
    public String userFirstname;
    public String userLastname;
    public int conversationID;
    public String conversationName;
    public long lastDataUpdate;

    public ContentValues getContentValues(){
        ContentValues content = new ContentValues();

        content.put(NOTIFICATION_ID_COLUMN_NAME, notificationID);
        content.put(DATE_COLUMN_NAME, date);
        content.put(NOTIFICATION_TYPE_COLUMN_NAME, notificationType);
        content.put(USER_ID_COlUMN_NAME, userID);
        content.put(USER_FIRSTNAME_COlUMN_NAME, userFirstname);
        content.put(USER_LASTNAME_COlUMN_NAME, userLastname);
        content.put(CONVERSATION_ID_COlUMN_NAME, conversationID);
        content.put(CONVERSATION_NAME_COlUMN_NAME, conversationName);
        content.put(LAST_DATA_UPDATE_COLUMN_NAME, lastDataUpdate);

        return content;
    }

    public static NotificationDbModel toDbModel(Cursor cursor){
        NotificationDbModel dbModel = new NotificationDbModel();

        dbModel.notificationID = cursor.getInt(cursor.getColumnIndexOrThrow(NOTIFICATION_ID_COLUMN_NAME));
        dbModel.date = cursor.getLong(cursor.getColumnIndexOrThrow(DATE_COLUMN_NAME));
        dbModel.notificationType = cursor.getInt(cursor.getColumnIndexOrThrow(NOTIFICATION_TYPE_COLUMN_NAME));
        dbModel.userID = cursor.getInt(cursor.getColumnIndexOrThrow(USER_ID_COlUMN_NAME));
        dbModel.userFirstname = cursor.getString(cursor.getColumnIndexOrThrow(USER_FIRSTNAME_COlUMN_NAME));
        dbModel.userLastname = cursor.getString(cursor.getColumnIndexOrThrow(USER_LASTNAME_COlUMN_NAME));
        dbModel.conversationID = cursor.getInt(cursor.getColumnIndexOrThrow(CONVERSATION_ID_COlUMN_NAME));
        dbModel.conversationName = cursor.getString(cursor.getColumnIndexOrThrow(CONVERSATION_NAME_COlUMN_NAME));
        dbModel.lastDataUpdate = cursor.getLong(cursor.getColumnIndexOrThrow(LAST_DATA_UPDATE_COLUMN_NAME));

        return dbModel;
    }

    public static NotificationDbModel toDbModel(NotificationModel model) {
        NotificationDbModel dbModel = new NotificationDbModel();
        dbModel.notificationID = model.notificationID;
        dbModel.notificationType = model.notificationType;
        dbModel.date = model.notificationDate.getTime();

        if(model.isAnUserNotification()){
            dbModel.userID = ((UserNotificationModel)model).userID;
            dbModel.userFirstname = ((UserNotificationModel)model).userFirstname;
            dbModel.userLastname = ((UserNotificationModel)model).userLastname;
        }else if(model.isAConversationNotification()) {
            dbModel.conversationID = ((ConversationNotificationModel)model).conversationID;
            dbModel.conversationName = ((ConversationNotificationModel)model).conversationName;
        }

        return dbModel;
    }
}
