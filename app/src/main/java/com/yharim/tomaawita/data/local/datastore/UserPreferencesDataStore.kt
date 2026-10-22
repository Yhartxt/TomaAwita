package com.yharim.tomaawita.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yharim.tomaawita.data.model.UserSettings
import java.io.IOException
import java.time.LocalTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

/**
 * Gestiona la persistencia de configuraciones de usuario en DataStore Preferences.
 */
class UserPreferencesDataStore(private val context: Context) {

    private object PreferencesKeys {
        val DAILY_GOAL_ML = intPreferencesKey("daily_goal_ml")
        val DAILY_LIMIT_ML = intPreferencesKey("daily_limit_ml")
        val REMINDER_INTERVAL_MIN = intPreferencesKey("reminder_interval_min")
        val NOTIFICATION_START_TIME = stringPreferencesKey("notif_start_time")
        val NOTIFICATION_END_TIME = stringPreferencesKey("notif_end_time")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notif_enabled")
        val CUSTOM_AMOUNT_ML = intPreferencesKey("custom_amount_ml")
    }

    val userSettingsFlow: Flow<UserSettings> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val dailyGoal = preferences[PreferencesKeys.DAILY_GOAL_ML] ?: 2000
            val dailyLimit = preferences[PreferencesKeys.DAILY_LIMIT_ML] ?: 4000
            val interval = preferences[PreferencesKeys.REMINDER_INTERVAL_MIN] ?: 60
            val startTimeStr = preferences[PreferencesKeys.NOTIFICATION_START_TIME] ?: "06:00"
            val endTimeStr = preferences[PreferencesKeys.NOTIFICATION_END_TIME] ?: "20:00"
            val enabled = preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] ?: true
            val customAmount = preferences[PreferencesKeys.CUSTOM_AMOUNT_ML] ?: 350

            UserSettings(
                dailyGoalMl = dailyGoal,
                dailyLimitMl = dailyLimit,
                reminderIntervalMinutes = interval,
                notificationStartTime = UserSettings.parseTime(startTimeStr, LocalTime.of(6, 0)),
                notificationEndTime = UserSettings.parseTime(endTimeStr, LocalTime.of(20, 0)),
                notificationsEnabled = enabled,
                customAmountMl = customAmount
            )
        }

    suspend fun saveDailyGoal(goalMl: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DAILY_GOAL_ML] = goalMl
        }
    }

    suspend fun saveDailyLimit(limitMl: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DAILY_LIMIT_ML] = limitMl
        }
    }

    suspend fun saveReminderInterval(minutes: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.REMINDER_INTERVAL_MIN] = minutes
        }
    }

    suspend fun saveNotificationSchedule(startTime: LocalTime, endTime: LocalTime) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATION_START_TIME] = UserSettings.formatTime(startTime)
            preferences[PreferencesKeys.NOTIFICATION_END_TIME] = UserSettings.formatTime(endTime)
        }
    }

    suspend fun saveNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun saveCustomAmount(amountMl: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.CUSTOM_AMOUNT_ML] = amountMl
        }
    }
}
