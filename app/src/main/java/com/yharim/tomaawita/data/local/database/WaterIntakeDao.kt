package com.yharim.tomaawita.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yharim.tomaawita.data.local.database.entity.WaterIntakeEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) para operaciones sobre la tabla "water_intakes".
 * Las consultas reactivas retornan [Flow].
 * Las inserciones y eliminaciones se ejecutan desde el repositorio con Dispatchers.IO.
 */
@Dao
interface WaterIntakeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(intake: WaterIntakeEntity): Long

    @Query("SELECT SUM(amountMl) FROM water_intakes WHERE date = :date")
    fun getTotalConsumedForDate(date: String): Flow<Int?>

    @Query("SELECT * FROM water_intakes WHERE date = :date ORDER BY timestamp DESC")
    fun getIntakesForDate(date: String): Flow<List<WaterIntakeEntity>>

    @Query("SELECT * FROM water_intakes WHERE date BETWEEN :startDate AND :endDate ORDER BY timestamp ASC")
    fun getIntakesBetweenDates(startDate: String, endDate: String): Flow<List<WaterIntakeEntity>>

    @Query("DELETE FROM water_intakes WHERE id = :id")
    fun deleteById(id: Long): Int

    @Query("DELETE FROM water_intakes")
    fun clearAll(): Int
}
