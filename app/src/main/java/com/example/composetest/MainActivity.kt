package com.example.composetest

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composetest.ui.theme.ComposeTestTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTestTheme(darkTheme = true) {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        LoginForm()
                    }
                }
            }
        }
    }
}


@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LoginForm() {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val lifecycle = rememberCoroutineScope()


    fun doLogin(): Boolean {
        if (isLoading) return false
        isLoading = true
        validateInput(email = email.trim(), password = password.trim())?.let {
            Log.d("mridx", "doLogin: $it")
            lifecycle.launch {
                delay(2000)
                isLoading = false
            }
            return true
        }
        isLoading = false
        Toast.makeText(context, "Enter credentials properly !", Toast.LENGTH_SHORT).show()
        return false
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
    ) {
        //Spacer(modifier = Modifier.fillMaxSize().weight(1f).then(Modifier.background(color = Color.Red)))
        ExpandedBox(
            modifier = Modifier
                .fillMaxSize()
                .weight(1.0f)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Hello World !")
            }
        }
        Box(
            modifier = Modifier
                .wrapContentHeight()
        )
        {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(vertical = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                OutlinedTextField(
                    value = email, onValueChange = {
                        email = it
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .then(Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp)),
                    label = {
                        Text(
                            text = "Email"
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                        autoCorrect = true,
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    })
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp)),
                    label = {
                        Text(
                            text = "Password"
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Go,
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) },
                        onGo = {
                            focusManager.clearFocus(true)
                            doLogin()
                        }
                    )
                )
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            focusManager.clearFocus(true)
                            doLogin()
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .then(Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp))
                            .then(Modifier.height(56.dp))
                    ) {
                        if (!isLoading)
                            Text(text = "Login", fontSize = 20.sp, fontWeight = FontWeight.W600)
                    }
                    this@Column.AnimatedVisibility(
                        visible = isLoading,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier
                                .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                        )
                    }
                }
            }

        }


    }
}

@Composable
fun header() {
    Text(text = "Hello World !")
}

fun validateInput(email: String, password: String): JSONObject? {
    if (email.isEmpty() || !email.contains("@") || password.isEmpty()) return null
    return JSONObject().apply {
        put("email", email)
        put("password", password)
    }
}

/*@Composable
fun ExpandedBox(modifier: Modifier, content: @Composable () -> Unit) {
    Layout(
        content = { content.invoke() },
        modifier = modifier
    ) { measurables, constraints ->
        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.map { measurable ->
            // Measure each children
            measurable.measure(constraints)
        }

        // Set the size of the layout as big as it can
        layout(constraints.maxWidth, constraints.maxHeight) {
            // Track the y co-ord we have placed children up to
            var yPosition = 0

            // Place children in the parent layout
            placeables.forEach { placeable ->
                // Position item on the screen
                placeable.placeRelative(x = 0, y = yPosition)

                // Record the y co-ord placed up to
                yPosition += placeable.height
            }
        }
    }
}*/


