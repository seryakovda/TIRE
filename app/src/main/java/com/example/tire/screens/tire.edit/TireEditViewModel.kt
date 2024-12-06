package com.example.tire.screens.main.tire

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.tire.data.ImageList
import com.example.tire.data.ImageListLocal
import com.example.tire.data.Repository
import com.example.tire.data.TireDataStore
import com.example.tire.data.TireList
import com.example.tire.di.ComposeFileProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject


@HiltViewModel
class TireEditViewModel @Inject constructor(
    private var repository: Repository,
    private var tireDataStore: TireDataStore,
    private var context:Context
): ViewModel() {
    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    // globally declare LocationRequest
    private lateinit var locationRequest: LocationRequest

    // globally declare LocationCallback
    private lateinit var locationCallback: LocationCallback


    val tireEditData: MutableLiveData<TireList?> = MutableLiveData()
    val imageList: MutableLiveData<List<ImageList>> = MutableLiveData()
    val imageListLocal: MutableLiveData<List<ImageListLocal>> = MutableLiveData()
    val coordinatePhoto: MutableLiveData<String> = MutableLiveData("")
    var hasImage: MutableLiveData<Boolean> = MutableLiveData(false)

    var bitmap: MutableLiveData<Bitmap> = MutableLiveData()

    init {
        getLocationUpdates()
        startLocationUpdates()
        viewModelScope.launch(Dispatchers.IO) {
            tireDataStore.getAccessToken.collect {
                if (it != null) {
                    tireEditData.postValue(it)
                    launch {
                        imageList.postValue(repository.getImageList(it))
                        imageListLocal.postValue(repository.getFilesToUpload(it.SN))
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */
        )
    }
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
    fun getLocationUpdates() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        locationRequest = LocationRequest()
        locationRequest.interval = 1000
        locationRequest.fastestInterval = 1000
        locationRequest.smallestDisplacement = 1f // 170 m = 0.1 mile
        locationRequest.priority =
            LocationRequest.PRIORITY_HIGH_ACCURACY //set according to your app function
        locationCallback = object : LocationCallback() {
            override  fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return

                if (locationResult.locations.isNotEmpty()) {
                    // get latest location
                    val location =
                        locationResult.lastLocation
                    if (location != null) {
                        val mLatitude:Double = location.latitude
                        val mLongitude:Double = location.longitude
                        coordinatePhoto.value = "[${mLatitude},${mLongitude}]"
                    }
                    // use your location object
                    // get latitude , longitude and other info from this
                }


            }
        }
    }

    fun getSessionHandle():String{
        return repository.getSessionHandle()
    }

    fun getImageList() {
        viewModelScope.launch(Dispatchers.IO) {
            val v = tireDataStore.getAccessToken.asLiveData().value
            val vv = Json.encodeToString(v)
            imageList.postValue(repository.getImageList(v))
        }
    }

    override fun onCleared() {
        stopLocationUpdates()
        super.onCleared()
    }


    fun insertFilesToUpload(fileName:String,coordinatePhoto:String,idTireEvents:Int)
    {
        var id_image:String = ""

        viewModelScope.launch(Dispatchers.IO) {
            repository.insertFilesToUpload(fileName,coordinatePhoto,idTireEvents) //сохраняет в локальном репозитории все данные для последующей загрузки на сервер
            imageListLocal.postValue(
                repository.getFilesToUpload(repository.getSN()) // обновляет данные с для отображения на странице с шиной
            )
           // repository.uploadAllImageFromLocalTableByName(fileName,id_image)

            //repository.uploadAllImageFromLocalTable()
        }
    }

    fun insertFilesToUploadFromGallery(listUriFromGallery:List<Uri>?,idTireEvents:Int)
    {
        listUriFromGallery?.forEach{

            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            val file = File.createTempFile(
                "selected_image_",
                ".jpg",
                directory,
            )

            val authority = context.packageName + ".FileProvider"
            val fp = FileProvider.getUriForFile(
                context,
                authority,
                file,
            )

            val fileName = fp.toString()
                //Log.i("MyMSG",fileName)
            val stream = context.contentResolver.openInputStream(it)

            org.apache.commons.io.FileUtils.copyInputStreamToFile(stream,file)
            insertFilesToUpload(fileName,"[]",idTireEvents)
        }
        addWorkToWorker()
    }

    fun addWorkToWorker()
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addWorkToWorker()
        }
    }

    fun setIdImageForViewFullScreen(id_image:String){
            repository.setIdImageForViewFullScreen(id_image)
    }

    fun getIdImageForViewFullScreen(){
        repository.getIdImageForViewFullScreen()
    }
}
