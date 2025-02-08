package com.ff.careai.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class UserProfile(
    val name: String = "",
    val age: Int = 0,
    val gender: String = "",
    val medicalConditions: String = "",
    val allergies: String = ""
)

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_profile")

class UserProfileManager(private val context: Context) {
    companion object {
        private val NAME = stringPreferencesKey("name")
        private val AGE = intPreferencesKey("age")
        private val GENDER = stringPreferencesKey("gender")
        private val MEDICAL_CONDITIONS = stringPreferencesKey("medical_conditions")
        private val ALLERGIES = stringPreferencesKey("allergies")
        private val IS_PROFILE_SETUP = booleanPreferencesKey("is_profile_setup")
    }

    val isProfileSetup: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[IS_PROFILE_SETUP] ?: false }

    val userProfile: Flow<UserProfile> = context.dataStore.data
        .map { preferences ->
            UserProfile(
                name = preferences[NAME] ?: "",
                age = preferences[AGE] ?: 0,
                gender = preferences[GENDER] ?: "",
                medicalConditions = preferences[MEDICAL_CONDITIONS] ?: "",
                allergies = preferences[ALLERGIES] ?: ""
            )
        }

    suspend fun saveProfile(profile: UserProfile) {
        context.dataStore.edit { preferences ->
            preferences[NAME] = profile.name
            preferences[AGE] = profile.age
            preferences[GENDER] = profile.gender
            preferences[MEDICAL_CONDITIONS] = profile.medicalConditions
            preferences[ALLERGIES] = profile.allergies
            preferences[IS_PROFILE_SETUP] = true
        }
    }
}
