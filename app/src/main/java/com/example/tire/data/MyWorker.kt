package com.example.tire.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.tire.ConstApp
import com.example.tire.data.filesToUpload.FilesToUploadDao
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

import javax.inject.Inject

@Serializable
data class ImageListByIdUser (
    var id_image:       String,
    var fileName:       String
)

class MyWorker @Inject constructor(@ApplicationContext
                                   context: Context,
                                   params: WorkerParameters,
) : CoroutineWorker(context, params) {
    var pref : SharedPreferences? = null
    var path:File
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
        this.engine {
            this.requestTimeout = 30000L
        }
    }
    init {
        pref = context.getSharedPreferences("TABLE",Context.MODE_PRIVATE)
        path = context.getFilesDir()
    }

    override suspend fun doWork(): Result {

        return try {
            messageLogToServer("doWork")
            //Log.i("MyMSG","doWork1 " + path.toString())
            MainWorkFun()
            Result.success()
        } catch (ex: Exception) {
            return Result.failure(); //или Result.retry()
        }
    }

    fun getTokenString(nameToken:String):String{
        return pref?.getString(nameToken,"")!!
    }

    suspend fun MainWorkFun() {
        val lI = getListImageByIdUser()
        //Log.i("MyMSG","MainWorkFun"+lI.toString())
        lI.forEach(){
            val fileName1 = it.fileName.replace("content://com.example.tire.FileProvider/root","",true)
            //Log.d ("MyMSG",fileName1)
            if (isFileExists(fileName1)) {
                //Log.d ("MyMSG","Есть файл")
                if (uploadPhoto(it.id_image, fileName1)) {
                    //Log.d ("MyMSG","Удачная загрузка")
                    deleteFile(fileName1)
                }
            }else{
                //Log.d ("MyMSG","Нет файлв")
                deleteFileInToSQL(it.id_image)
            }
        }
    }

    suspend fun deleteFileInToSQL(id_image:String){
        val requestTxt =
            "r0=SYS" +
                    "&r1=deleteFileInToSQL" +
                    "&id_image=$id_image"
        myRequest(requestTxt)
    }


    fun isFileExists(fileName:String): Boolean {
        val file = File(fileName)
        return file.exists() && !file.isDirectory
    }
    suspend fun myRequest(requestTxt1:String): HttpResponse {
        var requestTxt = requestTxt1.replace(" ","%20")
        requestTxt = requestTxt.replace("/","%2F")
        requestTxt = requestTxt.replace(":","%3A")
        requestTxt = requestTxt.replace(".","%2E")

        val sessionHandle = getTokenString("sessionHandle")
        requestTxt = requestTxt + "&sessionHandle=$sessionHandle"
        requestTxt = "${ConstApp.urlRemoteServer}?" + requestTxt

        var retStatus = ControlHttpResponse(httpResponse = null, error = true)
        while (retStatus.error) {
            retStatus = statusRequest(requestTxt)
            val res = retStatus.toString()
            //Log.i("MyMSG","ControlHttpResponse $res")
        }
        return retStatus.httpResponse!!
    }

    suspend fun statusRequest(requestTxt:String):ControlHttpResponse {
        var retStatus = ControlHttpResponse(httpResponse = null, error = true)
        retStatus.error = try {
            retStatus.httpResponse = client.request(requestTxt)
            false
        } catch (e: java.lang.Exception) {
            true
        }
        return retStatus
    }

    suspend fun messageLogToServer(message:String):String{
        val requestTxt =
                "r0=SYS" +
                "&r1=messageLog" +
                "&message=$message"

        val response = myRequest(requestTxt)
        val retdata = response.body<String>()
        return retdata

    }

    suspend fun getListImageByIdUser():List<ImageListByIdUser>{
        val requestTxt =
                    "r0=SYS" +
                    "&r1=getListImageByIdUser"
        val response = myRequest(requestTxt)
        val retdata = response.body<List<ImageListByIdUser>>()
        return retdata

    }

    suspend fun uploadPhoto( id_image:String,fileName:String):Boolean{
        //Log.v("MyMSG","начинаем читать $id_image")
        val imageBody:ByteArray = File(fileName).readBytes()
        //Log.v("MyMSG","прочиталы, вычисляем хеш")
        val hashSHA2_512 = encryptString(imageBody)
        //Log.v("MyMSG","хеш готов ")
        val sessionHandle  = getTokenString("sessionHandle")
        //Log.v("MyMSG","сессия $sessionHandle приступаем к попытке грузить")
        val res:Boolean = try {
            //Log.v("MyMSG","Попытка загрузить")
            val response =  client.submitFormWithBinaryData(
                url = ConstApp.urlRemoteServer,
                formData = formData {
                    append("r0",          "SYS")
                    append("r1",          "uploadPhoto")
                    append("sessionHandle",     sessionHandle)
                    append("id_image",          id_image)
                    append("hashSHA2_512",      hashSHA2_512)
                    append("image",             imageBody)
                }
            )

            if (response.status.value == 200){
                //Log.i("MyMSG","uploadPhoto 6 " + response.body<String>())

                if ( response.body<String>() == "OK" ) {
                    //Log.i("MyMSG","Вроде норм")
                    true
                }
                else {
                    //Log.i("MyMSG","Вроде не норм")
                    false
                }
            }else{
                //Log.i("MyMSG","Какаято херня")
                false
            }

        }catch (error: java.lang.Exception){
            false
        }

        return res
    }

    fun encryptString(input: ByteArray): String {
        return try {
            val md = MessageDigest.getInstance("SHA-512")
            val messageDigest = md.digest(input)
            val no = BigInteger(1, messageDigest)
            var hashtext = no.toString(16)
            //Log.v("MyMSG","хеш1 $hashtext")
            var l = hashtext.length
            while (l < 128) {
                val addChar = 128 - hashtext.length;
                //Log.v("MyMSG","хеш0 =$addChar= $l  $hashtext ")
                for (i in 1..addChar){
                    hashtext = "0" + hashtext
                }

                l = hashtext.length
            }
            //Log.v("MyMSG","хеш2 $hashtext")
            hashtext
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }

    fun deleteFile(filePath: String) {
        val file = File(filePath)
        if (file.exists() && file.isFile) {
            file.delete()
        }
    }
}