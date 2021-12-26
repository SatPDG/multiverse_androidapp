package multiverse.androidapp.multiverse.database.localDatabase.localDatabaseServices;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import multiverse.androidapp.multiverse.model.dbModel.ConversationDbModel;
import multiverse.androidapp.multiverse.model.dbModel.MessageDbModel;
import multiverse.androidapp.multiverse.model.dbModel.NotificationDbModel;

public class MessageLocalDbServices {

    private MessageLocalDbServices(){

    }

    public static List<MessageDbModel> getMessages(SQLiteDatabase db, int offset, int number){
        Cursor cursor = db.rawQuery("SELECT * FROM " + MessageDbModel.TABLE_NAME +
                " ORDER BY " + MessageDbModel.PUBLISHED_TIME_COLUMN_NAME + " DESC " +
                " LIMIT " + number + " OFFSET " + offset, null);

        List<MessageDbModel> messageList = new ArrayList<>();
        while(cursor.moveToNext()){
            messageList.add(MessageDbModel.toDbModel(cursor));
        }
        return messageList;
    }

    public static List<MessageDbModel> getAllMessage(SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT * FROM " + MessageDbModel.TABLE_NAME, null);

        List<MessageDbModel> messageList = new ArrayList<>();
        while(cursor.moveToNext()){
            messageList.add(MessageDbModel.toDbModel(cursor));
        }
        return messageList;
    }

    public static void deleteMessage(SQLiteDatabase db, int notificationID){
        db.delete(MessageDbModel.TABLE_NAME,
                MessageDbModel.MESSAGE_ID_COLUMN_NAME + "=?",
                new String[] {String.valueOf(notificationID)});
    }

    public static void deleteOlderMessage(SQLiteDatabase db, int nbrOlder) {
        db.execSQL("DELETE FROM " + MessageDbModel.TABLE_NAME + " WHERE " +
                MessageDbModel.MESSAGE_ID_COLUMN_NAME + " IN (" +
                "SELECT " + MessageDbModel.MESSAGE_ID_COLUMN_NAME +
                " FROM " + MessageDbModel.TABLE_NAME +
                " ORDER BY " + MessageDbModel.PUBLISHED_TIME_COLUMN_NAME +
                " LIMIT " + nbrOlder + ")");
    }

    public static void addMessage(SQLiteDatabase db, MessageDbModel message){
        db.insert(MessageDbModel.TABLE_NAME, null, message.getContentValues());
    }

    public static int getSize(SQLiteDatabase db) {
        return (int) DatabaseUtils.queryNumEntries(db, MessageDbModel.TABLE_NAME);
    }

    public static void updateMessage(SQLiteDatabase db, MessageDbModel message) {
        db.update(MessageDbModel.TABLE_NAME,
                message.getContentValues(),
                MessageDbModel.MESSAGE_ID_COLUMN_NAME + "=?",
                new String[] {String.valueOf(message.messageID)});
    }

    public static void updateOrAddMessage(SQLiteDatabase db, MessageDbModel message) {
        db.replace(MessageDbModel.TABLE_NAME, null, message.getContentValues());
    }
}
