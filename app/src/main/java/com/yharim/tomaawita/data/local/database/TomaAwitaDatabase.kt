package com.yharim.tomaawita.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yharim.tomaawita.data.local.database.entity.WaterIntakeEntity

@Database(entities = [WaterIntakeEntity::class], version = 1, exportSchema = false)
abstract class TomaAwitaDatabase : RoomDatabase() {

    abstract fun waterIntakeDao(): WaterIntakeDao

    companion object {
        @Volatile
        private var INSTANCE: TomaAwitaDatabase? = null

        fun getDatabase(context: Context): TomaAwitaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TomaAwitaDatabase::class.java,
                    "tomaawita_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
