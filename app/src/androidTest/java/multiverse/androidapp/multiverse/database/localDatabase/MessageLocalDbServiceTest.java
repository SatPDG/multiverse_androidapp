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

import java.util.List;

import multiverse.androidapp.multiverse.database.localDatabase.localDatabaseServices.MessageLocalDbServices;
import multiverse.androidapp.multiverse.databaseContext.DatabaseUtil;
import multiverse.androidapp.multiverse.model.dbModel.MessageDbModel;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MessageLocalDbServiceTest {

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
    public void getMessage_MessagePresent_ReturnMessages() {
        DatabaseUtil.addMessage(db, 20);

        List<MessageDbModel> msgList = MessageLocalDbServices.getMessages(db, 0, 10);

        assertNotNull(msgList);
        assertEquals(10, msgList.size());
        for(int i = 0; i < 10; i++) {
            assertEquals(19-i, msgList.get(i).messageID);
            assertEquals(19-i, msgList.get(i).publishedTime);
            assertEquals(0, msgList.get(i).authorID);
            assertEquals(0, msgList.get(i).conversationID);
            assertEquals(0, msgList.get(i).lastDataUpdate);
            assertEquals("message_" + String.valueOf(19-i), msgList.get(i).message);
        }
    }

    @Test
    public void getMessage_MessagePresentWithOffset_ReturnMessages() {
        DatabaseUtil.addMessage(db, 20);

        List<MessageDbModel> msgList = MessageLocalDbServices.getMessages(db, 10, 10);

        assertNotNull(msgList);
        assertEquals(10, msgList.size());
        for(int i = 0; i < 10; i++) {
            assertEquals(9-i, msgList.get(i).messageID);
            assertEquals(9-i, msgList.get(i).publishedTime);
            assertEquals(0, msgList.get(i).authorID);
            assertEquals(0, msgList.get(i).conversationID);
            assertEquals(0, msgList.get(i).lastDataUpdate);
            assertEquals("message_" + String.valueOf(9-i), msgList.get(i).message);
        }
    }

    @Test
    public void getMessage_MessagePresentWithBigOffset_ReturnMessages() {
        DatabaseUtil.addMessage(db, 20);

        List<MessageDbModel> msgList = MessageLocalDbServices.getMessages(db, 15, 10);

        assertNotNull(msgList);
        assertEquals(5, msgList.size());
        for(int i = 0; i < 5; i++) {
            assertEquals(4-i, msgList.get(i).messageID);
            assertEquals(4-i, msgList.get(i).publishedTime);
            assertEquals(0, msgList.get(i).authorID);
            assertEquals(0, msgList.get(i).conversationID);
            assertEquals(0, msgList.get(i).lastDataUpdate);
            assertEquals("message_" + String.valueOf(4-i), msgList.get(i).message);
        }
    }

    @Test
    public void getMessage_MessagePresentWithTooBigOffset_ReturnEmpty() {
        DatabaseUtil.addMessage(db, 20);

        List<MessageDbModel> msgList = MessageLocalDbServices.getMessages(db, 100, 10);

        assertNotNull(msgList);
        assertEquals(0, msgList.size());
    }

    @Test
    public void addMessage_addMessage_Succeded() {
        DatabaseUtil.addMessage(db, 4);

        MessageDbModel msg = new MessageDbModel();
        msg.messageID = 100;
        msg.publishedTime = 100;
        msg.conversationID = 0;
        msg.authorID = 0;
        msg.messageType = 0;
        msg.message = "custom msg";
        MessageLocalDbServices.addMessage(db, msg);

        List<MessageDbModel> msgList = MessageLocalDbServices.getMessages(db, 0, 5);
        assertEquals(5, msgList.size());
        assertEquals(100, msgList.get(0).messageID);
        assertEquals(100, msgList.get(0).publishedTime);
        assertEquals(0, msgList.get(0).authorID);
        assertEquals(0, msgList.get(0).conversationID);
        assertEquals(0, msgList.get(0).lastDataUpdate);
        assertEquals("custom msg", msgList.get(0).message);
    }

    @Test
    public void deleteMessage_MessageIsThere_Succeded() {
        DatabaseUtil.addMessage(db, 5);

        MessageLocalDbServices.deleteMessage(db, 1);

        List<MessageDbModel> msgList = MessageLocalDbServices.getAllMessage(db);

        assertEquals(4, msgList.size());
    }

    @Test
    public void deleteMessage_MessageNotThere_Succeded() {
        DatabaseUtil.addMessage(db, 5);

        MessageLocalDbServices.deleteMessage(db, 100);

        List<MessageDbModel> msgList = MessageLocalDbServices.getAllMessage(db);

        assertEquals(5, msgList.size());
    }

    @Test
    public void deleteOlderMessage_MessagePresent_Succeded() {
        DatabaseUtil.addMessage(db, 20);

        MessageLocalDbServices.deleteOlderMessage(db, 10);

        List<MessageDbModel> msgList = MessageLocalDbServices.getMessages(db, 0, 20);
        assertEquals(10, msgList.size());
        for(int i = 0; i < 10; i++) {
            assertEquals(19-i, msgList.get(i).publishedTime);
            assertEquals(19-i, msgList.get(i).messageID);
        }
    }

    @Test
    public void getSize_DbNotEmpty_RightSizeReturned() {
        DatabaseUtil.addMessage(db, 25);

        int size = MessageLocalDbServices.getSize(db);

        assertEquals(25, size);
    }

    @Test
    public void updateMessage_MessageIsThere_Succeded() {
        DatabaseUtil.addMessage(db, 10);

        MessageDbModel msg = new MessageDbModel();
        msg.messageID = 0;
        msg.publishedTime = 100;
        msg.message = "new Msg";
        msg.messageType = 1;
        msg.authorID = 2;
        msg.conversationID = 3;
        msg.lastDataUpdate = 4;
        MessageLocalDbServices.updateMessage(db, msg);

        List<MessageDbModel> msgList = MessageLocalDbServices.getMessages(db, 0, 1);
        assertEquals(0, msgList.get(0).messageID);
        assertEquals(100, msgList.get(0).publishedTime);
        assertEquals("new Msg", msgList.get(0).message);
        assertEquals(1, msgList.get(0).messageType);
        assertEquals(2, msgList.get(0).authorID);
        assertEquals(3, msgList.get(0).conversationID);
        assertEquals(4, msgList.get(0).lastDataUpdate);
    }
}
