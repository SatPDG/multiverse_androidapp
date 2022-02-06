package multiverse.androidapp.multiverse.ui.conversationList;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import multiverse.androidapp.multiverse.MultiverseApplication;
import multiverse.androidapp.multiverse.model.commonModel.ConversationModel;
import multiverse.androidapp.multiverse.repository.ConversationRepository;
import multiverse.androidapp.multiverse.repository.callback.ConversationCallback;
import multiverse.androidapp.multiverse.repository.callback.ConversationListCallback;
import multiverse.androidapp.multiverse.repository.callback.WebError;
import multiverse.androidapp.multiverse.repository.event.ConversationEvent;
import multiverse.androidapp.multiverse.util.functions.ListUtil;

public class ConversationListViewModel extends AndroidViewModel implements ConversationListCallback {

    private static final int PAGE_SIZE = 10;

    private ConversationRepository conversationRepository;

    private List<ConversationModel> conversationList;
    private MutableLiveData<Integer> totalSize;
    private MutableLiveData<Integer> loadedSize;
    private MutableLiveData<WebError> lastWebError;

    private String currentSearch;
    private Set<Integer> loadingPage;

    public ConversationListViewModel(@NonNull Application application) {
        super(application);

        conversationRepository = new ConversationRepository(((MultiverseApplication) getApplication()).executorService,
                ((MultiverseApplication) getApplication()).dbHelper);

        conversationList = new ArrayList<>();
        totalSize = new MutableLiveData<>(Integer.MAX_VALUE);
        loadedSize = new MutableLiveData<>(0);
        lastWebError = new MutableLiveData<>(null);

        loadingPage = new HashSet<>();
        currentSearch = "";
    }

    public void loadUserConversation(int position) {
        int page = getPage(position);
        if (!loadingPage.contains(page)) {
            // Load the page
            loadingPage.add(page);
            conversationRepository.getConversationList(PAGE_SIZE, page * PAGE_SIZE, this, getApplication().getApplicationContext());
        }
    }

    public void searchForConversation(String search) {
        // TODO - Add the conversation/user in conversation search.
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

    public List<ConversationModel> getConversationList() {
        return conversationList;
    }

    @Override
    public void conversationListCallback(ConversationListCallbackType type, List<ConversationModel> conversations, int count, int offset, int totalSize) {
        int page = offset / PAGE_SIZE;
        if (loadingPage.contains(page)) {
            // Add the result to the list
            ListUtil.mergeList(this.conversationList, conversations, offset);

            if (totalSize == -1) {
                if (conversations.size() < count) {
                    this.totalSize.postValue(this.conversationList.size());
                } else {
                    this.totalSize.postValue(Integer.MAX_VALUE);
                }
            } else {
                this.totalSize.postValue(totalSize);
            }
            this.loadedSize.postValue(this.conversationList.size());
            loadingPage.remove(page);
        }
    }

    @Override
    public void conversationListErrorCallback(ConversationListCallbackType type, WebError webError) {
        if (webError != null) {
            lastWebError.postValue(webError);
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onConversationEvent(ConversationEvent event) {
        if (event != null) {
            if (event.type == ConversationCallback.ConversationCallbackType.SET_CONVERSATION_INFO) {
                for (ConversationModel model : conversationList) {
                    if (model.conversationID == event.conversation.conversationID) {
                        model.name = event.conversation.name;
                        model.lastUpdate = event.conversation.lastUpdate;
                        loadedSize.postValue(loadedSize.getValue());
                        break;
                    }
                }
            } else if (event.type == ConversationCallback.ConversationCallbackType.CREATE_CONVERSATION) {
                // Add the conversation at the top of the list
                conversationList.add(0, event.conversation);
                totalSize.postValue(totalSize.getValue() + 1);
                loadedSize.postValue(loadedSize.getValue() + 1);
            } else if (event.type == ConversationCallback.ConversationCallbackType.DELETE_CONVERSATION) {
                for (int i = 0; i < conversationList.size(); i++) {
                    if (conversationList.get(i).conversationID == event.conversation.conversationID) {
                        conversationList.remove(i);
                        totalSize.postValue(totalSize.getValue() - 1);
                        loadedSize.postValue(loadedSize.getValue() - 1);
                        break;
                    }
                }
            }
        }
    }

    private int getPage(int position) {
        return position / PAGE_SIZE;
    }
}
