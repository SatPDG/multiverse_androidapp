package multiverse.androidapp.multiverse.database.localDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import multiverse.androidapp.multiverse.model.dbModel.ConversationDbModel;
import multiverse.androidapp.multiverse.model.dbModel.ConversationUserDbModel;
import multiverse.androidapp.multiverse.model.dbModel.MessageDbModel;
import multiverse.androidapp.multiverse.model.dbModel.NotificationDbModel;
import multiverse.androidapp.multiverse.model.dbModel.RelationshipDbModel;
import multiverse.androidapp.multiverse.model.dbModel.UserDbModel;

public class MultiverseDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Multiverse.db";

    public MultiverseDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserDbModel.SQL_CREATE_USER_TABLE);
        db.execSQL(ConversationDbModel.SQL_CREATE_CONVERSATION_TABLE);
        //db.execSQL(ConversationUserDbModel.SQL_CREATE_CONVERSATION_USER_TABLE);
        //db.execSQL(RelationshipDbModel.SQL_CREATE_RELATIONSHIP_TABLE);
        db.execSQL(MessageDbModel.SQL_CREATE_MESSAGE_TABLE);
        db.execSQL(NotificationDbModel.SQL_CREATE_NOTIFICATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(UserDbModel.SQL_DELETE_USER_TABLE);
        db.execSQL(ConversationDbModel.SQL_DELETE_CONVERSATION_TABLE);
        //db.execSQL(ConversationUserDbModel.SQL_DELETE_CONVERSATION_USER_TABLE);
        //db.execSQL(RelationshipDbModel.SQL_DELETE_RELATIONSHIP_TABLE);
        db.execSQL(MessageDbModel.SQL_DELETE_MESSAGE_TABLE);
        db.execSQL(NotificationDbModel.SQL_DELETE_NOTIFICATION_TABLE);

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
