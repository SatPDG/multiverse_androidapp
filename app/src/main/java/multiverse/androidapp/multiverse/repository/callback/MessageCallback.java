package multiverse.androidapp.multiverse.repository.callback;

import multiverse.androidapp.multiverse.model.commonModel.MessageModel;

public interface MessageCallback extends WebErrorCallback<MessageCallback.MessageCallbackType> {

    void messageActionCallback(MessageCallbackType type, MessageModel message);

    enum MessageCallbackType {
        SEND_MESSAGE, UPDATE_MESSAGE, DELETE_MESSAGE
    }
}
