package com.yharim.tomaawita.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yharim.tomaawita.R
import com.yharim.tomaawita.data.di.DefaultAppContainer
import com.yharim.tomaawita.logic.FormatLogic
import com.yharim.tomaawita.ui.AppViewModelFactory
import com.yharim.tomaawita.ui.theme.Ink
import com.yharim.tomaawita.ui.theme.Muted
import com.yharim.tomaawita.ui.theme.TomaAwitaTheme
import java.time.LocalDate
import java.time.LocalTime
import java.util.Locale

/** Punto de entrada: conecta el ViewModel con la pantalla. No tiene lógica de negocio. */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    factory: ViewModelProvider.Factory = run {
        val context = LocalContext.current.applicationContext
        remember(context) { AppViewModelFactory(DefaultAppContainer(context)) }
    },
    viewModel: WaterViewModel = viewModel(factory = factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        state = state,
        onAddWater = viewModel::onAddWaterClicked,
        onCustomAmountClick = viewModel::onCustomAmountClicked,
        onCustomizeClick = viewModel::onAddAmountSheetOpened,
        onEditReminderClick = { /* TODO: ir a los ajustes de recordatorio */ },
        modifier = modifier
    )

    if (state.showAddAmountSheet) {
        AddAmountSheet(
            amountMl = state.sheetAmountMl,
            onAmountChange = viewModel::onSheetAmountChanged,
            onConfirm = viewModel::onSheetConfirmed,
            onDismiss = viewModel::onSheetDismissed
        )
    }

    state.pendingLimitConfirmationMl?.let { amount ->
        AlertDialog(
            onDismissRequest = viewModel::onLimitWarningDismissed,
            title = { Text(stringResource(R.string.limit_dialog_title)) },
            text = { Text(stringResource(R.string.limit_dialog_message, amount, state.dailyLimitMl)) },
            confirmButton = {
                TextButton(onClick = viewModel::onLimitWarningConfirmed) { Text(stringResource(R.string.register)) }
            },
            dismissButton = {
                TextButton(onClick = viewModel::onLimitWarningDismissed) { Text(stringResource(R.string.cancel)) }
            }
        )
    }
}

/** Pantalla "sin estado propio": recibe todo por parámetros, por eso se puede previsualizar. */
@Composable
fun HomeContent(
    state: HomeUiState,
    onAddWater: (Int) -> Unit,
    onCustomAmountClick: () -> Unit,
    onCustomizeClick: () -> Unit,
    onEditReminderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val locale = remember { Locale.forLanguageTag("es-MX") }
    val dateText = remember(state.date) {
        FormatLogic.formatDateSpanish(state.date, locale)
    }

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        HomeHeader()
        Spacer(Modifier.height(28.dp))

        Text(dateText, style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.5.sp), color = Muted)
        Spacer(Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.home_greeting),
            style = MaterialTheme.typography.headlineLarge,
            color = Ink
        )
        Text(stringResource(R.string.home_subtitle), style = MaterialTheme.typography.bodyLarge, color = Muted)
        Spacer(Modifier.height(28.dp))

        ProgressCard(state)
        Spacer(Modifier.height(32.dp))

        SectionHeader(
            title = stringResource(R.string.register_water_title),
            subtitle = stringResource(R.string.register_water_subtitle),
            action = { CustomizeAction(onCustomizeClick) }
        )
        Spacer(Modifier.height(16.dp))
        QuickAmountsRow(state.quickAmountsMl, onAmountClick = onAddWater)
        Spacer(Modifier.height(14.dp))
        CustomAmountRow(state.customAmountMl, onClick = onCustomAmountClick)
        Spacer(Modifier.height(32.dp))

        SectionHeader(
            title = stringResource(R.string.next_reminder_title),
            subtitle = stringResource(R.string.next_reminder_subtitle)
        )
        Spacer(Modifier.height(16.dp))
        ReminderCard(state.reminder, onEditClick = onEditReminderClick)
        Spacer(Modifier.height(14.dp))
        WeekCard(state.weekGoalMet)
        Spacer(Modifier.height(16.dp))
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915, backgroundColor = 0xFFF5FBFA)
@Composable
private fun HomePreview() {
    TomaAwitaTheme {
        HomeContent(
            state = HomeUiState(
                date = LocalDate.of(2025, 6, 10),
                consumedMl = 1450,
                dailyGoalMl = 2500,
                reminder = ReminderInfo(LocalTime.of(12, 30), 60, LocalTime.of(20, 0)),
                weekGoalMet = listOf(true, true, true, true, false, false, false)
            ),
            onAddWater = {},
            onCustomAmountClick = {},
            onCustomizeClick = {},
            onEditReminderClick = {}
        )
    }
}
