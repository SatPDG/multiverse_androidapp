package multiverse.androidapp.multiverse.database.localDatabase.localDatabaseServices;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import multiverse.androidapp.multiverse.model.dbModel.ConversationDbModel;
import multiverse.androidapp.multiverse.model.dbModel.UserDbModel;

public class ConversationLocalDbService {

    private ConversationLocalDbService() {

    }

    public static List<ConversationDbModel> getConversations(SQLiteDatabase db, int offset, int number){
        Cursor cursor = db.rawQuery("SELECT * FROM " + ConversationDbModel.TABLE_NAME +
                " ORDER BY " + ConversationDbModel.LAST_UPDATE_COLUMN_NAME + " DESC " +
                " LIMIT " + number + " OFFSET " + offset, null);

        List<ConversationDbModel> convList = new ArrayList<>();
        while(cursor.moveToNext()){
            convList.add(ConversationDbModel.toDbModel(cursor));
        }
        return convList;
    }

    public static ConversationDbModel getConversation(SQLiteDatabase db, int conversationID) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + ConversationDbModel.TABLE_NAME +
                " WHERE " + ConversationDbModel.CONVERSATION_ID_COLUMN_NAME + "=" + conversationID +
                ";", null);

        ConversationDbModel dbModel = null;
        if(cursor.moveToNext()){
            dbModel = ConversationDbModel.toDbModel(cursor);
        }
        return dbModel;
    }

    public static List<ConversationDbModel> getAllConversation(SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT * FROM " + ConversationDbModel.TABLE_NAME, null);

        List<ConversationDbModel> conversationList = new ArrayList<>();
        while(cursor.moveToNext()){
            conversationList.add(ConversationDbModel.toDbModel(cursor));
        }
        return conversationList;
    }

    public static void deleteConversation(SQLiteDatabase db, int conversationID){
        db.delete(ConversationDbModel.TABLE_NAME,
                ConversationDbModel.CONVERSATION_ID_COLUMN_NAME + "=?",
                new String[] {String.valueOf(conversationID)});
    }

    public static void deleteOlderConversation(SQLiteDatabase db, int nbrOlder) {
        db.execSQL("DELETE FROM " + ConversationDbModel.TABLE_NAME + " WHERE " +
                ConversationDbModel.CONVERSATION_ID_COLUMN_NAME + " IN (" +
                "SELECT " + ConversationDbModel.CONVERSATION_ID_COLUMN_NAME +
                " FROM " + ConversationDbModel.TABLE_NAME +
                " ORDER BY " + ConversationDbModel.LAST_UPDATE_COLUMN_NAME +
                " LIMIT " + nbrOlder + ")");
    }

    public static void addConversation(SQLiteDatabase db, ConversationDbModel conversation){
        db.insert(ConversationDbModel.TABLE_NAME, null, conversation.getContentValues());
    }

    public static void updateConversation(SQLiteDatabase db, ConversationDbModel conversation) {
        db.update(ConversationDbModel.TABLE_NAME,
                conversation.getContentValues(),
                ConversationDbModel.CONVERSATION_ID_COLUMN_NAME + "=?",
                new String[] {String.valueOf(conversation.conversationID)});
    }

    public static void updateOrAddConversation(SQLiteDatabase db, ConversationDbModel conversation) {
        db.replace(ConversationDbModel.TABLE_NAME, null, conversation.getContentValues());
    }

    public static int getSize(SQLiteDatabase db) {
        return (int) DatabaseUtils.queryNumEntries(db, ConversationDbModel.TABLE_NAME);
    }
}
