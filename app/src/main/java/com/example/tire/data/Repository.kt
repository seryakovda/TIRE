package com.example.tire.data

import android.content.Context
import android.util.Log
import androidx.camera.core.processing.SurfaceProcessorNode.In
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import com.example.tire.data.brand.BrandItem
import com.example.tire.data.filesToUpload.FilesToUploadItem
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.UUID
import javax.inject.Inject


class Repository @Inject constructor(
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
            startApp()
        }
        localSource.saveTokenBoolean("ststusAutorisation",result)
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
        val id_image = UUID.randomUUID().toString()
        localSource.insertFilesToUpload(SN,fileName,id_image,coordinatePhoto,idTireEvents)
        return id_image
    }

//    suspend fun uploadAllImageFromLocalTableByName(fileName:String,id_image:String){
//        val sessionHandle = getSessionHandle()
//        val fileName1 = fileName.replace("content://com.example.tire.FileProvider/root","",true)
//        val imageBody:ByteArray = File(fileName1).readBytes()
//        val hashSHA2_512:String = encryptString(imageBody)
//
//        localSource.updateFilesToUpload(id_image,hashSHA2_512)
//
//        if (remoteSource.uploadPhoto(sessionHandle,id_image,imageBody)){
//            val hashSHA2_512_fromServer = remoteSource.getHashImageFromServer(sessionHandle,id_image)
//            if (hashSHA2_512_fromServer == hashSHA2_512)
//                localSource.deleteFilesToUploadFromLocalTable(fileName)
//        }
//    }

    suspend fun getFilesToUpload(SN:String):List<ImageListLocal>{
        return localSource.getFilesToUpload(SN)
    }

    suspend fun uploadAllImageFromLocalTable()
    {
        val sessionHandle = getSessionHandle()
        remoteSource.messageLogToServer(sessionHandle,"проверяем ведётся ли загрузка файлов")
        if (getStatusLoadImage() == "1") {// проверяем ведётся ли загрузка файлов
            startLoadImage()// если не ведётся устанавливаем значение в статус  (начало загрузки файлов)
            remoteSource.messageLogToServer(sessionHandle,"если не ведётся устанавливаем значение в статус  (начало загрузки файлов)")

            val allFiles = localSource.gelAllFilesToUpload()
            remoteSource.messageLogToServer(sessionHandle,"Получение файлов для отправки")
            val kol = allFiles.count()
            remoteSource.messageLogToServer(sessionHandle,kol.toString())

            var colFileForUpload = 0
            allFiles.forEach(){
                colFileForUpload ++
                val SN = it.SN

                remoteSource.messageLogToServer(sessionHandle,"$SN Имя файла")
                remoteSource.messageLogToServer(sessionHandle,"$SN ${it.fileName}")
                if (it.fileName != "null") {

                    // регестрируем на сервере шапку с проверкой уникальности idImage
                    // в случае если регистрируемая idImage совпала с имеющимися в БД
                        // то возвращается новое значение
                    remoteSource.messageLogToServer(sessionHandle,"$SN id_image до провеки")
                    remoteSource.messageLogToServer(sessionHandle,"$SN ${it.id_image}")
                    val id_image = remoteSource.insertHeadPhoto(sessionHandle,it.SN,it.coordinatePhoto,it.id_image,it.idTireEvents)
                    remoteSource.messageLogToServer(sessionHandle,"$SN id_image после провеки")
                    remoteSource.messageLogToServer(sessionHandle,"$SN $id_image")


                    val fileName1 = it.fileName.replace("content://com.example.tire.FileProvider/root","",true)
                    remoteSource.messageLogToServer(sessionHandle,"$SN fileName1")
                    remoteSource.messageLogToServer(sessionHandle,"$SN $fileName1")
                    remoteSource.messageLogToServer(sessionHandle,"начало чтения файла")
                    val imageBody:ByteArray = File(fileName1).readBytes()

//                    remoteSource.messageLogToServer(sessionHandle,"вычисление hashSHA2_512 ")
//                    val hashSHA2_512:String = encryptString(imageBody)
//                    remoteSource.messageLogToServer(sessionHandle,"$SN hashSHA2_512")
//                    remoteSource.messageLogToServer(sessionHandle,"$SN $hashSHA2_512")
                    //localSource.updateFilesToUpload(id_image,hashSHA2_512)



                    if (id_image != it.id_image){ // в случае расхождения имеющегося idImage и зарегистрированного
                        remoteSource.messageLogToServer(sessionHandle,"$SN в случае расхождения имеющегося idImage и зарегистрированного")
                        localSource.updateNewIdImageByIdImage(
                            id_image,
                            it.id_image
                        ) //меняем занчение idImage в локальном хранилище}
                    }
                    remoteSource.messageLogToServer(sessionHandle,"$SN Старт попытки загрузить фотографию")
                    if (remoteSource.uploadPhoto(sessionHandle, id_image, imageBody)) {
                        remoteSource.messageLogToServer(sessionHandle,"$SN Попытка вроде как удачная приступаем к удалению")

                          localSource.deleteFilesToUploadFromLocalTable(it.fileName)
                          remoteSource.messageLogToServer(sessionHandle,"Удаление произошло")
//                        val hashSHA2_512_fromServer = remoteSource.getHashImageFromServer(sessionHandle,id_image)
//                        remoteSource.messageLogToServer(sessionHandle,"$SN hashSHA2_512_fromServer")
//                        remoteSource.messageLogToServer(sessionHandle,"$SN $hashSHA2_512_fromServer")
//                        if (hashSHA2_512_fromServer == hashSHA2_512) {
//                            remoteSource.messageLogToServer(sessionHandle,"$SN hashSHA2_512_ совпадают приступаем у удалению фотографии")
//                            localSource.deleteFilesToUploadFromLocalTable(it.fileName)
//                        }
                    }else{
                        remoteSource.messageLogToServer(sessionHandle,"$SN Загрузка прошла неудачно")
                    }

                }else {
                    remoteSource.messageLogToServer(sessionHandle,"$SN Имя файла пустое, удаляем его ....")
                    localSource.deleteFilesToUploadFromLocalTable(it.fileName)
                }
            }
            remoteSource.messageLogToServer(sessionHandle," устанавливаем флаг что загрузка файлов завершена")
            endLoadImage() // устанавливаем флаг что загрузка файлов завершена
            if (colFileForUpload != 0 ) {//если небыло фалов для загрузки то не циклим
                remoteSource.messageLogToServer(
                    sessionHandle,
                    "повторяем проверку и загрузку файлов для отправки (рекурсия)"
                )
                uploadAllImageFromLocalTable() // повторяем проверку и загрузку файлов для отправки
            }
        }
    }

    fun setIdImageForViewFullScreen(id_image:String){
        localSource.saveTokenString("IdImageForViewFullScreen",id_image)
    }
    fun getIdImageForViewFullScreen():String{
        return  localSource.getTokenString("IdImageForViewFullScreen")
    }

    fun encryptString(input: ByteArray): String {
        return try {
            val md = MessageDigest.getInstance("SHA-512")
            val messageDigest = md.digest(input)
            val no = BigInteger(1, messageDigest)
            var hashtext = no.toString(16)

            while (hashtext.length < 128) {
                val addChar = 128 - hashtext.length;
                hashtext = hashtext.padStart(addChar, '0')

            }
            hashtext
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }

}

