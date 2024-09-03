package com.example.tire.data.modelCar

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "modelCar_list")
@Serializable
data class ModelItem(
    @PrimaryKey()
    var name: String,
)