package multiverse.androidapp.multiverse.model.dbModel;

import android.content.ContentValues;
import android.database.Cursor;

import multiverse.androidapp.multiverse.model.commonModel.ConversationModel;

public class ConversationDbModel {

    public static final String TABLE_NAME = "conversation";
    public static final String CONVERSATION_ID_COLUMN_NAME = "conversationID";
    public static final String NAME_COLUMN_NAME = "name";
    public static final String LAST_UPDATE_COLUMN_NAME = "lastUpdate";
    public static final String LAST_DATA_UPDATE_COLUMN_NAME = "lastDataUpdate";

    public static final String SQL_CREATE_CONVERSATION_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    CONVERSATION_ID_COLUMN_NAME + " INTEGER," +
                    NAME_COLUMN_NAME + " TEXT," +
                    LAST_UPDATE_COLUMN_NAME + " NUMERIC," +
                    LAST_DATA_UPDATE_COLUMN_NAME + " NUMERIC," +
                    "PRIMARY KEY(" + CONVERSATION_ID_COLUMN_NAME + "))";

    public static final String SQL_DELETE_CONVERSATION_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public int conversationID;
    public String name;
    public long lastUpdate;
    public long lastDataUpdate;

    public ContentValues getContentValues(){
        ContentValues content = new ContentValues();

        content.put(CONVERSATION_ID_COLUMN_NAME, conversationID);
        content.put(NAME_COLUMN_NAME, name);
        content.put(LAST_UPDATE_COLUMN_NAME, lastUpdate);
        content.put(LAST_DATA_UPDATE_COLUMN_NAME, lastDataUpdate);

        return content;
    }

    public static ConversationDbModel toDbModel(Cursor cursor) {
        ConversationDbModel dbModel = new ConversationDbModel();

        dbModel.conversationID = cursor.getInt(cursor.getColumnIndexOrThrow(CONVERSATION_ID_COLUMN_NAME));
        dbModel.name = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN_NAME));
        dbModel.lastUpdate = cursor.getLong(cursor.getColumnIndexOrThrow(LAST_UPDATE_COLUMN_NAME));
        dbModel.lastDataUpdate = cursor.getLong(cursor.getColumnIndexOrThrow(LAST_DATA_UPDATE_COLUMN_NAME));

        return dbModel;
    }
}
