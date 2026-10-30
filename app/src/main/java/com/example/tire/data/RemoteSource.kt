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
import io.ktor.client.request.forms.submitForm
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Parameters
import io.ktor.http.isSuccess

import kotlinx.serialization.Serializable
import javax.inject.Inject
import io.ktor.client.request.forms.submitForm


@Serializable
data class SessionHandle(
    val state: String,
    val sessionHandle: String
)

@Serializable
data class AutorisationResult(
    val state: String
)

@Serializable
data class ImageList(
    var id_image: String
)

@Serializable
data class TireEvents(
    var id: Int,
    var name: String
)

data class ControlHttpResponse(
    var error: Boolean,
    var httpResponse: HttpResponse?
)

class RemoteSource @Inject constructor(
    private val client: HttpClient
) {

    /**
     * Общий метод запросов.
     *
     * Все параметры отправляются в теле POST-запроса,
     * а не в URL.
     *
     * Значения автоматически кодируются как параметры формы.
     */
    private suspend fun myRequest(
        vararg fields: Pair<String, String?>
    ): HttpResponse {
        val params = Parameters.build {
            fields.forEach { (name, value) ->
                append(name, value ?: "")
            }
        }

        return client.submitForm(
            url = urlRemoteServer,
            formParameters = params
        )
    }

    suspend fun requestSession(): String {
        val response = myRequest(
            "r0" to "SYS",
            "r1" to "registrationKeyAPI",
            "keyAPI" to keyAPI
        )

        return if (response.status.isSuccess()) {
            response.body<SessionHandle>().sessionHandle
        } else {
            ""
        }
    }

    suspend fun autorisation(
        login: String,
        pass: String,
        sessionHandle: String
    ): Boolean {
        val response = myRequest(
            "r0" to "SYS",
            "r1" to "autorisation",
            "login" to login,
            "pass" to pass,
            "sessionHandle" to sessionHandle
        )

        if (!response.status.isSuccess()) {
            return false
        }

        return response.body<AutorisationResult>().state != "false"
    }

    suspend fun loadBrand(
        sessionHandle: String,
        name: String
    ): List<BrandItem> {
        val response = myRequest(
            "r0" to "SYS",
            "r1" to "loadBrand",
            "name" to name,
            "sessionHandle" to sessionHandle
        )

        return response.body<List<BrandItem>>()
    }

    suspend fun loadStandardSize(
        sessionHandle: String,
        name: String
    ): List<StandardSizeItem> {
        val response = myRequest(
            "r0" to "SYS",
            "r1" to "loadStandardSize",
            "name" to name,
            "sessionHandle" to sessionHandle
        )

        return response.body<List<StandardSizeItem>>()
    }

    suspend fun loadModel(
        sessionHandle: String,
        name: String
    ): List<ModelItem> {
        val response = myRequest(
            "r0" to "SYS",
            "r1" to "loadModel",
            "name" to name,
            "sessionHandle" to sessionHandle
        )

        return response.body<List<ModelItem>>()
    }

    suspend fun loadDivision(
        sessionHandle: String,
        name: String
    ): List<DivisionItem> {
        val response = myRequest(
            "r0" to "SYS",
            "r1" to "loadDivision",
            "name" to name,
            "sessionHandle" to sessionHandle
        )

        return response.body<List<DivisionItem>>()
    }

    suspend fun loadChassisNumber(
        sessionHandle: String,
        name: String
    ): List<ChassisNumberItem> {
        val response = myRequest(
            "r0" to "SYS",
            "r1" to "loadChassisNumber",
            "name" to name,
            "sessionHandle" to sessionHandle
        )

        return response.body<List<ChassisNumberItem>>()
    }

    suspend fun LoadListEvents(
        sessionHandle: String,
        name: String
    ): List<TireEvents> {
        val response = myRequest(
            "r0" to "SYS",
            "r1" to "LoadListEvents",
            "name" to name,
            "sessionHandle" to sessionHandle
        )

        return response.body<List<TireEvents>>()
    }

    suspend fun getListTire(
        sessionHandle: String,
        serialNumber: String?,
        chassisNumber: String?
    ): List<TireList> {
        val response = myRequest(
            "r0" to "SYS",
            "r1" to "getListTire",
            "sessionHandle" to sessionHandle,
            "serialNumber" to serialNumber,
            "chassisNumber" to chassisNumber
        )

        val responseText = response.bodyAsText()

        return response.body<List<TireList>>()
    }

    suspend fun getFullTire(
        sessionHandle: String,
        SN: String
    ): TireList {
        val response = myRequest(
            "r0" to "SYS",
            "r1" to "getFullTire",
            "sessionHandle" to sessionHandle,
            "serialNumber" to SN
        )

        return response.body<TireList>()
    }

    suspend fun addTire(
        sessionHandle: String,
        serialNumber: String?
    ): List<TireList> {
        val response = myRequest(
            "r0" to "SYS",
            "r1" to "addTire",
            "sessionHandle" to sessionHandle,
            "serialNumber" to serialNumber
        )

        return response.body<List<TireList>>()
    }

    suspend fun getImageList(
        sessionHandle: String,
        serialNumber: String?
    ): List<ImageList> {
        val response = myRequest(
            "r0" to "SYS",
            "r1" to "getImageList",
            "sessionHandle" to sessionHandle,
            "serialNumber" to serialNumber
        )

        return response.body<List<ImageList>>()
    }

    suspend fun updateTireItem(
        sessionHandle: String,
        SN: String,
        nameField: String,
        value: String
    ) {
        myRequest(
            "r0" to "SYS",
            "r1" to "updateTireItem",
            "sessionHandle" to sessionHandle,
            "SN" to SN,
            "nameField" to nameField,
            "value" to value
        )
    }

    suspend fun addEventForTire(
        sessionHandle: String,
        SN: String,
        id: Int
    ) {
        myRequest(
            "r0" to "SYS",
            "r1" to "addEventForTire",
            "sessionHandle" to sessionHandle,
            "SN" to SN,
            "id" to id.toString()
        )
    }

    suspend fun insertHeadPhoto(
        sessionHandle: String,
        SN: String,
        coordinatePhoto: String,
        id_image: String,
        id_tireEvents: Int
    ): String {
        val response = myRequest(
            "r0" to "SYS",
            "r1" to "insertHeadPhoto",
            "sessionHandle" to sessionHandle,
            "coordinatePhoto" to coordinatePhoto,
            "id_image" to id_image,
            "id_tireEvents" to id_tireEvents.toString(),
            "SN" to SN
        )

        return response.body<String>()
    }

    suspend fun getHashImageFromServer(
        sessionHandle: String,
        id_image: String
    ): String {
        val response = myRequest(
            "r0" to "SYS",
            "r1" to "getHashImage",
            "sessionHandle" to sessionHandle,
            "id_image" to id_image
        )

        return response.body<String>()
    }

    suspend fun messageLogToServer(
        sessionHandle: String,
        message: String
    ): String {
        val response = myRequest(
            "r0" to "SYS",
            "r1" to "messageLog",
            "sessionHandle" to sessionHandle,
            "message" to message
        )

        return response.body<String>()
    }

    suspend fun insertFilesToUpload(
        sessionHandle: String,
        SN: String,
        fileName: String,
        coordinatePhoto: String,
        idTireEvents: Int
    ): String {
        val response = myRequest(
            "r0" to "SYS",
            "r1" to "insertFilesToUpload",
            "sessionHandle" to sessionHandle,
            "SN" to SN,
            "fileName" to fileName,
            "coordinatePhoto" to coordinatePhoto,
            "idTireEvents" to idTireEvents.toString()
        )

        return response.body<String>()
    }

    suspend fun getFilesToUpload(
        sessionHandle: String,
        SN: String
    ): List<ImageListLocal> {
        val response = myRequest(
            "r0" to "SYS",
            "r1" to "getFilesToUpload",
            "sessionHandle" to sessionHandle,
            "SN" to SN
        )

        return response.body<List<ImageListLocal>>()
    }

    suspend fun getImageSmall(
        sessionHandle: String,
        idImage: String
    ): ByteArray {

        val response = client.submitForm(
            url = urlRemoteServer,
            formParameters = Parameters.build {
                append("r0", "SYS")
                append("r1", "getImageSmall")
                append("sessionHandle", sessionHandle)
                append("id_image", idImage)
            }
        )
        if (!response.status.isSuccess()) {
            val errorBody = response.body<String>()

            throw IllegalStateException(
                "getImageSmall failed: HTTP ${response.status}"
            )
        }

        val bytes = response.body<ByteArray>()
        return bytes
    }

    suspend fun getImageBig(
        sessionHandle: String,
        idImage: String
    ): ByteArray {
        val response = client.submitForm(
            url = urlRemoteServer,
            formParameters = Parameters.build {
                append("r0", "SYS")
                append("r1", "getImageBig")
                append("sessionHandle", sessionHandle)
                append("id_image", idImage)
            }
        )

        if (!response.status.isSuccess()) {
            throw IllegalStateException(
                "getImageBig failed: HTTP ${response.status}"
            )
        }

        return response.body<ByteArray>()
    }
}