package multiverse.androidapp.multiverse.model.dbModel;

public class RelationshipDbModel {

    public static final String TABLE_NAME = "relationship";
    public static final String FOLLOWER_ID_COLUMN_NAME = "followerID";
    public static final String FOLLOWED_ID_COLUMN_NAME = "followedID";
    public static final String LAST_DATA_UPDATE_COLUMN_NAME = "lastDataUpdate";

    public static final String SQL_CREATE_RELATIONSHIP_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    FOLLOWER_ID_COLUMN_NAME + " INTEGER," +
                    FOLLOWED_ID_COLUMN_NAME + " INTEGER," +
                    LAST_DATA_UPDATE_COLUMN_NAME + " NUMERIC)";

    public static final String SQL_DELETE_RELATIONSHIP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public int followerID;
    public int followedID;
    public int lastDataUpdate;
}
