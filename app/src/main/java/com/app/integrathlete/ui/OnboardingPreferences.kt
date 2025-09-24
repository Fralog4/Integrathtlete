package com.app.integrathlete.ui

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

object OnboardingPreferences {
    private val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")

    suspend fun isCompleted(context: Context): Boolean {
        return context.dataStore.data
            .map { it[ONBOARDING_COMPLETED] ?: false }
            .first()
    }

    suspend fun setCompleted(context: Context, completed: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun reset(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.remove(ONBOARDING_COMPLETED)
        }
    }

}
