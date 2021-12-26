package multiverse.androidapp.multiverse.repository.callback;

import java.util.List;

import multiverse.androidapp.multiverse.model.commonModel.ConversationModel;

public interface ConversationListCallback extends WebErrorCallback<ConversationListCallback.ConversationListCallbackType> {

    void conversationListCallback(ConversationListCallbackType type, List<ConversationModel> conversations, int count, int offset, int totalSize);

    enum ConversationListCallbackType {
        CONVERSATION_LIST
    }
}
