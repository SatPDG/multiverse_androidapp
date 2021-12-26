package multiverse.androidapp.multiverse.database.localDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.List;

import multiverse.androidapp.multiverse.database.localDatabase.localDatabaseServices.NotificationLocalDbService;
import multiverse.androidapp.multiverse.databaseContext.DatabaseUtil;
import multiverse.androidapp.multiverse.model.dbModel.NotificationDbModel;

@RunWith(AndroidJUnit4.class)
public class NotificationLocalDbServiceTest {

    public static SQLiteDatabase db;
    public static Context context;

    @BeforeClass
    public static void beforeClass() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        MultiverseDbHelper dbHelper = new MultiverseDbHelper(context);

        db = dbHelper.getWritableDatabase();
    }

    @AfterClass
    public static void afterClass() {
        new MultiverseDbHelper(context).onDowngrade(db, 0, 0);
    }

    @Before
    public void before(){
        new MultiverseDbHelper(context).onDowngrade(db, 0, 0);
    }

    @Test
    public void getNotifications_NotifExist_ReturnNotification() {
        DatabaseUtil.addNotifications(db, 20);

        List<NotificationDbModel> notifList = NotificationLocalDbService.getNotifications(db, 0, 10);

        assertNotNull(notifList);
        assertEquals(10, notifList.size());
        for(int i = 0; i < 10; i++) {
            assertEquals(19-i, notifList.get(i).notificationID);
            assertEquals(19-i, notifList.get(i).date);
            assertEquals(0, notifList.get(i).lastDataUpdate);
            assertEquals(0, notifList.get(i).notificationType);
            assertEquals(0, notifList.get(i).objectID);
        }
    }

    @Test
    public void getNotifications_NotifExistWithOffset_ReturnNotification() {
        DatabaseUtil.addNotifications(db, 20);

        List<NotificationDbModel> notifList = NotificationLocalDbService.getNotifications(db, 10, 10);

        assertNotNull(notifList);
        assertEquals(10, notifList.size());
        for(int i = 0; i < 10; i++) {
            assertEquals(9-i, notifList.get(i).notificationID);
            assertEquals(9-i, notifList.get(i).date);
            assertEquals(0, notifList.get(i).lastDataUpdate);
            assertEquals(0, notifList.get(i).notificationType);
            assertEquals(0, notifList.get(i).objectID);
        }
    }

    @Test
    public void getNotifications_NotifExistWithOffset_ReturnNotificationButSmallerListSize() {
        DatabaseUtil.addNotifications(db, 20);

        List<NotificationDbModel> notifList = NotificationLocalDbService.getNotifications(db, 15, 10);

        assertNotNull(notifList);
        assertEquals(5, notifList.size());
        for(int i = 0; i < 5; i++) {
            assertEquals(4-i, notifList.get(i).notificationID);
            assertEquals(4-i, notifList.get(i).date);
            assertEquals(0, notifList.get(i).lastDataUpdate);
            assertEquals(0, notifList.get(i).notificationType);
            assertEquals(0, notifList.get(i).objectID);
        }
    }

    @Test
    public void getNotifications_NotifExistWithOffsetTooBig_ReturnEmptyList() {
        DatabaseUtil.addNotifications(db, 20);

        List<NotificationDbModel> notifList = NotificationLocalDbService.getNotifications(db, 100, 10);

        assertNotNull(notifList);
        assertEquals(0, notifList.size());
    }

    @Test
    public void addNotification_AddNotif_Succeded() {
        DatabaseUtil.addNotifications(db, 5);

        NotificationDbModel notif = new NotificationDbModel();
        notif.notificationID = 100;
        notif.date = 100;
        notif.objectID = 0;
        notif.notificationType = 0;
        notif.lastDataUpdate = 0;
        NotificationLocalDbService.addNotification(db, notif);

        List<NotificationDbModel> notifList = NotificationLocalDbService.getNotifications(db, 0, 10);
        assertEquals(6, notifList.size());
        assertEquals(100, notifList.get(0).notificationID);
    }

    @Test
    public void deleteNotification_NotifPresent_Succeded() {
        DatabaseUtil.addNotifications(db, 5);

        NotificationLocalDbService.deleteNotification(db, 2);

        List<NotificationDbModel> notifList = NotificationLocalDbService.getNotifications(db, 0, 10);
        assertEquals(4, notifList.size());
    }

    @Test
    public void deleteOlderNotification_NotifPresent_Succeded() {
        DatabaseUtil.addNotifications(db, 20);

        NotificationLocalDbService.deleteOlderNotification(db, 10);

        List<NotificationDbModel> notifList = NotificationLocalDbService.getNotifications(db, 0, 20);
        assertEquals(10, notifList.size());
        for(int i = 0; i < 10; i++) {
            assertEquals(19-i, notifList.get(i).date);
            assertEquals(19-i, notifList.get(i).notificationID);
        }
    }

    @Test
    public void getSize_ItemInList_Succeded() {
        DatabaseUtil.addNotifications(db, 25);

        int count = NotificationLocalDbService.getSize(db);

        assertEquals(25, count);
    }
}
