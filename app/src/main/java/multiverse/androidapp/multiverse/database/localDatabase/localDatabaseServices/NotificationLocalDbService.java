package multiverse.androidapp.multiverse.database.localDatabase.localDatabaseServices;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import multiverse.androidapp.multiverse.model.dbModel.NotificationDbModel;

public class NotificationLocalDbService {

    private NotificationLocalDbService() {

    }

    public static List<NotificationDbModel> getNotifications(SQLiteDatabase db, int offset, int number){
        Cursor cursor = db.rawQuery("SELECT * FROM " + NotificationDbModel.TABLE_NAME +
                " ORDER BY " + NotificationDbModel.DATE_COLUMN_NAME + " DESC " +
                " LIMIT " + number + " OFFSET " + offset, null);

        List<NotificationDbModel> notificationList = new ArrayList<>();
        while(cursor.moveToNext()){
            notificationList.add(NotificationDbModel.toDbModel(cursor));
        }
        return notificationList;
    }

    public static List<NotificationDbModel> getAllNotification(SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT * FROM " + NotificationDbModel.TABLE_NAME, null);

        List<NotificationDbModel> notificationList = new ArrayList<>();
        while(cursor.moveToNext()){
            notificationList.add(NotificationDbModel.toDbModel(cursor));
        }
        return notificationList;
    }

    public static void deleteNotification(SQLiteDatabase db, int notificationID){
        db.delete(NotificationDbModel.TABLE_NAME,
                NotificationDbModel.NOTIFICATION_ID_COLUMN_NAME + "=?",
                new String[] {String.valueOf(notificationID)});
    }

    public static void deleteOlderNotification(SQLiteDatabase db, int nbrOlder) {
        db.execSQL("DELETE FROM " + NotificationDbModel.TABLE_NAME + " WHERE " +
                NotificationDbModel.NOTIFICATION_ID_COLUMN_NAME + " IN (" +
                "SELECT " + NotificationDbModel.NOTIFICATION_ID_COLUMN_NAME +
                " FROM " + NotificationDbModel.TABLE_NAME +
                " ORDER BY " + NotificationDbModel.DATE_COLUMN_NAME +
                " LIMIT " + nbrOlder + ")");
    }

    public static void updateOrAddNotification(SQLiteDatabase db, NotificationDbModel notification) {
        db.replace(NotificationDbModel.TABLE_NAME, null, notification.getContentValues());
    }

    public static void addNotification(SQLiteDatabase db, NotificationDbModel notification){
        db.insert(NotificationDbModel.TABLE_NAME, null, notification.getContentValues());
    }

    public static int getSize(SQLiteDatabase db) {
        return (int) DatabaseUtils.queryNumEntries(db, NotificationDbModel.TABLE_NAME);
    }
}
