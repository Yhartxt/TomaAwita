package com.yharim.tomaawita.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yharim.tomaawita.data.model.WaterIntake
import com.yharim.tomaawita.data.repository.SettingsRepository
import com.yharim.tomaawita.data.repository.WaterRepository
import com.yharim.tomaawita.util.Constants
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla principal.
 * Conectado a WaterRepository (Room) y SettingsRepository (DataStore).
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
        val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val sunday = monday.plusDays(6)

        // 1. Observar consumo de hoy (Room)
        viewModelScope.launch {
            waterRepository.getTotalConsumedForDate(today).collect { total ->
                _uiState.update { it.copy(consumedMl = total) }
            }
        }

        // 2. Observar ajustes (DataStore): meta diaria, límite y recordatorio
        viewModelScope.launch {
            settingsRepository.userSettings.collect { settings ->
                _uiState.update { current ->
                    current.copy(
                        dailyGoalMl = settings.dailyGoalMl,
                        dailyLimitMl = settings.dailyLimitMl,
                        customAmountMl = settings.customAmountMl,
                        reminder = if (settings.notificationsEnabled) {
                            ReminderInfo(
                                nextTime = settings.notificationStartTime,
                                intervalMinutes = settings.reminderIntervalMinutes,
                                endTime = settings.notificationEndTime
                            )
                        } else null
                    )
                }
            }
        }

        // 3. Observar tomas de la semana actual para actualizar WeekCard
        viewModelScope.launch {
            waterRepository.getWeeklyIntakes(monday, sunday).collect { weeklyList ->
                val goal = _uiState.value.dailyGoalMl
                val weekMet = calculateWeekGoal(weeklyList, monday, goal)
                _uiState.update { it.copy(weekGoalMet = weekMet) }
            }
        }
    }

    private fun calculateWeekGoal(
        intakes: List<WaterIntake>,
        monday: LocalDate,
        dailyGoal: Int
    ): List<Boolean> {
        return (0..6).map { offset ->
            val date = monday.plusDays(offset.toLong())
            val consumed = intakes.filter { it.date == date }.sumOf { it.amountMl }
            consumed >= dailyGoal && dailyGoal > 0
        }
    }

    // ---------- Registrar agua ----------

    fun onAddWaterClicked(amountMl: Int) {
        val state = _uiState.value
        if (state.consumedMl + amountMl > state.dailyLimitMl) {
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

    // ---------- Hoja "Agregar cantidad" (botón Personalizar) ----------

    fun onAddAmountSheetOpened() {
        _uiState.update { it.copy(showAddAmountSheet = true, sheetAmountMl = it.customAmountMl) }
    }

    fun onSheetAmountChanged(amountMl: Int) {
        val clamped = amountMl.coerceIn(Constants.CustomAmountMinMl, Constants.CustomAmountMaxMl)
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

    companion object {
        fun provideFactory(
            waterRepository: WaterRepository,
            settingsRepository: SettingsRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return WaterViewModel(waterRepository, settingsRepository) as T
            }
        }
    }
}
