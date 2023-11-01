package com.example.preteirb.model

import app.cash.turbine.test
import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.user.User
import com.example.preteirb.data.user.UsersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ProfileSelectionViewModelTest {
    private lateinit var usersRepository: UsersRepository
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var viewModel: ChooseProfileViewModel
    
    @Before
    fun setUp() {
        usersRepository = Mockito.mock(UsersRepository::class.java)
        settingsRepository = Mockito.mock(SettingsRepository::class.java)
        viewModel = ChooseProfileViewModel(usersRepository, settingsRepository)
    }
    
    // Test that the initial ui state is empty
    @Test
    fun testInitialUiState() = runBlocking {
        val uiState = viewModel.chooseProfileUiState.first()
        assertEquals(ChooseProfileUiState(), uiState)
    }
    
    // Test that the ui state reflects the users stream from the repository
    @Test
    fun testUiStateWithUsersStream() = runTest {
        // Create a fake users stream using a mutable state flow
        val usersStream = MutableStateFlow(listOf<User>())
        
        // Stub the repository to return the fake stream when getAllUsersStream is called
        Mockito.`when`(usersRepository.getAllUsersStream()).thenReturn(usersStream)
        
        
        // Emit some fake users to the stream
        val fakeUsers = listOf(
            User(1, "Alice", "alice@example.com"),
            User(2, "Bob", "bob@example.com"),
            User(3, "Charlie", "charlie@example.com")
        )
        
        usersStream.test {
            usersStream.emit(fakeUsers)
            assertEquals(fakeUsers, awaitItem())
            awaitComplete()
        }
    }
    
    // Test that setting the current user calls the repository storeUserId method
    @Test
    fun testSetCurrentUser() = runBlocking {
        val fakeUser = User(1, "Alice", "alice@example.com")
        viewModel.setCurrentUser(fakeUser)
        verify(settingsRepository).storeUserId(fakeUser.userId)
    }
    
    // Test that setting whether the user is logged in calls the repository storeIsLoggedIn method
    @Test
    fun testSetIsLoggedIn() = runBlocking {
        viewModel.setIsLoggedIn(true)
        verify(settingsRepository).storeIsLoggedIn(true)
    }
    
}

