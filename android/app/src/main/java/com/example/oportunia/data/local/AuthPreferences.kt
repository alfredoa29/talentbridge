package com.example.oportunia.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "auth_preferences")

@Singleton
class AuthPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    private object PreferencesKeys {
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
        val USERNAME = stringPreferencesKey("username")
        val USER_ROLE = stringPreferencesKey("user_role")
        val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
    }

    suspend fun saveAuthToken(token: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.AUTH_TOKEN] = token
        }
    }

    suspend fun saveUsername(username: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USERNAME] = username
        }
    }

    suspend fun getAuthToken() : String? {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.AUTH_TOKEN]
        }.first()
    }

    suspend fun getUsername() : String? {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.USERNAME]
        }.first()
    }

    suspend fun clearAuthData() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.AUTH_TOKEN)
            preferences.remove(PreferencesKeys.USERNAME)
            preferences.remove(PreferencesKeys.USER_ROLE)
          //  preferences.remove(PreferencesKeys.BIOMETRIC_ENABLED) // ← opcional para limpiar tambien que guarde la  huella
        }
    }

    suspend fun saveUserRole(role: String) {
        dataStore.edit { prefs ->
            prefs[PreferencesKeys.USER_ROLE] = role
        }
    }

    suspend fun getUserRole(): String? {
        return dataStore.data.map { prefs ->
            prefs[PreferencesKeys.USER_ROLE]
        }.first()
    }

    suspend fun setBiometricEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[PreferencesKeys.BIOMETRIC_ENABLED] = enabled
        }
    }

    suspend fun isBiometricEnabled(): Boolean {
        return dataStore.data
            .map { prefs -> prefs[PreferencesKeys.BIOMETRIC_ENABLED] ?: false }
            .first()
    }

}