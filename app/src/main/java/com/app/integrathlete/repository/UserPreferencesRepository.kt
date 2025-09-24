package com.app.integrathlete.repository

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import com.app.integrathlete.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.InputStream
import java.io.OutputStream

object UserPreferencesSerializer : Serializer<UserPreferences> {
    override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserPreferences {
        try {
            return UserPreferences.parseFrom(input)
        } catch (exception: Exception) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) = t.writeTo(output)
}

class UserPreferencesRepository(context: Context) {


    private val dataStore = context.userPreferencesDataStore

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is CorruptionException) {
                emit(UserPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }

    suspend fun updateUserPreferences(
        sport: String,
        frequency: Int,
        weightKg: Float,
        heightCm: Int
    ) {
        dataStore.updateData { currentPrefs ->
            currentPrefs.toBuilder()
                .setSport(sport)
                .setFrequency(frequency)
                .setWeightKg(weightKg)
                .setHeightCm(heightCm)
                .build()
        }
    }
}
val Context.userPreferencesDataStore: DataStore<UserPreferences> by dataStore(
    fileName = "user_prefs.pb",
    serializer = UserPreferencesSerializer,
    corruptionHandler = ReplaceFileCorruptionHandler { UserPreferences.getDefaultInstance() }
)
