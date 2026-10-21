package com.yharim.tomaawita.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yharim.tomaawita.ui.components.TomaAwitaBottomBar
import com.yharim.tomaawita.ui.home.HomeScreen

// Navegación simple por estado. Cuando haya más pantallas se cambia por NavHost.
@Composable
fun AppNavigation() {
    var tab by rememberSaveable { mutableStateOf(AppTab.Home) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            TomaAwitaBottomBar(
                selected = tab,
                onSelect = { tab = it },
                onAddClick = { /* TODO: abrir el selector de cantidad */ }
            )
        }
    ) { innerPadding ->
        when (tab) {
            AppTab.Home -> HomeScreen(modifier = Modifier.padding(innerPadding))
            AppTab.Goals -> Box(
                Modifier.padding(innerPadding).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { Text("Objetivos (pendiente)") }
        }
    }
}