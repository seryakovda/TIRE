package com.example.tire.screens.tire.view

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun TireViewRemoteImageScreen(
    navController: NavHostController,
    viewModel: TireViewViewModel = hiltViewModel()
) {
    val sessionHandle = viewModel.getSessionHandle()
    val idImage = viewModel.getIdImage()

    TireViewContent(
        sessionHandle = sessionHandle,
        id_image = idImage,
        remoteSource = viewModel.remoteSource
    )
}