package multiverse.androidapp.multiverse.repository.callback;

public interface RelationshipCallback {

    void relationshipActionCallback(RelationCallbackType type, int userID);

    void relationshipErrorCallback(RelationCallbackType type, WebError webError);

    enum RelationCallbackType {
        SEND_FOLLOWED_REQ, ACCEPT_FOLLOWER_REQ, DELETE_FOLLOWER_REQ, DELETE_FOLLOWED_REQ,
        DELETE_FOLLOWER, DELETE_FOLLOWED
    }
}
