package multiverse.androidapp.multiverse.model.commonModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import multiverse.androidapp.multiverse.model.dbModel.ConversationDbModel;
import multiverse.androidapp.multiverse.model.webModel.commonModel.ConversationWebModel;

public class ConversationModel {

    public int conversationID;
    public String name;
    public Date lastUpdate;

    public ConversationWebModel toWebModel() {
        ConversationWebModel webModel = new ConversationWebModel();
        webModel.conversationID = conversationID;
        webModel.name = name;
        webModel.lastUpdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastUpdate);
        return webModel;
    }

    public ConversationDbModel toDbModel() {
        ConversationDbModel dbModel = new ConversationDbModel();
        dbModel.conversationID = conversationID;
        dbModel.name = name;
        dbModel.lastUpdate = lastUpdate.getTime();
        dbModel.lastDataUpdate = Calendar.getInstance().getTime().getTime();
        return dbModel;
    }
}
