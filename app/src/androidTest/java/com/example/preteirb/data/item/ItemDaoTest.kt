package com.example.preteirb.data.item

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.preteirb.data.AppDatabase
import com.example.preteirb.data.usage.Usage
import com.example.preteirb.data.user.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ItemDaoTest {
    private lateinit var itemDao: ItemDao;
    private lateinit var appDatabase: AppDatabase;
    
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>();
        appDatabase =
            Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build();
        itemDao = appDatabase.itemDao();
    }
    
    @After
    @Throws(IOException::class)
    fun closeDb() {
        appDatabase.close();
    }
    
    private var item1 = Item(1, "Item 1", "Item 1 description", 1);
    private var item2 = Item(2, "Item 2", "Item 2 description", 1);
    private var item3 = Item(3, "Item 3", "Item 3 description", 1);
    
    private suspend fun addOneItemToDb() {
        itemDao.insert(item1);
    }
    
    private suspend fun addAllItemsToDb() {
        itemDao.insert(item1)
        itemDao.insert(item2)
        itemDao.insert(item3)
    }
    
    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsItemIntoDB() = runBlocking {
        val rowId = itemDao.insert(item3)
        val allItems = itemDao.getAllItems().first()
        assertEquals(allItems[0], item3)
        assertEquals(rowId.toInt(), item3.itemId)
    }
    
    @Test
    @Throws(Exception::class)
    fun daoGetAllItems_returnsAllItemsFromDB() = runBlocking {
        addAllItemsToDb()
        val allItems = itemDao.getAllItems().first()
        assertEquals(allItems[0], item1)
        assertEquals(allItems[1], item2)
    }
    
    @Test
    @Throws(Exception::class)
    fun daoGetItem_returnsItemFromDB() = runBlocking {
        addAllItemsToDb()
        val item = itemDao.getItem(1).first()
        assertEquals(item, item1)
    }
    
    @Test
    @Throws(Exception::class)
    fun daoUpdate_updatesItemInDB() = runBlocking {
        addOneItemToDb()
        var item = itemDao.getItem(1).first()
        assertEquals(item, item1)
        item.name = "New Name"
        itemDao.update(item)
        val updatedItem = itemDao.getItem(1).first()
        assertEquals(updatedItem, item)
    }
    
    @Test
    @Throws(Exception::class)
    fun daoDelete_deletesItemFromDB() = runBlocking {
        addOneItemToDb()
        var item = itemDao.getItem(1).first()
        assertEquals(item, item1)
        itemDao.delete(item)
        val allItems = itemDao.getAllItems().first()
        assertEquals(allItems.size, 0)
    }
    
    private var usage1 = Usage(1, 1, "", "");
    private var usage2 = Usage(2, 1, "", "");
    private var usage3 = Usage(1, 2, "", "");
    
    private var user1 = User(1, "User 1", "location 1");
    private var user2 = User(2, "User 2", "location 2");
    
    private var itemAndUsages1 = ItemAndUsages(item1, listOf(usage1, usage3));
    private var itemAndUsages2 = ItemAndUsages(item2, listOf(usage2));
    
    private suspend fun addOneItemAndUsagesToDb() {
        itemDao.insert(itemAndUsages1.item)
        val userDao = appDatabase.userDao();
        userDao.insert(user1)
        userDao.insert(user2)
        val usageDao = appDatabase.usageDao();
        usageDao.insert(itemAndUsages1.usages)
    }
    
    private suspend fun addAllItemsAndUsagesToDb() {
        val usageDao = appDatabase.usageDao();
        val userDao = appDatabase.userDao();
        itemDao.insert(itemAndUsages1.item)
        itemDao.insert(itemAndUsages2.item)
        userDao.insert(user1)
        userDao.insert(user2)
        usageDao.insert(itemAndUsages1.usages)
        usageDao.insert(itemAndUsages2.usages)
    }
    
    @Test
    @Throws(Exception::class)
    fun daoGetItemAndUsages_returnsItemAndUsagesFromDB() = runBlocking {
        addAllItemsAndUsagesToDb()
        val itemAndUsages = itemDao.getItemAndUsages(1).first()
        assertEquals(itemAndUsages, itemAndUsages1)
    }
}