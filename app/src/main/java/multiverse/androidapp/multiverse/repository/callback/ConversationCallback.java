package multiverse.androidapp.multiverse.repository.callback;

import multiverse.androidapp.multiverse.model.repositoryModel.conversation.ConversationInfoRespositoryModel;

public interface ConversationCallback extends WebErrorCallback<ConversationCallback.ConversationCallbackType> {

    void conversationInfoCallback(ConversationInfoRespositoryModel convInfo);

    void conversationActionCallback(ConversationCallbackType type);

    enum ConversationCallbackType {
        GET_CONVERSATION_INFO, SET_CONVERSATION_INFO, DELETE_CONVERSATION, CREATE_CONVERSATION, ADD_USER_TO_CONV, REMOVE_USER_FROM_CONV
    }
}
