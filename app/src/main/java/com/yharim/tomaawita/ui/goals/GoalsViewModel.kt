package com.yharim.tomaawita.ui.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yharim.tomaawita.data.repository.SettingsRepository
import com.yharim.tomaawita.logic.GoalLogic
import java.time.DayOfWeek
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Objetivos semanales.
 * Delega la validación de nombres y cantidades a [GoalLogic].
 */
class GoalsViewModel(
    private val settingsRepository: SettingsRepository? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(fakeInitialState())
    val uiState: StateFlow<GoalsUiState> = _uiState.asStateFlow()

    init {
        settingsRepository?.let { repo ->
            viewModelScope.launch {
                repo.userSettings.collect { settings ->
                    _uiState.update { state ->
                        val updated = state.goals.mapIndexed { index, item ->
                            if (index == 0) item.copy(dailyGoalMl = settings.dailyGoalMl) else item
                        }
                        state.copy(goals = updated)
                    }
                }
            }
        }
    }

    fun onAddGoalClicked() {
        _uiState.update { it.copy(showAddGoalSheet = true, editingGoal = null) }
    }

    fun onEditGoalClicked(goal: GoalItem) {
        _uiState.update { it.copy(showAddGoalSheet = true, editingGoal = goal) }
    }

    fun onDismissSheet() {
        _uiState.update { it.copy(showAddGoalSheet = false, editingGoal = null) }
    }

    fun onSaveGoal(
        id: String?,
        name: String,
        dailyGoalMl: Int,
        selectedDays: Set<DayOfWeek>
    ) {
        val safeDailyGoal = GoalLogic.sanitizeDailyGoal(dailyGoalMl)
        val cleanName = GoalLogic.sanitizeGoalName(name)

        _uiState.update { state ->
            val updatedList = if (id != null) {
                state.goals.map {
                    if (it.id == id) {
                        it.copy(
                            name = cleanName,
                            dailyGoalMl = safeDailyGoal,
                            selectedDays = selectedDays
                        )
                    } else it
                }
            } else {
                state.goals + GoalItem(
                    name = cleanName,
                    dailyGoalMl = safeDailyGoal,
                    selectedDays = selectedDays,
                    isActive = true,
                    todayProgressPercent = 0
                )
            }
            state.copy(goals = updatedList, showAddGoalSheet = false, editingGoal = null)
        }

        val isActiveToday = GoalLogic.isGoalActiveToday(selectedDays)
        if (isActiveToday && settingsRepository != null) {
            viewModelScope.launch {
                settingsRepository.setDailyGoal(safeDailyGoal)
            }
        }
    }

    fun onToggleGoalActive(goalId: String) {
        _uiState.update { state ->
            val updated = state.goals.map {
                if (it.id == goalId) it.copy(isActive = !it.isActive) else it
            }
            state.copy(goals = updated)
        }
    }

    private fun fakeInitialState(): GoalsUiState {
        val allDays = DayOfWeek.values().toSet()
        val officeDays = setOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
        )

        val defaultGoals = listOf(
            GoalItem(
                id = "goal_1",
                name = "Hidratación diaria",
                dailyGoalMl = 2500,
                selectedDays = allDays,
                isActive = true,
                todayProgressPercent = 58
            ),
            GoalItem(
                id = "goal_2",
                name = "Días de oficina",
                dailyGoalMl = 2000,
                selectedDays = officeDays,
                isActive = true,
                todayProgressPercent = 73
            )
        )

        return GoalsUiState(goals = defaultGoals)
    }
}
