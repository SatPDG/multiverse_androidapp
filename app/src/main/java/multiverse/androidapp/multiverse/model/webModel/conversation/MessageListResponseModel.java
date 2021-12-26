package multiverse.androidapp.multiverse.model.webModel.conversation;

import java.util.List;

import multiverse.androidapp.multiverse.model.commonModel.MessageModel;

public class MessageListResponseModel {

    public List<MessageModel> messages;
    public int count;
    public int offset;
    public int totalSize;
}
