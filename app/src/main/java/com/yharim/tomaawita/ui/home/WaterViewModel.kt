package com.yharim.tomaawita.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yharim.tomaawita.data.repository.SettingsRepository
import com.yharim.tomaawita.data.repository.WaterRepository
import com.yharim.tomaawita.logic.HydrationLogic
import java.time.LocalDate
import java.time.LocalTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla principal (Home).
 * Conectado a los repositorios y delegando toda la lógica de cálculo a [HydrationLogic].
 */
class WaterViewModel(
    private val waterRepository: WaterRepository? = null,
    private val settingsRepository: SettingsRepository? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(fakeState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeDataSources()
    }

    private fun observeDataSources() {
        if (waterRepository == null || settingsRepository == null) return

        val today = LocalDate.now()
        val (monday, sunday) = HydrationLogic.calculateWeekDateRange(today)

        // 1. Observar consumo de hoy (Room)
        viewModelScope.launch {
            waterRepository.getTotalConsumedForDate(today).collect { total ->
                _uiState.update { it.copy(consumedMl = total) }
            }
        }

        // 2. Observar ajustes (DataStore)
        viewModelScope.launch {
            settingsRepository.userSettings.collect { settings ->
                _uiState.update { current ->
                    val reminderInfo = HydrationLogic.buildReminderInfo(
                        notificationsEnabled = settings.notificationsEnabled,
                        startTime = settings.notificationStartTime,
                        intervalMinutes = settings.reminderIntervalMinutes,
                        endTime = settings.notificationEndTime
                    )
                    current.copy(
                        dailyGoalMl = settings.dailyGoalMl,
                        dailyLimitMl = settings.dailyLimitMl,
                        customAmountMl = settings.customAmountMl,
                        reminder = reminderInfo
                    )
                }
            }
        }

        // 3. Observar tomas de la semana para la racha
        viewModelScope.launch {
            waterRepository.getWeeklyIntakes(monday, sunday).collect { weeklyList ->
                val goal = _uiState.value.dailyGoalMl
                val weekMet = HydrationLogic.calculateWeekStreak(weeklyList, monday, goal)
                _uiState.update { it.copy(weekGoalMet = weekMet) }
            }
        }
    }

    // ---------- Registrar agua ----------

    fun onAddWaterClicked(amountMl: Int) {
        val state = _uiState.value
        val willExceed = HydrationLogic.willExceedDailyLimit(state.consumedMl, amountMl, state.dailyLimitMl)
        if (willExceed) {
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

    // ---------- Hoja "Agregar cantidad" (Personalizar) ----------

    fun onAddAmountSheetOpened() {
        _uiState.update { it.copy(showAddAmountSheet = true, sheetAmountMl = it.customAmountMl) }
    }

    fun onSheetAmountChanged(amountMl: Int) {
        val clamped = HydrationLogic.clampCustomAmount(amountMl)
        _uiState.update { it.copy(sheetAmountMl = clamped) }
    }

    fun onSheetDismissed() {
        _uiState.update { it.copy(showAddAmountSheet = false) }
    }

    fun onSheetConfirmed() {
        val amount = _uiState.value.sheetAmountMl
        _uiState.update { it.copy(showAddAmountSheet = false, customAmountMl = amount) }
        settingsRepository?.let { repo ->
            viewModelScope.launch { repo.setCustomAmount(amount) }
        }
        onAddWaterClicked(amount)
    }

    private fun addWater(amountMl: Int) {
        if (waterRepository != null) {
            viewModelScope.launch {
                waterRepository.recordWaterIntake(amountMl)
            }
        } else {
            _uiState.update { it.copy(consumedMl = it.consumedMl + amountMl) }
        }
    }

    private fun fakeState() = HomeUiState(
        consumedMl = 0,
        dailyGoalMl = 2000,
        dailyLimitMl = 4000,
        customAmountMl = 350,
        sheetAmountMl = 350,
        reminder = ReminderInfo(LocalTime.of(12, 30), 60, LocalTime.of(20, 0)),
        weekGoalMet = List(7) { false }
    )
}
