package com.example.preteirb.model

import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.item.Item
import com.example.preteirb.data.item.ItemsRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ItemEntryViewModelTest {
    
    // Mock the items repository using Mockito
    private lateinit var itemsRepository: ItemsRepository
    private lateinit var settingsRepository: SettingsRepository
    
    // Create an instance of the view model under test
    private lateinit var viewModel: ItemEntryViewModel
    
    
    private val userOwnerId = 1
    
    // Set up the view model before each test
    @Before
    fun setUp() {
        itemsRepository = Mockito.mock(ItemsRepository::class.java)
        settingsRepository = Mockito.mock(SettingsRepository::class.java)
        Mockito.`when`(settingsRepository.getUserId()).thenReturn(flowOf(userOwnerId))
        viewModel = ItemEntryViewModel(itemsRepository, settingsRepository)
    }
    
    // Test that the initial ui state is empty and invalid
    @Test
    fun testInitialUiState() {
        val uiState = viewModel.itemUiState
        assertEquals(ItemDetails(0, "", ""), uiState.itemDetails)
        assertEquals(false, uiState.isEntryValid)
    }
    
    // Test that updating the ui state with valid input makes it valid
    @Test
    fun testUpdateUiStateWithValidInput() {
        val itemDetails = ItemDetails(1, "Book", "A good read")
        viewModel.updateUiState(itemDetails)
        val uiState = viewModel.itemUiState
        assertEquals(itemDetails, uiState.itemDetails)
        assertEquals(true, uiState.isEntryValid)
    }
    
    // Test that updating the ui state with invalid input makes it invalid
    @Test
    fun testUpdateUiStateWithInvalidInput() {
        val itemDetails = ItemDetails(1, "", "A good read")
        viewModel.updateUiState(itemDetails)
        val uiState = viewModel.itemUiState
        assertEquals(itemDetails, uiState.itemDetails)
        assertEquals(false, uiState.isEntryValid)
    }
    
    // Test that saving an item with valid input calls the repository insert method
    @Test
    fun testSaveItemWithValidInput(): Unit = runBlocking {
        val itemDetails = ItemDetails(1, "Book", "A good read")
        viewModel.updateUiState(itemDetails)
        viewModel.saveItem()
        verify(itemsRepository).insertItem(itemDetails.toItem(userOwnerId))
    }
    
    // Test that saving an item with invalid input does not call the repository insert method
    @Test
    fun testSaveItemWithInvalidInput(): Unit = runBlocking {
        val itemDetails = ItemDetails(1, "", "A good read")
        viewModel.updateUiState(itemDetails)
        viewModel.saveItem()
        verify(itemsRepository, Mockito.never()).insertItem(itemDetails.toItem(userOwnerId))
    }
    
    // Test that converting an item to a ui state works
    @Test
    fun testItemToItemUiState() {
        val item = Item(1, "Book", "A good read", 1)
        val itemUiState = item.toItemUiState()
        assertEquals(item.toItemDetails(), itemUiState.itemDetails)
        assertEquals(false, itemUiState.isEntryValid)
    }
    
    // Test that converting an item to item details works
    @Test
    fun testItemToItemDetails() {
        val item = Item(1, "Book", "A good read", 1)
        val itemDetails = item.toItemDetails()
        assertEquals(item.itemId, itemDetails.id)
        assertEquals(item.name, itemDetails.name)
        assertEquals(item.description, itemDetails.description)
    }
    
    // Test that converting item details to an item works
    @Test
    fun testItemDetailsToItem() {
        val itemDetails = ItemDetails(1, "Book", "A good read")
        val item = itemDetails.toItem(userOwnerId)
        assertEquals(itemDetails.id, item.itemId)
        assertEquals(itemDetails.name, item.name)
        assertEquals(itemDetails.description, item.description)
        assertEquals(userOwnerId, item.userOwnerId)
    }
}