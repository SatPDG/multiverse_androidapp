package multiverse.androidapp.multiverse.repository.callback;

import multiverse.androidapp.multiverse.model.commonModel.MessageModel;

public interface MessageCallback {

    void messageActionCallback(MessageCallbackType type, MessageModel message);

    void messageErrorCallback(MessageCallbackType type, WebError webError);

    enum MessageCallbackType {
        SEND_MESSAGE, UPDATE_MESSAGE, DELETE_MESSAGE
    }
}
