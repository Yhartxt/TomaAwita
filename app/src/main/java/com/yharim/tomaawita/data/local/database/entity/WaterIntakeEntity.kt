package com.yharim.tomaawita.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yharim.tomaawita.data.model.WaterIntake
import java.time.LocalDate

/**
 * Entidad SQLite administrada por Room para persistir cada toma de agua individual.
 * Cumple con RNF 2 (Persistencia de datos local sin conexión).
 */
@Entity(tableName = "water_intakes")
data class WaterIntakeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amountMl: Int,
    val date: String, // Formato "YYYY-MM-DD" para agrupar por día rápidamente
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toDomain(): WaterIntake = WaterIntake(
        id = id,
        amountMl = amountMl,
        date = try { LocalDate.parse(date) } catch (e: Exception) { LocalDate.now() },
        timestamp = timestamp
    )

    companion object {
        fun fromDomain(intake: WaterIntake): WaterIntakeEntity = WaterIntakeEntity(
            id = intake.id,
            amountMl = intake.amountMl,
            date = intake.date.toString(),
            timestamp = intake.timestamp
        )
    }
}
