package com.yharim.tomaawita.data.model

import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Configuraciones y preferencias del usuario persistidas en DataStore.
 * Cubre:
 * - RF 1: Intervalo de recordatorio (15 a 120 min)
 * - RF 2: Rango horario de notificaciones (inicio y fin)
 * - RF 3: Meta diaria / semanal base
 * - RF 4: Límite diario de seguridad
 */
data class UserSettings(
    val dailyGoalMl: Int = 2000,
    val dailyLimitMl: Int = 4000,
    val reminderIntervalMinutes: Int = 60,
    val notificationStartTime: LocalTime = LocalTime.of(6, 0),
    val notificationEndTime: LocalTime = LocalTime.of(20, 0),
    val notificationsEnabled: Boolean = true,
    val customAmountMl: Int = 350
) {
    companion object {
        private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        fun parseTime(timeStr: String, default: LocalTime): LocalTime = try {
            LocalTime.parse(timeStr, timeFormatter)
        } catch (e: Exception) {
            default
        }

        fun formatTime(time: LocalTime): String = time.format(timeFormatter)
    }
}
