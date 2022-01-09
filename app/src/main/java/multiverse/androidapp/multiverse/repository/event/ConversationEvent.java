package multiverse.androidapp.multiverse.repository.event;

import java.util.List;

import multiverse.androidapp.multiverse.model.commonModel.ConversationModel;
import multiverse.androidapp.multiverse.repository.callback.ConversationCallback;

public class ConversationEvent {

    public ConversationCallback.ConversationCallbackType type;
    public ConversationModel conversation;

    public List<Integer> user;

    public ConversationEvent(ConversationCallback.ConversationCallbackType type, ConversationModel conversation) {
        this.type = type;
        this.conversation = conversation;
    }

    public ConversationEvent(ConversationCallback.ConversationCallbackType type, List<Integer> user) {
        this.type = type;
        this.user = user;
    }
}
