package com.yharim.tomaawita.ui.home

import androidx.lifecycle.ViewModel
import java.time.LocalTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Versión con datos de prueba en memoria. Más adelante se le inyectan
 * WaterRepository y SettingsRepository y desaparece fakeState().
 */
class WaterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(fakeState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun onAddWaterClicked(amountMl: Int) {
        val state = _uiState.value
        if (state.consumedMl + amountMl > state.dailyLimitMl) {
            // No se bloquea: se pide confirmación para no falsear el registro.
            _uiState.update { it.copy(pendingLimitConfirmationMl = amountMl) }
        } else {
            addWater(amountMl)
        }
    }

    fun onCustomAmountClicked() = onAddWaterClicked(_uiState.value.customAmountMl)

    fun onLimitWarningConfirmed() {
        val amount = _uiState.value.pendingLimitConfirmationMl ?: return
        _uiState.update { it.copy(pendingLimitConfirmationMl = null) }
        addWater(amount)
    }

    fun onLimitWarningDismissed() {
        _uiState.update { it.copy(pendingLimitConfirmationMl = null) }
    }

    private fun addWater(amountMl: Int) {
        _uiState.update { it.copy(consumedMl = it.consumedMl + amountMl) }
    }

    private fun fakeState() = HomeUiState(
        userName = "Diego",
        consumedMl = 1450,
        dailyGoalMl = 2500,
        dailyLimitMl = 4000,
        customAmountMl = 350,
        reminder = ReminderInfo(LocalTime.of(12, 30), 60, LocalTime.of(20, 0)),
        weekGoalMet = listOf(true, true, true, true, false, false, false)
    )
}