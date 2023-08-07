package com.example.movies.ui.screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun SimpleDialog(
    onAbort: () -> Unit,
    onExit: () -> Unit,
    titleText: String,
    positiveText: String,
    positiveAction: () -> Unit,
    negativeText: String,
    negativeAction: () -> Unit
) {
    Dialog(
        onDismissRequest = { onExit() }, properties = DialogProperties(
            dismissOnBackPress = false, dismissOnClickOutside = false
        )
    ) {
        Card(
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier
                .padding(8.dp)
                .height(IntrinsicSize.Min),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = titleText,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TextButton(
                        onClick = {
                            onAbort()
                            negativeAction()
                        },
                        Modifier
                            .padding(horizontal = 4.dp)
                            .width(IntrinsicSize.Min)
                            .clickable { }
                    ) {
                        Text(
                            text = negativeText,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    TextButton(
                        onClick = {
                            onExit()
                            positiveAction()
                        },
                        Modifier
                            .padding(horizontal = 4.dp)
                            .clickable { }
                    ) {
                        Text(
                            text = positiveText,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SimpleDialogPreview() {
    SimpleDialog(
        onAbort = {},
        onExit = {},
        titleText = "Title",
        positiveAction = {},
        positiveText = "Positive",
        negativeAction = {},
        negativeText = "Negative"
    )
}