package com.yharim.tomaawita.ui.goals

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yharim.tomaawita.R
import com.yharim.tomaawita.ui.theme.AppBackground
import com.yharim.tomaawita.ui.theme.AppOutline
import com.yharim.tomaawita.ui.theme.DayEmpty
import com.yharim.tomaawita.ui.theme.Ink
import com.yharim.tomaawita.ui.theme.Muted
import com.yharim.tomaawita.ui.theme.Teal100
import com.yharim.tomaawita.ui.theme.Teal50
import com.yharim.tomaawita.ui.theme.Teal500
import com.yharim.tomaawita.ui.theme.Teal700
import java.time.DayOfWeek
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGoalSheet(
    goal: GoalItem?,
    onSave: (id: String?, name: String, dailyGoalMl: Int, days: Set<DayOfWeek>) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    var name by remember(goal) { mutableStateOf(goal?.name ?: "") }
    var dailyGoalMl by remember(goal) { mutableIntStateOf(goal?.dailyGoalMl ?: 2000) }
    var selectedDays by remember(goal) {
        mutableStateOf(
            goal?.selectedDays ?: setOf(
                DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
            )
        )
    }

    val closeThen: (() -> Unit) -> Unit = { after ->
        scope.launch { sheetState.hide() }.invokeOnCompletion { after() }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AppBackground,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = { BottomSheetDefaults.DragHandle(color = Teal100) }
    ) {
        Column(
            Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
        ) {
            // Encabezado
            Row(verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f)) {
                    Text(
                        stringResource(R.string.sheet_goal_category),
                        style = MaterialTheme.typography.labelSmall,
                        color = Teal500
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        stringResource(R.string.sheet_goal_title),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                }
                Box(
                    Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Teal50)
                        .clickable { closeThen(onDismiss) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = stringResource(R.string.close),
                        tint = Teal700
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Campo Nombre del objetivo
            Text(
                stringResource(R.string.sheet_goal_name_label),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Ink
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text(stringResource(R.string.sheet_goal_name_hint), color = Muted) },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Teal500,
                    unfocusedBorderColor = AppOutline,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Chips de sugerencia de nombre
            Spacer(Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Oficina", "Entrenamiento", "Fin de semana", "General").forEach { suggestion ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (name == suggestion) Teal100 else Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, AppOutline),
                        modifier = Modifier.clickable { name = suggestion }
                    ) {
                        Text(
                            text = suggestion,
                            fontSize = 12.sp,
                            color = if (name == suggestion) Teal700 else Muted,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Selector de Meta diaria con Stepper y Slider (Mínimo 2000 ml)
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        stringResource(R.string.sheet_goal_amount_label),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Ink
                    )
                    Text(
                        stringResource(R.string.sheet_goal_min_warning),
                        fontSize = 12.sp,
                        color = Muted
                    )
                }
                Text(
                    text = "${formatMl(dailyGoalMl)} ml",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Teal700
                )
            }

            Spacer(Modifier.height(14.dp))

            // Botones - / + y Slider
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Botón -
                Box(
                    Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .border(1.dp, AppOutline, RoundedCornerShape(14.dp))
                        .clickable { dailyGoalMl = (dailyGoalMl - 100).coerceAtLeast(2000) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Remove, contentDescription = null, tint = Ink)
                }

                Slider(
                    value = dailyGoalMl.toFloat(),
                    onValueChange = { dailyGoalMl = (it / 100).roundToInt() * 100 },
                    valueRange = 2000f..5000f,
                    steps = 29, // Cada 100 ml entre 2000 y 5000
                    colors = SliderDefaults.colors(
                        thumbColor = Teal500,
                        activeTrackColor = Teal500,
                        inactiveTrackColor = Teal100
                    ),
                    modifier = Modifier.weight(1f)
                )

                // Botón +
                Box(
                    Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .border(1.dp, AppOutline, RoundedCornerShape(14.dp))
                        .clickable { dailyGoalMl = (dailyGoalMl + 100).coerceAtMost(5000) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null, tint = Ink)
                }
            }

            Spacer(Modifier.height(24.dp))

            // Selector de Días de la semana
            Text(
                stringResource(R.string.sheet_goal_days_label),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Ink
            )
            Spacer(Modifier.height(12.dp))
            val dayLetters = stringArrayResource(R.array.week_days_short)
            val allDays = DayOfWeek.values()
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                allDays.forEachIndexed { index, day ->
                    val isSelected = day in selectedDays
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Teal500 else DayEmpty)
                            .clickable {
                                selectedDays = if (isSelected) {
                                    if (selectedDays.size > 1) selectedDays - day else selectedDays
                                } else {
                                    selectedDays + day
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dayLetters.getOrElse(index) { "" },
                            color = if (isSelected) Color.White else Muted,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Tarjeta de Resumen Semanal Calculada (RF 3)
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Teal50,
                border = androidx.compose.foundation.BorderStroke(1.dp, Teal100),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.WaterDrop,
                        contentDescription = null,
                        tint = Teal500,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            stringResource(R.string.sheet_goal_weekly_summary),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Teal700
                        )
                        Spacer(Modifier.height(2.dp))
                        val weeklyMl = dailyGoalMl * selectedDays.size
                        Text(
                            stringResource(
                                R.string.sheet_goal_weekly_detail,
                                "${formatMl(weeklyMl)} ml",
                                selectedDays.size
                            ),
                            fontSize = 14.sp,
                            color = Ink,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Botón Guardar
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Teal500,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable {
                        closeThen {
                            onSave(goal?.id, name, dailyGoalMl, selectedDays)
                        }
                    }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Text(
                            text = stringResource(R.string.sheet_goal_save),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}
