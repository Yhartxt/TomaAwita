package com.yharim.tomaawita.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Schedule
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yharim.tomaawita.R
import com.yharim.tomaawita.ui.components.WaterGlassIcon
import com.yharim.tomaawita.ui.components.dashedBorder
import com.yharim.tomaawita.ui.theme.AppOutline
import com.yharim.tomaawita.ui.theme.DayEmpty
import com.yharim.tomaawita.ui.theme.Ink
import com.yharim.tomaawita.ui.theme.Muted
import com.yharim.tomaawita.ui.theme.Teal100
import com.yharim.tomaawita.ui.theme.Teal50
import com.yharim.tomaawita.ui.theme.Teal500
import com.yharim.tomaawita.ui.theme.Teal700
import java.time.format.DateTimeFormatter
import java.util.Locale

import com.yharim.tomaawita.logic.FormatLogic

/** 1450 -> "1,450" (como en el diseño). */
internal fun formatMl(ml: Int): String = FormatLogic.formatMl(ml)

private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.US)

// ---------- Encabezado ----------

@Composable
fun HomeHeader() {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        val shape = RoundedCornerShape(16.dp)
        Box(
            Modifier.size(52.dp).shadow(8.dp, shape, spotColor = Teal500).background(Teal500, shape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.WaterDrop, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
        }
        Text(stringResource(R.string.app_name), fontSize = 28.sp, fontWeight = FontWeight.SemiBold, color = Ink)
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null
) {
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleLarge, color = Ink)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = Muted)
        }
        action?.invoke()
    }
}

@Composable
fun CustomizeAction(onClick: () -> Unit) {
    Row(
        Modifier.clip(RoundedCornerShape(12.dp)).clickable(onClick = onClick).padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(stringResource(R.string.customize), color = Teal500, fontWeight = FontWeight.SemiBold)
        Icon(Icons.Outlined.Edit, contentDescription = null, tint = Teal500, modifier = Modifier.size(18.dp))
    }
}

// ---------- Tarjeta de progreso ----------

@Composable
fun ProgressCard(state: HomeUiState, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(32.dp),
        color = Teal700,
        shadowElevation = 10.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(stringResource(R.string.progress_today), color = Color.White.copy(alpha = 0.8f), fontSize = 16.sp)
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(formatMl(state.consumedMl), fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(
                        " ml",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.padding(start = 4.dp, bottom = 9.dp)
                    )
                }
                Text(
                    stringResource(R.string.of_goal, formatMl(state.dailyGoalMl)),
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 15.sp
                )
                Spacer(Modifier.height(16.dp))
                Surface(shape = CircleShape, color = Teal100) {
                    Row(
                        Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Outlined.TrackChanges, contentDescription = null, tint = Teal700, modifier = Modifier.size(18.dp))
                        Text(
                            stringResource(R.string.percent_of_goal, state.percent),
                            color = Teal700,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Outlined.WaterDrop, contentDescription = null, tint = Teal100, modifier = Modifier.size(36.dp))
                Text("${state.percent}%", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

// ---------- Botones rápidos ----------

@Composable
fun QuickAmountsRow(amountsMl: List<Int>, onAmountClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        amountsMl.forEach { amount ->
            QuickAmountButton(amount, onClick = { onAmountClick(amount) }, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun QuickAmountButton(amountMl: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = modifier
    ) {
        Column(
            Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            WaterGlassIcon(fill = 0.25f + 0.65f * (amountMl / 1000f).coerceAtMost(1f))
            AmountLabel(amountMl)
        }
    }
}

@Composable
private fun AmountLabel(amountMl: Int) {
    val isLiter = amountMl >= 1000 && amountMl % 1000 == 0
    Text(
        buildAnnotatedString {
            if (isLiter) {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)) { append("${amountMl / 1000} L") }
            } else {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)) { append("$amountMl") }
                withStyle(SpanStyle(fontSize = 14.sp, color = Muted)) { append(" ml") }
            }
        },
        color = Ink
    )
}

@Composable
fun CustomAmountRow(customAmountMl: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(22.dp)
    Row(
        modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Teal50)
            .clickable(onClick = onClick)
            .dashedBorder(color = Color(0xFFA9DCD5), cornerRadius = 22.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            Modifier.size(48.dp).background(Teal500, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White)
        }
        Column(Modifier.weight(1f)) {
            Text(stringResource(R.string.add_other_amount), style = MaterialTheme.typography.titleMedium, color = Ink)
            Text(
                stringResource(R.string.custom_amount_configured, customAmountMl),
                style = MaterialTheme.typography.bodyMedium,
                color = Muted
            )
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Muted)
    }
}

// ---------- Próximo recordatorio ----------

@Composable
fun ReminderCard(reminder: ReminderInfo?, onEditClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(64.dp).background(Teal100.copy(alpha = 0.6f), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Schedule, contentDescription = null, tint = Teal500, modifier = Modifier.size(30.dp))
            }
            Spacer(Modifier.size(16.dp))
            Column(Modifier.weight(1f)) {
                if (reminder == null) {
                    Text(stringResource(R.string.reminders_off), style = MaterialTheme.typography.titleMedium, color = Ink)
                } else {
                    Text(
                        stringResource(R.string.next_intake),
                        style = MaterialTheme.typography.labelSmall,
                        color = Muted
                    )
                    Text(
                        reminder.nextTime.format(timeFormatter),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                    Text(
                        stringResource(
                            R.string.reminder_detail,
                            reminder.intervalMinutes,
                            reminder.endTime.format(timeFormatter)
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = Muted
                    )
                }
            }
            Box(
                Modifier.size(44.dp).clip(CircleShape).background(Teal50).clickable(onClick = onEditClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Edit, contentDescription = null, tint = Teal500, modifier = Modifier.size(20.dp))
            }
        }
    }
}

// ---------- Semana ----------

@Composable
fun WeekCard(weekGoalMet: List<Boolean>, modifier: Modifier = Modifier) {
    val letters = stringArrayResource(R.array.week_days_short)
    val shape = RoundedCornerShape(28.dp)
    Column(
        modifier.fillMaxWidth().clip(shape).background(Color.White).border(1.dp, AppOutline, shape).padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.CalendarMonth, contentDescription = null, tint = Muted, modifier = Modifier.size(22.dp))
            Spacer(Modifier.size(8.dp))
            Text(stringResource(R.string.this_week), color = Muted, modifier = Modifier.weight(1f))
            Text(
                stringResource(R.string.days_completed, weekGoalMet.count { it }),
                color = Teal500,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth()) {
            weekGoalMet.forEachIndexed { index, met ->
                Column(
                    Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(letters.getOrElse(index) { "" }, color = Muted, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Box(
                        Modifier
                            .fillMaxWidth(0.7f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (met) Teal500 else DayEmpty),
                        contentAlignment = Alignment.Center
                    ) {
                        if (met) {
                            Icon(
                                Icons.Outlined.WaterDrop,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.fillMaxSize(0.55f)
                            )
                        }
                    }
                }
            }
        }
    }
}