package multiverse.androidapp.multiverse.model.commonModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import multiverse.androidapp.multiverse.model.dbModel.MessageDbModel;

public class MessageModel {

    public int messageID;
    public int conversationID;
    public int authorID;
    public String publishedTime;
    public byte messageType;
    public String message;

    public MessageDbModel toDbModel() {
        MessageDbModel dbModel = new MessageDbModel();
        dbModel.messageID = messageID;
        dbModel.conversationID = conversationID;
        dbModel.authorID = authorID;
        try {
            dbModel.publishedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(publishedTime).getTime();
        } catch (ParseException e) {
            dbModel.publishedTime = Calendar.getInstance().getTime().getTime();
        }
        dbModel.messageType = messageType;
        dbModel.message = message;

        return dbModel;
    }

    public static MessageModel toModel(MessageDbModel dbModel) {
        MessageModel model = new MessageModel();
        model.messageID = dbModel.messageID;
        model.conversationID = dbModel.conversationID;
        model.authorID = dbModel.authorID;
        model.publishedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(dbModel.publishedTime));
        model.messageType = (byte) dbModel.messageType;
        model.message = dbModel.message;

        return model;
    }
}
