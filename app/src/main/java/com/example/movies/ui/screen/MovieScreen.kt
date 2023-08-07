package com.example.movies.ui.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.movies.R
import com.example.movies.domain.Movie
import com.example.movies.service.ApiConstants
import com.example.movies.ui.screen.viewmodel.MovieViewModel
import com.example.movies.ui.screen.viewmodel.UserViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.animation.circular.CircularRevealPlugin
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent

@SuppressLint("MutableCollectionMutableState")
@Composable
fun MovieScreen(
    moviesViewModel: MovieViewModel,
    userViewModel: UserViewModel,
    onNavigateToHomeScreen: () -> Unit,
    onNavigateToFavouritesScreen: () -> Unit
) {
    val context = LocalContext.current

    var movie by remember {
        mutableStateOf(moviesViewModel.getCurrentMovie())
    }

    var liked by remember {
        mutableStateOf(userViewModel.movieExists(movie.id))
    }

    if (!moviesViewModel.creditsLoading.value){
        movie = moviesViewModel.getCurrentMovie()
        liked = userViewModel.movieExists(movie.id)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = movie.title)
                },
                actions = {
                    IconButton(onClick = {
                        liked = !liked!!
                        if (liked!!) {
                            userViewModel.addMovie(movie)
                            Toast.makeText(context, context.getText(R.string.add_to_favourites), Toast.LENGTH_SHORT).show()
                        }
                        else {
                            userViewModel.removeMovie(movie.id)
                            Toast.makeText(context, context.getText(R.string.removed_from_favourites), Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        if (!liked!!)
                            Icon(Icons.Default.FavoriteBorder, contentDescription = "Not favourite")
                        else
                            Icon(Icons.Default.Favorite, contentDescription = "Favourite")
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (userViewModel.fromHome)
                                onNavigateToHomeScreen()
                            else
                                onNavigateToFavouritesScreen()
                        }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start,

            ) {

                if (moviesViewModel.creditsLoading.value) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(128.dp)
                            .fillMaxSize()
                            .align(Alignment.CenterHorizontally)
                            .wrapContentHeight(Alignment.CenterVertically),
                        color = ProgressIndicatorDefaults.circularColor,
                        strokeWidth = ProgressIndicatorDefaults.CircularStrokeWidth
                    )
                }
                else {
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
                    if (movie.title != movie.originalTitle) {
                        Row(
                            modifier = Modifier
                                .height(IntrinsicSize.Min)
                                .align(Alignment.Start)
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.original_title),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = ": ${movie.originalTitle}"
                            )
                        }
                    }
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
                    Row(
                        modifier = Modifier
                            .height(IntrinsicSize.Min)
                            .align(Alignment.Start)
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.rating),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = ": ${if (movie.voteAverage > 0f) "${String.format("%.2f", movie.voteAverage)} / 10" else stringResource(id = R.string.n_a)}"
                        )
                    }
                    Row(
                        modifier = Modifier
                            .height(IntrinsicSize.Min)
                            .align(Alignment.Start)
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = getDirector(movie),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = getTextFromList(movie.getDirectors())
                        )
                    }
                    Row(
                        modifier = Modifier
                            .height(IntrinsicSize.Min)
                            .align(Alignment.Start)
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.cast),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = getTextFromList(movie.getCast())
                        )
                    }
                    Text(
                        text = movie.overview,
                        modifier = Modifier.padding(vertical = 8.dp),
                        textAlign = TextAlign.Justify
                    )
                }
            }
        }
    }
}



fun getDirector(movie: Movie): String {
    val directors = movie.getDirectors()
    var directorsText = "Director"
    if (directors.size > 1)
        directorsText += "s"
    return directorsText
}

//fun getDirectorNames(movie: Movie): String{
//    val directors = movie.getDirectors()
//    var directorsText = ": "
//    if (directors.size > 1) {
//        var counter = 0
//        for (name in directors) {
//            counter++
//            directorsText += if (counter == directors.size)
//                name
//            else
//                "$name, "
//        }
//    }
//    else if (directors.size == 1)
//        directorsText += " ${directors[0]}"
//    else
//        return "$directorsText N/A"
//
//    return directorsText
//}
//
//fun getCastNames(movie: Movie): String {
//    var castNames = ": "
//    val cast = movie.getCast()
//    if (cast.isNotEmpty()) {
//        var counter = 0
//        for (name in cast) {
//            counter++
//            castNames += if (counter == cast.size)
//                name
//            else
//                "$name, "
//        }
//    }
//    else
//        return "$castNames N/A"
//    return castNames
//}

fun getTextFromList(list: List<Any>): String {
    var text = ": "
    if (list.isNotEmpty()) {
        var counter = 0
        for (name in list) {
            counter++
            text += if (counter == list.size)
                name
            else
                "$name, "
        }
    }
    else
        return "$text N/A"
    return text
}