package com.example.preteirb.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.preteirb.data.AppDatabase
import com.example.preteirb.data.item.Item
import com.example.preteirb.data.usage.Usage
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
class UserDaoTest {
    private lateinit var userDao: UserDao;
    private lateinit var appDatabase: AppDatabase;

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>();
        appDatabase =
            Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build();
        userDao = appDatabase.userDao();
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        appDatabase.close();
    }

    private var user1 = User(1, "User 1");
    private var user2 = User(2, "User 2");

    private suspend fun addOneUserToDb() {
        userDao.insert(user1);
    }

    private suspend fun addAllUsersToDb() {
        userDao.insert(user1)
        userDao.insert(user2)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsUserIntoDB() = runBlocking {
        addOneUserToDb()
        val allUsers = userDao.getAllUsers().first()
        assertEquals(allUsers[0], user1)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsAllUsersIntoDB() = runBlocking {
        addAllUsersToDb()
        val allUsers = userDao.getAllUsers().first()
        assertEquals(allUsers[0], user1)
        assertEquals(allUsers[1], user2)
    }

    @Test
    @Throws(Exception::class)
    fun daoDelete_deletesUserFromDB() = runBlocking {
        addOneUserToDb()
        userDao.delete(user1)
        val allUsers = userDao.getAllUsers().first()
        assertEquals(allUsers.size, 0)
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdate_updatesUserInDB() = runBlocking {
        addOneUserToDb()
        val updatedUser = User(1, "User 1 updated")
        userDao.update(updatedUser)
        val allUsers = userDao.getAllUsers().first()
        assertEquals(allUsers[0], updatedUser)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetUser_returnsUserFromDB() = runBlocking {
        addAllUsersToDb()
        val user = userDao.getUser(1).first()
        assertEquals(user, user1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllItemsOwnedByUser_returnsAllItemsOwnedByUserFromDB() = runBlocking {
        addAllUsersToDb()
        val itemDao = appDatabase.itemDao();
        val item1 = Item(1, "Item 1", "Item 1 description", 1);
        val item2 = Item(2, "Item 2", "Item 2 description", 1);
        itemDao.insert(item1)
        itemDao.insert(item2)

        val usageDao = appDatabase.usageDao();
        val usage1 = Usage(1, 1, 1, 0, 0);
        val usage2 = Usage(2, 2, 1, 0, 0);
        val usage3 = Usage(3, 1, 2, 0, 0);
        usageDao.insert(listOf(usage1, usage2, usage3))

        val itemsOwned = userDao.getAllItemsOwnedByUser(1).first()
        assertEquals(itemsOwned.owner, user1)
        assertEquals(itemsOwned.items[0], item1)
        assertEquals(itemsOwned.items[1], item2)
    }

}
