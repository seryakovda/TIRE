package com.example.tire.screens.tire.edit

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.health.connect.datatypes.ExerciseRoute
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.tire.Destination
import com.example.tire.screens.main.tire.TireEditViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import coil.compose.rememberImagePainter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.io.File
import java.io.IOException
import java.util.Locale
import java.util.UUID
import java.util.concurrent.TimeUnit


@Composable
fun TireEditScreen(
    navController: NavHostController,
    viewModel: TireEditViewModel = hiltViewModel()
) {

    val tireEditData        by viewModel.tireEditData.observeAsState()
    val imageList           by viewModel.imageList.observeAsState()
    val imageListLocal      by viewModel.imageListLocal.observeAsState()
    val coordinatePhoto     by viewModel.coordinatePhoto.observeAsState()

    val sessionHandle = viewModel.getSessionHandle();

    val context = LocalContext.current

    // deleteFile("/data/user/0/com.example.tire/cache/image.png")
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
        viewModel.insertFilesToUpload(imageUri.value.toString(),coordinatePhoto!!,idTireEvents.value)
        hasImage.value = false
    }

    if (hasIriFormGallery.value){
        viewModel.insertFilesToUploadFromGallery(uriFormGallery.value,idTireEvents.value)
        hasIriFormGallery.value = false
    }


    TireEditContent(
        tireEditData,
        navigateToSelect =  navController ,
        imageList,
        imageListLocal,
        sessionHandle,
        context,
        imageUri = {imageUri.value = it},
        hasImage = {hasImage.value = it},
        viewModel::setIdImageForViewFullScreen,
        uriFormGallery = {uriFormGallery.value = it},
        hasIriFormGallery = {hasIriFormGallery.value = it},
        idTireEvents = {idTireEvents.value = it}

    )
}





