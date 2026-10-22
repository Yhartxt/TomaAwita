package com.yharim.tomaawita.ui.goals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yharim.tomaawita.data.di.DefaultAppContainer
import com.yharim.tomaawita.ui.AppViewModelFactory
import com.yharim.tomaawita.ui.theme.TomaAwitaTheme
import java.time.DayOfWeek

/**
 * Punto de entrada: conecta el ViewModel con la pantalla de Objetivos.
 */
@Composable
fun GoalsScreen(
    modifier: Modifier = Modifier,
    factory: ViewModelProvider.Factory = run {
        val context = LocalContext.current.applicationContext
        remember(context) { AppViewModelFactory(DefaultAppContainer(context)) }
    },
    viewModel: GoalsViewModel = viewModel(factory = factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    GoalsContent(
        state = state,
        onAddGoal = viewModel::onAddGoalClicked,
        onEditGoal = viewModel::onEditGoalClicked,
        modifier = modifier
    )

    if (state.showAddGoalSheet) {
        AddGoalSheet(
            goal = state.editingGoal,
            onSave = viewModel::onSaveGoal,
            onDismiss = viewModel::onDismissSheet
        )
    }
}

/**
 * Pantalla sin estado propio: recibe datos y callbacks, permitiendo previsualizaciones.
 */
@Composable
fun GoalsContent(
    state: GoalsUiState,
    onAddGoal: () -> Unit,
    onEditGoal: (GoalItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        GoalsHeader()
        Spacer(Modifier.height(24.dp))

        GoalsBannerCard(activeCount = state.activeGoalsCount)
        Spacer(Modifier.height(28.dp))

        // Encabezado con botón pequeño "+ Agregar"
        GoalsSectionHeader(onAddClick = onAddGoal)
        Spacer(Modifier.height(16.dp))

        // Lista de tarjetas de objetivos
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            state.goals.forEach { goal ->
                GoalCard(goal = goal, onClick = { onEditGoal(goal) })
            }
        }

        Spacer(Modifier.height(20.dp))

        // Botón grande punteado "+ Agregar objetivo"
        AddGoalDashedButton(onClick = onAddGoal)
        Spacer(Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915, backgroundColor = 0xFFF5FBFA)
@Composable
private fun GoalsPreview() {
    TomaAwitaTheme {
        GoalsContent(
            state = GoalsUiState(
                goals = listOf(
                    GoalItem(
                        name = "Hidratación diaria",
                        dailyGoalMl = 2500,
                        selectedDays = DayOfWeek.values().toSet(),
                        isActive = true,
                        todayProgressPercent = 58
                    ),
                    GoalItem(
                        name = "Días de oficina",
                        dailyGoalMl = 2000,
                        selectedDays = setOf(
                            DayOfWeek.MONDAY,
                            DayOfWeek.TUESDAY,
                            DayOfWeek.WEDNESDAY,
                            DayOfWeek.THURSDAY,
                            DayOfWeek.FRIDAY
                        ),
                        isActive = true,
                        todayProgressPercent = 73
                    )
                )
            ),
            onAddGoal = {},
            onEditGoal = {}
        )
    }
}
