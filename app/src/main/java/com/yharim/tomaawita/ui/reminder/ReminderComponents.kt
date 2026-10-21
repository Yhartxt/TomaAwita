package com.yharim.tomaawita.ui.reminder

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yharim.tomaawita.R
import com.yharim.tomaawita.ui.components.DropdownPill
import com.yharim.tomaawita.ui.theme.AppOutline
import com.yharim.tomaawita.ui.theme.Ink
import com.yharim.tomaawita.ui.theme.Muted
import com.yharim.tomaawita.ui.theme.Teal100
import com.yharim.tomaawita.ui.theme.Teal50
import com.yharim.tomaawita.ui.theme.Teal500
import com.yharim.tomaawita.ui.theme.Teal700
import com.yharim.tomaawita.util.Constants
import java.time.DayOfWeek
import java.util.Locale

internal fun formatAmount(ml: Int): String =
    if (ml >= 1000 && ml % 1000 == 0) "${ml / 1000} L" else "$ml ml"

internal fun formatInterval(minutes: Int): String = "$minutes min"

@Composable
private fun quantityString(id: Int, count: Int): String =
    LocalContext.current.resources.getQuantityString(id, count, count)

// ---------- Barra superior ----------

@Composable
fun ReminderTopBar(onBack: () -> Unit, onSave: () -> Unit, saveEnabled: Boolean, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth()) {
        Row(
            Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val shape = RoundedCornerShape(16.dp)
            Box(
                Modifier
                    .size(52.dp)
                    .clip(shape)
                    .background(Color.White)
                    .border(1.dp, AppOutline, shape)
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.ChevronLeft,
                    contentDescription = stringResource(R.string.back),
                    tint = Ink,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(stringResource(R.string.reminder_new), style = MaterialTheme.typography.labelSmall, color = Teal500)
                Text(stringResource(R.string.reminder_title), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Ink)
            }
            Text(
                stringResource(R.string.save),
                color = if (saveEnabled) Teal500 else Muted,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.clip(RoundedCornerShape(10.dp)).clickable(enabled = saveEnabled, onClick = onSave).padding(8.dp)
            )
        }
        Box(Modifier.fillMaxWidth().height(1.dp).background(AppOutline))
    }
}

// ---------- Tarjeta de hora ----------

@Composable
fun TimeCard(
    state: ReminderUiState,
    onTimeClick: () -> Unit,
    onPeriodSelected: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(32.dp),
        color = Teal700,
        shadowElevation = 10.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Outlined.Schedule, contentDescription = null, tint = Teal100, modifier = Modifier.size(18.dp))
                Text(
                    stringResource(R.string.reminder_time_label),
                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.5.sp),
                    color = Teal100
                )
            }
            Spacer(Modifier.height(18.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TimeBox(state.hour12.toString(), onTimeClick)
                Text(":", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Teal100)
                TimeBox(String.format(Locale.US, "%02d", state.time.minute), onTimeClick)
                Spacer(Modifier.width(2.dp))
                AmPmSelector(state.isPm, onPeriodSelected)
            }
            Spacer(Modifier.height(16.dp))
            Text(
                stringResource(R.string.next_intake_in, durationText(state.minutesUntilNext)),
                color = Teal100,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun durationText(minutes: Int): String {
    if (minutes < 60) return quantityString(R.plurals.minutes_count, minutes)
    val hours = minutes / 60
    val rest = minutes % 60
    return if (rest == 0) stringResource(R.string.duration_hours, hours)
    else stringResource(R.string.duration_hours_minutes, hours, rest)
}

@Composable
private fun TimeBox(text: String, onClick: () -> Unit) {
    val shape = RoundedCornerShape(22.dp)
    Box(
        Modifier
            .size(width = 70.dp, height = 68.dp)
            .clip(shape)
            .background(Color.White.copy(alpha = 0.12f))
            .border(1.5.dp, Color.White.copy(alpha = 0.2f), shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
private fun AmPmSelector(isPm: Boolean, onSelect: (Boolean) -> Unit) {
    Column(
        Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        AmPmOption(stringResource(R.string.am), selected = !isPm) { onSelect(false) }
        AmPmOption(stringResource(R.string.pm), selected = isPm) { onSelect(true) }
    }
}

@Composable
private fun AmPmOption(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (selected) Teal100 else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 9.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = if (selected) Teal700 else Teal100.copy(alpha = 0.75f)
        )
    }
}

// ---------- Tarjetas de ajustes ----------

@Composable
private fun SettingCardSurface(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        border = BorderStroke(1.dp, AppOutline),
        modifier = modifier.fillMaxWidth(),
        content = content
    )
}

@Composable
private fun IconBadge(icon: ImageVector) {
    Box(
        Modifier.size(44.dp).background(Teal50, RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = Teal500, modifier = Modifier.size(22.dp))
    }
}

/** Fila genérica: ícono + título + subtítulo y un control a la derecha. */
@Composable
fun SettingRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    trailing: @Composable () -> Unit
) {
    SettingCardSurface(modifier) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            IconBadge(icon)
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Ink)
                Text(subtitle, fontSize = 14.sp, color = Muted)
            }
            Spacer(Modifier.width(8.dp))
            trailing()
        }
    }
}

/** NUEVO: apartado para avisar cada cierto tiempo (RF 1). */
@Composable
fun IntervalCard(intervalMinutes: Int, onSelect: (Int) -> Unit, modifier: Modifier = Modifier) {
    SettingRow(
        icon = Icons.Outlined.Timer,
        title = stringResource(R.string.interval_title),
        subtitle = stringResource(R.string.interval_subtitle),
        modifier = modifier
    ) {
        DropdownPill(
            selected = intervalMinutes,
            options = Constants.IntervalOptionsMinutes,
            label = ::formatInterval,
            onSelect = onSelect
        )
    }
}

@Composable
fun DaysCard(selectedDays: Set<DayOfWeek>, onToggle: (DayOfWeek) -> Unit, modifier: Modifier = Modifier) {
    val letters = stringArrayResource(R.array.week_days_short)
    SettingCardSurface(modifier) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconBadge(Icons.Outlined.CalendarMonth)
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(stringResource(R.string.repeat_title), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Ink)
                    Text(stringResource(R.string.repeat_subtitle), fontSize = 14.sp, color = Muted)
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DayOfWeek.values().forEachIndexed { index, day ->
                    DayChip(
                        letter = letters.getOrElse(index) { "" },
                        selected = day in selectedDays,
                        onClick = { onToggle(day) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = if (selectedDays.isEmpty()) stringResource(R.string.days_none)
                else quantityString(R.plurals.days_selected, selectedDays.size),
                color = if (selectedDays.isEmpty()) Muted else Teal500,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun DayChip(letter: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(14.dp)
    Box(
        modifier
            .aspectRatio(1f)
            .clip(shape)
            .background(if (selected) Teal500 else Color.White)
            .border(1.dp, if (selected) Teal500 else AppOutline, shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            letter,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = if (selected) Color.White else Muted
        )
    }
}

@Composable
fun AmountCard(amountMl: Int, onSelect: (Int) -> Unit, modifier: Modifier = Modifier) {
    SettingRow(
        icon = Icons.Outlined.WaterDrop,
        title = stringResource(R.string.amount_title),
        subtitle = stringResource(R.string.amount_subtitle),
        modifier = modifier
    ) {
        DropdownPill(
            selected = amountMl,
            options = Constants.AmountOptionsMl,
            label = ::formatAmount,
            onSelect = onSelect
        )
    }
}

@Composable
fun NotificationCard(enabled: Boolean, onToggle: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    SettingRow(
        icon = Icons.Outlined.Notifications,
        title = stringResource(R.string.notif_title),
        subtitle = stringResource(if (enabled) R.string.notif_on else R.string.notif_off),
        modifier = modifier
    ) {
        Switch(
            checked = enabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Teal500,
                checkedBorderColor = Teal500,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = AppOutline,
                uncheckedBorderColor = AppOutline
            )
        )
    }
}

@Composable
fun TipCard(modifier: Modifier = Modifier) {
    val bold = stringResource(R.string.tip_bold)
    val rest = stringResource(R.string.tip_rest)
    Row(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Teal50)
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(Icons.Outlined.WaterDrop, contentDescription = null, tint = Teal500, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(12.dp))
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Teal700)) { append(bold) }
                append(" ")
                append(rest)
            },
            color = Muted,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}

@Composable
fun CreateReminderButton(enabled: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = if (enabled) Teal500 else AppOutline,
        shadowElevation = if (enabled) 8.dp else 0.dp,
        modifier = modifier.fillMaxWidth().height(60.dp)
    ) {
        Row(
            Modifier.fillMaxWidth().clickable(enabled = enabled, onClick = onClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Outlined.Notifications, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(10.dp))
            Text(stringResource(R.string.create_reminder), color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}