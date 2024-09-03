package com.example.tire.data


import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.util.LogPrinter
import com.example.tire.data.brand.BrandDao
import com.example.tire.data.brand.BrandItem
import com.example.tire.data.chassisNumber.ChassisNumberDao
import com.example.tire.data.chassisNumber.ChassisNumberItem
import com.example.tire.data.division.DivisionDao
import com.example.tire.data.division.DivisionItem
import com.example.tire.data.filesToUpload.FilesToUploadDao
import com.example.tire.data.filesToUpload.FilesToUploadItem
import com.example.tire.data.modelCar.ModelDao
import com.example.tire.data.modelCar.ModelItem
import com.example.tire.data.standard_size.StandardSizeDao
import com.example.tire.data.standard_size.StandardSizeItem
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.Serializable
import java.io.File
import javax.inject.Inject

@Serializable
data class ImageListLocal (
    var idTireEvents:   Int,
    var fileName:       String
)
class LocalSource @Inject constructor(
    @ApplicationContext private val context: Context,
    //private var db: DB
    private var brandDao:           BrandDao,
    private var standardSizeDao:    StandardSizeDao,
    private var modelDao:           ModelDao,
    private var divisionDao:        DivisionDao,
    private var chassisNumberDao:   ChassisNumberDao,
    private var filesToUploadDao:   FilesToUploadDao,
    ) {
    var pref : SharedPreferences? = null

    init {
        pref = context.getSharedPreferences("TABLE",Context.MODE_PRIVATE)
    }


    fun saveTokenString(nameToken:String ,token: String) {
        val editor = pref?.edit()
        editor?.putString(nameToken,token)
        editor?.apply()
    }

    fun saveTokenBoolean(nameToken:String ,token: Boolean) {
        val editor = pref?.edit()
        editor?.putBoolean(nameToken,token)
        editor?.apply()
    }
    fun getTokenString(nameToken:String):String{
        return pref?.getString(nameToken,"")!!
    }
    fun getTokenBoolean(nameToken:String):Boolean{
        return pref?.getBoolean(nameToken,false)!!
    }

    suspend fun setBrandToLocalRoom(items: List<BrandItem>){
        brandDao.deleteAllBrand()
        items.forEach{
            brandDao.insertBrand(it)
        }
    }

    suspend fun getBrandFromLocalRoom(): List<BrandItem>{
        return brandDao.getAllBrand()
    }

    suspend fun setStandardSizeToLocalRoom(items: List<StandardSizeItem>){
        standardSizeDao.deleteAllStandardSize()
        items.forEach{
            standardSizeDao.insertStandardSize(it)
        }
    }

    suspend fun getStandardSizeFromLocalRoom(): List<StandardSizeItem>{
        return standardSizeDao.getAllStandardSize()
    }

    suspend fun setModelToLocalRoom(items: List<ModelItem>){
        modelDao.deleteAllModel()
        items.forEach{
            modelDao.insertModel(it)
        }
    }

    suspend fun getModelFromLocalRoom(): List<ModelItem>{
        return modelDao.getAllModel()
    }

    suspend fun setDivisionToLocalRoom(items: List<DivisionItem>){
        divisionDao.deleteAllDivision()
        items.forEach{
            divisionDao.insertDivision(it)
        }
    }

    suspend fun getDivisionFromLocalRoom(): List<DivisionItem>{
        return divisionDao.getAllDivision()
    }


    suspend fun setChassisNumberToLocalRoom(items: List<ChassisNumberItem>){
        chassisNumberDao.deleteAllChassisNumber()
        items.forEach{
            chassisNumberDao.insertChassisNumber(it)
        }
    }

    suspend fun getChassisNumberFromLocalRoom(): List<ChassisNumberItem>{
        return chassisNumberDao.getAllChassisNumber()
    }

    suspend fun insertFilesToUpload(SN:String,fileName:String,id_image:String,coordinate:String,idTireEvents:Int){
        filesToUploadDao.insertFilesToUpload(SN,fileName,id_image,coordinate,idTireEvents)
    }

    suspend fun deleteFilesToUploadFromLocalTable(fileName:String){
        filesToUploadDao.deleteFilesToUploadByFileName(fileName)
        deleteFile(fileName)
    }

    fun gelAllFilesToUpload():List<FilesToUploadItem>{
        return filesToUploadDao.getAllFilesToUpload()
    }

    suspend fun updateFilesToUpload(id_image:String,hashSHA2_512:String){
        filesToUploadDao.updateFilesToUpload(id_image,hashSHA2_512)
    }
    suspend fun getFilesToUpload(SN:String):List<ImageListLocal>
    {
        return filesToUploadDao.getByIdFilesToUpload(SN)
    }

    fun deleteFile(filePath: String) {
        val file = File(filePath)
        if (file.exists() && file.isFile) {
            file.delete()
        }
    }

    suspend fun updateNewIdImageByIdImage(new_idImage:String,old_id_image:String)
    {
        filesToUploadDao.updateNewIdImageByIdImage (new_idImage,old_id_image)
    }
}


