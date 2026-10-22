package com.yharim.tomaawita.data.model

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

/**
 * Modelo de dominio que representa una toma de agua individual.
 */
data class WaterIntake(
    val id: Long = 0,
    val amountMl: Int,
    val date: LocalDate = LocalDate.now(),
    val timestamp: Long = System.currentTimeMillis()
) {
    val time: LocalTime
        get() = Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalTime()
}
