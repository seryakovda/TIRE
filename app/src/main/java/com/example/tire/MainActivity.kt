package com.example.tire

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog

import androidx.navigation.compose.rememberNavController
import com.example.tire.ui.theme.TireTheme

import com.example.tire.screens.auth.authError.AuthScreenError

import com.example.tire.screens.auth.AuthViewModel
import com.example.tire.screens.auth.enterLogin.AuthScreen2
import com.example.tire.screens.tire.edit.TireEditScreen
import com.example.tire.screens.selection.tire.SelectionTireScreen
import com.example.tire.screens.tire.edit.TireEditCatalogBrandScreen
import com.example.tire.screens.tire.edit.TireEditCatalogChassisNumberScreen
import com.example.tire.screens.tire.edit.TireEditCatalogDivisionScreen
import com.example.tire.screens.tire.edit.TireEditCatalogModelScreen
import com.example.tire.screens.tire.edit.TireEditCatalogStandardsSizeScreen
import com.google.android.gms.location.FusedLocationProviderClient


import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tire.data.Repository
import com.example.tire.screens.tire.edit.TireEditSelectEventScreen
import com.example.tire.screens.tire.view.TireViewRemoteImageScreen
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    //private lateinit var mainBinding: ActivityMainBinding
    //val uploadImagesViewModel: UploadImagesViewModel by viewModels()
    override fun onResume() {
        super.onResume()
        initLocation()
        initCamera()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //private val viewModel by viewModels()
        super.onCreate(savedInstanceState)
        //requestCameraPermition()
        setContent {
            TireTheme{
                //AuthScreen()
                //TireNavHost()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TireNavHost()
                }

            }
        }
//------------------------------------------------------------


//------------------------------------------------------------
    }

    //private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2
    @SuppressLint("MissingPermission", "SetTextI18n")
    fun initLocation() {
        if (checkPermissions()) {

        } else {
            requestPermissions()
        }
    }
    fun initCamera(){
        if (checkPermissionsCamera()) {

        } else {
            requestPermissionsCamera()
        }
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }

    private fun checkPermissionsCamera(): Boolean {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
    private fun requestPermissionsCamera() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.CAMERA
            ),
            permissionId
        )
    }
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                initLocation()
            }
        }
    }


}



//******************************************************************************************
//******************************************************************************************
//******************************************************************************************
//******************************************************************************************
//******************************************************************************************
//******************************************************************************************
//******************************************************************************************


sealed class Destination(val route: String) {
    object AuthScreen: Destination("AuthScreen")
    object AuthScreenError: Destination("AuthScreenError")
    object ScreenMainJob: Destination("ScreenMainJob")
    object ScreenEditTire: Destination("ScreenEditTire")
    object ScreenEditTireBrand: Destination("ScreenEditTireBrand")
    object ScreenEditTireStandardSize: Destination("ScreenEditTireStandardSize")
    object ScreenEditTireModel: Destination("ScreenEditTireModel")
    object ScreenEditTireDivision: Destination("ScreenEditTireDivision")
    object ScreenEditTireChassisNumber: Destination("ScreenEditTireChassisNumber")
    object ScreenViewRemoteImage: Destination("ScreenViewRemoteImage")
    object ScreenAddEvent: Destination("ScreenAddEvent")

}



@SuppressLint("RestrictedApi")
@Composable
fun TireNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Destination.AuthScreen.route
) {
    val navHostViewModel = hiltViewModel<NavHostViewModel>()
    val viewModel = hiltViewModel<AuthViewModel>()



    val state = navHostViewModel.authState.observeAsState()
    when (state.value) {
        NavHostViewModel.AuthState.FAIL ->                  navController.navigate(Destination.AuthScreenError.route)
        NavHostViewModel.AuthState.AUTH ->                  navController.popBackStack()
        NavHostViewModel.AuthState.SUCCESS ->               navController.navigate(Destination.ScreenMainJob.route)
        NavHostViewModel.AuthState.EDIT ->                  navController.navigate(Destination.ScreenEditTire.route)
        NavHostViewModel.AuthState.EDIT_BRAND ->            navController.navigate(Destination.ScreenEditTireBrand.route)
        NavHostViewModel.AuthState.EDIT_STANDARD_SIZE ->    navController.navigate(Destination.ScreenEditTireStandardSize.route)
        NavHostViewModel.AuthState.EDIT_MODEL ->            navController.navigate(Destination.ScreenEditTireModel.route)
        NavHostViewModel.AuthState.EDIT_DIVISION ->         navController.navigate(Destination.ScreenEditTireDivision.route)
        NavHostViewModel.AuthState.EDIT_CHASSIS_NUMBER ->   navController.navigate(Destination.ScreenEditTireChassisNumber.route)
        NavHostViewModel.AuthState.VIEW_REMOTE_IMAGE ->     navController.navigate(Destination.ScreenViewRemoteImage.route)
        NavHostViewModel.AuthState.ADD_EVENT ->             navController.navigate(Destination.ScreenAddEvent.route)

        else -> {}
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {


        composable(Destination.AuthScreen.route){
//            val lambda: (String, String) -> Unit = { login, pass, ->
//                viewModel.autorisation(login, pass)
//            }
            //AuthScreen(lambda)

            AuthScreen2(navController = navController)
        }

        composable(Destination.AuthScreenError.route) {
            AuthScreenError(
                navController = navController
            )
        }

        composable(Destination.ScreenMainJob.route) {
            SelectionTireScreen(navController = navController)
        }

        composable(Destination.ScreenEditTire.route) {
            TireEditScreen(navController = navController)
        }

        dialog(Destination.ScreenEditTireBrand.route) {
            TireEditCatalogBrandScreen(navController = navController)
        }

        dialog(Destination.ScreenEditTireStandardSize.route) {
            TireEditCatalogStandardsSizeScreen(navController = navController)
        }

        dialog(Destination.ScreenEditTireModel.route) {
            TireEditCatalogModelScreen(navController = navController)
        }

        dialog(Destination.ScreenEditTireDivision.route) {
            TireEditCatalogDivisionScreen(navController = navController)
        }

        dialog(Destination.ScreenEditTireChassisNumber.route){
            TireEditCatalogChassisNumberScreen(navController = navController)
        }

        composable(Destination.ScreenViewRemoteImage.route){
            TireViewRemoteImageScreen(navController = navController)
        }

        dialog(Destination.ScreenAddEvent.route){
            TireEditSelectEventScreen(navController = navController)
        }
    }
}






@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
//    MessageCard(
//        msg = Message(name,"bigTeST")
//    )

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {

    TireTheme {
        Greeting("Android!!")
    }
}

