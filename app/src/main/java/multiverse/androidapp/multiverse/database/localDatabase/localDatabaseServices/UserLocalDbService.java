package multiverse.androidapp.multiverse.database.localDatabase.localDatabaseServices;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import multiverse.androidapp.multiverse.database.localDatabase.MultiverseDbHelper;
import multiverse.androidapp.multiverse.model.dbModel.UserDbModel;

public class UserLocalDbService {

    private UserLocalDbService()
    {

    }

    public static void addUser(SQLiteDatabase db, UserDbModel user){
        db.insert(UserDbModel.TABLE_NAME, null, user.getContentValues());
    }

    public static void addUsers(SQLiteDatabase db, List<UserDbModel> users){
        for (UserDbModel user: users) {
            db.insert(UserDbModel.TABLE_NAME, null, user.getContentValues());
        }
    }

    public static UserDbModel getUser(SQLiteDatabase db, int userID){
        Cursor cursor = db.rawQuery("SELECT * FROM " + UserDbModel.TABLE_NAME +
                " WHERE " + UserDbModel.USER_ID_COLUMN_NAME + "=" + userID + ";", null);

        UserDbModel dbModel = null;
        if(cursor.moveToNext()){
            dbModel = UserDbModel.toDbModel(cursor);
        }
        return dbModel;
    }

    public static List<UserDbModel> getUsers(SQLiteDatabase db, List<Integer> users){
        String tab = "";
        for(int i = 0; i < users.size(); i++){
            tab += users.get(i);
            if(i < users.size() - 1)
                tab += ", ";
        }

        Cursor cursor = db.rawQuery("SELECT * FROM " + UserDbModel.TABLE_NAME +
                " WHERE " + UserDbModel.USER_ID_COLUMN_NAME + " IN (" + tab + ");", null);

        List<UserDbModel> userList = new ArrayList<>();
        while(cursor.moveToNext()) {
            userList.add(UserDbModel.toDbModel(cursor));
        }
        return userList;
    }

    public static List<UserDbModel> getMostRecentUser(SQLiteDatabase db, int count) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + UserDbModel.TABLE_NAME +
                " ORDER BY " + UserDbModel.LAST_DATA_UPDATE_COLUMN_NAME +
                " LIMIT " + count, null);

        List<UserDbModel> userList = new ArrayList<>();
        while(cursor.moveToNext()) {
            userList.add(UserDbModel.toDbModel(cursor));
        }
        return userList;
    }

    public static List<UserDbModel> getAllUser(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + UserDbModel.TABLE_NAME, null);

        List<UserDbModel> userList = new ArrayList<>();
        while(cursor.moveToNext()){
            userList.add(UserDbModel.toDbModel(cursor));
        }
        return userList;
    }

    public static void updateUser(SQLiteDatabase db, UserDbModel user) {
        db.update(UserDbModel.TABLE_NAME,
                user.getContentValues(),
                UserDbModel.USER_ID_COLUMN_NAME + "=?",
                new String[] {String.valueOf(user.userID)});
    }

    public static void addOrUpdateUser(SQLiteDatabase db, UserDbModel user) {
        db.replace(UserDbModel.TABLE_NAME,
                null,
                user.getContentValues());
    }

    public static void deleteUser(SQLiteDatabase db, int userID) {
        db.delete(UserDbModel.TABLE_NAME,
                UserDbModel.USER_ID_COLUMN_NAME + "=?",
                new String[] {String.valueOf(userID)});
    }

    public static int getSize(SQLiteDatabase db) {
        return (int) DatabaseUtils.queryNumEntries(db, UserDbModel.TABLE_NAME);
    }
}
