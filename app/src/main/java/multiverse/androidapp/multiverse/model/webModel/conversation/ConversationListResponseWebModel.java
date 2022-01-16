package multiverse.androidapp.multiverse.model.webModel.conversation;

import java.util.List;

import multiverse.androidapp.multiverse.model.webModel.commonModel.ConversationWebModel;

public class ConversationListResponseWebModel {

    public List<ConversationWebModel> conversations;
    public int count;
    public int offset;
    public int totalSize;

}
