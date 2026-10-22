package com.yharim.tomaawita.data.repository

import com.yharim.tomaawita.data.model.WaterIntake
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

/**
 * Contrato del Repositorio de Agua.
 * Actúa como la Única Fuente de Verdad para las tomas de agua.
 */
interface WaterRepository {
    fun getTotalConsumedForDate(date: LocalDate): Flow<Int>
    fun getIntakesForDate(date: LocalDate): Flow<List<WaterIntake>>
    fun getWeeklyIntakes(startDate: LocalDate, endDate: LocalDate): Flow<List<WaterIntake>>
    suspend fun recordWaterIntake(amountMl: Int, date: LocalDate = LocalDate.now()): Long
    suspend fun deleteIntake(id: Long)
}
