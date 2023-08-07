package com.example.movies.ui.screen


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.movies.R
import com.example.movies.ui.screen.viewmodel.MovieViewModel
import com.example.movies.ui.screen.viewmodel.UserViewModel
import kotlinx.coroutines.launch


@Composable
fun FavouritesScreen(
    moviesViewModel: MovieViewModel,
    userViewModel: UserViewModel,
    onNavigateToHomeScreen: () -> Unit,
    onNavigateToMovieScreen: () -> Unit
) {

    var favouriteMovies by remember {
        mutableStateOf(userViewModel.user?.favorites)
    }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(bottomBar = {
        BottomAppBar(
            actions = {
                IconButton(
                    onClick = {
                        onNavigateToHomeScreen()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.empty_movie),
                        contentDescription = "All movies"
                    )
                }
//            Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(index = 0)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.filled_heart),
                        contentDescription = "Favourite movies"
                    )

                }
            },
            modifier = Modifier.height(IntrinsicSize.Min)
        )
    }) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopAppBar(
                    title = { Text(text = "${userViewModel.user?.username} ${stringResource(id = R.string.movie_list)}") },
                )

//                if(!moviesViewModel.searching.value) {
//                    movies = moviesViewModel.movies.value
//                }
//                else
//                    CircularProgressIndicator(
//                        modifier = Modifier
//                            .size(128.dp)
//                            .fillMaxSize()
//                            .wrapContentHeight(Alignment.CenterVertically),
//                        color = ProgressIndicatorDefaults.circularColor,
//                        strokeWidth = ProgressIndicatorDefaults.CircularStrokeWidth
//                    )

                if (favouriteMovies != null) {
                    if (favouriteMovies!!.isNotEmpty()) {

                        LazyColumn(
                            state = listState,
                            userScrollEnabled = true
                        ) {
                            items(items = favouriteMovies!!, key = { movie -> movie.id }) { movie ->
                                MovieCard(
                                    movie = movie,
                                    onClickNavigate = {
                                        moviesViewModel.getCredits(it)
                                        userViewModel.fromHome = false
                                        onNavigateToMovieScreen()
                                    },
                                    onClick = { moviesViewModel.getMovie(it) },
                                    addToFavourites = { },
                                    removeFromFavourites = {
                                        userViewModel.removeMovie(it)
                                        favouriteMovies = userViewModel.user?.favorites
                                    },
                                    movieExists = { if (userViewModel.movieExists(it) != null) userViewModel.movieExists(it)!! else false }
                                )
                            }
                        }
//                        MovieCardList(
//                            movieList = favouriteMovies!!,
//                            onMovieSelected = {
//                                moviesViewModel.getCredits(it)
//                                userViewModel.fromHome = false
//                                onNavigateToMovieScreen()
//                            },
//                            onClick = { moviesViewModel.getMovie(it) },
//                            addToFavourites = { },
//                            removeFromFavourites = {
//                                userViewModel.removeMovie(it)
//                                favouriteMovies = userViewModel.user?.favorites
//                            },
//                            movieExists = { if (userViewModel.movieExists(it) != null) userViewModel.movieExists(it)!! else false }
//                        )
                    } else Text(
                        text = stringResource(id = R.string.no_favourites_text),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 8.dp, end = 8.dp, top = 40.dp)
                            .wrapContentHeight(Alignment.CenterVertically),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}