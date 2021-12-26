package multiverse.androidapp.multiverse.model.dbModel;

import android.content.ContentValues;
import android.database.Cursor;

import multiverse.androidapp.multiverse.model.commonModel.UserModel;

public class UserDbModel {

    public static final String TABLE_NAME = "user";
    public static final String USER_ID_COLUMN_NAME = "userID";
    public static final String FIRSTNAME_COLUMN_NAME = "firstname";
    public static final String LASTNAME_COLUMN_NAME = "lastname";
    public static final String LAST_LOCATION_COLUMN_NAME = "lastLocation";
    public static final String LAST_DATA_UPDATE_COLUMN_NAME = "lastDataUpdate";

    public static final String SQL_CREATE_USER_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    USER_ID_COLUMN_NAME + " INTEGER," +
                    FIRSTNAME_COLUMN_NAME + " TEXT," +
                    LASTNAME_COLUMN_NAME + " TEXT," +
                    LAST_LOCATION_COLUMN_NAME + " TEXT," +
                    LAST_DATA_UPDATE_COLUMN_NAME + " NUMERIC," +
                    "PRIMARY KEY(" + USER_ID_COLUMN_NAME + "))";

    public static final String SQL_DELETE_USER_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public int userID;
    public String firstname;
    public String lastname;
    public String lastLocation;
    public long lastDataUpdate;

    public ContentValues getContentValues(){
        ContentValues content = new ContentValues();

        content.put(USER_ID_COLUMN_NAME, userID);
        content.put(FIRSTNAME_COLUMN_NAME, firstname);
        content.put(LASTNAME_COLUMN_NAME, lastname);
        content.put(LAST_LOCATION_COLUMN_NAME, lastLocation);
        content.put(LAST_DATA_UPDATE_COLUMN_NAME, lastDataUpdate);

        return content;
    }

    public UserModel toModel() {
        UserModel user = new UserModel();
        user.firstname = firstname;
        user.lastname = lastname;
        user.userID = userID;
        user.lastLocation = null;
        return user;
    }

    public static UserDbModel toDbModel(Cursor cursor){
        UserDbModel dbModel = new UserDbModel();

        dbModel.userID = cursor.getInt(cursor.getColumnIndexOrThrow(USER_ID_COLUMN_NAME));
        dbModel.firstname = cursor.getString(cursor.getColumnIndexOrThrow(FIRSTNAME_COLUMN_NAME));
        dbModel.lastname = cursor.getString(cursor.getColumnIndexOrThrow(LASTNAME_COLUMN_NAME));
        dbModel.lastLocation = cursor.getString(cursor.getColumnIndexOrThrow(LAST_LOCATION_COLUMN_NAME));
        dbModel.lastDataUpdate = cursor.getLong(cursor.getColumnIndexOrThrow(LAST_DATA_UPDATE_COLUMN_NAME));

        return dbModel;
    }
}
