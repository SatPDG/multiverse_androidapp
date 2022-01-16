package multiverse.androidapp.multiverse.model.commonModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import multiverse.androidapp.multiverse.model.dbModel.MessageDbModel;
import multiverse.androidapp.multiverse.model.webModel.commonModel.MessageWebModel;

public class MessageModel {
    public int messageID;
    public int conversationID;
    public int authorID;
    public Date publishedTime;
    public byte messageType;
    public String message;

    public MessageWebModel toWebModel() {
        MessageWebModel webModel = new MessageWebModel();
        webModel.messageID = messageID;
        webModel.conversationID = conversationID;
        webModel.authorID = authorID;
        webModel.publishedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(publishedTime);
        webModel.messageType = messageType;
        webModel.message = message;
        return webModel;
    }

    public MessageDbModel toDbModel() {
        MessageDbModel dbModel = new MessageDbModel();
        dbModel.messageID = messageID;
        dbModel.conversationID = conversationID;
        dbModel.authorID = authorID;
        dbModel.publishedTime = publishedTime.getTime();
        dbModel.messageType = (int) messageType;
        dbModel.message = message;
        return dbModel;
    }
}
