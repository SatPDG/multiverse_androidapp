package multiverse.androidapp.multiverse.repository.event;

import multiverse.androidapp.multiverse.model.commonModel.MessageModel;
import multiverse.androidapp.multiverse.repository.callback.MessageCallback;

public class MessageEvent {

    public MessageCallback.MessageCallbackType type;
    public MessageModel message;

    public MessageEvent(MessageCallback.MessageCallbackType type, MessageModel message) {
        this.type = type;
        this.message = message;
    }
}
