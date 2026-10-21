package com.yharim.tomaawita.ui.reminder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yharim.tomaawita.R
import com.yharim.tomaawita.ui.theme.TomaAwitaTheme
import java.time.DayOfWeek
import java.time.LocalTime

/** Todas las acciones que la pantalla puede disparar (por defecto no hacen nada, útil para previews). */
data class ReminderActions(
    val onBack: () -> Unit = {},
    val onSave: () -> Unit = {},
    val onTimeClick: () -> Unit = {},
    val onTimePicked: (Int, Int) -> Unit = { _, _ -> },
    val onTimePickerDismiss: () -> Unit = {},
    val onPeriodSelected: (Boolean) -> Unit = {},
    val onIntervalSelected: (Int) -> Unit = {},
    val onDayToggled: (DayOfWeek) -> Unit = {},
    val onAmountSelected: (Int) -> Unit = {},
    val onNotificationsToggled: (Boolean) -> Unit = {}
)

/** Punto de entrada: conecta el ViewModel con la pantalla. [onClose] cierra la pantalla. */
@Composable
fun ReminderScreen(
    viewModel: ReminderViewModel,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val actions = remember(viewModel, onClose) {
        ReminderActions(
            onBack = onClose,
            onSave = { viewModel.onSaveClicked(onClose) },
            onTimeClick = viewModel::onTimeClicked,
            onTimePicked = viewModel::onTimePicked,
            onTimePickerDismiss = viewModel::onTimePickerDismissed,
            onPeriodSelected = viewModel::onPeriodSelected,
            onIntervalSelected = viewModel::onIntervalSelected,
            onDayToggled = viewModel::onDayToggled,
            onAmountSelected = viewModel::onAmountSelected,
            onNotificationsToggled = viewModel::onNotificationsToggled
        )
    }
    ReminderContent(state, actions, modifier)
}

@Composable
fun ReminderContent(state: ReminderUiState, actions: ReminderActions, modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        ReminderTopBar(onBack = actions.onBack, onSave = actions.onSave, saveEnabled = state.canSave)

        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            TimeCard(state, onTimeClick = actions.onTimeClick, onPeriodSelected = actions.onPeriodSelected)
            IntervalCard(state.intervalMinutes, onSelect = actions.onIntervalSelected)
            DaysCard(state.selectedDays, onToggle = actions.onDayToggled)
            AmountCard(state.suggestedAmountMl, onSelect = actions.onAmountSelected)
            NotificationCard(state.notificationsEnabled, onToggle = actions.onNotificationsToggled)
            TipCard()
            CreateReminderButton(enabled = state.canSave, onClick = actions.onSave)
        }
    }

    if (state.showTimePicker) {
        TimePickerDialog(
            initialHour = state.time.hour,
            initialMinute = state.time.minute,
            onConfirm = actions.onTimePicked,
            onDismiss = actions.onTimePickerDismiss
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    onConfirm: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val pickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = false
    )
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(28.dp), color = Color.White) {
            Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                TimePicker(state = pickerState)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
                    TextButton(onClick = { onConfirm(pickerState.hour, pickerState.minute) }) {
                        Text(stringResource(R.string.picker_ok))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun ReminderPreview() {
    TomaAwitaTheme {
        ReminderContent(
            state = ReminderUiState(time = LocalTime.of(12, 30), minutesUntilNext = 42),
            actions = ReminderActions()
        )
    }
}