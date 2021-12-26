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

import multiverse.androidapp.multiverse.database.localDatabase.localDatabaseServices.ConversationLocalDbService;
import multiverse.androidapp.multiverse.databaseContext.DatabaseUtil;
import multiverse.androidapp.multiverse.model.dbModel.ConversationDbModel;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ConversationLocalDbServiceTest {

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
    public void getConversation_ConversationPresent_Succeded() {
        DatabaseUtil.addConversation(db, 20);

        List<ConversationDbModel> convList = ConversationLocalDbService.getConversations(db, 0, 10);

        assertNotNull(convList);
        assertEquals(10, convList.size());
        for(int i = 0; i < 10; i++) {
            assertEquals(19-i, convList.get(i).conversationID);
            assertEquals(19-i, convList.get(i).lastUpdate);
            assertEquals("conv_" + String.valueOf(19-i), convList.get(i).name);
            assertEquals(0, convList.get(i).lastDataUpdate);
        }
    }

    @Test
    public void getConversation_ConversationPresentWithOffset_Succeded() {
        DatabaseUtil.addConversation(db, 20);

        List<ConversationDbModel> convList = ConversationLocalDbService.getConversations(db, 10, 10);

        assertNotNull(convList);
        assertEquals(10, convList.size());
        for(int i = 0; i < 10; i++) {
            assertEquals(9-i, convList.get(i).conversationID);
            assertEquals(9-i, convList.get(i).lastUpdate);
            assertEquals("conv_" + String.valueOf(9-i), convList.get(i).name);
            assertEquals(0, convList.get(i).lastDataUpdate);
        }
    }

    @Test
    public void getConversation_ConversationPresentWithTooBigOffset_Succeded() {
        DatabaseUtil.addConversation(db, 20);

        List<ConversationDbModel> convList = ConversationLocalDbService.getConversations(db, 100, 10);

        assertNotNull(convList);
        assertEquals(0, convList.size());
    }

    @Test
    public void addConversation_addConversation_Succeded() {
        DatabaseUtil.addConversation(db, 4);

        ConversationDbModel conv = new ConversationDbModel();
        conv.conversationID = 100;
        conv.lastUpdate = 100;
        conv.name = "new conv";
        conv.lastDataUpdate = 0;
        ConversationLocalDbService.addConversation(db, conv);

        List<ConversationDbModel> convList = ConversationLocalDbService.getConversations(db, 0, 5);
        assertEquals(5, convList.size());
        assertEquals(100, convList.get(0).conversationID);
        assertEquals(100, convList.get(0).lastUpdate);
        assertEquals(0, convList.get(0).lastDataUpdate);
        assertEquals("new conv", convList.get(0).name);
    }

    @Test
    public void deleteConversation_ConversationIsThere_Succeded() {
        DatabaseUtil.addConversation(db, 5);

        ConversationLocalDbService.deleteConversation(db, 1);

        List<ConversationDbModel> msgList = ConversationLocalDbService.getAllConversation(db);

        assertEquals(4, msgList.size());
    }

    @Test
    public void deleteConversation_ConversationNotThere_Succeded() {
        DatabaseUtil.addConversation(db, 5);

        ConversationLocalDbService.deleteConversation(db, 100);

        List<ConversationDbModel> msgList = ConversationLocalDbService.getAllConversation(db);

        assertEquals(5, msgList.size());
    }

    @Test
    public void deleteOlderConversation_ConversationPresent_Succeded() {
        DatabaseUtil.addConversation(db, 20);

        ConversationLocalDbService.deleteOlderConversation(db, 10);

        List<ConversationDbModel> convList = ConversationLocalDbService.getConversations(db, 0, 20);
        assertEquals(10, convList.size());
        for(int i = 0; i < 10; i++) {
            assertEquals(19-i, convList.get(i).lastUpdate);
            assertEquals(19-i, convList.get(i).conversationID);
        }
    }

    @Test
    public void getSize_DbNotEmpty_RightSizeReturned() {
        DatabaseUtil.addConversation(db, 25);

        int size = ConversationLocalDbService.getSize(db);

        assertEquals(25, size);
    }

    @Test
    public void updateConversation_ConversationIsThere_Succeded() {
        DatabaseUtil.addConversation(db, 10);

        ConversationDbModel conv = new ConversationDbModel();
        conv.conversationID = 0;
        conv.lastUpdate = 100;
        conv.name = "new conv";
        conv.lastDataUpdate = 1;
        ConversationLocalDbService.updateConversation(db, conv);

        List<ConversationDbModel> msgList = ConversationLocalDbService.getConversations(db, 0, 1);
        assertEquals(0, msgList.get(0).conversationID);
        assertEquals(100, msgList.get(0).lastUpdate);
        assertEquals("new conv", msgList.get(0).name);
        assertEquals(1, msgList.get(0).lastDataUpdate);
    }
}
