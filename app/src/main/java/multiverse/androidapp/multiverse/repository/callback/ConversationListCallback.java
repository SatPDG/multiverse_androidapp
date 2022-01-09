package multiverse.androidapp.multiverse.repository.callback;

import java.util.List;

import multiverse.androidapp.multiverse.model.commonModel.ConversationModel;

public interface ConversationListCallback {

    void conversationListCallback(ConversationListCallbackType type, List<ConversationModel> conversations, int count, int offset, int totalSize);

    void conversationListErrorCallback(ConversationListCallbackType type, WebError webError);

    enum ConversationListCallbackType {
        CONVERSATION_LIST
    }
}
