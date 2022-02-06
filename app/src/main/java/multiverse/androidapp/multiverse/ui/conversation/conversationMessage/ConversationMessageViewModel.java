package multiverse.androidapp.multiverse.ui.conversation.conversationMessage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import multiverse.androidapp.multiverse.MultiverseApplication;
import multiverse.androidapp.multiverse.model.commonModel.MessageModel;
import multiverse.androidapp.multiverse.repository.ConversationRepository;
import multiverse.androidapp.multiverse.repository.callback.MessageCallback;
import multiverse.androidapp.multiverse.repository.callback.MessageListCallback;
import multiverse.androidapp.multiverse.repository.callback.WebError;
import multiverse.androidapp.multiverse.util.functions.ListUtil;
import multiverse.androidapp.multiverse.util.sharedPreference.SharedPreference;

public class ConversationMessageViewModel extends AndroidViewModel implements MessageCallback, MessageListCallback {

    private static final int PAGE_SIZE = 10;

    private ConversationRepository conversationRepository;

    private List<MessageModel> messages;
    private MutableLiveData<Integer> totalSize;
    private MutableLiveData<Integer> loadedSize;
    private MutableLiveData<WebError> lastWebError;

    private int conversationID;
    private Set<Integer> loadingPage;

    public ConversationMessageViewModel(@NonNull Application application) {
        super(application);

        conversationRepository = new ConversationRepository(((MultiverseApplication) getApplication()).executorService,
                ((MultiverseApplication) getApplication()).dbHelper);

        messages = new ArrayList<>();
        totalSize = new MutableLiveData<>(-1);
        loadedSize = new MutableLiveData<>(0);
        lastWebError = new MutableLiveData<>(null);

        conversationID = -1;
        loadingPage = new HashSet<>();
    }

    public void setConversation(int conversationID) {
        if (this.conversationID != conversationID) {
            this.conversationID = conversationID;

            messages.clear();
            totalSize.setValue(-1);
            loadedSize.setValue(0);
        }
    }

    public void loadMessage(int position) {
        int page = getPageNumber(position);
        if (!loadingPage.contains(page)) {
            loadingPage.add(page);

            conversationRepository.getMessageList(conversationID, PAGE_SIZE, page * PAGE_SIZE, this, getApplication().getApplicationContext());
        }
    }

    public void sendStringMessage(String message) {
        conversationRepository.sendMessage(conversationID, message, this, getApplication().getApplicationContext());

        // Add the message to the gui to accelerate the message display
        MessageModel newModel = new MessageModel();
        newModel.messageID = -1;
        newModel.conversationID = conversationID;
        newModel.authorID = SharedPreference.getUserID(getApplication().getApplicationContext());
        newModel.publishedTime = Calendar.getInstance().getTime();
        newModel.messageType = 0;
        newModel.message = message;

        messages.add(0, newModel);
        if (totalSize.getValue() != -1)
            totalSize.setValue(totalSize.getValue() + 1);
        loadedSize.setValue(loadedSize.getValue() + 1);
    }

    public void updateStringMessage(int messageID, String newMessage) {
        conversationRepository.updateMessage(conversationID, messageID, newMessage, this, getApplication().getApplicationContext());
    }

    public void deleteMessage(int messageID) {
        conversationRepository.deleteMessage(conversationID, messageID, this, getApplication().getApplicationContext());
    }

    public List<MessageModel> getMessageList() {
        return messages;
    }

    public LiveData<Integer> getTotalSize() {
        return totalSize;
    }

    public LiveData<Integer> getLoadedSize() {
        return loadedSize;
    }

    public LiveData<WebError> getLastWebError() {
        return lastWebError;
    }

    @Override
    public void messageListCallback(MessageListCallbackType type, List<MessageModel> messages, int count, int offset, int totalSize) {
        int page = getPageNumber(offset);
        if (loadingPage.contains(page)) {
            loadingPage.remove(page);
            // Add the result to the list
            ListUtil.mergeList(this.messages, messages, offset);

            if (totalSize == -1) {
                if (messages.size() < count) {
                    this.totalSize.postValue(this.messages.size());
                } else {
                    this.totalSize.postValue(Integer.MAX_VALUE);
                }
            } else {
                this.totalSize.postValue(totalSize);
            }
            this.loadedSize.postValue(this.messages.size());
            loadingPage.remove(page);
        }
    }

    @Override
    public void messageListErrorCallback(MessageListCallbackType type, WebError webError) {
        if (webError != null) {
            lastWebError.postValue(webError);
        }
    }

    @Override
    public void messageActionCallback(MessageCallbackType type, MessageModel message) {
        if (type == MessageCallbackType.SEND_MESSAGE) {
            // Find the mock message to sync it with the api
            for (int i = 0; i < messages.size(); i++) {
                MessageModel model = messages.get(i);
                if (model.messageType == message.messageType && model.message.equals(message)) {
                    model.messageID = message.messageID;
                    break;
                }
            }
        } else if (type == MessageCallbackType.DELETE_MESSAGE) {
            for (int i = 0; i < messages.size(); i++) {
                if (messages.get(i).messageID == message.messageID) {
                    messages.remove(i);
                    totalSize.postValue(totalSize.getValue() - 1);
                    loadedSize.postValue(loadedSize.getValue() - 1);
                    break;
                }
            }
        } else if (type == MessageCallbackType.UPDATE_MESSAGE) {
            for (int i = 0; i < messages.size(); i++) {
                MessageModel model = messages.get(i);
                if (model.messageID == message.messageID) {
                    messages.set(i, message);
                    loadedSize.postValue(loadedSize.getValue());
                    break;
                }
            }
        }
    }

    @Override
    public void messageErrorCallback(MessageCallbackType type, WebError webError) {
        if (webError != null) {
            lastWebError.postValue(webError);
        }
    }

    private int getPageNumber(int offset) {
        return offset / PAGE_SIZE;
    }
}
