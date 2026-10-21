package com.yharim.tomaawita.ui.reminder

import java.time.DayOfWeek
import java.time.LocalTime

/**
 * CONTRATO entre ReminderScreen y ReminderViewModel.
 * [time] es la hora de la primera toma; desde ahí se repite cada [intervalMinutes].
 */
data class ReminderUiState(
    val time: LocalTime = LocalTime.of(12, 0),
    val intervalMinutes: Int = 60,
    val selectedDays: Set<DayOfWeek> = setOf(
        DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
    ),
    val suggestedAmountMl: Int = 250,
    val notificationsEnabled: Boolean = true,
    val minutesUntilNext: Int = 0,
    val showTimePicker: Boolean = false
) {
    /** Hora en formato de 12 horas (1 a 12). */
    val hour12: Int get() = if (time.hour % 12 == 0) 12 else time.hour % 12
    val isPm: Boolean get() = time.hour >= 12
    val canSave: Boolean get() = selectedDays.isNotEmpty()
}