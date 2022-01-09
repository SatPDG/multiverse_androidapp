package multiverse.androidapp.multiverse.ui.community;

public enum CommunityType {
    FOLLOWER_LIST(0), FOLLOWED_LIST(1), FOLLOWER_REQUEST_TYPE(2), FOLLOWED_REQUEST_TYPE(3);

    public int no;

    CommunityType(int no) {
        this.no = no;
    }

    public static CommunityType getType(int no) {
        CommunityType type = null;
        for (CommunityType cType : values()) {
            if (cType.no == no) {
                type = cType;
                break;
            }
        }
        return type;
    }
}
