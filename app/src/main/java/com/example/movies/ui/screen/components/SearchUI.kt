package com.example.movies.ui.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.movies.R
import com.example.movies.domain.Movie
import com.example.movies.service.ApiConstants
import com.example.movies.ui.screen.getTextFromList
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.animation.circular.CircularRevealPlugin
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent

@Composable
fun SearchUI(onNavigateToMovieScreen: (Movie) -> Unit, searchData: List<Movie>, onClick: () -> Unit, selectMovie: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(0.dp, 350.dp)
            .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
            .background(color = Color.White)
            .padding(top = 8.dp)

    ) {
        items(items = searchData, itemContent = { item ->
            Row(modifier = Modifier
                .padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
                .clickable {
                    selectMovie(item.id)
                    onClick()
                    onNavigateToMovieScreen(item)
                }) {
                CoilImage(
                    modifier = Modifier
                        .height(100.dp)
                        .width(80.dp),
                    imageModel = { ApiConstants.IMAGE_URL.plus(item.posterPath) },
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
                Column {
                    Text(
                        text = item.title,
                        color = Color.Black,
                        modifier = Modifier.padding(
                            start = 8.dp,
                            top = 4.dp
                        ),
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    )
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .height(IntrinsicSize.Min),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = stringResource(id = R.string.genres),
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(
                                start = 8.dp
                            ),
                        )
                        Text(
                            text = getTextFromList(item.getGenreNames()),
                            color = Color.Black
                        )
                    }
                    Row(
                        modifier = Modifier
                            .height(IntrinsicSize.Min)
                            .align(Alignment.Start)
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.rating),
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(
                                start = 8.dp
                            ),
                        )
                        Text(
                            text = ": ${
                                if (item.voteAverage > 0f) "${
                                    String.format(
                                        "%.2f",
                                        item.voteAverage
                                    )
                                } / 10" else stringResource(id = R.string.n_a)
                            }",
                            color = Color.Black,
                        )
                    }
                }
            }
        })
    }
}