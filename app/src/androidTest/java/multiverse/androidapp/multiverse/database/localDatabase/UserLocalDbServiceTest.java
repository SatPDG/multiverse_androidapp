package multiverse.androidapp.multiverse.database.localDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import multiverse.androidapp.multiverse.database.localDatabase.localDatabaseServices.UserLocalDbService;
import multiverse.androidapp.multiverse.databaseContext.DatabaseUtil;
import multiverse.androidapp.multiverse.model.dbModel.UserDbModel;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class UserLocalDbServiceTest {

    public static SQLiteDatabase db;
    public static Context context;

    @BeforeClass
    public static void beforeClass(){
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        MultiverseDbHelper dbHelper = new MultiverseDbHelper(context);

        db = dbHelper.getWritableDatabase();
    }

    @AfterClass
    public static void afterClass(){
        new MultiverseDbHelper(context).onDowngrade(db, 0, 0);
    }

    @Before
    public void before(){
        new MultiverseDbHelper(context).onDowngrade(db, 0, 0);
    }

    @Test
    public void getUser_UserExistsInDb_ReturnUser(){
        // Add users to db
        DatabaseUtil.addUsers(db, 3);

        // Act
        UserDbModel user = UserLocalDbService.getUser(db, 1);

        // Test
        assertNotNull(user);
        assertEquals(1, user.userID);
        assertEquals("firstname_1", user.firstname);
        assertEquals("lastname_1", user.lastname);
        assertEquals("(0, 0)", user.lastLocation);
        assertEquals(0, user.lastDataUpdate);
    }

    @Test
    public void getUser_UserNotInDb_ReturnNull(){
        // Add users to db
        DatabaseUtil.addUsers(db, 3);

        // Act
        UserDbModel user = UserLocalDbService.getUser(db, 100);

        assertNull(user);
    }

    @Test
    public void getUsers_UsersInDb_ReturnUsers(){
        // Add users to db
        DatabaseUtil.addUsers(db, 5);
        List<Integer> usersID = new ArrayList<>();
        usersID.add(1);
        usersID.add(3);
        usersID.add(4);

        // Act
        List<UserDbModel> users = UserLocalDbService.getUsers(db, usersID);

        // Test
        assertNotNull(users);
        assertEquals(3, users.size());
        assertEquals(1, users.get(0).userID);
        assertEquals("firstname_1", users.get(0).firstname);
        assertEquals("lastname_1", users.get(0).lastname);
        assertEquals("(0, 0)", users.get(0).lastLocation);
        assertEquals(0, users.get(0).lastDataUpdate);
        assertEquals(3, users.get(1).userID);
        assertEquals("firstname_3", users.get(1).firstname);
        assertEquals("lastname_3", users.get(1).lastname);
        assertEquals("(0, 0)", users.get(1).lastLocation);
        assertEquals(0, users.get(1).lastDataUpdate);
        assertEquals(4, users.get(2).userID);
        assertEquals("firstname_4", users.get(2).firstname);
        assertEquals("lastname_4", users.get(2).lastname);
        assertEquals("(0, 0)", users.get(2).lastLocation);
        assertEquals(0, users.get(2).lastDataUpdate);
    }

    @Test
    public void getUsers_SomeUserNotInDb_ReturnUserInDb() {
        // Add users to db
        DatabaseUtil.addUsers(db, 5);
        List<Integer> usersID = new ArrayList<>();
        usersID.add(1);
        usersID.add(3);
        usersID.add(7);

        // Act
        List<UserDbModel> users = UserLocalDbService.getUsers(db, usersID);

        // Test
        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals(1, users.get(0).userID);
        assertEquals("firstname_1", users.get(0).firstname);
        assertEquals("lastname_1", users.get(0).lastname);
        assertEquals("(0, 0)", users.get(0).lastLocation);
        assertEquals(0, users.get(0).lastDataUpdate);
        assertEquals(3, users.get(1).userID);
        assertEquals("firstname_3", users.get(1).firstname);
        assertEquals("lastname_3", users.get(1).lastname);
        assertEquals("(0, 0)", users.get(1).lastLocation);
        assertEquals(0, users.get(1).lastDataUpdate);
    }

    @Test
    public void getUsers_UserNotInDb_ReturnNothing() {
        // Add users to db
        DatabaseUtil.addUsers(db, 5);
        List<Integer> usersID = new ArrayList<>();
        usersID.add(7);
        usersID.add(8);
        usersID.add(10);

        // Act
        List<UserDbModel> users = UserLocalDbService.getUsers(db, usersID);

        // Test
        assertNotNull(users);
        assertEquals(0, users.size());
    }

    @Test
    public void updateUser_UserInDb_UserIsUpdated(){
        // Add users to db
        DatabaseUtil.addUsers(db, 5);

        UserDbModel user = new UserDbModel();
        user.userID = 2;
        user.firstname = "newFirstname";
        user.lastname = "newLastname";
        user.lastLocation = "(1, 2)";
        user.lastDataUpdate = 3;

        // Act
        UserLocalDbService.updateUser(db, user);

        // Test
        UserDbModel newUser = UserLocalDbService.getUser(db, 2);
        assertNotNull(newUser);
        assertEquals(user.userID, newUser.userID);
        assertEquals(user.firstname, newUser.firstname);
        assertEquals(user.lastname, newUser.lastname);
        assertEquals(user.lastLocation, newUser.lastLocation);
        assertEquals(user.lastDataUpdate, newUser.lastDataUpdate);
        newUser = UserLocalDbService.getUser(db, 3);
        assertNotNull(newUser);
        assertEquals(3, newUser.userID);
        assertEquals("firstname_3", newUser.firstname);
    }

    @Test
    public void deleteUser_DeleteUser_UserIsDeleted(){
        // Add users to db
        DatabaseUtil.addUsers(db, 5);

        UserLocalDbService.deleteUser(db, 3);

        UserDbModel user = UserLocalDbService.getUser(db, 3);
        assertNull(user);
    }

    @Test
    public void addOrUpdateUser_UserNotPresent_UserAdded() {
        // Add users to db
        DatabaseUtil.addUsers(db, 5);

        UserDbModel model = new UserDbModel();
        model.firstname = "new firstname";
        model.lastname = "new lastname";
        model.userID = 100;
        model.lastLocation = "";
        model.lastDataUpdate = 1;
        UserLocalDbService.addOrUpdateUser(db, model);

        UserDbModel user = UserLocalDbService.getUser(db, 100);
        assertNotNull(user);
        assertEquals(100, user.userID);
        assertEquals("new firstname", user.firstname);
        assertEquals("new lastname", user.lastname);
    }

    @Test
    public void addOrUpdateUser_UserIsPresent_UserUpdated() {
        // Add users to db
        DatabaseUtil.addUsers(db, 5);

        UserDbModel model = new UserDbModel();
        model.firstname = "new firstname";
        model.lastname = "new lastname";
        model.userID = 1;
        model.lastLocation = "new location";
        model.lastDataUpdate = 1;
        UserLocalDbService.addOrUpdateUser(db, model);

        UserDbModel user = UserLocalDbService.getUser(db, 1);
        assertNotNull(user);;
        assertEquals(1, user.userID);
        assertEquals("new firstname", user.firstname);
        assertEquals("new lastname", user.lastname);
        assertEquals("new location", user.lastLocation);
        assertEquals(1, user.lastDataUpdate);

        int size = UserLocalDbService.getSize(db);
        assertEquals(5, size);
    }
}
