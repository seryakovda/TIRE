package com.example.tire.screens.tire.edit

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.datastore.preferences.protobuf.Value
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.tire.data.Repository
import com.example.tire.data.TireDataStore
import com.example.tire.data.TireEvents
import com.example.tire.data.brand.BrandItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.system.measureTimeMillis


@HiltViewModel
class TireEditCatalogViewModel @Inject constructor(
    private var repository: Repository,
): ViewModel() {

    val filterString: MutableLiveData<String> = MutableLiveData("")
    val listTireEvents: MutableLiveData<List<TireEvents>> = MutableLiveData()
    var listString: MutableLiveData<List<String>> = MutableLiveData()



    fun getCatalogBrand(name:String){
        filterString.value = name
        viewModelScope.launch(Dispatchers.IO) {
            listString.postValue(  repository.getListBrand(name))
        }
    }

    fun getCatalogStandardSize(name:String){
        filterString.value = name
        viewModelScope.launch(Dispatchers.IO) {
            listString.postValue(  repository.getListStandardSize(name))
        }
    }

    fun getCatalogModel(name:String){
        filterString.value = name
        viewModelScope.launch(Dispatchers.IO) {
            listString.postValue(  repository.getListModel(name))
        }
    }

    fun getCatalogDivision(name:String){
        filterString.value = name
        viewModelScope.launch(Dispatchers.IO) {
            listString.postValue(  repository.getListDivision(name))
        }
    }

    fun getCatalogChassisNumber(name:String){
        filterString.value = name
        viewModelScope.launch(Dispatchers.IO) {
            listString.postValue(  repository.getListChassisNumber(name))
        }
    }

    fun getListEvents(name:String){
        filterString.value = name
        viewModelScope.launch(Dispatchers.IO) {
            listTireEvents.postValue(  repository.getListEvents(name))
        }
    }

    fun addEventForTire(id:Int)
    {
        runBlocking(Dispatchers.IO) {
            repository.addEventForTire(id)

        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.refreshTire()
        }
    }
     fun setValueBrand(value:String){
         viewModelScope.launch(Dispatchers.IO) {
             repository.setValueBrand(value)
         }
         runBlocking(Dispatchers.IO) {
             repository.setValueItemTire_Remote("brand", value)
         }
    }

    fun setValueStandardSize(value:String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.setValueStandardSize(value)
        }
        runBlocking(Dispatchers.IO) {
            repository.setValueItemTire_Remote("standardSize", value)
        }
    }


    fun setValueModel(value:String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.setValueModel(value)
        }
        runBlocking(Dispatchers.IO) {
            repository.setValueItemTire_Remote("model", value)
        }
    }


    fun setValueDivision(value:String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.setValueDivision(value)
        }
        runBlocking(Dispatchers.IO) {
            repository.setValueItemTire_Remote("division", value)
        }
    }


    fun setValueChassisNumber(value:String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.setValueChassisNumber(value)
        }
        runBlocking(Dispatchers.IO) {
            repository.setValueItemTire_Remote("chassisNumber", value)
        }
    }
}