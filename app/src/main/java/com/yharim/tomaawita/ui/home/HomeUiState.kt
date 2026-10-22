package com.yharim.tomaawita.ui.home

import com.yharim.tomaawita.logic.HydrationLogic
import java.time.LocalDate
import java.time.LocalTime

data class ReminderInfo(
    val nextTime: LocalTime,
    val intervalMinutes: Int,
    val endTime: LocalTime
)

/**
 * Estado inmutable de la pantalla principal (Home).
 * Contrato entre HomeScreen y WaterViewModel.
 */
data class HomeUiState(
    val date: LocalDate = LocalDate.now(),
    val consumedMl: Int = 0,
    val dailyGoalMl: Int = 2000,            // meta del día: llena la barra de progreso
    val dailyLimitMl: Int = 4000,           // tope de seguridad (RF 4)
    val quickAmountsMl: List<Int> = listOf(100, 250, 500, 1000),
    val customAmountMl: Int = 350,          // última cantidad personalizada elegida por el usuario
    val reminder: ReminderInfo? = null,
    val weekGoalMet: List<Boolean> = List(7) { false },   // lunes a domingo
    val pendingLimitConfirmationMl: Int? = null,          // si no es null, muestra el diálogo de advertencia
    val showAddAmountSheet: Boolean = false,              // visibilidad de la hoja "Agregar cantidad"
    val sheetAmountMl: Int = 350                          // cantidad seleccionada en la hoja
) {
    val progress: Float
        get() = HydrationLogic.calculateProgressPercent(consumedMl, dailyGoalMl)

    val percent: Int
        get() = (progress * 100).toInt()

    val daysCompleted: Int
        get() = weekGoalMet.count { it }
}
