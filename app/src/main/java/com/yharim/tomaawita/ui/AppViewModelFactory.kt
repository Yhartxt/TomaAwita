package com.yharim.tomaawita.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yharim.tomaawita.data.di.AppContainer
import com.yharim.tomaawita.ui.goals.GoalsViewModel
import com.yharim.tomaawita.ui.home.WaterViewModel
import com.yharim.tomaawita.ui.reminder.ReminderViewModel

/**
 * Fábrica centralizada de ViewModels (Patrón Factory).
 * Implementa [ViewModelProvider.Factory] e inyecta el [AppContainer] en los ViewModels.
 */
class AppViewModelFactory(
    private val container: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(WaterViewModel::class.java) -> {
                WaterViewModel(
                    waterRepository = container.waterRepository,
                    settingsRepository = container.settingsRepository
                ) as T
            }
            modelClass.isAssignableFrom(GoalsViewModel::class.java) -> {
                GoalsViewModel(
                    settingsRepository = container.settingsRepository
                ) as T
            }
            modelClass.isAssignableFrom(ReminderViewModel::class.java) -> {
                ReminderViewModel(
                    settingsRepository = container.settingsRepository
                ) as T
            }
            else -> throw IllegalArgumentException("Clase ViewModel no soportada: ${modelClass.name}")
        }
    }
}
