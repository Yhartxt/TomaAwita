package com.yharim.tomaawita.ui.home

import java.time.LocalDate
import java.time.LocalTime

/** Datos del recordatorio que se muestra en la tarjeta "Próxima toma". */
data class ReminderInfo(
    val nextTime: LocalTime,
    val intervalMinutes: Int,
    val endTime: LocalTime
)

/**
 * CONTRATO entre HomeScreen y WaterViewModel.
 * Si se cambia un campo aquí, hay que avisarlo en todos los lados donde se use.
 */
data class HomeUiState(
    val date: LocalDate = LocalDate.now(),
    val consumedMl: Int = 0,
    val dailyGoalMl: Int = 2000,            // meta del día: llena la barra de progreso
    val dailyLimitMl: Int = 4000,           // tope de seguridad (RF 4)
    val quickAmountsMl: List<Int> = listOf(100, 250, 500, 1000),
    val customAmountMl: Int = 350,          // última cantidad personalizada que eligió el usuario
    val reminder: ReminderInfo? = null,
    val weekGoalMet: List<Boolean> = List(7) { false },   // lunes a domingo
    val pendingLimitConfirmationMl: Int? = null,          // si no es null, se muestra el aviso de límite
    val showAddAmountSheet: Boolean = false,              // hoja "Agregar cantidad" visible
    val sheetAmountMl: Int = 350                          // cantidad que se está eligiendo en la hoja
) {
    val progress: Float
        get() = if (dailyGoalMl <= 0) 0f else (consumedMl.toFloat() / dailyGoalMl).coerceIn(0f, 1f)

    val percent: Int
        get() = (progress * 100).toInt()

    val daysCompleted: Int
        get() = weekGoalMet.count { it }
}