package com.example.oportunia.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.oportunia.presentation.ui.theme.buttonBrownLine
import com.example.oportunia.presentation.ui.theme.buttonCream

@Composable
fun AlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector? = null
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = dialogTitle, color = Color.Black)
        },
        text = {
            Text(text = dialogText, color = Color.Black)
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmation,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, buttonBrownLine),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray,
                    contentColor = Color.White
                )
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, buttonBrownLine),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray,
                    contentColor = Color.DarkGray
                )
            ) {
                Text("Volver")
            }
        },
        icon = icon?.let {
            { Icon(imageVector = it, contentDescription = null) }
        },
        containerColor = Color(0xFFFFF3E0)
    )
}
