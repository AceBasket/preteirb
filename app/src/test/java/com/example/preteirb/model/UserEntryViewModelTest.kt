package com.example.preteirb.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.preteirb.data.user.User
import com.example.preteirb.data.user.UsersRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserEntryViewModelTest {
    
    @get:Rule
    val rule = InstantTaskExecutorRule()
    
    // Mock the users repository using Mockito
    private lateinit var usersRepository: UsersRepository
    
    // Create an instance of the view model under test
    private lateinit var viewModel: UserEntryViewModel
    
    // Set up the view model before each test
    @Before
    fun setUp() {
        usersRepository = Mockito.mock(UsersRepository::class.java)
        viewModel = UserEntryViewModel(usersRepository)
    }
    
    // Test that the initial ui state is empty and invalid
    @Test
    fun testInitialUiState() {
        val uiState = viewModel.userUiState
        assertEquals(UserDetails(0, "", ""), uiState.userDetails)
        assertEquals(false, uiState.isEntryValid)
    }
    
    // Test that updating the ui state with valid input makes it valid
    @Test
    fun testUpdateUiStateWithValidInput() {
        val userDetails = UserDetails(1, "Book", "A good read")
        viewModel.updateUiState(userDetails)
        val uiState = viewModel.userUiState
        assertEquals(userDetails, uiState.userDetails)
        assertEquals(true, uiState.isEntryValid)
    }
    
    // Test that updating the ui state with invalid input makes it invalid
    @Test
    fun testUpdateUiStateWithInvalidInput() {
        val userDetails = UserDetails(1, "", "A good read")
        viewModel.updateUiState(userDetails)
        val uiState = viewModel.userUiState
        assertEquals(userDetails, uiState.userDetails)
        assertEquals(false, uiState.isEntryValid)
    }
    
    // Test that saving the user with valid input calls the repository
    @Test
    fun testSaveUserWithValidInput(): Unit = runBlocking {
        val userDetails = UserDetails(1, "Book", "A good read")
        viewModel.updateUiState(userDetails)
        viewModel.saveUser()
        verify(usersRepository).insertUser(userDetails.toUser())
    }
    
    // Test that saving the user with invalid input does not call the repository
    @Test
    fun testSaveUserWithInvalidInput(): Unit = runBlocking {
        val userDetails = UserDetails(1, "", "A good read")
        viewModel.updateUiState(userDetails)
        viewModel.saveUser()
        verify(usersRepository, Mockito.never()).insertUser(userDetails.toUser())
    }
    
    // Test that converting a user to a user ui state returns the correct user ui state
    @Test
    fun testToUserUiState() {
        val user = User(1, "Book", "A good read")
        val userUiState = user.toUserUiState()
        assertEquals(
            UserUiState(userDetails = user.toUserDetails(), isEntryValid = false),
            userUiState
        )
    }
    
    // Test that converting a user to a user details returns the correct user details
    @Test
    fun testToUserDetails() {
        val user = User(1, "Book", "A good read")
        val userDetails = user.toUserDetails()
        assertEquals(UserDetails(id = 1, username = "Book", location = "A good read"), userDetails)
    }
    
    // Test that converting user details to a user returns the correct user
    @Test
    fun testToUser() {
        val userDetails = UserDetails(id = 1, username = "Book", location = "A good read")
        val user = userDetails.toUser()
        assertEquals(User(userId = 1, username = "Book", location = "A good read"), user)
    }
}