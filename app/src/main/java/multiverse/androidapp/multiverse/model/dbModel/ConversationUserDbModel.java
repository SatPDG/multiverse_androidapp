package multiverse.androidapp.multiverse.model.dbModel;

import multiverse.androidapp.multiverse.model.commonModel.ConversationModel;

public class ConversationUserDbModel {

    public static final String TABLE_NAME = "conversationUser";
    public static final String CONVERSATION_ID_COLUMN_NAME = "conversationID";
    public static final String USER_ID_COLUMN_NAME = "userID";
    public static final String LAST_DATA_UPDATE_COLUMN_NAME = "lastDataUpdate";

    public static final String SQL_CREATE_CONVERSATION_USER_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    CONVERSATION_ID_COLUMN_NAME + " INTEGER," +
                    USER_ID_COLUMN_NAME + " INTEGER," +
                    LAST_DATA_UPDATE_COLUMN_NAME + " NUMERIC)";

    public static final String SQL_DELETE_CONVERSATION_USER_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public int conversationID;
    public int userID;
    public int lastDataUpdate;

}
