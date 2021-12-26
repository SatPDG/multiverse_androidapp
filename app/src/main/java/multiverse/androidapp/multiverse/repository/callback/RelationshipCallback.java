package multiverse.androidapp.multiverse.repository.callback;

public interface RelationshipCallback extends WebErrorCallback<RelationshipCallback.RelationCallbackType> {

    void relationshipActionCallback(RelationCallbackType type);

    enum RelationCallbackType {
        SEND_FOLLOWED_REQ, ACCEPT_FOLLOWED_REQ, DELETE_FOLLOWER_REQ, DELETE_FOLLOWED_REQ,
        DELETE_FOLLOWER, DELETE_FOLLOWED
    }
}
