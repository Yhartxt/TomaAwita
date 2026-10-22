package com.yharim.tomaawita.data.di

import android.content.Context
import com.yharim.tomaawita.data.local.database.TomaAwitaDatabase
import com.yharim.tomaawita.data.local.datastore.UserPreferencesDataStore
import com.yharim.tomaawita.data.repository.SettingsRepository
import com.yharim.tomaawita.data.repository.SettingsRepositoryImpl
import com.yharim.tomaawita.data.repository.WaterRepository
import com.yharim.tomaawita.data.repository.WaterRepositoryImpl

/**
 * Contenedor de dependencias manual (Service Locator) para entregar los repositorios a la aplicación.
 */
interface AppContainer {
    val waterRepository: WaterRepository
    val settingsRepository: SettingsRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val database by lazy {
        TomaAwitaDatabase.getDatabase(context)
    }

    private val userPreferencesDataStore by lazy {
        UserPreferencesDataStore(context)
    }

    override val waterRepository: WaterRepository by lazy {
        WaterRepositoryImpl(database.waterIntakeDao())
    }

    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepositoryImpl(userPreferencesDataStore)
    }
}
