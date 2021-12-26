package multiverse.androidapp.multiverse.model.commonModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import multiverse.androidapp.multiverse.model.dbModel.ConversationDbModel;

public class ConversationModel {

    public int conversationID;
    public String name;
    public String lastUpdate;

    public ConversationDbModel toDbModel() {
        ConversationDbModel dbModel = new ConversationDbModel();
        dbModel.conversationID = conversationID;
        dbModel.name = name;
        try {
            dbModel.lastUpdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lastUpdate).getTime();
        } catch (ParseException e) {
            dbModel.lastUpdate = Calendar.getInstance().getTime().getTime();
        }
        dbModel.lastDataUpdate = dbModel.lastUpdate = Calendar.getInstance().getTime().getTime();
        return dbModel;
    }

    public static ConversationModel toModel(ConversationDbModel dbModel) {
        ConversationModel model = new ConversationModel();
        model.conversationID = dbModel.conversationID;
        model.name = dbModel.name;
        model.lastUpdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(dbModel.lastUpdate));
        return model;
    }
}
