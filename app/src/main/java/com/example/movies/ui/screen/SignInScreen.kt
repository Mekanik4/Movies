package com.example.movies.ui.screen

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movies.R
import com.example.movies.domain.User
import com.example.movies.ui.screen.viewmodel.MovieViewModel
import com.example.movies.ui.screen.viewmodel.UserViewModel
import com.example.movies.utils.Constants

@Composable
fun SignInScreen(
    userViewModel: UserViewModel,
    movieViewModel: MovieViewModel,
    onNavigateToHome: () -> Unit,
) {

    SignInScreenNoDependencies(
        onNavigateToHome = onNavigateToHome,
        getSignedIn = { userViewModel.isConnected() },
        setSignedInUser = { userViewModel.setConnected(it) },
        setUsername = {userViewModel.setUsername(it)},
        userExists = {userViewModel.userExist(it)},
        saveUser = { userViewModel.save(it) },
        initMovies = { movieViewModel.getAllMovies(1) }
    )
}

@Composable
private fun SignInScreenNoDependencies(
    onNavigateToHome: () -> Unit,
    getSignedIn: () -> Boolean,
    setSignedInUser: (Boolean) -> Unit,
    setUsername: (String) -> Unit,
    userExists: (String) -> Boolean,
    saveUser: (User) -> Unit,
    initMovies: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    if (getSignedIn()) {
        LaunchedEffect(Unit) {
            onNavigateToHome()
        }
    }

    Column(
        modifier = Modifier
            .wrapContentHeight(Alignment.CenterVertically)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(Modifier.width(IntrinsicSize.Min)) {
            Icon(
                painterResource(id = R.drawable.movies_app_icon_foreground),
                contentDescription = "app icon",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(150.dp)
//                    .background(color = Color(229, 36, 58))
                    .drawBehind {
                        drawCircle(
                            color = Color(229, 36, 58), radius = this.size.maxDimension / 2
                        )
                    },
                tint = Color.Unspecified
            )
            Text(
                stringResource(id = R.string.sign_in_text),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 28.dp),
                style = MaterialTheme.typography.headlineMedium
            )
            val primaryColor = MaterialTheme.colorScheme.primary
            val errorColor = MaterialTheme.colorScheme.error
            val containerColor = MaterialTheme.colorScheme.background
            var outlinedTextFieldColor by remember { mutableStateOf(primaryColor) }
            OutlinedTextField(
                value = username,
                onValueChange = { username = it; outlinedTextFieldColor = primaryColor },
                label = { Text(stringResource(id = R.string.username)) },
                modifier = Modifier.padding(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = outlinedTextFieldColor,
                    focusedLabelColor = outlinedTextFieldColor,
                    unfocusedIndicatorColor = outlinedTextFieldColor,
                    unfocusedLabelColor = outlinedTextFieldColor,
                    containerColor = containerColor
                )
            )
            val passwordVisible by rememberSaveable { mutableStateOf(false) }
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; outlinedTextFieldColor = primaryColor },
                label = { Text("Password") },
                modifier = Modifier.padding(12.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = outlinedTextFieldColor,
                    focusedLabelColor = outlinedTextFieldColor,
                    unfocusedIndicatorColor = outlinedTextFieldColor,
                    unfocusedLabelColor = outlinedTextFieldColor,
                    containerColor = containerColor
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )
            Button(
                onClick = {
                    if (signInUser(username, password)) {
                        setSignedInUser(true)
                        setUsername(username)
                        if (!userExists(username))
                            saveUser(User(username = username))
                        initMovies()
                        onNavigateToHome()
                    } else {
                        outlinedTextFieldColor = errorColor
                        Toast.makeText(
                            context,
                            context.getString(R.string.wrong_credentials),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                Text(stringResource(id = R.string.sign_in_text))
            }
        }
    }
    BackHandler {
        val activity = context as? Activity
        activity?.finish()
    }
}

fun signInUser(username: String, password: String): Boolean {
    val userPassMap = Constants.USER_MAP
    return userPassMap.containsKey(username) && userPassMap[username] == password
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {

}