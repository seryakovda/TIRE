package com.example.tire.data.standard_size

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "StandardSize_list")
@Serializable
data class StandardSizeItem(
    @PrimaryKey()
    var name: String,
)