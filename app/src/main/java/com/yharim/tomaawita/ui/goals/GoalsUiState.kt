package com.yharim.tomaawita.ui.goals

/**
 * CONTRATO entre GoalsScreen y GoalsViewModel.
 */
data class GoalsUiState(
    val goals: List<GoalItem> = emptyList(),
    val showAddGoalSheet: Boolean = false,
    val editingGoal: GoalItem? = null
) {
    val activeGoalsCount: Int
        get() = goals.count { it.isActive }
}
