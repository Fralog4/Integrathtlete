package com.app.integrathlete.repository

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.integrathlete.model.UserProfileViewModel

/**
 * da sostituire con una DI framework
 */
class UserProfileViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = UserPreferencesRepository(context)
        return UserProfileViewModel(repo) as T
    }
}
