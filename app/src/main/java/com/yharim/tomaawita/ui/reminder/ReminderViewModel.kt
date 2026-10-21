package com.yharim.tomaawita.ui.reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yharim.tomaawita.data.repository.SettingsRepository
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Gestiona el estado de la pantalla de configuración de recordatorio.
 * Conectado a SettingsRepository para persistir las opciones en DataStore.
 */
class ReminderViewModel(
    private val settingsRepository: SettingsRepository? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState())
    val uiState: StateFlow<ReminderUiState> = _uiState.asStateFlow()

    init {
        // Cargar ajustes guardados si el repositorio está disponible
        settingsRepository?.let { repo ->
            viewModelScope.launch {
                repo.userSettings.collect { settings ->
                    _uiState.update { current ->
                        current.copy(
                            intervalMinutes = settings.reminderIntervalMinutes,
                            notificationsEnabled = settings.notificationsEnabled,
                            time = settings.notificationStartTime
                        )
                    }
                }
            }
        }
    }

    fun onTimeClicked() = _uiState.update { it.copy(showTimePicker = true) }

    fun onTimePickerDismissed() = _uiState.update { it.copy(showTimePicker = false) }

    fun onTimePicked(hour: Int, minute: Int) {
        val time = LocalTime.of(hour, minute)
        _uiState.update { it.copy(time = time, minutesUntilNext = minutesUntil(time), showTimePicker = false) }
    }

    fun onPeriodSelected(isPm: Boolean) {
        val current = _uiState.value.time
        val time = current.withHour(current.hour % 12 + if (isPm) 12 else 0)
        _uiState.update { it.copy(time = time, minutesUntilNext = minutesUntil(time)) }
    }

    fun onIntervalSelected(minutes: Int) = _uiState.update { it.copy(intervalMinutes = minutes) }

    fun onDayToggled(day: DayOfWeek) = _uiState.update {
        val days = if (day in it.selectedDays) it.selectedDays - day else it.selectedDays + day
        it.copy(selectedDays = days)
    }

    fun onAmountSelected(ml: Int) = _uiState.update { it.copy(suggestedAmountMl = ml) }

    fun onNotificationsToggled(enabled: Boolean) = _uiState.update { it.copy(notificationsEnabled = enabled) }

    fun onSaveClicked(onSaved: () -> Unit) {
        val state = _uiState.value
        if (!state.canSave) return
        settingsRepository?.let { repo ->
            viewModelScope.launch {
                repo.setReminderInterval(state.intervalMinutes)
                repo.setNotificationsEnabled(state.notificationsEnabled)
                repo.setNotificationSchedule(state.time, state.time.plusHours(12))
            }
        }
        onSaved()
    }

    /** Deja la pantalla como nueva (se llama al cerrarla). */
    fun reset() {
        _uiState.value = initialState()
    }

    private fun initialState(): ReminderUiState {
        val now = LocalTime.now().withSecond(0).withNano(0)
        val rounded = now.plusMinutes((15 - now.minute % 15).toLong()) // siguiente cuarto de hora
        return ReminderUiState(time = rounded, minutesUntilNext = minutesUntil(rounded))
    }

    /** Minutos desde ahora hasta [target] (si ya pasó hoy, cuenta hasta mañana). */
    private fun minutesUntil(target: LocalTime): Int {
        val now = LocalTime.now().withSecond(0).withNano(0)
        var diff = Duration.between(now, target).toMinutes()
        if (diff < 0) diff += 24 * 60
        return diff.toInt()
    }

    companion object {
        fun provideFactory(
            settingsRepository: SettingsRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ReminderViewModel(settingsRepository) as T
            }
        }
    }
}
