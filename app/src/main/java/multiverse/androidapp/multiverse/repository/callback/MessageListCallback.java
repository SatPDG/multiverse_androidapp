package multiverse.androidapp.multiverse.repository.callback;

import java.util.List;

import multiverse.androidapp.multiverse.model.commonModel.MessageModel;

public interface MessageListCallback extends WebErrorCallback<MessageListCallback.MessageListCallbackType> {

    void messageListCallback(MessageListCallback.MessageListCallbackType type, List<MessageModel> messages, int count, int offset, int totalSize);

    enum MessageListCallbackType {
        MESSAGE_LIST
    }
}
