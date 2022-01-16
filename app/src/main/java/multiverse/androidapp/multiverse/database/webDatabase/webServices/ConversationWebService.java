package multiverse.androidapp.multiverse.database.webDatabase.webServices;

import android.content.Context;

import java.util.function.Supplier;

import multiverse.androidapp.multiverse.R;
import multiverse.androidapp.multiverse.database.webDatabase.HttpService;
import multiverse.androidapp.multiverse.database.webDatabase.WebServiceResponse;
import multiverse.androidapp.multiverse.model.webModel.commonModel.ConversationWebModel;
import multiverse.androidapp.multiverse.model.webModel.commonModel.MessageWebModel;
import multiverse.androidapp.multiverse.model.webModel.conversation.ConversationInfoRequestWebModel;
import multiverse.androidapp.multiverse.model.webModel.conversation.ConversationInfoResponseWebModel;
import multiverse.androidapp.multiverse.model.webModel.conversation.ConversationListResponseWebModel;
import multiverse.androidapp.multiverse.model.webModel.conversation.CreateConversationRequestWebModel;
import multiverse.androidapp.multiverse.model.webModel.conversation.MessageListResponseModel;
import multiverse.androidapp.multiverse.model.webModel.conversation.SendMessageRequestWebModel;
import multiverse.androidapp.multiverse.model.webModel.conversation.UpdateMessageRequestWebModel;
import multiverse.androidapp.multiverse.model.webModel.util.IDListRequestWebModel;
import multiverse.androidapp.multiverse.model.webModel.util.ListRequestWebModel;

public class ConversationWebService {

    private ConversationWebService() {

    }

    public static WebServiceResponse<ConversationInfoResponseWebModel> getConversationInfo(final int conversationID, final Context context) {
        return ApiCaller.callApi(new Supplier<ApiResponse<ConversationInfoResponseWebModel>>() {
            @Override
            public ApiResponse<ConversationInfoResponseWebModel> get() {
                return HttpService.get(String.format(context.getString(R.string.network_conversation_get), conversationID), ConversationInfoResponseWebModel.class, context);
            }
        }, context);
    }

    public static WebServiceResponse<Void> setConversationInfo(final int conversationID, final ConversationInfoRequestWebModel request, final Context context){
        return ApiCaller.callApi(new Supplier<ApiResponse<Void>>() {
            @Override
            public ApiResponse<Void> get() {
                return HttpService.post(String.format(context.getString(R.string.network_conversation_set), conversationID), request, Void.class, context);
            }
        }, context);
    }

    public static WebServiceResponse<Void> deleteConversationInfo(final int conversationID, final Context context){
        return ApiCaller.callApi(new Supplier<ApiResponse<Void>>() {
            @Override
            public ApiResponse<Void> get() {
                return HttpService.delete(String.format(context.getString(R.string.network_conversation_delete), conversationID), null, Void.class, context);
            }
        }, context);
    }

    public static WebServiceResponse<ConversationWebModel> createConversation(final CreateConversationRequestWebModel request, final Context context) {
        return ApiCaller.callApi(new Supplier<ApiResponse<ConversationWebModel>>() {
            @Override
            public ApiResponse<ConversationWebModel> get() {
                return HttpService.post(context.getString(R.string.network_conversation_new), request, ConversationWebModel.class, context);
            }
        }, context);
    }

    public static WebServiceResponse<ConversationListResponseWebModel> getConversationList(final ListRequestWebModel request, final Context context) {
        return ApiCaller.callApi(new Supplier<ApiResponse<ConversationListResponseWebModel>>() {
            @Override
            public ApiResponse<ConversationListResponseWebModel> get() {
                return HttpService.post(context.getString(R.string.network_conversation_getList), request, ConversationListResponseWebModel.class, context);
            }
        }, context);
    }

    public static WebServiceResponse<MessageWebModel> sendMessage(final SendMessageRequestWebModel request, final int conversationID, final Context context) {
        return ApiCaller.callApi(new Supplier<ApiResponse<MessageWebModel>>() {
            @Override
            public ApiResponse<MessageWebModel> get() {
                return HttpService.post(String.format(context.getString(R.string.network_conversation_sendMessage), conversationID), request, MessageWebModel.class, context);
            }
        }, context);
    }

    public static WebServiceResponse<MessageListResponseModel> getMessageList(final ListRequestWebModel request, final int conversationID, final Context context) {
        return ApiCaller.callApi(new Supplier<ApiResponse<MessageListResponseModel>>() {
            @Override
            public ApiResponse<MessageListResponseModel> get() {
                return HttpService.post(String.format(context.getString(R.string.network_conversation_messageList), conversationID), request, MessageListResponseModel.class, context);
            }
        }, context);
    }

    public static WebServiceResponse<Void> deleteMessage(final int conversationID, final int messageID, final Context context) {
        return ApiCaller.callApi(new Supplier<ApiResponse<Void>>() {
            @Override
            public ApiResponse<Void> get() {
                return HttpService.delete(String.format(context.getString(R.string.network_conversation_deleteMessage), conversationID, messageID), null, Void.class, context);
            }
        }, context);
    }

    public static WebServiceResponse<MessageWebModel> updateMessage(final UpdateMessageRequestWebModel request, final int conversationID, final int messageID, final Context context) {
        return ApiCaller.callApi(new Supplier<ApiResponse<MessageWebModel>>() {
            @Override
            public ApiResponse<MessageWebModel> get() {
                return HttpService.post(String.format(context.getString(R.string.network_conversation_updateMessage), conversationID, messageID), request, MessageWebModel.class, context);
            }
        }, context);
    }

    public static WebServiceResponse<Void> addUserToConversation(final IDListRequestWebModel request, final int conversationID, final Context context) {
        return ApiCaller.callApi(new Supplier<ApiResponse<Void>>() {
            @Override
            public ApiResponse<Void> get() {
                return HttpService.post(String.format(context.getString(R.string.network_conversation_addUserToConv), conversationID), request, Void.class, context);
            }
        }, context);
    }

    public static WebServiceResponse<Void> removeUserFromConversation(final IDListRequestWebModel request, final int conversationID, final Context context) {
        return ApiCaller.callApi(new Supplier<ApiResponse<Void>>() {
            @Override
            public ApiResponse<Void> get() {
                return HttpService.delete(String.format(context.getString(R.string.network_conversation_deleteUserFromConv), conversationID), request, Void.class, context);
            }
        }, context);
    }
}
