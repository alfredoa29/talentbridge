package com.example.oportunia.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.oportunia.R
import com.example.oportunia.presentation.ui.theme.backgroundCream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralDropdown (
    optionsWithIcons: Map<String, Int?>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    placeholderText: String,
    width: Dp = 348.dp,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val currentIcon = optionsWithIcons[selectedOption]

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.width(width)
        ) {
            TextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text(placeholderText) },
                leadingIcon = currentIcon?.let {
                    {
                        Icon(
                            painter = painterResource(id = it),
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bottom_triangle),
                        contentDescription = "Dropdown icon",
                        tint = Color.Gray
                    )
                },
                modifier = Modifier
                    .menuAnchor(
                        type = ExposedDropdownMenuAnchorType.PrimaryEditable,
                        enabled = true
                    )
                    .width(width)
                    .clip(RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White
                ),
                singleLine = true
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(backgroundCream)
            ) {
                optionsWithIcons.forEach { (opt, _) ->
                    DropdownMenuItem(
                        text = { Text(opt) },
                        onClick = {
                            onOptionSelected(opt)
                            expanded = false
                        }
                    )
                }
            }
        }
    }

}