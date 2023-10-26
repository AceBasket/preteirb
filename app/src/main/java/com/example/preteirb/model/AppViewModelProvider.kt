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
        
        // Initializer for ChooseProfileViewModel
        initializer {
            ChooseProfileViewModel(
                preteirbApplication().container.usersRepository
            )
        }
        
        // Initializer for UsageEntryViewModel
        initializer {
            UsageEntryViewModel(
                preteirbApplication().container.usagesRepository
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
                preteirbApplication().container.itemsRepository
            )
        }
        
        // Initializer for ItemSelectionViewModel
        initializer {
            ItemSelectionViewModel(
                preteirbApplication().container.itemsRepository
            )
        }
    }
}

fun CreationExtras.preteirbApplication(): PreteirbApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PreteirbApplication)