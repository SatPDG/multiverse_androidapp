package multiverse.androidapp.multiverse.model.webModel.conversation;

import java.util.List;

import multiverse.androidapp.multiverse.model.webModel.commonModel.MessageWebModel;

public class MessageListResponseModel {

    public List<MessageWebModel> messages;
    public int count;
    public int offset;
    public int totalSize;
}
