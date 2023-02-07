@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package com.cb.migrationtest

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cb.migrationtest.ui.theme.MigrationTestTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MigrationTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        UserScreen(this@HomeActivity)
                    }
                }
            }
        }
    }
}


@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun UserScreen(
    homeActivity: HomeActivity, viewModel: UserViewModel = hiltViewModel(),
) {
    val users by viewModel.users.observeAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Home",
                        modifier = Modifier.padding(top = 3.dp), maxLines = 1
                    )
                },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            val inputText = remember { mutableStateOf("") }
            InputName(inputText)
            SaveButton(viewModel, inputText, homeActivity)
            LoadUsers(users, viewModel)

        }
    }
}

@Composable
fun LoadUsers(users: List<UserRecord>, viewModel: UserViewModel) {
    if (users.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.surface
                )
        ) {
            items(users) { user ->
                SingleUser(user, viewModel)
            }
        }
    }
}

@Composable
fun SingleUser(user: UserRecord, viewModel: UserViewModel) {
    ListItem(
        headlineText = {
            Text(
                text = user.userName,
                style = MaterialTheme.typography.titleMedium,

                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

        },
        leadingContent = ({
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "icon",
            )

        }),
        trailingContent = ({
            IconButton(onClick = { viewModel.deleteUser(user.id) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "icon",
                )
            }
        })
    )
}


@Composable
fun SaveButton(
    viewModel: UserViewModel,
    inputText: MutableState<String>,
    homeActivity: HomeActivity
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Card(
            modifier = Modifier
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(12.dp),
        )
        {
            TextButton(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        val id = viewModel.insertUser(UserRecord(userName = inputText.value))
                        if (id < 0) {
                            homeActivity.runOnUiThread {
                                Toast.makeText(
                                    homeActivity,
                                    "User already exists",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            )
            {
                Text(
                    text = "Save",
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Composable
fun InputName(inputText: MutableState<String>) {
    var showClearButton by remember { mutableStateOf(false) }


    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()

    }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .onFocusChanged { focusState ->
                showClearButton = (focusState.isFocused)
            }
            .focusRequester(focusRequester)
            .drawBehind {
                val strokeWidth = Stroke.DefaultMiter
                val y = size.height - strokeWidth / 2

                drawLine(
                    Color.Green,
                    Offset(0f, y),
                    Offset(size.width, y),
                    strokeWidth
                )
            },
        shape = RoundedCornerShape(12.dp),
        value = inputText.value,

        onValueChange = { newText ->
            inputText.value = newText
        },
        singleLine = false,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Blue,
            unfocusedBorderColor = Color.Gray,
            cursorColor = Color.Blue,
            textColor = Color.Black,
        ),
        label = {
            Text(
                text = "User name",
                color = Color.Gray
            )
        },
        keyboardActions = KeyboardActions(onDone = {
            softwareKeyboardController?.hide()
        }),

        trailingIcon = {
            AnimatedVisibility(
                visible = inputText.value.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(onClick = {
                    inputText.value = ""
                }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear",
                    )
                }

            }
        },

        maxLines = 1
    )
}

