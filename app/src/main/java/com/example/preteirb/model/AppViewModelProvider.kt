package com.example.preteirb.model

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.preteirb.PreteirbApplication

/**
 * Provides Factory to create instance of ViewModel for the entire Prêt'eirb app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for SearchViewModel
        initializer {
            SearchViewModel(
                this.createSavedStateHandle(),
                preteirbApplication().container.itemsRepository
            )
        }
        
        // Initializer for UserEntryViewModel
        initializer {
            UserEntryViewModel(
                preteirbApplication().container.usersRepository
            )
        }
        
        // Initializer for ItemEntryViewModel
        initializer {
            ItemEntryViewModel(
                preteirbApplication().container.itemsRepository,
                preteirbApplication().container.settingsRepository
            )
        }
        
        // Initializer for ItemSelectionViewModel
        initializer {
            ItemSelectionViewModel(
                preteirbApplication().container.itemsRepository
            )
        }
        
        // Initializer for UserProfileViewModel
        initializer {
            UserProfileViewModel(
                preteirbApplication().container.usersRepository,
                preteirbApplication().container.settingsRepository,
            )
        }
        
        // Initializer for ProfileSelectionViewModel
        initializer {
            ProfileSelectionViewModel(
                preteirbApplication().container.usersRepository,
                preteirbApplication().container.settingsRepository,
            )
        }
        
        // Initializer for PreteirbAppViewModel
        initializer {
            PreteirbAppViewModel(
                preteirbApplication().container.settingsRepository
            )
        }
        
        // Initializer for ItemsOwnedUsageEntryViewModel
        initializer {
            ItemsOwnedUsageEntryViewModel(
                preteirbApplication().container.usersRepository,
                preteirbApplication().container.usagesRepository,
                preteirbApplication().container.settingsRepository,
            )
        }
        
        // Initializer for BookItemsViewModel
        initializer {
            BookItemsViewModel(
                this.createSavedStateHandle(),
                preteirbApplication().container.usagesRepository,
                preteirbApplication().container.itemsRepository,
                preteirbApplication().container.settingsRepository
            )
        }
        
        // Initializer for ListItemsViewModel
        initializer {
            ListItemsViewModel(
                preteirbApplication().container.usersRepository,
                preteirbApplication().container.settingsRepository
            )
        }
        
        // Initializer for ItemAndUsagesDetailsViewModel
        initializer {
            ItemAndUsagesDetailsViewModel(
                this.createSavedStateHandle(),
                preteirbApplication().container.itemsRepository
            )
        }
    }
}

fun CreationExtras.preteirbApplication(): PreteirbApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PreteirbApplication)