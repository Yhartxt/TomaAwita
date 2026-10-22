package com.yharim.tomaawita.data.repository

import com.yharim.tomaawita.data.local.datastore.UserPreferencesDataStore
import com.yharim.tomaawita.data.model.UserSettings
import java.time.LocalTime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class SettingsRepositoryImpl(
    private val dataStore: UserPreferencesDataStore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SettingsRepository {

    override val userSettings: Flow<UserSettings> = dataStore.userSettingsFlow.flowOn(ioDispatcher)

    override suspend fun setDailyGoal(goalMl: Int) {
        withContext(ioDispatcher) {
            dataStore.saveDailyGoal(goalMl)
        }
    }

    override suspend fun setDailyLimit(limitMl: Int) {
        withContext(ioDispatcher) {
            dataStore.saveDailyLimit(limitMl)
        }
    }

    override suspend fun setReminderInterval(minutes: Int) {
        withContext(ioDispatcher) {
            dataStore.saveReminderInterval(minutes)
        }
    }

    override suspend fun setNotificationSchedule(startTime: LocalTime, endTime: LocalTime) {
        withContext(ioDispatcher) {
            dataStore.saveNotificationSchedule(startTime, endTime)
        }
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        withContext(ioDispatcher) {
            dataStore.saveNotificationsEnabled(enabled)
        }
    }

    override suspend fun setCustomAmount(amountMl: Int) {
        withContext(ioDispatcher) {
            dataStore.saveCustomAmount(amountMl)
        }
    }
}
