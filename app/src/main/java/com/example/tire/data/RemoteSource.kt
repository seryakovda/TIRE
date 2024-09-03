package com.example.tire.data
import android.util.Log
import com.example.tire.ConstApp.keyAPI
import com.example.tire.ConstApp.urlRemoteServer
import com.example.tire.data.brand.BrandItem
import com.example.tire.data.chassisNumber.ChassisNumberItem
import com.example.tire.data.division.DivisionItem
import com.example.tire.data.modelCar.ModelItem
import com.example.tire.data.standard_size.StandardSizeItem
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.date
import io.ktor.http.isSuccess
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import java.io.File
import java.lang.Exception
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.UUID
import javax.inject.Inject


@Serializable
data class SessionHandle (val state: String, val sessionHandle: String)

@Serializable
data class AutorisationResult(val state: String)

@Serializable
data class ImageList (
    var id_image:String
)
@Serializable
data class TireEvents (
    var id:Int,
    var name:String
)
data class ControlHttpResponse(
    var error:Boolean,
    var httpResponse:HttpResponse?
)
class RemoteSource @Inject constructor(private val client: HttpClient) {

//    private suspend inline fun <reified T> obrabotka(requestTxt: String, default: T): T {
//        val requestTxt1 = requestTxt.replace(" ","%20")
//        return try {
//            val response = client.request(requestTxt1)
//            if (response.status.isSuccess()) response.body<T>() else default
//        } catch (e: Exception) {
//            default
//        }
//    }

////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
    suspend fun myRequest(requestTxt1:String):HttpResponse{
        val requestTxt = requestTxt1.replace(" ","%20")
        var retStatus = ControlHttpResponse(httpResponse = null, error = true)
        while (retStatus.error) {
            retStatus = statusRequest(requestTxt)
        }
        return retStatus.httpResponse!!
    }

    suspend fun statusRequest(requestTxt:String):ControlHttpResponse{
        var retStatus = ControlHttpResponse(httpResponse = null, error = true)

        retStatus.error = try {
            retStatus.httpResponse = client.request(requestTxt)
            false
        } catch (e: Exception) {
            true
        }

        return retStatus
    }
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
    suspend fun requestSession(): String {
        val requestSessionUrl = "$urlRemoteServer?" +
                "r0=SYS" +
                "&r1=registrationKeyAPI" +
                "&keyAPI=$keyAPI"

        val response = myRequest(requestSessionUrl)
        var retValue = ""
        if (response.status.isSuccess()) {
            retValue = response.body<SessionHandle>().sessionHandle
        } else {
            retValue = ""
        }
        return retValue
    }

    suspend fun autorisation(login: String, pass: String, sessionHandle: String): Boolean {

        val requestTxt ="$urlRemoteServer?" +
                "r0=SYS" +
                "&r1=autorisation" +
                "&login=$login" +
                "&pass=$pass" +
                "&sessionHandle=$sessionHandle"
        //Log.v("MyMSG",requestTxt)
        val response = myRequest(requestTxt)
        if (response.status.isSuccess()) {
            val state  = response.body<AutorisationResult>().state
            if ( state == "false"){
                return false
            }else
                return true
        } else {
            return false
        }

    }

    suspend fun loadBrand(sessionHandle: String,name:String): List<BrandItem>
    {
        val requestTxt ="$urlRemoteServer?" +
                "r0=SYS" +
                "&r1=loadBrand" +
                "&name=$name" +
                "&sessionHandle=$sessionHandle"
        
        val response = myRequest(requestTxt)

        return response.body<List<BrandItem>>()
    }

    suspend fun loadStandardSize(sessionHandle: String,name:String): List<StandardSizeItem>
    {
        val requestTxt ="$urlRemoteServer?" +
                "r0=SYS" +
                "&r1=loadStandardSize" +
                "&name=$name" +
                "&sessionHandle=$sessionHandle"
        
        val response = myRequest(requestTxt)

        return response.body<List<StandardSizeItem>>()
    }

    suspend fun loadModel(sessionHandle: String,name:String): List<ModelItem>
    {
        val requestTxt ="$urlRemoteServer?" +
                "r0=SYS" +
                "&r1=loadModel" +
                "&name=$name" +
                "&sessionHandle=$sessionHandle"
        
        val response = myRequest(requestTxt)

        return response.body<List<ModelItem>>()
    }

    suspend fun loadDivision(sessionHandle: String,name:String):List<DivisionItem>
    {
        val requestTxt ="$urlRemoteServer?" +
                "r0=SYS" +
                "&r1=loadDivision" +
                "&name=$name" +
                "&sessionHandle=$sessionHandle"
        
        val response = myRequest(requestTxt)

        return response.body<List<DivisionItem>>()
    }

    suspend fun loadChassisNumber(sessionHandle: String,name:String):List<ChassisNumberItem>
    {
        val requestTxt ="$urlRemoteServer?" +
                "r0=SYS" +
                "&r1=loadChassisNumber" +
                "&name=$name" +
                "&sessionHandle=$sessionHandle"
        
        val response = myRequest(requestTxt)

        return response.body<List<ChassisNumberItem>>()
    }

    suspend fun LoadListEvents(sessionHandle: String,name:String):List<TireEvents>
    {
        val requestTxt ="$urlRemoteServer?" +
                "r0=SYS" +
                "&r1=LoadListEvents" +
                "&name=$name" +
                "&sessionHandle=$sessionHandle"

        val response = myRequest(requestTxt)

        return response.body<List<TireEvents>>()

    }
    suspend fun getListTire(sessionHandle: String, serialNumber: String?, chassisNumber: String?):List<TireList> {
        val requestTxt ="$urlRemoteServer?" +
                "r0=SYS" +
                "&r1=getListTire" +
                "&sessionHandle=$sessionHandle" +
                "&serialNumber=$serialNumber" +
                "&chassisNumber=$chassisNumber"
        
        val response = myRequest(requestTxt)
        return response.body<List<TireList>>()
    }

    suspend fun getFullTire(sessionHandle: String,SN: String):TireList
    {
        val requestTxt ="$urlRemoteServer?" +
                "r0=SYS" +
                "&r1=getFullTire" +
                "&sessionHandle=$sessionHandle" +
                "&serialNumber=$SN"
        val response = myRequest(requestTxt)
        return response.body<TireList>()
    }
    suspend fun addTire(sessionHandle: String, serialNumber: String?):List<TireList> {
        val requestTxt ="$urlRemoteServer?" +
                "r0=SYS" +
                "&r1=addTire" +
                "&sessionHandle=$sessionHandle" +
                "&serialNumber=$serialNumber"
        

        val response = myRequest(requestTxt)

        return response.body<List<TireList>>()
    }

    suspend fun getImageList(sessionHandle: String, serialNumber: String?):List<ImageList> {
        val requestTxt ="$urlRemoteServer?" +
                "r0=SYS" +
                "&r1=getImageList" +
                "&sessionHandle=$sessionHandle" +
                "&serialNumber=$serialNumber"
        
        val response = myRequest(requestTxt)
        return response.body<List<ImageList>>()
    }

    suspend fun updateTireItem (sessionHandle: String, SN:String, nameField: String, value:String){
        val requestTxt ="$urlRemoteServer?" +
                "r0=SYS" +
                "&r1=updateTireItem" +
                "&sessionHandle=$sessionHandle" +
                "&SN=$SN" +
                "&nameField=$nameField" +
                "&value=$value"
        
        val response = myRequest(requestTxt)
    }
    suspend fun addEventForTire(sessionHandle: String, SN:String,id:Int){
        val requestTxt ="$urlRemoteServer?" +
                "r0=SYS" +
                "&r1=addEventForTire" +
                "&sessionHandle=$sessionHandle" +
                "&SN=$SN" +
                "&id=$id"

        val response = myRequest(requestTxt)

    }
    suspend fun insertHeadPhoto (sessionHandle: String, SN:String,coordinatePhoto:String,id_image: String,id_tireEvents:Int):String{
        val requestTxt =  "$urlRemoteServer?" +
                "r0=SYS" +
                "&r1=insertHeadPhoto" +
                "&sessionHandle=$sessionHandle" +
                "&coordinatePhoto=$coordinatePhoto" +
                "&id_image=$id_image" +
                "&id_tireEvents=$id_tireEvents" +

                "&SN=$SN"

        val response = myRequest(requestTxt)
        val retdata = response.body<String>()
        return retdata
    }

    suspend fun getHashImageFromServer(sessionHandle: String,id_image:String):String{
        val requestTxt =  "$urlRemoteServer?" +
                "r0=SYS" +
                "&r1=getHashImage" +
                "&sessionHandle=$sessionHandle" +
                "&id_image=$id_image"

        val response = myRequest(requestTxt)
        val retdata = response.body<String>()
        return retdata

    }

    suspend fun messageLogToServer(sessionHandle: String,message:String):String{
        val requestTxt =  "$urlRemoteServer?" +
                "r0=SYS" +
                "&r1=messageLog" +
                "&sessionHandle=$sessionHandle" +
                "&message=$message"

        val response = myRequest(requestTxt)
        val retdata = response.body<String>()
        return retdata

    }
    suspend fun uploadPhoto(sessionHandle: String, id_image:String,imageBody:ByteArray):Boolean{
        val res:Boolean = try {
            val retData =  client.submitFormWithBinaryData(
                url = urlRemoteServer,
                formData = formData {
                    append("r0",          "SYS")
                    append("r1",          "uploadPhoto")
                    append("sessionHandle",     sessionHandle)
                    append("id_image",          id_image)
                    append("image",             imageBody,
                    )
                }
            )


            if (retData.status.value == 200){
                true
            }else{
                false
            }

        }catch (error: Exception){
            false
        }

        return res
    }



}


