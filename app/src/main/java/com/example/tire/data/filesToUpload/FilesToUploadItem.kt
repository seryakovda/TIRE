package com.example.tire.data.filesToUpload

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "fToU_list",)
@Serializable
data class FilesToUploadItem(
    @PrimaryKey(autoGenerate = true)
    var id:                 Int,
    var SN:                 String,
    var fileName:           String,
    var id_image:           String,
    var hashSHA2_512:       String,
    var coordinatePhoto:    String,
    var idTireEvents:       Int,
)