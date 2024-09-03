package com.example.tire.data.filesToUpload


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.tire.data.ImageListLocal


@Dao
interface FilesToUploadDao {
    @Query("SELECT * from fToU_list")
    fun getAllFilesToUpload(): List<FilesToUploadItem>

    @Query("SELECT idTireEvents,fileName from fToU_list where SN = :SN ORDER BY id DESC")
    suspend fun getByIdFilesToUpload(SN: String) : List<ImageListLocal>
    @Query("INSERT INTO fToU_list (SN,fileName,id_image,hashSHA2_512,coordinatePhoto,idTireEvents) VALUES (:SN,:fileName,:id_image,'',:coordinatePhoto,:idTireEvents)")
    suspend fun insertFilesToUpload(SN:String,fileName:String,id_image:String,coordinatePhoto:String,idTireEvents:Int)

    @Query("UPDATE fToU_list SET hashSHA2_512 = :hashSHA2_512 WHERE id_image = :id_image")
    suspend fun updateFilesToUpload(id_image:String,hashSHA2_512:String)

    @Query("DELETE FROM fToU_list WHERE fileName = :fileName")
    suspend fun deleteFilesToUploadByFileName(fileName:String)

    @Query("DELETE FROM fToU_list")
    suspend fun deleteAllFilesToUpload()

    @Query("UPDATE fToU_list SET id_image = :new_idImage WHERE id_image = :old_id_image")
    suspend fun updateNewIdImageByIdImage(new_idImage:String,old_id_image:String)

}