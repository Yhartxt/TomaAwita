package com.yharim.tomaawita.ui.reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yharim.tomaawita.data.repository.SettingsRepository
import com.yharim.tomaawita.logic.ReminderLogic
import java.time.DayOfWeek
import java.time.LocalTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de configuración de recordatorios.
 * Delega los cálculos de horas e intervalos a [ReminderLogic].
 */
class ReminderViewModel(
    private val settingsRepository: SettingsRepository? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState())
    val uiState: StateFlow<ReminderUiState> = _uiState.asStateFlow()

    init {
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
        val remaining = ReminderLogic.calculateMinutesUntil(time)
        _uiState.update { it.copy(time = time, minutesUntilNext = remaining, showTimePicker = false) }
    }

    fun onPeriodSelected(isPm: Boolean) {
        val current = _uiState.value.time
        val newTime = ReminderLogic.adjustTimePeriod(current, isPm)
        val remaining = ReminderLogic.calculateMinutesUntil(newTime)
        _uiState.update { it.copy(time = newTime, minutesUntilNext = remaining) }
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
                val endTime = ReminderLogic.calculateEndTime(state.time)
                repo.setNotificationSchedule(state.time, endTime)
            }
        }
        onSaved()
    }

    /** Restablece la pantalla a su estado inicial. */
    fun reset() {
        _uiState.value = initialState()
    }

    private fun initialState(): ReminderUiState {
        val rounded = ReminderLogic.getNextQuarterHourTime()
        val remaining = ReminderLogic.calculateMinutesUntil(rounded)
        return ReminderUiState(time = rounded, minutesUntilNext = remaining)
    }
}
