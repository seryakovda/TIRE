package com.example.tire.screens.tire.edit

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.tire.screens.main.tire.TireEditViewModel

@Composable
fun TireEditScreen(
    navController: NavHostController,
    viewModel: TireEditViewModel = hiltViewModel()
) {

    val tireEditData by viewModel.tireEditData.observeAsState()
    val imageList by viewModel.imageList.observeAsState()
    val imageListLocal by viewModel.imageListLocal.observeAsState()
    val coordinatePhoto by viewModel.coordinatePhoto.observeAsState()

    val sessionHandle = viewModel.getSessionHandle()

    val context = LocalContext.current

    var hasImage = remember {
        mutableStateOf(false)
    }

    var imageUri = remember {
        mutableStateOf<Uri?>(null)
    }

    var uriFormGallery = remember {
        mutableStateOf<List<Uri>?>(null)
    }

    var hasIriFormGallery = remember {
        mutableStateOf(false)
    }

    var idTireEvents = remember {
        mutableStateOf(0)
    }

    if (hasImage.value) {
        viewModel.insertFilesToUpload(
            imageUri.value.toString(),
            coordinatePhoto!!,
            idTireEvents.value
        )
        hasImage.value = false
    }

    if (hasIriFormGallery.value) {
        viewModel.insertFilesToUploadFromGallery(
            uriFormGallery.value,
            idTireEvents.value
        )
        hasIriFormGallery.value = false
    }

    TireEditContent(
        tireEditData,
        navigateToSelect = navController,
        imageList,
        imageListLocal,
        sessionHandle,
        context,
        remoteSource = viewModel.remoteSource,
        imageUri = {
            imageUri.value = it
        },
        hasImage = {
            hasImage.value = it
        },
        setIdImageForViewFullScreen = viewModel::setIdImageForViewFullScreen,
        uriFormGallery = {
            uriFormGallery.value = it
        },
        hasIriFormGallery = {
            hasIriFormGallery.value = it
        },
        idTireEvents = {
            idTireEvents.value = it
        }
    )
}