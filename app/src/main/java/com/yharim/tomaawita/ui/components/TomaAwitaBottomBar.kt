package com.yharim.tomaawita.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yharim.tomaawita.R
import com.yharim.tomaawita.ui.navigation.AppTab
import com.yharim.tomaawita.ui.theme.Muted
import com.yharim.tomaawita.ui.theme.Teal500

@Composable
fun TomaAwitaBottomBar(
    selected: AppTab,
    onSelect: (AppTab) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier.fillMaxWidth()) {
        Surface(
            color = Color.White,
            shadowElevation = 12.dp,
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
        ) {
            Row(
                Modifier.navigationBarsPadding().height(76.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomBarItem(
                    icon = Icons.Outlined.Home,
                    label = stringResource(R.string.tab_home),
                    selected = selected == AppTab.Home,
                    onClick = { onSelect(AppTab.Home) },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.weight(1f)) // hueco para el botón central
                BottomBarItem(
                    icon = Icons.Outlined.TrackChanges,
                    label = stringResource(R.string.tab_goals),
                    selected = selected == AppTab.Goals,
                    onClick = { onSelect(AppTab.Goals) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Surface(
            shape = RoundedCornerShape(22.dp),
            color = Teal500,
            shadowElevation = 14.dp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-8).dp)
                .size(64.dp)
        ) {
            Box(
                Modifier.fillMaxSize().clickable(onClick = onAddClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add_water),
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
private fun BottomBarItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tint = if (selected) Teal500 else Muted
    Column(
        modifier.fillMaxHeight().clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(26.dp))
        Spacer(Modifier.height(4.dp))
        Text(label, style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.sp), color = tint)
    }
}