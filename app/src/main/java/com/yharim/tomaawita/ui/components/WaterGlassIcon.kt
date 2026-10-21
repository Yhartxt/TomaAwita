package com.yharim.tomaawita.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yharim.tomaawita.ui.theme.Teal100
import com.yharim.tomaawita.ui.theme.Teal500

/** Vaso con nivel de agua. [fill] va de 0 (vacío) a 1 (lleno). */
@Composable
fun WaterGlassIcon(fill: Float, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp, bottomStart = 14.dp, bottomEnd = 14.dp)
    Box(
        modifier
            .size(width = 44.dp, height = 52.dp)
            .clip(shape)
            .border(2.dp, Teal100, shape)
            .background(Color.White)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(fill.coerceIn(0f, 1f))
                .align(Alignment.BottomCenter)
                .background(Teal100)
        )
        Icon(
            imageVector = Icons.Outlined.WaterDrop,
            contentDescription = null,
            tint = Teal500,
            modifier = Modifier.size(18.dp).align(Alignment.Center)
        )
    }
}