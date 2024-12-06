package com.example.tire.data

import android.content.Context
import android.util.Log
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.LifecycleObserver
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class Repository @Inject constructor(
    @ApplicationContext
    private var context: Context,
    private var remoteSource: RemoteSource,
    private var localSource: LocalSource,
    private var tireDataStore: TireDataStore
) {

    fun setLogin(login:String)
    {
        localSource.saveTokenString("login",login)
    }
    fun getLogin():String
    {
        var login = localSource.getTokenString("login");
        if (login == null)
            login = ""
        return login
    }

    fun setPassword(password:String)
    {
        localSource.saveTokenString("password",password)
    }
    fun getPassword():String
    {
        var login = localSource.getTokenString("password");
        if (login == null)
            login = ""
        return login
    }

    fun startApp(){
        localSource.saveTokenString("startApp","1")
    }

    fun startLoadImage(){
        localSource.saveTokenString("startApp","2")
    }

    fun endLoadImage(){
        startApp()
    }

    fun getStatusLoadImage():String{
        return localSource.getTokenString("startApp")
    }

    fun getSessionHandle():String{
        return localSource.getTokenString("sessionHandle")
    }
    suspend fun requestSession() {
        val result = remoteSource.requestSession()
        if (result.isNotBlank())
            localSource.saveTokenString("sessionHandle",result)

    }

    suspend fun autorisation(login: String, pass: String) {
        requestSession()
        var sessionHandle: String
        sessionHandle = getSessionHandle()

        val result  = remoteSource.autorisation(login, pass, sessionHandle)
        if (result){
            setLogin(login)
            setPassword(pass)
            //startApp()
            addWorkToWorker()
        }
        localSource.saveTokenBoolean("ststusAutorisation",result)
    }



    fun addWorkToWorker(){
//        val inputData = workDataOf("first" to 1, "second" to 2)
//        val  myWorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
//            .setInputData(inputData)
//            .addTag("myTag").build()




        var addWork = false
        val res1 = WorkManager.getInstance(context).getWorkInfosByTag("myTagWork").get()
        if (res1.count() == 0) {
            addWork = true
        }else{
            if (res1.get(0).state.toString() == "SUCCEEDED") {
                //Log.i("MyMSG", "SUCCEEDED")
                addWork = true
            }
            if (res1.get(0).state.toString() == "CANCELLED") {
                //Log.i("MyMSG", "CANCELLED")
                addWork = true
            }
        }


        if (addWork ){
            val re1s = WorkManager.getInstance(context).cancelAllWork()
           // Log.i("MyMSG", "RES0-" + re1s.result)

            val inputData = workDataOf("msg" to 0)
            //Log.i("MyMSG", "Add Work")
            val myWorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
                .setInputData(inputData)
                .addTag("myTagWork")
                .build()
            WorkManager.getInstance(context).beginWith(myWorkRequest)
                .enqueue()
            //.enqueue(myWorkRequest)

            // WorkManager.getInstance(context).beginWith(myWorkRequest)
        }

        val res = WorkManager.getInstance(context).getWorkInfosByTag("myTagWork").get()
        //Log.i("MyMSG", "RES2-" + res.toString())
    }


    fun getStatusAutorisation(): Boolean{
        return localSource.getTokenBoolean("ststusAutorisation")
    }

    suspend fun setDataTireForEdit(tireList:TireList){
        localSource.saveTokenString("SN",tireList.SN)
        val sessionHandle = getSessionHandle()
        val tireList = remoteSource.getFullTire(sessionHandle,tireList.SN)
        val vv = Json.encodeToString(tireList)
        localSource.saveTokenString("tireList",vv)
        refreshTire()
    }
    suspend fun refreshTire()
    {
        val vv = localSource.getTokenString("tireList")
        val tireList = Json.decodeFromString<TireList>(vv)
        tireDataStore.saveToken(tireList)
    }
    fun getDataTireField_forEdit(nameField:String):String{
        return localSource.getTokenString(nameField)
    }


    suspend fun getListTire(serialNumber: String?, chassisNumber: String?):List<TireList> {
        val sessionHandle = getSessionHandle()
        val res = remoteSource.getListTire(sessionHandle,serialNumber,chassisNumber)
        return res
    }

    suspend fun addTire(serialNumber: String?):List<TireList> {
        val sessionHandle = getSessionHandle()
        val res = remoteSource.addTire(sessionHandle,serialNumber)
        return res
    }

    suspend fun getImageList(tireDataClass:TireList?):List<ImageList> {

        val serialNumber = tireDataClass?.SN
        val sessionHandle = getSessionHandle()
        val res = remoteSource.getImageList(sessionHandle,serialNumber)
        return res
    }

     suspend fun getListBrand(name:String):List<String>{
         var retList:List<String>?
         retList = mutableListOf()

         val sessionHandle = getSessionHandle()
         val brand = remoteSource.loadBrand(sessionHandle,name)
         brand.forEach(){
             retList.add(it.name)
         }

         return retList
    }

    suspend fun getListStandardSize(name:String):List<String>{
        var retList:List<String>?
        retList = mutableListOf()

        val sessionHandle = getSessionHandle()
        val brand = remoteSource.loadStandardSize(sessionHandle,name)
        brand.forEach(){
            retList.add(it.name)
        }

        return retList
    }

    suspend fun getListModel(name:String):List<String>{
        var retList:List<String>?
        retList = mutableListOf()

        val sessionHandle = getSessionHandle()
        val brand = remoteSource.loadModel(sessionHandle,name)
        brand.forEach(){
            retList.add(it.name)
        }

        return retList
    }

    suspend fun getListDivision(name:String):List<String>{
        var retList:List<String>?
        retList = mutableListOf()

        val sessionHandle = getSessionHandle()
        val brand = remoteSource.loadDivision(sessionHandle,name)
        brand.forEach(){
            retList.add(it.name)
        }

        return retList
    }

    suspend fun getListChassisNumber(name:String):List<String>{
        var retList:List<String>?
        retList = mutableListOf()

        val sessionHandle = getSessionHandle()
        val brand = remoteSource.loadChassisNumber(sessionHandle,name)
        brand.forEach(){
            retList.add(it.name)
        }

        return retList
    }
    suspend fun getListEvents(name:String):List<TireEvents>{
        val sessionHandle = getSessionHandle()
        return remoteSource.LoadListEvents(sessionHandle,name)
    }


    suspend fun setValueBrand(value:String){
        tireDataStore.setValueBrand(value)
    }


    suspend fun setValueStandardSize(value:String){
        tireDataStore.setValueStandardSize(value)
    }


    suspend fun setValueModel(value:String){
        tireDataStore.setValueModel(value)
    }


    suspend fun setValueDivision(value:String){
        tireDataStore.setValueDivision(value)
    }


    suspend  fun setValueChassisNumber(value:String){
        tireDataStore.setValueChassisNumber(value)
    }

    fun getSN():String{
        return localSource.getTokenString("SN")
    }
    suspend fun setValueItemTire_Remote(nameField:String,value:String){
        val sessionHandle = getSessionHandle()
        val SN = getSN()
        remoteSource.updateTireItem(sessionHandle,SN, nameField,value)
    }

    suspend fun addEventForTire(id:Int){
        val sessionHandle = getSessionHandle()
        val SN = getSN()
        remoteSource.addEventForTire(sessionHandle,SN, id)

        val tireList = remoteSource.getFullTire(sessionHandle,SN)
        val vv = Json.encodeToString(tireList)
        localSource.saveTokenString("tireList",vv)

    }
    suspend fun insertFilesToUpload(fileName:String,coordinatePhoto:String,idTireEvents:Int):String{
        val SN = getSN()
        val sessionHandle = getSessionHandle()
        val id_image = remoteSource.insertFilesToUpload(sessionHandle,SN,fileName,coordinatePhoto,idTireEvents)
        localSource.insertFilesToUpload(SN,fileName,id_image,coordinatePhoto,idTireEvents)

        return id_image
    }


    suspend fun getFilesToUpload(SN:String):List<ImageListLocal>{
        val sessionHandle = getSessionHandle()
        return remoteSource.getFilesToUpload(sessionHandle,SN)
    }


    fun setIdImageForViewFullScreen(id_image:String){
        localSource.saveTokenString("IdImageForViewFullScreen",id_image)
    }
    fun getIdImageForViewFullScreen():String{
        return  localSource.getTokenString("IdImageForViewFullScreen")
    }



}

