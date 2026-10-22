package com.yharim.tomaawita.logic

import java.time.DayOfWeek
import java.time.LocalDate

/**
 * Lógica pura de validación y sanitización de Objetivos de agua.
 */
object GoalLogic {

    /** Garantiza un nombre por defecto si el campo está en blanco. */
    fun sanitizeGoalName(name: String): String {
        return name.ifBlank { "Meta de agua" }
    }

    /** Garantiza que la meta diaria respete el mínimo sugerido de 2,000 ml. */
    fun sanitizeDailyGoal(dailyGoalMl: Int): Int {
        return dailyGoalMl.coerceAtLeast(2000)
    }

    /** Determina si un objetivo aplica para el día actual. */
    fun isGoalActiveToday(
        selectedDays: Set<DayOfWeek>,
        today: DayOfWeek = LocalDate.now().dayOfWeek
    ): Boolean {
        return today in selectedDays
    }
}
