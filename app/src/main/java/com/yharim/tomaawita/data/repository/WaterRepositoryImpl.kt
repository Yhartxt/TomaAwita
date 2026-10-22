package com.yharim.tomaawita.data.repository

import com.yharim.tomaawita.data.local.database.WaterIntakeDao
import com.yharim.tomaawita.data.local.database.entity.WaterIntakeEntity
import com.yharim.tomaawita.data.model.WaterIntake
import java.time.LocalDate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class WaterRepositoryImpl(
    private val dao: WaterIntakeDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : WaterRepository {

    override fun getTotalConsumedForDate(date: LocalDate): Flow<Int> {
        return dao.getTotalConsumedForDate(date.toString())
            .map { it ?: 0 }
            .flowOn(ioDispatcher)
    }

    override fun getIntakesForDate(date: LocalDate): Flow<List<WaterIntake>> {
        return dao.getIntakesForDate(date.toString())
            .map { list -> list.map { it.toDomain() } }
            .flowOn(ioDispatcher)
    }

    override fun getWeeklyIntakes(startDate: LocalDate, endDate: LocalDate): Flow<List<WaterIntake>> {
        return dao.getIntakesBetweenDates(startDate.toString(), endDate.toString())
            .map { list -> list.map { it.toDomain() } }
            .flowOn(ioDispatcher)
    }

    override suspend fun recordWaterIntake(amountMl: Int, date: LocalDate): Long {
        return withContext(ioDispatcher) {
            val entity = WaterIntakeEntity(
                amountMl = amountMl,
                date = date.toString(),
                timestamp = System.currentTimeMillis()
            )
            dao.insert(entity)
        }
    }

    override suspend fun deleteIntake(id: Long) {
        withContext(ioDispatcher) {
            dao.deleteById(id)
        }
    }
}
