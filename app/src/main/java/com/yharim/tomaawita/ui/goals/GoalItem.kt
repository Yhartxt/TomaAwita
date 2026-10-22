package com.yharim.tomaawita.ui.goals

import java.time.DayOfWeek
import java.util.UUID

/**
 * Representa un objetivo semanal de consumo de agua según los días configurados.
 * Cumple con el RF 3 (Meta semanal calculada por días).
 */
data class GoalItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val dailyGoalMl: Int = 2000,
    val selectedDays: Set<DayOfWeek> = setOf(
        DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
    ),
    val isActive: Boolean = true,
    val todayProgressPercent: Int = 0
) {
    /** Meta semanal total acumulada según los días activos (RF 3). */
    val weeklyGoalMl: Int
        get() = dailyGoalMl * selectedDays.size

    val isEveryday: Boolean
        get() = selectedDays.size == 7
}
