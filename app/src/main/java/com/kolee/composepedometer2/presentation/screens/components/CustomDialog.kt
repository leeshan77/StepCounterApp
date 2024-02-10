package com.kolee.composepedometer2.presentation.screens.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kolee.composepedometer2.ui.theme.Teal200

@Composable
fun CustomDialog(
    title: String,
    text: String,
    confirm: String,
    onConfirm: () -> Unit
) {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            modifier = Modifier.wrapContentSize(),
            title = { Text(text = title) },
            text = { Text(text = text) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm.invoke()
                        showDialog = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Teal200,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = confirm)
                }
            },
            onDismissRequest = { }
        )
    }
}