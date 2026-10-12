package com.example.oportunia.presentation.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.oportunia.presentation.ui.theme.PurpleGrey40


@Composable
fun CenteredDivider(
    width: Dp = 350.dp,
    color: Color = PurpleGrey40.copy(alpha = 0.5f),
    thickness: Dp = 2.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier.fillMaxWidth(), // take full width
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = modifier
                .width(width)
                .height(thickness)
                .background(color)
        )
    }
}
