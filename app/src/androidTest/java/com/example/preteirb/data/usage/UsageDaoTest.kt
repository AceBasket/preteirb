package com.example.preteirb.data.usage

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.preteirb.data.PreteirbDatabase
import com.example.preteirb.data.item.Item
import com.example.preteirb.data.item.ItemDao
import com.example.preteirb.data.user.User
import com.example.preteirb.data.user.UserDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class UsageDaoTest {
    private lateinit var usageDao: UsageDao;
    private lateinit var userDao: UserDao;
    private lateinit var itemDao: ItemDao;
    private lateinit var preteirbDatabase: PreteirbDatabase;
    
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>();
        preteirbDatabase =
            Room.inMemoryDatabaseBuilder(context, PreteirbDatabase::class.java)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build();
        usageDao = preteirbDatabase.usageDao();
        userDao = preteirbDatabase.userDao();
        itemDao = preteirbDatabase.itemDao();
    }
    
    @After
    @Throws(IOException::class)
    fun closeDb() {
        preteirbDatabase.close();
    }
    
    private var user1 = User(1, "User 1", "location 1");
    private var user2 = User(2, "User 2", "location 2");
    
    private var item1 = Item(1, "Item 1", "Item 1 description", 1);
    private var item2 = Item(2, "Item 2", "Item 2 description", 1);
    
    
    private var usage1 = Usage(1, 1, "", "");
    private var usage2 = Usage(2, 1, "", "");
    private var usage3 = Usage(1, 2, "", "");
    
    private suspend fun addOneUsageToDb() {
        userDao.insert(user1)
        itemDao.insert(item1)
        usageDao.insert(usage1)
    }
    
    private suspend fun addAllUsagesToDb() {
        userDao.insert(user1)
        userDao.insert(user2)
        
        itemDao.insert(item1)
        itemDao.insert(item2)
        
        usageDao.insert(listOf(usage1, usage2, usage3))
    }
    
    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsUsageIntoDB() = runBlocking {
        addOneUsageToDb()
        val allUsages = usageDao.getAllUsages().first()
        assertEquals(allUsages[0], usage1)
    }
    
    @Test
    @Throws(Exception::class)
    fun daoInsertAll_insertsUsagesIntoDB() = runBlocking {
        addAllUsagesToDb()
        val allUsages = usageDao.getAllUsages().first()
        assertEquals(allUsages[0], usage1)
        assertEquals(allUsages[1], usage2)
        assertEquals(allUsages[2], usage3)
    }
    
    @Test
    @Throws(Exception::class)
    fun daoGetAllUsages_returnsAllUsagesFromDB() = runBlocking {
        addAllUsagesToDb()
        val allUsages = usageDao.getAllUsages().first()
        assertEquals(allUsages[0], usage1)
        assertEquals(allUsages[1], usage2)
        assertEquals(allUsages[2], usage3)
    }
    
    @Test
    @Throws(Exception::class)
    fun daoGetUsage_returnsUsageFromDB() = runBlocking {
        addAllUsagesToDb()
        val usage = usageDao.getUsage(1, 2).first()
        assertEquals(usage, usage3)
    }
    
    @Test
    @Throws(Exception::class)
    fun daoUpdate_updatesUsageInDB() = runBlocking {
        addAllUsagesToDb()
        val usage = usageDao.getUsage(1, 2).first()
        assertEquals(usage, usage3)
        usageDao.update(usage.copy(itemId = 2, userId = 1))
        val updatedUsage = usageDao.getUsage(2, 1).first()
        assertEquals(updatedUsage, usage3.copy(itemId = 2, userId = 1))
    }
    
    @Test
    @Throws(Exception::class)
    fun daoDelete_deletesUsageFromDB() = runBlocking {
        addAllUsagesToDb()
        val usage = usageDao.getUsage(1, 2).first()
        assertEquals(usage, usage3)
        usageDao.delete(usage)
        val allUsages = usageDao.getAllUsages().first()
        assertEquals(allUsages[0], usage1)
        assertEquals(allUsages[1], usage2)
    }
}