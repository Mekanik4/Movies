package com.example.movies.ui.screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.movies.R

@Composable
fun Pages(
    hasNext: Boolean,
    hasPrevious: Boolean,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    scrollToTop: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.Center
    ){

        if (hasPrevious) {
            TextButton(
                onClick = {
                    onPreviousClick()
                    scrollToTop()
                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "previous page" )
                    Text(
                        text = stringResource(id = R.string.previous_page),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        if (hasNext) {
            TextButton(
                onClick = {
                    onNextClick()
                    scrollToTop()
                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = stringResource(id = R.string.next_page),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Icon(Icons.Default.ArrowForward, contentDescription = "next page" )
                }
            }
        }
    }
}