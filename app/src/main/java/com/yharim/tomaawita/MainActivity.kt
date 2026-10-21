package com.yharim.tomaawita

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.yharim.tomaawita.ui.navigation.AppNavigation
import com.yharim.tomaawita.ui.theme.TomaAwitaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TomaAwitaTheme {
                AppNavigation()
            }
        }
    }
}