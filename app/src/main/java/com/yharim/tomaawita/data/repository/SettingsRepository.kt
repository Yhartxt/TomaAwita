package com.yharim.tomaawita.data.repository

import com.yharim.tomaawita.data.model.UserSettings
import java.time.LocalTime
import kotlinx.coroutines.flow.Flow

/**
 * Contrato del Repositorio de Configuraciones del usuario.
 */
interface SettingsRepository {
    val userSettings: Flow<UserSettings>
    suspend fun setDailyGoal(goalMl: Int)
    suspend fun setDailyLimit(limitMl: Int)
    suspend fun setReminderInterval(minutes: Int)
    suspend fun setNotificationSchedule(startTime: LocalTime, endTime: LocalTime)
    suspend fun setNotificationsEnabled(enabled: Boolean)
    suspend fun setCustomAmount(amountMl: Int)
}
