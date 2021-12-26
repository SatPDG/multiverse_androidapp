package multiverse.androidapp.multiverse.databaseContext;

import android.database.sqlite.SQLiteDatabase;

import multiverse.androidapp.multiverse.model.dbModel.ConversationDbModel;
import multiverse.androidapp.multiverse.model.dbModel.MessageDbModel;
import multiverse.androidapp.multiverse.model.dbModel.NotificationDbModel;
import multiverse.androidapp.multiverse.model.dbModel.UserDbModel;

public class DatabaseUtil {

    private  DatabaseUtil(){

    }

    public static void addUsers(SQLiteDatabase db, int nbrOfUsers){
        for(int i = 1; i < nbrOfUsers+1; i++) {
            UserDbModel user = new UserDbModel();
            user.userID = i;
            user.firstname = "firstname_" + i;
            user.lastname = "lastname_" + i;
            user.lastLocation = "(0, 0)";
            user.lastDataUpdate = 0;

            db.insert(UserDbModel.TABLE_NAME, null, user.getContentValues());
        }
    }

    public static void addNotifications(SQLiteDatabase db, int nbrOfNotification) {
        for(int i = 0; i < nbrOfNotification; i++) {
            NotificationDbModel notif = new NotificationDbModel();
            notif.notificationID = i;
            notif.notificationType = 0;
            notif.objectID = 0;
            notif.lastDataUpdate = 0;
            notif.date = i;

            db.insert(NotificationDbModel.TABLE_NAME, null, notif.getContentValues());
        }
    }

    public static void addConversation(SQLiteDatabase db, int nbrOfConversation) {
        for(int i = 0; i < nbrOfConversation; i++) {
            ConversationDbModel conv = new ConversationDbModel();
            conv.conversationID = i;
            conv.lastUpdate = i;
            conv.name = "conv_" + String.valueOf(i);
            conv.lastDataUpdate = 0;

            db.insert(ConversationDbModel.TABLE_NAME, null, conv.getContentValues());
        }
    }

    public static void addMessage(SQLiteDatabase db, int nbrOfMessage) {
        for(int i = 0; i < nbrOfMessage; i++) {
            MessageDbModel message = new MessageDbModel();
            message.messageID = i;
            message.messageType = 0;
            message.message = "message_" + String.valueOf(i);
            message.publishedTime = i;
            message.authorID = 0;
            message.conversationID = 0;

            db.insert(MessageDbModel.TABLE_NAME, null, message.getContentValues());
        }
    }
}
