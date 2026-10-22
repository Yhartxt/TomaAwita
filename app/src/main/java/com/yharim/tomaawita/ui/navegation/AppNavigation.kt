package com.yharim.tomaawita.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yharim.tomaawita.data.di.DefaultAppContainer
import com.yharim.tomaawita.ui.AppViewModelFactory
import com.yharim.tomaawita.ui.components.TomaAwitaBottomBar
import com.yharim.tomaawita.ui.goals.GoalsScreen
import com.yharim.tomaawita.ui.home.HomeScreen
import com.yharim.tomaawita.ui.reminder.ReminderScreen
import com.yharim.tomaawita.ui.reminder.ReminderViewModel

@Composable
fun AppNavigation() {
    var tab by rememberSaveable { mutableStateOf(AppTab.Home) }
    var showAddReminder by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current.applicationContext
    val factory = remember(context) { AppViewModelFactory(DefaultAppContainer(context)) }

    val reminderViewModel: ReminderViewModel = viewModel(factory = factory)

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
                AppTab.Home -> HomeScreen(
                    factory = factory,
                    modifier = Modifier.padding(innerPadding)
                )
                AppTab.Goals -> GoalsScreen(
                    factory = factory,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
