package com.yharim.tomaawita.logic

import com.yharim.tomaawita.data.model.WaterIntake
import com.yharim.tomaawita.ui.home.ReminderInfo
import com.yharim.tomaawita.util.Constants
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

/**
 * Lógica pura de hidratación, cálculo de progreso, fechas semanales, rachas y recordatorios.
 */
object HydrationLogic {

    /** Retorna el rango de fechas (Lunes y Domingo) para la semana actual. */
    fun calculateWeekDateRange(today: LocalDate = LocalDate.now()): Pair<LocalDate, LocalDate> {
        val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val sunday = monday.plusDays(6)
        return Pair(monday, sunday)
    }

    /** Construye la información de recordatorio si las notificaciones están activas. */
    fun buildReminderInfo(
        notificationsEnabled: Boolean,
        startTime: LocalTime,
        intervalMinutes: Int,
        endTime: LocalTime
    ): ReminderInfo? {
        if (!notificationsEnabled) return null
        return ReminderInfo(
            nextTime = startTime,
            intervalMinutes = intervalMinutes,
            endTime = endTime
        )
    }

    /** Calcula el porcentaje de progreso (entre 0.0 y 1.0). */
    fun calculateProgressPercent(consumedMl: Int, goalMl: Int): Float {
        if (goalMl <= 0) return 0f
        return (consumedMl.toFloat() / goalMl.toFloat()).coerceIn(0f, 1f)
    }

    /** Evalúa cada día de la semana (Lunes a Domingo) para verificar si se cumplió la meta diaria. */
    fun calculateWeekStreak(
        intakes: List<WaterIntake>,
        monday: LocalDate,
        dailyGoal: Int
    ): List<Boolean> {
        return (0..6).map { offset ->
            val date = monday.plusDays(offset.toLong())
            val consumed = intakes.filter { it.date == date }.sumOf { it.amountMl }
            consumed >= dailyGoal && dailyGoal > 0
        }
    }

    /** Retorna true si agregar [amountToAddMl] sobrepasa el [dailyLimitMl]. */
    fun willExceedDailyLimit(consumedMl: Int, amountToAddMl: Int, dailyLimitMl: Int): Boolean {
        return (consumedMl + amountToAddMl) > dailyLimitMl
    }

    /** Restringe la cantidad personalizada entre los límites permitidos. */
    fun clampCustomAmount(amountMl: Int): Int {
        return amountMl.coerceIn(Constants.CustomAmountMinMl, Constants.CustomAmountMaxMl)
    }

    /** Calcula la meta semanal total en mililitros según la meta diaria y los días activos. */
    fun calculateWeeklyGoalTotal(dailyGoalMl: Int, daysCount: Int): Int {
        return dailyGoalMl * daysCount
    }
}
