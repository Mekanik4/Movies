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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.movies.R
import com.example.movies.domain.Movie
import com.example.movies.service.ApiConstants
import com.example.movies.ui.screen.getTextFromList
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.animation.circular.CircularRevealPlugin
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent

@Composable
fun MovieDetailsDialog(
    movie: Movie,
    onNavigateToMovieScreen: () -> Unit,
    onDismiss: () -> Unit,
    onExit: () -> Unit
) {

    Dialog(
        onDismissRequest = { onDismiss() }, properties = DialogProperties(
            dismissOnBackPress = false, dismissOnClickOutside = false
        )
    ) {
        Card(
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier
                .padding(2.dp)
                .height(IntrinsicSize.Min),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                CoilImage(
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    imageModel = { ApiConstants.IMAGE_URL.plus(movie.backdropPath) },
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        contentDescription = "search item",
                        colorFilter = null,
                    ),
                    component = rememberImageComponent {
                        +CircularRevealPlugin(
                            duration = 800
                        )
                    },
                )
                Text(
                    text = movie.title,
                    modifier = Modifier.padding(vertical = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                )
                Row(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .align(Alignment.Start)
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.genres),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = getTextFromList(movie.getGenreNames())
                    )
                }
                Row(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .align(Alignment.Start)
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.runtime),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = ": ${movie.runtime} ${stringResource(id = R.string.min)}"
                    )
                }
                Text(
                    text = movie.overview,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TextButton(
                        onClick = {
                            onNavigateToMovieScreen()
                        },
                        Modifier
                            .padding(horizontal = 4.dp)
                            .clickable { }
                    ) {
                        Text(
                            text = stringResource(id = R.string.more_movie_details),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                    TextButton(
                        onClick = {
                            onExit()
                        },
                        Modifier
                            .padding(horizontal = 4.dp)
                            .clickable { }
                    ) {
                        Text(
                            text = stringResource(id = R.string.close_dialog_text),
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
fun MovieDetailsDialogPreview() {
    MovieDetailsDialog(
        movie = Movie(),
        onNavigateToMovieScreen = {},
        onDismiss = {},
        onExit = {}
    )
}