package multiverse.androidapp.multiverse.ui.conversation.conversationSettings;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import multiverse.androidapp.multiverse.MultiverseApplication;
import multiverse.androidapp.multiverse.model.commonModel.ConversationModel;
import multiverse.androidapp.multiverse.model.commonModel.UserModel;
import multiverse.androidapp.multiverse.model.repositoryModel.conversation.ConversationInfoRespositoryModel;
import multiverse.androidapp.multiverse.repository.ConversationRepository;
import multiverse.androidapp.multiverse.repository.UserRepository;
import multiverse.androidapp.multiverse.repository.callback.ConversationCallback;
import multiverse.androidapp.multiverse.repository.callback.UserListCallback;
import multiverse.androidapp.multiverse.repository.callback.WebError;
import multiverse.androidapp.multiverse.util.functions.ListUtil;
import multiverse.androidapp.multiverse.util.sharedPreference.SharedPreference;

public class ConversationSettingsViewModel extends AndroidViewModel implements ConversationCallback, UserListCallback {

    private final static int PAGE_SIZE = 10;

    private UserRepository userRepository;
    private ConversationRepository conversationRepository;

    private boolean isCreatingNewConversation;
    private boolean isInSearchMode;
    private String currentSearch;
    private Set<Integer> loadingPage;

    private int conversationID;
    private ConversationModel conversation;
    private List<UserModel> conversationUsers;
    private List<UserModel> displayList;

    private List<UserModel> addedUsers;
    private List<UserModel> removedUsers;

    private MutableLiveData<String> conversationName;
    private MutableLiveData<Integer> totalSize;
    private MutableLiveData<Integer> loadedSize;
    private MutableLiveData<WebError> lastWebError;
    private MutableLiveData<ConversationCallbackType> lastApiCallback;

    public ConversationSettingsViewModel(@NonNull Application application) {
        super(application);

        userRepository = new UserRepository(((MultiverseApplication) getApplication()).executorService,
                ((MultiverseApplication) getApplication()).dbHelper);
        conversationRepository = new ConversationRepository(((MultiverseApplication) getApplication()).executorService,
                ((MultiverseApplication) getApplication()).dbHelper);

        isCreatingNewConversation = false;
        isInSearchMode = false;
        currentSearch = "";
        loadingPage = new HashSet<>();

        conversationID = -1;
        conversation = null;
        conversationUsers = new ArrayList<>();
        displayList = new ArrayList<>();

        addedUsers = new ArrayList<>();
        removedUsers = new ArrayList<>();

        conversationName = new MutableLiveData<>("");
        totalSize = new MutableLiveData<>(0);
        loadedSize = new MutableLiveData<>(0);
        lastWebError = new MutableLiveData<>(null);
        lastApiCallback = new MutableLiveData<>(null);
    }

    public void setSettingsMode(boolean isCreatingNewConversation, int conversationID) {
        if (!isCreatingNewConversation) {
            // We are going in the update
            this.isCreatingNewConversation = false;

            conversationID = conversationID;
            conversation = null;
            conversationUsers.clear();
            displayList.clear();
            displayList.clear();

            addedUsers.clear();
            removedUsers.clear();

            conversationName.setValue("");
            totalSize.setValue(0);
            loadedSize.setValue(0);
            lastWebError.setValue(null);

            // Load the conversation infos
            conversationRepository.getConversationInfo(conversationID, this, getApplication().getApplicationContext());

            // Load the 3 first pages of the user in the conversation
            loadingPage.add(0);
            loadingPage.add(1);
            loadingPage.add(2);
            conversationRepository.getUserFromConversation(conversationID, PAGE_SIZE, 0, this, getApplication().getApplicationContext());
            conversationRepository.getUserFromConversation(conversationID, PAGE_SIZE, PAGE_SIZE, this, getApplication().getApplicationContext());
            conversationRepository.getUserFromConversation(conversationID, PAGE_SIZE, PAGE_SIZE * 2, this, getApplication().getApplicationContext());
        } else if (isCreatingNewConversation) {
            // We are going in the create
            this.isCreatingNewConversation = true;

            conversationID = -1;
            conversation = null;
            conversationUsers.clear();
            displayList.clear();

            addedUsers.clear();
            removedUsers.clear();

            conversationName.setValue("");
            totalSize.setValue(0);
            loadedSize.setValue(0);
            lastWebError.setValue(null);
        }
    }

    public void saveConversation(String conversationName) {
        if (isCreatingNewConversation) {
            // Create the conversation
            List<Integer> users = new ArrayList<>();
            for (UserModel user : addedUsers) {
                users.add(user.userID);
            }

            // Check if the current user is in the list
            int userID = SharedPreference.getUserID(getApplication().getApplicationContext());
            boolean isUserInList = false;
            for (Integer user : users) {
                if (user == userID) {
                    isUserInList = true;
                    break;
                }
            }
            // If the user is not in the list add it
            if (!isUserInList) {
                users.add(userID);
            }

            conversationRepository.createConversation(conversationName, users, this, getApplication().getApplicationContext());
        } else {
            // Update the conversation
            if (!conversationName.equals(conversation.name)) {
                conversationRepository.setConversationInfo(conversationID, conversationName, this, getApplication().getApplicationContext());
            }
            if (addedUsers.size() > 0) {
                List<Integer> users = new ArrayList<>();
                for (UserModel user : addedUsers) {
                    users.add(user.userID);
                }
                conversationRepository.addUserToConversation(conversationID, users, this, getApplication().getApplicationContext());
            }
            if (removedUsers.size() > 0) {
                List<Integer> users = new ArrayList<>();
                for (UserModel user : removedUsers) {
                    users.add(user.userID);
                }
                conversationRepository.removeUserToConversation(conversationID, users, this, getApplication().getApplicationContext());
            }
        }
    }

    public void searchForUser(String search) {
        if (!isInSearchMode) {
            isInSearchMode = true;
        }
        displayList.clear();
        loadingPage.clear();
        loadingPage.add(0);
        totalSize.setValue(Integer.MAX_VALUE);
        loadedSize.setValue(0);

        currentSearch = search;
        userRepository.searchForUserByName(currentSearch, PAGE_SIZE, 0, this, getApplication().getApplicationContext());
    }

    public void loadUser(int position) {
        if (isCreatingNewConversation) {
            int pageNo = position / PAGE_SIZE;
            loadingPage.add(pageNo);
            userRepository.searchForUserByName(currentSearch, PAGE_SIZE, pageNo * PAGE_SIZE, this, getApplication().getApplicationContext());
        } else {
            if (isInSearchMode) {
                int pageNo = position / PAGE_SIZE;
                loadingPage.add(pageNo);
                userRepository.searchForUserByName(currentSearch, PAGE_SIZE, pageNo * PAGE_SIZE, this, getApplication().getApplicationContext());
            } else {
                int pageNo = position / PAGE_SIZE;
                loadingPage.add(pageNo);
                conversationRepository.getUserFromConversation(conversationID, PAGE_SIZE, pageNo * PAGE_SIZE, this, getApplication().getApplicationContext());
            }
        }
    }

    public void closeSearch() {
        if (isInSearchMode) {
            isInSearchMode = false;

            loadingPage.clear();
            displayList.clear();

            // Fill the list with the conversation user follow by the added and remove user
            ListUtil.mergeList(displayList, addedUsers, 0);
            ListUtil.mergeList(displayList, removedUsers, displayList.size());
            ListUtil.mergeList(displayList, conversationUsers, displayList.size());

            if (isCreatingNewConversation) {
                totalSize.postValue(addedUsers.size());
            } else {
                totalSize.postValue(Integer.MAX_VALUE);
            }
            loadedSize.postValue(displayList.size());
        }
    }

    public void addUser(int userID) {
        // Find the user in the data
        UserModel user = null;
        for (UserModel model : displayList) {
            if (model.userID == userID) {
                user = model;
                break;
            }
        }

        // Check if the user was in the remove list
        boolean isInRemoveList = false;
        UserModel toRemove = null;
        for (UserModel model : removedUsers) {
            if (model.userID == userID) {
                isInRemoveList = true;
                toRemove = model;
                break;
            }
        }

        if (isInRemoveList) {
            // Just remove it from the remove list
            removedUsers.remove(toRemove);
        } else {
            // add it to the add list
            addedUsers.add(user);
        }

        // Update the graphic
        loadedSize.setValue(loadedSize.getValue());
    }

    public void removeUser(int userID) {
        // Find the user in the data
        UserModel user = null;
        for (UserModel model : displayList) {
            if (model.userID == userID) {
                user = model;
                break;
            }
        }

        // If we are in search mode, remove the user from the display list
        if (!isInSearchMode) {
            displayList.remove(user);
        }

        // Check if the user was in the add list
        boolean isInAddList = false;
        UserModel toRemove = null;
        for (UserModel model : addedUsers) {
            if (model.userID == userID) {
                isInAddList = true;
                toRemove = model;
                break;
            }
        }

        if (isInAddList) {
            // Just remove it from the add list
            addedUsers.remove(toRemove);
        } else {
            // Add it to the remove list
            removedUsers.add(user);
        }

        // Update the graphic
        if (isInSearchMode) {
            loadedSize.setValue(loadedSize.getValue());
        } else {
            totalSize.setValue(totalSize.getValue() - 1);
            loadedSize.setValue(loadedSize.getValue() - 1);
        }
    }

    public boolean isAnAddedUser(int userID) {
        for (UserModel user : addedUsers) {
            if (user.userID == userID) {
                return true;
            }
        }
        return false;
    }

    public boolean isARemovedUser(int userID) {
        for (UserModel user : removedUsers) {
            if (user.userID == userID) {
                return true;
            }
        }
        return false;
    }

    public boolean isUserInConversation(int userID) {
        for (UserModel user : conversationUsers) {
            if (user.userID == userID) {
                return true;
            }
        }
        return false;
    }

    public LiveData<String> getConversationName() {
        return conversationName;
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

    public LiveData<ConversationCallbackType> getLastApiCallback() {
        return lastApiCallback;
    }

    public int getConversationID() {
        return conversationID;
    }

    public List<UserModel> getUsersList() {
        return displayList;
    }

    @Override
    public void conversationActionCallback(ConversationCallbackType type, Object param) {
        if (type == ConversationCallbackType.GET_CONVERSATION_INFO) {
            if (!isCreatingNewConversation) {
                // Set the conversation info
                ConversationInfoRespositoryModel convInfo = (ConversationInfoRespositoryModel) param;
                conversation = new ConversationModel();
                conversation.conversationID = convInfo.conversationID;
                conversation.name = convInfo.name;
                conversation.lastUpdate = convInfo.lastUpdate;

                conversationName.postValue(convInfo.name);
            }
        } else if (type == ConversationCallbackType.CREATE_CONVERSATION) {
            // The conversation was created.
            ConversationModel conversationModel = (ConversationModel) param;

            conversationID = conversationModel.conversationID;
            conversation = conversationModel;
            lastApiCallback.postValue(type);
        } else if (type == ConversationCallbackType.ADD_USER_TO_CONV) {
            // Remove the user from the list and add them to the conversation.
            List<Integer> users = (List<Integer>) param;
            List<UserModel> toAddUser = new ArrayList<>();
            for (Integer userID : users) {
                for (UserModel userModel : addedUsers) {
                    if (userModel.userID == userID) {
                        toAddUser.add(userModel);
                        break;
                    }
                }
            }
            addedUsers.removeAll(toAddUser);
            conversationUsers.addAll(toAddUser);

            if (isInSearchMode) {
                closeSearch();
            } else {
                totalSize.setValue(conversationUsers.size());
                loadedSize.setValue(conversationUsers.size());
            }
            lastApiCallback.postValue(type);
        } else if (type == ConversationCallbackType.REMOVE_USER_FROM_CONV) {
            // Remove the user from the list and remove them to the conversation.
            List<Integer> users = (List<Integer>) param;
            List<UserModel> toRemoveUser = new ArrayList<>();
            for (Integer userID : users) {
                for (UserModel userModel : removedUsers) {
                    if (userModel.userID == userID) {
                        toRemoveUser.add(userModel);
                        break;
                    }
                }
            }
            removedUsers.removeAll(toRemoveUser);
            conversationUsers.removeAll(toRemoveUser);

            if (isInSearchMode) {
                closeSearch();
            } else {
                totalSize.postValue(conversationUsers.size());
                loadedSize.postValue(conversationUsers.size());
            }
            lastApiCallback.postValue(type);
        } else if (type == ConversationCallbackType.SET_CONVERSATION_INFO) {
            lastApiCallback.postValue(type);
        }
    }

    @Override
    public void conversationErrorCallback(ConversationCallbackType type, WebError webError) {
        if (lastWebError != null)
            lastWebError.postValue(webError);
    }

    @Override
    public void userListCallback(UserCallbackType type, List<UserModel> users, int count, int offset, int totalSize) {
        if (type == UserCallbackType.CONVERSATION_USER_LIST) {
            int page = offset / PAGE_SIZE;
            if (loadingPage.contains(page)) {
                // Add the result to the list
                ListUtil.mergeList(this.conversationUsers, users, offset);

                // If there is another page to load, load it.
                if (users.size() == count) {
                    int nextPage = page + 1;
                    if (!loadingPage.contains(nextPage)) {
                        loadingPage.add(nextPage);
                        conversationRepository.getUserFromConversation(conversationID, PAGE_SIZE, nextPage * PAGE_SIZE, this, getApplication().getApplicationContext());
                    }
                }


                // If we are not in the search mode, add the user to the display list
                if (!isInSearchMode) {
                    ListUtil.mergeList(this.displayList, users, offset);

                    if (totalSize == -1) {
                        if (conversationUsers.size() < count) {
                            this.totalSize.postValue(this.displayList.size());
                        } else {
                            this.totalSize.postValue(Integer.MAX_VALUE);
                        }
                    } else {
                        this.totalSize.postValue(totalSize);
                    }
                    this.loadedSize.postValue(this.displayList.size());
                }
                loadingPage.remove(page);
            }
        } else if (type == UserCallbackType.USER_NAME_SEARCH) {
            int page = offset / PAGE_SIZE;
            if (loadingPage.contains(page)) {
                if (isInSearchMode) {
                    ListUtil.mergeList(this.displayList, users, offset);

                    if (totalSize == -1) {
                        if (displayList.size() < count) {
                            this.totalSize.postValue(this.displayList.size());
                        } else {
                            this.totalSize.postValue(Integer.MAX_VALUE);
                        }
                    } else {
                        this.totalSize.postValue(totalSize);
                    }
                    this.loadedSize.postValue(this.displayList.size());
                }
                loadingPage.remove(page);
            }
        }
    }

    @Override
    public void userListErrorCallback(UserCallbackType type, WebError webError) {
        if (lastWebError != null)
            lastWebError.postValue(webError);
    }
}
