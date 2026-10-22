package com.yharim.tomaawita.logic

import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Lógica pura de formateo numérico y de fechas.
 */
object FormatLogic {

    /** Formatea un valor en mililitros con separador de miles. Ej: 2500 -> "2,500". */
    fun formatMl(ml: Int, locale: Locale = Locale.forLanguageTag("es-MX")): String {
        return NumberFormat.getNumberInstance(locale).format(ml)
    }

    /** Formatea una fecha en texto en español. Ej: "LUNES, 10 DE JUNIO". */
    fun formatDateSpanish(date: LocalDate, locale: Locale = Locale.forLanguageTag("es-MX")): String {
        val pattern = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", locale)
        return date.format(pattern).uppercase(locale)
    }
}
