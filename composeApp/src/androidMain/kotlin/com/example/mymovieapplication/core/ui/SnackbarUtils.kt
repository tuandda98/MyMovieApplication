package com.example.mymovieapplication.core.ui

import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun ErrorSnackbarHandler(
    scaffoldState: ScaffoldState,
    errorMessage: String?,
    duration: SnackbarDuration = SnackbarDuration.Long
) {
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            scaffoldState.snackbarHostState.showSnackbar(
                message = message,
                duration = duration
            )
        }
    }
}