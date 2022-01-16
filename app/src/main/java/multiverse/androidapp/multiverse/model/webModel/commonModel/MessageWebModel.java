package multiverse.androidapp.multiverse.model.webModel.commonModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import multiverse.androidapp.multiverse.model.commonModel.MessageModel;
import multiverse.androidapp.multiverse.model.dbModel.MessageDbModel;

public class MessageWebModel {

    public int messageID;
    public int conversationID;
    public int authorID;
    public String publishedTime;
    public byte messageType;
    public String message;

    public MessageModel toCommonModel() {
        MessageModel model = new MessageModel();
        model.messageID = messageID;
        model.conversationID = conversationID;
        model.authorID = authorID;
        try {
            model.publishedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(publishedTime);
        } catch (ParseException e) {
            model.publishedTime = Calendar.getInstance().getTime();
        }
        model.messageType = messageType;
        model.message = message;
        return model;
    }

//    public MessageDbModel toDbModel() {
//        MessageDbModel dbModel = new MessageDbModel();
//        dbModel.messageID = messageID;
//        dbModel.conversationID = conversationID;
//        dbModel.authorID = authorID;
//        try {
//            dbModel.publishedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(publishedTime).getTime();
//        } catch (ParseException e) {
//            dbModel.publishedTime = Calendar.getInstance().getTime().getTime();
//        }
//        dbModel.messageType = messageType;
//        dbModel.message = message;
//
//        return dbModel;
//    }
}
