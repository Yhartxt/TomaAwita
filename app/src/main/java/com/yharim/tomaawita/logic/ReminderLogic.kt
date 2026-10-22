package com.yharim.tomaawita.logic

import java.time.Duration
import java.time.LocalTime

/**
 * Lógica pura para cálculos de horarios e intervalos de recordatorios.
 */
object ReminderLogic {

    /** Calcula los minutos desde la hora actual hasta la hora objetivo [targetTime]. */
    fun calculateMinutesUntil(targetTime: LocalTime): Int {
        val now = LocalTime.now().withSecond(0).withNano(0)
        var diff = Duration.between(now, targetTime).toMinutes()
        if (diff < 0) diff += 24 * 60
        return diff.toInt()
    }

    /** Retorna la hora actual redondeada al siguiente cuarto de hora (15 minutos). */
    fun getNextQuarterHourTime(): LocalTime {
        val now = LocalTime.now().withSecond(0).withNano(0)
        return now.plusMinutes((15 - now.minute % 15).toLong())
    }

    /** Ajusta la hora actual cambiando entre el período AM y PM. */
    fun adjustTimePeriod(currentTime: LocalTime, isPm: Boolean): LocalTime {
        val hour12 = currentTime.hour % 12
        return currentTime.withHour(hour12 + if (isPm) 12 else 0)
    }

    /** Calcula la hora de fin sumando la ventana predeterminada de 12 horas. */
    fun calculateEndTime(startTime: LocalTime): LocalTime {
        return startTime.plusHours(12)
    }
}
