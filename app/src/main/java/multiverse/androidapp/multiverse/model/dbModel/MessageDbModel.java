package multiverse.androidapp.multiverse.model.dbModel;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

import multiverse.androidapp.multiverse.model.commonModel.MessageModel;

public class MessageDbModel {

    public static final String TABLE_NAME = "message";
    public static final String MESSAGE_ID_COLUMN_NAME = "messageID";
    public static final String CONVERSATION_ID_COLUMN_NAME = "conversationID";
    public static final String AUTHOR_ID_COLUMN_NAME = "authorID";
    public static final String PUBLISHED_TIME_COLUMN_NAME = "publishedTime";
    public static final String MESSAGE_TYPE_COLUMN_NAME = "messageType";
    public static final String MESSAGE_COLUMN_NAME = "message";
    public static final String LAST_DATA_UPDATE_COLUMN_NAME = "lastDataUpdate";

    public static final String SQL_CREATE_MESSAGE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    MESSAGE_ID_COLUMN_NAME + " INTEGER," +
                    CONVERSATION_ID_COLUMN_NAME + " INTEGER," +
                    AUTHOR_ID_COLUMN_NAME + " INTEGER," +
                    PUBLISHED_TIME_COLUMN_NAME + " NUMERIC," +
                    MESSAGE_TYPE_COLUMN_NAME + " INTEGER," +
                    MESSAGE_COLUMN_NAME + " TEXT," +
                    LAST_DATA_UPDATE_COLUMN_NAME + " NUMERIC," +
                    "PRIMARY KEY(" + MESSAGE_ID_COLUMN_NAME + "))";

    public static final String SQL_DELETE_MESSAGE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public int messageID;
    public int conversationID;
    public int authorID;
    public long publishedTime;
    public int messageType;
    public String message;
    public long lastDataUpdate;

    public ContentValues getContentValues(){
        ContentValues content = new ContentValues();

        content.put(MESSAGE_ID_COLUMN_NAME, messageID);
        content.put(CONVERSATION_ID_COLUMN_NAME, conversationID);
        content.put(AUTHOR_ID_COLUMN_NAME, authorID);
        content.put(PUBLISHED_TIME_COLUMN_NAME, publishedTime);
        content.put(MESSAGE_TYPE_COLUMN_NAME, messageType);
        content.put(MESSAGE_COLUMN_NAME, message);
        content.put(LAST_DATA_UPDATE_COLUMN_NAME, lastDataUpdate);

        return content;
    }

    public MessageModel toCommonModel() {
        MessageModel model = new MessageModel();
        model.messageID = messageID;
        model.conversationID = conversationID;
        model.authorID = authorID;
        model.publishedTime = new Date(publishedTime);
        model.messageType = (byte) messageType;
        model.message = message;
        return model;
    }

    public static MessageDbModel toDbModel(Cursor cursor){
        MessageDbModel dbModel = new MessageDbModel();

        dbModel.messageID = cursor.getInt(cursor.getColumnIndexOrThrow(MESSAGE_ID_COLUMN_NAME));
        dbModel.conversationID = cursor.getInt(cursor.getColumnIndexOrThrow(CONVERSATION_ID_COLUMN_NAME));
        dbModel.authorID = cursor.getInt(cursor.getColumnIndexOrThrow(AUTHOR_ID_COLUMN_NAME));
        dbModel.publishedTime = cursor.getLong(cursor.getColumnIndexOrThrow(PUBLISHED_TIME_COLUMN_NAME));
        dbModel.messageType = cursor.getInt(cursor.getColumnIndexOrThrow(MESSAGE_TYPE_COLUMN_NAME));
        dbModel.message = cursor.getString(cursor.getColumnIndexOrThrow(MESSAGE_COLUMN_NAME));
        dbModel.lastDataUpdate = cursor.getLong(cursor.getColumnIndexOrThrow(LAST_DATA_UPDATE_COLUMN_NAME));

        return dbModel;
    }
}
