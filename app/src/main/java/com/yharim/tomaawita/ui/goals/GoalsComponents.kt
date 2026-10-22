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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yharim.tomaawita.R
import com.yharim.tomaawita.ui.components.dashedBorder
import com.yharim.tomaawita.ui.theme.AppOutline
import com.yharim.tomaawita.ui.theme.DayEmpty
import com.yharim.tomaawita.ui.theme.Ink
import com.yharim.tomaawita.ui.theme.Muted
import com.yharim.tomaawita.ui.theme.Teal100
import com.yharim.tomaawita.ui.theme.Teal50
import com.yharim.tomaawita.ui.theme.Teal500
import com.yharim.tomaawita.ui.theme.Teal700
import java.time.DayOfWeek
import com.yharim.tomaawita.logic.FormatLogic

internal fun formatMl(ml: Int): String = FormatLogic.formatMl(ml)

// ---------- Encabezado ----------

@Composable
fun GoalsHeader() {
    Column {
        Text(
            text = stringResource(R.string.goals_category),
            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.5.sp),
            color = Teal500
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.goals_title),
            style = MaterialTheme.typography.headlineLarge,
            color = Ink
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.goals_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = Muted
        )
    }
}

// ---------- Tarjeta Resumen / Banner ----------

@Composable
fun GoalsBannerCard(activeCount: Int, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Teal700,
        shadowElevation = 8.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Teal100,
                modifier = Modifier.size(52.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.TrackChanges,
                        contentDescription = null,
                        tint = Teal700,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Spacer(Modifier.width(14.dp))
            Column {
                Text(
                    text = activeCount.toString(),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = stringResource(R.string.goals_active_count_label),
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
            Spacer(Modifier.width(16.dp))
            Box(
                Modifier
                    .width(1.dp)
                    .height(40.dp)
                    .background(Color.White.copy(alpha = 0.25f))
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = stringResource(R.string.goals_banner_tip),
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 13.sp,
                lineHeight = 18.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// ---------- Encabezado de Sección con botón + Agregar ----------

@Composable
fun GoalsSectionHeader(onAddClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.my_goals_title),
                style = MaterialTheme.typography.titleLarge,
                color = Ink
            )
            Text(
                text = stringResource(R.string.my_goals_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = Muted
            )
        }
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = Teal50,
            border = androidx.compose.foundation.BorderStroke(1.dp, Teal100)
        ) {
            Row(
                Modifier
                    .clickable(onClick = onAddClick)
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = Teal700,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = stringResource(R.string.add_goal_action),
                    color = Teal700,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

// ---------- Tarjeta individual de objetivo ----------

@Composable
fun GoalCard(
    goal: GoalItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dayLetters = stringArrayResource(R.array.week_days_short)
    val context = LocalContext.current
    val shape = RoundedCornerShape(26.dp)

    Surface(
        shape = shape,
        color = Color.White,
        shadowElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, AppOutline),
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(onClick = onClick)
    ) {
        Column(Modifier.padding(18.dp)) {
            // Fila superior: Icono, Nombre/Meta y Chip de Estado
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Teal50,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Teal100),
                    modifier = Modifier.size(46.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.WaterDrop,
                            contentDescription = null,
                            tint = Teal500,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        text = goal.name,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                    Spacer(Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${formatMl(goal.dailyGoalMl)} ml",
                            fontWeight = FontWeight.Bold,
                            color = Teal700,
                            fontSize = 14.sp
                        )
                        Text(
                            text = " ${stringResource(R.string.per_day)}",
                            color = Muted,
                            fontSize = 13.sp
                        )
                    }
                }
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (goal.isActive) Teal50 else Color(0xFFF1F5F5)
                ) {
                    Text(
                        text = if (goal.isActive) stringResource(R.string.goal_status_active)
                        else stringResource(R.string.goal_status_inactive),
                        color = if (goal.isActive) Teal700 else Muted,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Fila de días L M M J V S D
            val weekDays = DayOfWeek.values()
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                weekDays.forEachIndexed { index, day ->
                    val isSelected = day in goal.selectedDays
                    val activeBg = if (isSelected && goal.isActive) Teal500 else DayEmpty
                    val textColor = if (isSelected && goal.isActive) Color.White else Muted

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(activeBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dayLetters.getOrElse(index) { "" },
                            color = textColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Fila inferior: Días por semana + Total semanal (RF 3) y % progreso hoy
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                        tint = Muted,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    val daysLabel = if (goal.isEveryday) {
                        stringResource(R.string.all_days)
                    } else {
                        context.resources.getQuantityString(
                            R.plurals.days_per_week,
                            goal.selectedDays.size,
                            goal.selectedDays.size
                        )
                    }
                    Text(
                        text = "$daysLabel · ",
                        color = Muted,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    // Visualización clara de la meta semanal calculada (RF 3)
                    Text(
                        text = "${formatMl(goal.weeklyGoalMl)} ml${stringResource(R.string.per_week_suffix)}",
                        color = Teal700,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    text = stringResource(R.string.today_progress_badge, goal.todayProgressPercent),
                    color = Teal700,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

// ---------- Botón con borde punteado + Agregar objetivo ----------

@Composable
fun AddGoalDashedButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(22.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Teal50)
            .clickable(onClick = onClick)
            .dashedBorder(color = Color(0xFFA9DCD5), cornerRadius = 22.dp)
            .padding(vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
            tint = Teal700,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.add_goal_full),
            color = Teal700,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}
