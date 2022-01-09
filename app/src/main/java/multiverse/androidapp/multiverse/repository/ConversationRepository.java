package multiverse.androidapp.multiverse.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import multiverse.androidapp.multiverse.database.localDatabase.MultiverseDbHelper;
import multiverse.androidapp.multiverse.database.localDatabase.localDatabaseServices.ConversationLocalDbService;
import multiverse.androidapp.multiverse.database.localDatabase.localDatabaseServices.MessageLocalDbServices;
import multiverse.androidapp.multiverse.database.webDatabase.WebServiceResponse;
import multiverse.androidapp.multiverse.database.webDatabase.webServices.ConversationWebService;
import multiverse.androidapp.multiverse.model.commonModel.ConversationModel;
import multiverse.androidapp.multiverse.model.commonModel.MessageModel;
import multiverse.androidapp.multiverse.model.dbModel.ConversationDbModel;
import multiverse.androidapp.multiverse.model.dbModel.MessageDbModel;
import multiverse.androidapp.multiverse.model.repositoryModel.conversation.ConversationInfoRespositoryModel;
import multiverse.androidapp.multiverse.model.webModel.conversation.ConversationInfoRequestWebModel;
import multiverse.androidapp.multiverse.model.webModel.conversation.ConversationInfoResponseWebModel;
import multiverse.androidapp.multiverse.model.webModel.conversation.ConversationListResponseWebModel;
import multiverse.androidapp.multiverse.model.webModel.conversation.CreateConversationRequestWebModel;
import multiverse.androidapp.multiverse.model.webModel.conversation.MessageListResponseModel;
import multiverse.androidapp.multiverse.model.webModel.conversation.SendMessageRequestWebModel;
import multiverse.androidapp.multiverse.model.webModel.conversation.UpdateMessageRequestWebModel;
import multiverse.androidapp.multiverse.model.webModel.util.IDListRequestWebModel;
import multiverse.androidapp.multiverse.model.webModel.util.ListRequestWebModel;
import multiverse.androidapp.multiverse.repository.callback.ConversationCallback;
import multiverse.androidapp.multiverse.repository.callback.ConversationListCallback;
import multiverse.androidapp.multiverse.repository.callback.MessageCallback;
import multiverse.androidapp.multiverse.repository.callback.MessageListCallback;
import multiverse.androidapp.multiverse.repository.callback.WebError;
import multiverse.androidapp.multiverse.repository.event.ConversationEvent;
import multiverse.androidapp.multiverse.repository.event.MessageEvent;

public class ConversationRepository {

    private final Executor executor;
    private final MultiverseDbHelper dbHelper;

    public ConversationRepository(Executor executor, MultiverseDbHelper dbHelper) {
        this.executor = executor;
        this.dbHelper = dbHelper;
    }

    public void getConversationInfo(final int conversationID, final ConversationCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // Fetch db info
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ConversationDbModel dbModel = ConversationLocalDbService.getConversation(db, conversationID);
                if(dbModel != null) {
                    ConversationInfoRespositoryModel model = new ConversationInfoRespositoryModel();
                    model.conversationID = conversationID;
                    model.name = dbModel.name;
                    model.lastUpdate = new Date(dbModel.lastUpdate);
                    model.nbrOfUser = -1;
                    callback.conversationInfoCallback(model);
                }

                // Fetch the web api
                WebServiceResponse<ConversationInfoResponseWebModel> webResponse = ConversationWebService.getConversationInfo(conversationID, context);
                if(webResponse.isResponseOK) {
                    ConversationInfoRespositoryModel model = new ConversationInfoRespositoryModel();
                    model.conversationID = webResponse.data.conversationID;
                    model.name = webResponse.data.name;
                    try {
                        model.lastUpdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(webResponse.data.lastUpdate);
                    } catch (ParseException e) {
                        model.lastUpdate = Calendar.getInstance().getTime();
                    }
                    model.nbrOfUser = webResponse.data.nbrOfUser;
                    callback.conversationInfoCallback(model);

                    // Update the database
                    dbModel = new ConversationDbModel();
                    dbModel.conversationID = model.conversationID;
                    dbModel.name = model.name;
                    dbModel.lastUpdate = model.lastUpdate.getTime();
                    dbModel.lastDataUpdate = Calendar.getInstance().getTime().getTime();
                    ConversationLocalDbService.updateOrAddConversation(db, dbModel);
                } else {
                    callback.conversationErrorCallback(ConversationCallback.ConversationCallbackType.GET_CONVERSATION_INFO, new WebError(webResponse));
                }
            }
        });
    }

    public void setConversationInfo(final int conversationID, final String conversationName, final ConversationCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ConversationInfoRequestWebModel webRequest = new ConversationInfoRequestWebModel();
                webRequest.name = conversationName;

                WebServiceResponse<Void> webResponse = ConversationWebService.setConversationInfo(conversationID, webRequest, context);
                if(webResponse.isResponseOK) {
                    callback.conversationActionCallback(ConversationCallback.ConversationCallbackType.SET_CONVERSATION_INFO);

                    // Update the conversation in the db if it is present
                    ConversationDbModel dbModel = new ConversationDbModel();
                    dbModel.conversationID = conversationID;
                    dbModel.name = conversationName;
                    dbModel.lastDataUpdate = Calendar.getInstance().getTime().getTime();
                    dbModel.lastUpdate = Calendar.getInstance().getTime().getTime();
                    ConversationLocalDbService.updateConversation(dbHelper.getWritableDatabase(), dbModel);

                    // Send the event to the bus
                    EventBus.getDefault().post(new ConversationEvent(ConversationCallback.ConversationCallbackType.SET_CONVERSATION_INFO, ConversationModel.toModel(dbModel)));
                } else {
                    callback.conversationErrorCallback(ConversationCallback.ConversationCallbackType.SET_CONVERSATION_INFO, new WebError(webResponse));
                }
            }
        });
    }

    public void deleteConversation(final int conversationID, final ConversationCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                WebServiceResponse<Void> webResponse = ConversationWebService.deleteConversationInfo(conversationID, context);
                if(webResponse.isResponseOK) {
                    callback.conversationActionCallback(ConversationCallback.ConversationCallbackType.DELETE_CONVERSATION);

                    // Remove conversation from database if there
                    ConversationLocalDbService.deleteConversation(dbHelper.getWritableDatabase(), conversationID);
                } else {
                    callback.conversationErrorCallback(ConversationCallback.ConversationCallbackType.DELETE_CONVERSATION, new WebError(webResponse));
                }
            }
        });
    }

    public void createConversation(final String name, final List<Integer> userList, final ConversationCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                CreateConversationRequestWebModel request = new CreateConversationRequestWebModel();
                request.name = name;
                request.users = userList;
                WebServiceResponse<ConversationModel> webResponse = ConversationWebService.createConversation(request, context);
                if(webResponse.isResponseOK) {
                    // Add conversation to db
                    ConversationDbModel dbModel = new ConversationDbModel();
                    dbModel.conversationID = webResponse.data.conversationID;
                    dbModel.name = webResponse.data.name;
                    try {
                        dbModel.lastUpdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(webResponse.data.lastUpdate).getTime();
                    } catch (ParseException e) {
                        dbModel.lastUpdate = Calendar.getInstance().getTime().getTime();
                    }
                    dbModel.lastDataUpdate = Calendar.getInstance().getTime().getTime();
                    ConversationLocalDbService.addConversation(dbHelper.getWritableDatabase(), dbModel);

                    callback.conversationActionCallback(ConversationCallback.ConversationCallbackType.CREATE_CONVERSATION);
                } else {
                    callback.conversationErrorCallback(ConversationCallback.ConversationCallbackType.CREATE_CONVERSATION, new WebError(webResponse));
                }
            }
        });
    }

    public void getConversationList(final int conversationID, final int count, final int offset, final ConversationListCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // Fetch conversation from the local database
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                List<ConversationDbModel> dbList = ConversationLocalDbService.getConversations(db, offset, count);
                if(dbList.size() > 0) {
                    List<ConversationModel> list = new ArrayList<>();
                    for(ConversationDbModel dbModel : dbList) {
                        list.add(ConversationModel.toModel(dbModel));
                    }
                    int totalSize = ConversationLocalDbService.getSize(db);
                    callback.conversationListCallback(ConversationListCallback.ConversationListCallbackType.CONVERSATION_LIST, list, count, offset, totalSize);
                }

                // Fetch the web
                ListRequestWebModel request = new ListRequestWebModel();
                request.count = count;
                request.offset = offset;
                WebServiceResponse<ConversationListResponseWebModel> webResponse = ConversationWebService.getConversationList(request, context);
                if(webResponse.isResponseOK) {
                    ArrayList<ConversationModel> list = new ArrayList<>();
                    list.addAll(webResponse.data.conversations);
                    callback.conversationListCallback(ConversationListCallback.ConversationListCallbackType.CONVERSATION_LIST, list, webResponse.data.count, webResponse.data.offset, webResponse.data.totalSize);

                    // Update the database
                    for(ConversationModel model : webResponse.data.conversations) {
                        ConversationLocalDbService.updateOrAddConversation(db, model.toDbModel());
                    }
                } else {
                    callback.conversationListErrorCallback(ConversationListCallback.ConversationListCallbackType.CONVERSATION_LIST, new WebError(webResponse));
                }
            }
        });
    }

    public void addUserToConversation(final int conversationID, final List<Integer> users, final ConversationCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                IDListRequestWebModel request = new IDListRequestWebModel();
                request.idList = users;
                WebServiceResponse<Void> webResponse = ConversationWebService.addUserToConversation(request, conversationID, context);
                if(webResponse.isResponseOK) {
                    callback.conversationActionCallback(ConversationCallback.ConversationCallbackType.ADD_USER_TO_CONV);

                    // Send event
                    EventBus.getDefault().post(new ConversationEvent(ConversationCallback.ConversationCallbackType.ADD_USER_TO_CONV, users));
                } else {
                    callback.conversationErrorCallback(ConversationCallback.ConversationCallbackType.ADD_USER_TO_CONV, new WebError(webResponse));
                }
            }
        });
    }

    public void removeUserToConversation(final int conversationID, final List<Integer> users, final ConversationCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                IDListRequestWebModel request = new IDListRequestWebModel();
                request.idList = users;
                WebServiceResponse<Void> webResponse = ConversationWebService.removeUserFromConversation(request, conversationID, context);
                if(webResponse.isResponseOK) {
                    callback.conversationActionCallback(ConversationCallback.ConversationCallbackType.REMOVE_USER_FROM_CONV);

                    // Send event
                    EventBus.getDefault().post(new ConversationEvent(ConversationCallback.ConversationCallbackType.REMOVE_USER_FROM_CONV, users));
                } else {
                    callback.conversationErrorCallback(ConversationCallback.ConversationCallbackType.REMOVE_USER_FROM_CONV, new WebError(webResponse));
                }
            }
        });
    }

    public void sendMessage(final int conversationID, final String message, final MessageCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                SendMessageRequestWebModel request = new SendMessageRequestWebModel();
                request.message = message;
                WebServiceResponse<MessageModel> webResponse = ConversationWebService.sendMessage(request, conversationID, context);
                if(webResponse.isResponseOK) {
                    // Add the message to the database
                    MessageLocalDbServices.addMessage(dbHelper.getWritableDatabase(), webResponse.data.toDbModel());

                    callback.messageActionCallback(MessageCallback.MessageCallbackType.SEND_MESSAGE, webResponse.data);

                    // Send event to bus
                    EventBus.getDefault().post(new MessageEvent(MessageCallback.MessageCallbackType.SEND_MESSAGE, webResponse.data));
                } else {
                    callback.messageErrorCallback(MessageCallback.MessageCallbackType.SEND_MESSAGE, new WebError(webResponse));
                }
            }
        });
    }

    public void updateMessage(final int conversationID, final int messageID, final String message, final MessageCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                UpdateMessageRequestWebModel request = new UpdateMessageRequestWebModel();
                request.message = message;
                WebServiceResponse<MessageModel> webResponse = ConversationWebService.updateMessage(request, conversationID, messageID, context);
                if(webResponse.isResponseOK) {
                    // Update the message to the database
                    MessageLocalDbServices.updateMessage(dbHelper.getWritableDatabase(), webResponse.data.toDbModel());

                    callback.messageActionCallback(MessageCallback.MessageCallbackType.UPDATE_MESSAGE, webResponse.data);

                    // Send event to bus
                    EventBus.getDefault().post(new MessageEvent(MessageCallback.MessageCallbackType.UPDATE_MESSAGE, webResponse.data));
                } else {
                    callback.messageErrorCallback(MessageCallback.MessageCallbackType.UPDATE_MESSAGE, new WebError(webResponse));
                }
            }
        });
    }

    public void deleteMessage(final int conversationID, final int messageID, final MessageCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                WebServiceResponse<Void> webResponse = ConversationWebService.deleteMessage(conversationID, messageID, context);
                if(webResponse.isResponseOK) {
                    // Delete the message to the database
                    MessageLocalDbServices.deleteMessage(dbHelper.getWritableDatabase(), messageID);

                    callback.messageActionCallback(MessageCallback.MessageCallbackType.DELETE_MESSAGE, null);

                    // Send event ot bus
                    MessageModel messageModel = new MessageModel();
                    messageModel.messageID = messageID;
                    messageModel.conversationID = conversationID;
                    EventBus.getDefault().post(new MessageEvent(MessageCallback.MessageCallbackType.DELETE_MESSAGE, messageModel));
                } else {
                    callback.messageErrorCallback(MessageCallback.MessageCallbackType.DELETE_MESSAGE, new WebError(webResponse));
                }
            }
        });
    }

    public void getMessageList(final int conversationID, final int count, final int offset, final MessageListCallback callback, final Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // Fetch data from the database
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                List<MessageDbModel> dbList = MessageLocalDbServices.getMessages(db, offset, count);
                if(dbList.size() > 0) {
                    List<MessageModel> modelList = new ArrayList<>();
                    for(MessageDbModel dbModel : dbList) {
                        modelList.add(MessageModel.toModel(dbModel));
                    }
                    int totalSize = MessageLocalDbServices.getSize(db);
                    callback.messageListCallback(MessageListCallback.MessageListCallbackType.MESSAGE_LIST, modelList, offset, count, totalSize);
                }

                // Fetch the web
                ListRequestWebModel request = new ListRequestWebModel();
                request.offset = offset;
                request.count = count;
                WebServiceResponse<MessageListResponseModel> webResponse = ConversationWebService.getMessageList(request, conversationID, context);
                if(webResponse.isResponseOK) {
                    callback.messageListCallback(MessageListCallback.MessageListCallbackType.MESSAGE_LIST, webResponse.data.messages, webResponse.data.count, webResponse.data.offset, webResponse.data.totalSize);

                    // Add messages to db
                    for(MessageModel model : webResponse.data.messages) {
                        MessageLocalDbServices.updateOrAddMessage(db, model.toDbModel());
                    }
                } else {
                    callback.messageListErrorCallback(MessageListCallback.MessageListCallbackType.MESSAGE_LIST, new WebError(webResponse));
                }
            }
        });
    }
}
