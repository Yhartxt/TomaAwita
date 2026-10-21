package com.yharim.tomaawita.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yharim.tomaawita.TomaAwitaApplication
import com.yharim.tomaawita.ui.components.TomaAwitaBottomBar
import com.yharim.tomaawita.ui.goals.GoalsScreen
import com.yharim.tomaawita.ui.home.HomeScreen
import com.yharim.tomaawita.ui.reminder.ReminderScreen
import com.yharim.tomaawita.ui.reminder.ReminderViewModel

// Navegación simple por estado. Cuando haya más pantallas se cambia por NavHost.
@Composable
fun AppNavigation() {
    var tab by rememberSaveable { mutableStateOf(AppTab.Home) }
    var showAddReminder by rememberSaveable { mutableStateOf(false) }
    val app = LocalContext.current.applicationContext as? TomaAwitaApplication
    val reminderViewModel: ReminderViewModel = if (app != null) {
        viewModel(factory = ReminderViewModel.provideFactory(app.container.settingsRepository))
    } else {
        viewModel()
    }

    if (showAddReminder) {
        val close: () -> Unit = {
            reminderViewModel.reset()
            showAddReminder = false
        }
        BackHandler(onBack = close)
        ReminderScreen(viewModel = reminderViewModel, onClose = close)
    } else {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                TomaAwitaBottomBar(
                    selected = tab,
                    onSelect = { tab = it },
                    onAddClick = { showAddReminder = true }
                )
            }
        ) { innerPadding ->
            when (tab) {
                AppTab.Home -> HomeScreen(modifier = Modifier.padding(innerPadding))
                AppTab.Goals -> GoalsScreen(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}