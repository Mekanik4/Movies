package com.example.movies.ui.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movies.R
import com.example.movies.TopAppBar
import com.example.movies.domain.Movie
import com.example.movies.service.ApiConstants
import com.example.movies.ui.screen.components.MovieDetailsDialog
import com.example.movies.ui.screen.components.Pages
import com.example.movies.ui.screen.components.SearchBar
import com.example.movies.ui.screen.components.SearchUI
import com.example.movies.ui.screen.components.SimpleDialog
import com.example.movies.ui.screen.viewmodel.MovieViewModel
import com.example.movies.ui.screen.viewmodel.UserViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.animation.circular.CircularRevealPlugin
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch


@OptIn(ExperimentalCoroutinesApi::class)
@SuppressLint("MutableCollectionMutableState")
@Composable
fun HomeScreen(
    moviesViewModel: MovieViewModel,
    userViewModel: UserViewModel,
    onNavigateToFavorites: () -> Unit,
    onNavigateToMovieScreen: () -> Unit,
    onNavigateToSignInScreen: () -> Unit
) {
    val context = LocalContext.current

    checkConnection(context)

    val isAppBarVisible = remember { mutableStateOf(true) }

    var isMovieDetailsDialogShown by remember {
        mutableStateOf(false)
    }

    var isSignOutDialogShown by remember {
        mutableStateOf(false)
    }

    var movie by remember {
        mutableStateOf(moviesViewModel.movie.value)
    }

    //Show sign out dialog
    if (isSignOutDialogShown) {
        SimpleDialog(
            onExit = {
                isSignOutDialogShown = !isSignOutDialogShown
            },
            onAbort = {
                isSignOutDialogShown = !isSignOutDialogShown
            },
            titleText = stringResource(id = R.string.sign_out_dialog),
            positiveText = stringResource(id = R.string.yes),
            positiveAction = {
                userViewModel.setConnected(false)
                userViewModel.setUsername("")
                onNavigateToSignInScreen()
            },
            negativeText = stringResource(id = R.string.no),
            negativeAction = {}
        )
    }

    //Show movie details dialog
    if (isMovieDetailsDialogShown) {
        if(!moviesViewModel.isLoading.value) {
            movie = moviesViewModel.movie.value
            movie?.let { data ->
                moviesViewModel.setCurrentMovie(data[0])
                MovieDetailsDialog(
                    movie = data[0],
                    onNavigateToMovieScreen = {
                        moviesViewModel.getCredits(moviesViewModel.getCurrentMovie().id)
                        isMovieDetailsDialogShown = !isMovieDetailsDialogShown
                        onNavigateToMovieScreen()
                    },
                    onDismiss = {
                        isMovieDetailsDialogShown = !isMovieDetailsDialogShown
                    },
                    onExit = {
                        isMovieDetailsDialogShown = !isMovieDetailsDialogShown
                    })
            }
        }
    }

    if(!moviesViewModel.isLoading.value) {
        movie = moviesViewModel.movie.value
    }

    var movies by remember {
        mutableStateOf(moviesViewModel.movies.value)
    }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(bottomBar = {
        BottomAppBar(actions = {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(index = 0)
                    }
            },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.filled_movie),
                    contentDescription = "All movies"
                )
            }
            IconButton(
                onClick = { onNavigateToFavorites() },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.empty_heart),
                    contentDescription = "Favourite movies"
                )

            }
        })
    }) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(isAppBarVisible.value){
                    TopAppBar(title = { Text("${stringResource(id = R.string.year)} ${stringResource(id = R.string.app_name)}") },
                        searchAction = {isAppBarVisible.value = !isAppBarVisible.value},
                        signOutAction = { isSignOutDialogShown = !isSignOutDialogShown }
                    )
                }


                if(!moviesViewModel.searching.value && !moviesViewModel.genresLoading.value) {
                    movies = moviesViewModel.movies.value
                }
                else
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(128.dp)
                            .fillMaxSize()
                            .wrapContentHeight(Alignment.CenterVertically),
                        color = ProgressIndicatorDefaults.circularColor,
                        strokeWidth = ProgressIndicatorDefaults.CircularStrokeWidth
                    )

                if (movies != null) {
                    if (movies!!.isNotEmpty()) {


                        LazyColumn(
                            state = listState,
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            userScrollEnabled = true
                        ) {
                            items(items = movies!!, key = { movie -> movie.id }) { movie ->
                                MovieCard(
                                    movie = movie,
                                    onClickNavigate = {isMovieDetailsDialogShown = !isMovieDetailsDialogShown},
                                    onClick = { moviesViewModel.getMovie(it) },
                                    addToFavourites = {userViewModel.addMovie(it)},
                                    removeFromFavourites = { userViewModel.removeMovie(it) },
                                    movieExists = { if (userViewModel.movieExists(it) != null) userViewModel.movieExists(it)!! else false }
                                )
                            }
                            item {
                                Pages(
                                    hasNext = moviesViewModel.currentPage + 1 <= moviesViewModel.maxPage,
                                    hasPrevious = moviesViewModel.currentPage - 1 > 0,
                                    onNextClick = {
                                        moviesViewModel.currentPage++
                                        moviesViewModel.getAllMovies(moviesViewModel.currentPage)
                                    },
                                    onPreviousClick = {
                                        moviesViewModel.currentPage--
                                        moviesViewModel.getAllMovies(moviesViewModel.currentPage)
                                    },
                                    scrollToTop = {
                                        coroutineScope.launch {
                                            listState.animateScrollToItem(index = 0)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
                else if (!checkConnection(context))
                    Text(
                        text = stringResource(id = R.string.no_movies_text),
                        modifier = Modifier
                            .clickable {
                                if (checkConnection(context)) {
                                    moviesViewModel.getGenres()
                                    moviesViewModel.getMaxPages()
                                    moviesViewModel.getAllMovies(1)
                                    movies = moviesViewModel.movies.value
                                }
                            }
                            .fillMaxSize()
                            .padding(start = 8.dp, end = 8.dp, top = 40.dp)
                            .wrapContentHeight(Alignment.CenterVertically),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.outline
                    )

            }
            Column {

                if (isAppBarVisible.value.not()) {
                    SearchBar(isAppBarVisible = isAppBarVisible, movieViewModel = moviesViewModel)

                    moviesViewModel.searchData.value?.let { data ->
                        SearchUI(
                            onNavigateToMovieScreen = {
                                moviesViewModel.setCurrentMovie(it)
                                moviesViewModel.getCredits(moviesViewModel.getCurrentMovie().id)
                                userViewModel.fromHome = true
                                onNavigateToMovieScreen()
                            },
                            searchData = data,
                            onClick = { isAppBarVisible.value = true },
                            selectMovie = {
                                moviesViewModel.getMovie(it)
                            }
                        )
                    }
                }
            }
        }
    }
    var backClicks by remember {
        mutableIntStateOf(0)
    }
    BackHandler {
        if (backClicks == 0) {
            backClicks++
            Toast.makeText(context, context.getString(R.string.exit_app_toast), Toast.LENGTH_LONG)
                .show()
        } else {
            val activity = context as? Activity
            activity?.finish()
        }
    }
}

private fun checkConnection(context: Context): Boolean {
    val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities =  connManager.getNetworkCapabilities(connManager.activeNetwork)
    if (networkCapabilities == null) {
        Toast.makeText(context, context.getText(R.string.no_internet), Toast.LENGTH_LONG).show()
        return false
    }
    return true
}


@Composable
fun MovieCard(
    movie: Movie,
    onClickNavigate: (Int) -> Unit,
    onClick: (Int) -> Unit,
    addToFavourites: (Movie) -> Unit,
    removeFromFavourites: (Int) -> Unit,
    movieExists: (Int) -> Boolean
) {
    val context = LocalContext.current
    var liked by remember {
        mutableStateOf(movieExists(movie.id))
    }
    ElevatedCard(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(start = 12.dp, end = 12.dp, bottom = 4.dp, top = 4.dp)
            .clickable {
                onClick(movie.id)
                onClickNavigate(movie.id)
            }
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column (
                modifier = Modifier
                    .weight(0.5f)
                    .padding(8.dp)
            ) {
                CoilImage(
                    modifier = Modifier
                        .height(100.dp)
                        .width(80.dp),
                    imageModel = { ApiConstants.IMAGE_URL.plus(movie.posterPath) },
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
            }
            Column (
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .weight(1.5f)
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ){
                    Column (
                        modifier = Modifier
                            .weight(1.5f)
//                        .padding(start = 16.dp)
                    ) {
                        Text(
                            text = movie.title,
                            fontWeight = FontWeight.Bold
//                    modifier = Modifier.weight(1f)
                        )
                    }
                    Column (
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(start = 16.dp)
                    ) {
                        IconButton(onClick = {
                            liked = !liked
                            if (liked) {
                                addToFavourites(movie)
                                Toast.makeText(context, context.getText(R.string.add_to_favourites), Toast.LENGTH_SHORT).show()
                            }
                            else{
                                removeFromFavourites(movie.id)
                                Toast.makeText(context, context.getText(R.string.removed_from_favourites), Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            if (!liked)
                                Icon(Icons.Default.FavoriteBorder, contentDescription = "Not favourite")
                            else
                                Icon(Icons.Default.Favorite, contentDescription = "Favourite")
                        }
                    }
                }
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = stringResource(id = R.string.genres),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = getTextFromList(movie.getGenreNames())
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val list: ArrayList<Movie> = ArrayList()


    val listFromDB by remember {
        mutableStateOf(list)
    }

}

@Preview
@Composable
fun SubjectCardPreview() {
    val movie = "test movie"
    MovieCard(
        movie = Movie(), onClickNavigate = { }, onClick = {}, addToFavourites = {}, removeFromFavourites = {}, movieExists = {false}
    )
}