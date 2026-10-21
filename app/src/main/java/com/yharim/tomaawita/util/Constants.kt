package com.yharim.tomaawita.util

object Constants {
    /** RF 1: intervalo entre tomas, de 15 minutos a 2 horas. */
    val IntervalOptionsMinutes = listOf(15, 30, 45, 60, 90, 120)

    /** Cantidades que se pueden elegir como "cantidad sugerida" de un recordatorio. */
    val AmountOptionsMl = listOf(100, 250, 500, 750, 1000)

    /** Hoja "Agregar cantidad": rango, paso del − / + y atajos. */
    const val CustomAmountMinMl = 100
    const val CustomAmountMaxMl = 2000
    const val CustomAmountStepMl = 50
    val CustomAmountPresetsMl = listOf(100, 250, 500, 1000, 2000)
}