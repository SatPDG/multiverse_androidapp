package multiverse.androidapp.multiverse.repository.event;

import multiverse.androidapp.multiverse.repository.callback.RelationshipCallback;

public class RelationshipEvent {

    public RelationshipCallback.RelationCallbackType type;
    public int userID;

    public RelationshipEvent(RelationshipCallback.RelationCallbackType type, int userID) {
        this.type = type;
        this.userID = userID;
    }
}
