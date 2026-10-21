package com.yharim.tomaawita.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yharim.tomaawita.R
import com.yharim.tomaawita.ui.theme.AppBackground
import com.yharim.tomaawita.ui.theme.AppOutline
import com.yharim.tomaawita.ui.theme.Ink
import com.yharim.tomaawita.ui.theme.Muted
import com.yharim.tomaawita.ui.theme.Teal100
import com.yharim.tomaawita.ui.theme.Teal50
import com.yharim.tomaawita.ui.theme.Teal500
import com.yharim.tomaawita.ui.theme.Teal700
import com.yharim.tomaawita.util.Constants
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

/** 350 -> "350 ml", 1000 -> "1 L", 2000 -> "2 L". */
private fun amountLabel(ml: Int): String =
    if (ml >= 1000 && ml % 1000 == 0) "${ml / 1000} L" else "$ml ml"

/**
 * Hoja "Agregar cantidad". No guarda nada por sí sola: avisa con [onConfirm] y el
 * ViewModel decide qué hacer con la cantidad elegida.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAmountSheet(
    amountMl: Int,
    onAmountChange: (Int) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    // Cierra la hoja con animación y después ejecuta la acción.
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
                .navigationBarsPadding()
        ) {
            // Encabezado
            Row(verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f)) {
                    Text(
                        stringResource(R.string.add_amount_label),
                        style = MaterialTheme.typography.labelSmall,
                        color = Teal500
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        stringResource(R.string.add_amount_title),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                }
                val closeShape = RoundedCornerShape(18.dp)
                Box(
                    Modifier
                        .size(52.dp)
                        .clip(closeShape)
                        .background(Color.White)
                        .clickable { closeThen(onDismiss) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = stringResource(R.string.close),
                        tint = Ink,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(stringResource(R.string.add_amount_question), fontSize = 17.sp, color = Muted)
            Spacer(Modifier.height(24.dp))

            // Cantidad con − y +
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StepButton(
                    icon = Icons.Filled.Remove,
                    description = stringResource(R.string.decrease_amount),
                    enabled = amountMl > Constants.CustomAmountMinMl,
                    onClick = { onAmountChange(amountMl - Constants.CustomAmountStepMl) }
                )
                val cardShape = RoundedCornerShape(28.dp)
                Row(
                    Modifier
                        .weight(1f)
                        .height(96.dp)
                        .clip(cardShape)
                        .background(Color.White)
                        .border(1.dp, AppOutline, cardShape),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("$amountMl", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Ink)
                    Text(
                        " ml",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Muted,
                        modifier = Modifier.padding(top = 14.dp)
                    )
                }
                StepButton(
                    icon = Icons.Filled.Add,
                    description = stringResource(R.string.increase_amount),
                    enabled = amountMl < Constants.CustomAmountMaxMl,
                    onClick = { onAmountChange(amountMl + Constants.CustomAmountStepMl) }
                )
            }
            Spacer(Modifier.height(16.dp))

            // Deslizador
            Slider(
                value = amountMl.toFloat(),
                onValueChange = { onAmountChange(it.roundToInt()) },
                valueRange = Constants.CustomAmountMinMl.toFloat()..Constants.CustomAmountMaxMl.toFloat(),
                steps = (Constants.CustomAmountMaxMl - Constants.CustomAmountMinMl) / Constants.CustomAmountStepMl - 1,
                colors = SliderDefaults.colors(
                    thumbColor = Teal500,
                    activeTrackColor = Teal500,
                    inactiveTrackColor = Teal100,
                    activeTickColor = Color.Transparent,
                    inactiveTickColor = Color.Transparent
                )
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(amountLabel(Constants.CustomAmountMinMl), fontSize = 13.sp, color = Muted)
                Text(amountLabel(Constants.CustomAmountMaxMl), fontSize = 13.sp, color = Muted)
            }
            Spacer(Modifier.height(20.dp))

            // Atajos
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Constants.CustomAmountPresetsMl.forEach { preset ->
                    PresetChip(
                        text = amountLabel(preset),
                        selected = preset == amountMl,
                        onClick = { onAmountChange(preset) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(Modifier.height(24.dp))

            // Botón de confirmar
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = Teal500,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth().height(60.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth().clickable { closeThen(onConfirm) },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Outlined.WaterDrop,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        stringResource(R.string.add_amount_cta, amountLabel(amountMl)),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun StepButton(
    icon: ImageVector,
    description: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    Box(
        Modifier
            .size(52.dp)
            .clip(shape)
            .background(Teal50)
            .border(1.dp, Teal100, shape)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icon,
            contentDescription = description,
            tint = if (enabled) Teal700 else Muted.copy(alpha = 0.5f),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun PresetChip(text: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(16.dp)
    Box(
        modifier
            .height(48.dp)
            .clip(shape)
            .background(if (selected) Teal100 else Color.White)
            .border(1.dp, if (selected) Teal500 else AppOutline, shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (selected) Teal700 else Ink
        )
    }
}