package com.example.movies

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movies.ui.screen.Destinations.*
import com.example.movies.ui.screen.FavouritesScreen
import com.example.movies.ui.screen.HomeScreen
import com.example.movies.ui.screen.MovieScreen
import com.example.movies.ui.screen.SignInScreen
import com.example.movies.ui.screen.viewmodel.MovieViewModel
import com.example.movies.ui.screen.viewmodel.UserViewModel
import com.example.movies.ui.theme.MoviesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val perms = arrayOf(
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET
    )
    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestMultiplePermissions.launch(perms)

        setContent {
            MoviesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MoviesApp("Android")
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(title: @Composable () -> Unit, searchAction: () -> Unit, signOutAction: () -> Unit) {
    androidx.compose.material3.TopAppBar(title = title,
        actions = {
            IconButton(onClick = { searchAction() }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
            IconButton(onClick = { signOutAction() }) {
                Icon(Icons.Default.ExitToApp, contentDescription = "More")
            }
        })
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesApp(
    name: String,
              userViewModel: UserViewModel = hiltViewModel(),
              movieViewModel: MovieViewModel = hiltViewModel()
) {

    val navController = rememberNavController()
    Scaffold { paddingValues ->
        NavHost(navController, startDestination = if (userViewModel.isConnected()) HOME_SCREEN.route else SIGN_IN_SCREEN.route) {
            composable(FAVORITES_SCREEN.route) {
                FavouritesScreen(
                    onNavigateToMovieScreen = { navController.navigate(MOVIE_SCREEN.route) },
                    onNavigateToHomeScreen = { navController.navigate(HOME_SCREEN.route) },
                    moviesViewModel = movieViewModel,
                    userViewModel = userViewModel
                )
            }
            composable(HOME_SCREEN.route) {
                HomeScreen(
                    moviesViewModel = movieViewModel,
                    userViewModel = userViewModel,
                    onNavigateToFavorites = { navController.navigate(FAVORITES_SCREEN.route) },
                    onNavigateToMovieScreen = {navController.navigate(MOVIE_SCREEN.route)},
                    onNavigateToSignInScreen = {navController.navigate(SIGN_IN_SCREEN.route)}
                )
            }
            composable(MOVIE_SCREEN.route) {
                MovieScreen(
                    onNavigateToFavouritesScreen = { navController.navigate(FAVORITES_SCREEN.route) },
                    onNavigateToHomeScreen = { navController.navigate(HOME_SCREEN.route) },
                    moviesViewModel = movieViewModel,
                    userViewModel = userViewModel
                )
            }
            composable(SIGN_IN_SCREEN.route) {
                SignInScreen(
                    userViewModel = userViewModel,
                    movieViewModel = movieViewModel,
                    onNavigateToHome =  { navController.navigate(HOME_SCREEN.route) }
                )
            }
            paddingValues.apply { }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MoviesTheme {
        MoviesApp("Android")
    }
}