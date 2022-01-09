package multiverse.androidapp.multiverse.repository.callback;

import java.util.List;

import multiverse.androidapp.multiverse.model.commonModel.MessageModel;

public interface MessageListCallback {

    void messageListCallback(MessageListCallback.MessageListCallbackType type, List<MessageModel> messages, int count, int offset, int totalSize);

    void messageListErrorCallback(MessageListCallbackType type, WebError webError);

    enum MessageListCallbackType {
        MESSAGE_LIST
    }
}
