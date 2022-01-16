package multiverse.androidapp.multiverse.model.webModel.commonModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import multiverse.androidapp.multiverse.model.commonModel.ConversationModel;
import multiverse.androidapp.multiverse.model.dbModel.ConversationDbModel;

public class ConversationWebModel {

    public int conversationID;
    public String name;
    public String lastUpdate;

    public ConversationModel toCommonModel() {
        ConversationModel model = new ConversationModel();
        model.conversationID = conversationID;
        model.name = name;
        try {
            model.lastUpdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lastUpdate);
        } catch (ParseException e) {
            model.lastUpdate = Calendar.getInstance().getTime();
        }
        return model;
    }
}
