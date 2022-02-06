package multiverse.androidapp.multiverse.repository.callback;

import multiverse.androidapp.multiverse.model.repositoryModel.conversation.ConversationInfoRespositoryModel;

public interface ConversationCallback {

    void conversationActionCallback(ConversationCallbackType type, Object param);

    void conversationErrorCallback(ConversationCallbackType type, WebError webError);

    enum ConversationCallbackType {
        GET_CONVERSATION_INFO, SET_CONVERSATION_INFO, DELETE_CONVERSATION, CREATE_CONVERSATION, ADD_USER_TO_CONV, REMOVE_USER_FROM_CONV
    }
}
